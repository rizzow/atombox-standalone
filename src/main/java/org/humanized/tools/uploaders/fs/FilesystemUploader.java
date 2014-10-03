package org.humanized.tools.uploaders.fs;

import org.humanized.tools.uploaders.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Local filesystem 'uploader' - copy files to other location
 */
public class FilesystemUploader implements Uploader {
    private static final Logger mLogger = LoggerFactory.getLogger(FilesystemUploader.class);

    private static final String PROP_KEY_FS_DESTINATION = "uploaders.fs.destination";

    private String mDestination;


    @Override
    public String getId() {
        return "fs";
    }


    @Override
    public String getName() {
        return "Filesystem uploader (copy)";
    }


    @Override
    public void initialize(final Properties properties) {
        mDestination = properties.getProperty(PROP_KEY_FS_DESTINATION);
        if (mDestination != null) {
            mDestination = mDestination.trim();
            if (!mDestination.trim().endsWith(File.separator)) {
                mDestination += File.separatorChar;
            }
        }
    }


    @Override
    public void uploadFile(final File file, final String contentType, final String remoteName) throws IOException {
        final String destFilename = mDestination + remoteName;
        mLogger.debug("uploadFile: copying file {} to {}", file.getAbsolutePath(), destFilename);

        final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        final OutputStream outputStream = new FileOutputStream(destFilename);

        int read;
        byte[] bytes = new byte[1024];

        while ((read = stream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        stream.close();
        outputStream.flush();
        outputStream.close();
    }
}
