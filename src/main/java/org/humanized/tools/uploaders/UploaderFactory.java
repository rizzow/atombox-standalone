package org.humanized.tools.uploaders;

import org.humanized.tools.uploaders.amazon.AmazonS3Uploader;
import org.humanized.tools.uploaders.dummy.DummyUploader;
import org.humanized.tools.uploaders.fs.FilesystemUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UploaderFactory {
    private static final Logger mLogger = LoggerFactory.getLogger(UploaderFactory.class);

    private static final String PROP_KEY_ACTIVE_UPLOADERS = "uploaders.active";
    private static final String PROP_SPLITTER = ",";

    private static final List<Uploader> mUploaders = new ArrayList<>();


    private UploaderFactory() {
    }


    public static void registerUploader(final Uploader uploader) {
        mUploaders.add(uploader);
    }


    public static Uploader getDefaultUploader() {
        if (mUploaders.size() > 0) {
            return mUploaders.get(0);
        }

        return null;
    }


    public static List<Uploader> getUploaders() {
        return mUploaders;
    }


    public static void initUploaders(final Properties properties) {

        registerUploaders(properties);

        for (Uploader uploader : mUploaders) {
            uploader.initialize(properties);
        }
    }


    private static void registerUploaders(final Properties properties) {


        final String key = properties.getProperty(PROP_KEY_ACTIVE_UPLOADERS);
        if (key != null && !key.trim().isEmpty()) {
            // List of available uploaders
            // FIXME - ugly
            final Map<String, Uploader> candidates = getUploaderCandidates();

            // Cycle..
            final String[] identifiers = key.split(PROP_SPLITTER);
            for (String id : identifiers) {
                id = id.trim().toLowerCase();
                if (candidates.containsKey(id)) {
                    mLogger.debug("registerUploaders: activating uploader {}", id);
                    UploaderFactory.registerUploader(candidates.get(id));
                }
            }

            mLogger.info("registerUploaders: {} uploaders active", mUploaders.size());
        } else {
            mLogger.warn("registerUploaders: property {} not set, no uploaders active", PROP_KEY_ACTIVE_UPLOADERS);
        }
    }


    private static Map<String, Uploader> getUploaderCandidates() {
        final Map<String, Uploader> candidates = new HashMap<>();
        Uploader u;

        u = new AmazonS3Uploader();
        candidates.put(u.getId(), u);

        u = new DummyUploader();
        candidates.put(u.getId(), u);

        u = new FilesystemUploader();
        candidates.put(u.getId(), u);

        return candidates;
    }
}
