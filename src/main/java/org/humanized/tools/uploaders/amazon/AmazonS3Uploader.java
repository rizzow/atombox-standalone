package org.humanized.tools.uploaders.amazon;

import org.humanized.tools.uploaders.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AmazonS3Uploader implements Uploader {
    private static final Logger mLogger = LoggerFactory.getLogger(AmazonS3Uploader.class);

    private static final String PROP_KEY_S3_ACCESS_KEY = "uploaders.s3.access_key";
    private static final String PROP_KEY_S3_ACCESS_SECRET = "uploaders.s3.access_secret";
    private static final String PROP_KEY_S3_BUCKET = "uploaders.s3.bucket";

    private AmazonS3Service mAmazonS3Service = null;
    private String mBucketName = null;
    private String mKey;
    private String mSecret;


    @Override
    public String getId() {
        return "s3";
    }


    @Override
    public String getName() {
        return "Amazon S3";
    }


    @Override
    public void initialize(Properties properties) {
        mKey = properties.getProperty(PROP_KEY_S3_ACCESS_KEY);
        mSecret = properties.getProperty(PROP_KEY_S3_ACCESS_SECRET);

        if (mKey == null || mKey.isEmpty()) {
            throw new IllegalArgumentException("Missing key " + PROP_KEY_S3_ACCESS_KEY);
        }
        if (mSecret == null || mSecret.isEmpty()) {
            throw new IllegalArgumentException("Missing key " + PROP_KEY_S3_ACCESS_SECRET);
        }

        mAmazonS3Service = new AmazonS3Service(mKey, mSecret);
        mBucketName = properties.getProperty(PROP_KEY_S3_BUCKET);
        if (mBucketName == null || mBucketName.isEmpty()) {
            throw new IllegalArgumentException("Missing key " + PROP_KEY_S3_BUCKET);
        }

        dumpConfig();
    }


    private void dumpConfig() {
        mLogger.debug(getName() + " Uploader configuration");
        mLogger.debug(" * Key...: {}", mKey);
        mLogger.debug(" * Secret: {}", mSecret);
        mLogger.debug(" * Bucket: {}", mBucketName);
    }


    @Override
    public void uploadFile(final File file, final String contentType, final String remoteName) throws IOException {
        final InputStream stream = new FileInputStream(file);
        mAmazonS3Service.storeFile(mBucketName, remoteName, contentType, stream, 0);
    }
}
