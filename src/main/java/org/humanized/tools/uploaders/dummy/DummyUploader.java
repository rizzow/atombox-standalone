package org.humanized.tools.uploaders.dummy;

import org.humanized.tools.uploaders.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Dummy uploader - useful for debugging or connectionless environments
 */
public class DummyUploader implements Uploader {
    private static final Logger mLogger = LoggerFactory.getLogger(DummyUploader.class);


    @Override
    public String getId() {
        return "dummy";
    }


    @Override
    public String getName() {
        return "Dummy uploader";
    }


    @Override
    public void initialize(final Properties properties) {
        // ignore
    }


    @Override
    public void uploadFile(final File file, final String contentType, final String remoteName) throws IOException {
        mLogger.debug("uploadFile: simulating upload of file {}", file.getAbsolutePath());
    }
}
