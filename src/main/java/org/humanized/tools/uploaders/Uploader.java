package org.humanized.tools.uploaders;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public interface Uploader {

    /**
     * Machine-readable identifier for this uploader.
     */
    public String getId();


    /**
     * Human-readable name for this uploader.
     */
    public String getName();


    /**
     * Initialize uploader from properties file.
     *
     * @param properties properties document. Must not be null.
     */
    public void initialize(final Properties properties);


    /**
     * Perform file upload.
     *
     * @param file        file to upload
     * @param contentType MIME-type of content to be uploaded
     * @param remoteName  remote file name
     * @throws IOException
     */
    public void uploadFile(final File file, final String contentType, final String remoteName) throws IOException;
}
