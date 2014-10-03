package org.humanized.tools.uploaders.amazon;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class AmazonS3Service {

    private final AmazonS3 mAmazonS3;
    private final AWSCredentials mAWSCredentials;


    ClientConfiguration getDefaultConfiguration() {
        final ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        return clientConfig;
    }


    public AmazonS3Service(final String accessKey, final String accessSecret) {
        mAWSCredentials = new BasicAWSCredentials(accessKey, accessSecret);
        mAmazonS3 = new AmazonS3Client(mAWSCredentials, getDefaultConfiguration());
    }


    /**
     * Store a file in an S3 bucket.
     *
     * @param bucketName  name of bucket to use
     * @param filename    remote file name to use
     * @param contentType MIME-type of content
     * @param stream      input file stream
     * @param length      file length in bytes. May be 0 (but will cause warning)
     */
    public void storeFile(final String bucketName,
                          final String filename,
                          final String contentType,
                          final InputStream stream,
                          final int length) {

        final ObjectMetadata meta = new ObjectMetadata();
        if (length > 0) {
            meta.setContentLength(length);
        }

        meta.setContentType(contentType);

        final PutObjectRequest putRequest = new PutObjectRequest(bucketName, filename, stream, meta);
        mAmazonS3.putObject(putRequest);
    }


    /**
     * Store a file in an S3 bucket (bytes version).
     *
     * @param bucketName  name of bucket to use
     * @param filename    remote file name to use
     * @param contentType MIME-type of content
     * @param data        file data
     */
    public void storeFile(final String bucketName,
                          final String filename,
                          final String contentType,
                          final byte[] data) {

        storeFile(bucketName, filename, contentType, new ByteArrayInputStream(data), data.length);
    }
}
