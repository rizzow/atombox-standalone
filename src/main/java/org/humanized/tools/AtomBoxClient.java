package org.humanized.tools;

import org.humanized.tools.database.Database;
import org.humanized.tools.handlers.PathWatcher;
import org.humanized.tools.uploaders.UploaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AtomBoxClient {

    private static final String DEFAULT_PROPERTIES = "atombox.properties";

    private static final Logger mLogger = LoggerFactory.getLogger(AtomBoxClient.class);

    private final PathWatcher mPathWatcher = new PathWatcher();
    private Properties mProperties;


    public AtomBoxClient() {
        try {
            mLogger.debug("Initializing...");
            initConfiguration();
            initDatabase();
            initUploaders();
        } catch (final FileNotFoundException e) {
            mLogger.error("Could not open configuration file: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        } catch (final IOException e) {
            mLogger.error("Error reading configuration file: {}", e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }


    private void initConfiguration() throws IOException {
        final String propertyFile = DEFAULT_PROPERTIES;

        mLogger.debug("initUploaders: initializing configuration from properties file...");
        mProperties = new Properties();
        mProperties.load(new FileInputStream(propertyFile));
    }


    private void initDatabase() {
        mLogger.debug("initDatabase: initializing database from properties file...");
        Database.initialize(mProperties);
    }


    private void initUploaders() {
        assert mProperties != null;

        mLogger.debug("initUploaders: initializing uploaders from properties file...");

        UploaderFactory.initUploaders(mProperties);
    }


    private void addWatch(final String path) {
        try {
            mPathWatcher.addWatch(path);
        } catch (IOException e) {
            mLogger.error("addWatch: could not add watch for path {}: {}", path, e.getMessage());
        }
    }


    private void startWatching() {
        try {
            mPathWatcher.startWatching();
        } catch (InterruptedException e) {
            mLogger.error("startWatching: interrupted: {}", e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {

        if (args.length > 0) {
            AtomBoxClient watcher = new AtomBoxClient();

            // FIXME: for some reason we can't watch multiple paths yet.
            if (args.length > 1) {
                mLogger.error("Multiple paths not supported yet!");
                System.exit(-2);
            }

            for (String path : args) {
                mLogger.info("Watching path {}", path);
                watcher.addWatch(path);
            }

            watcher.startWatching();
        } else {
            mLogger.info("No paths to watch, exiting");
        }
    }
}
