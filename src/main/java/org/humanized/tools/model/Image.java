package org.humanized.tools.model;

import java.util.Date;

public class Image {

    /**
     * Unique identifier (GUID)
     */
    private String mId;

    /**
     * Image title
     */
    private String mTitle;

    /**
     * Image description
     */
    private String mDescription;

    /**
     * Date created in system
     */
    private Date mDateCreated;

    /**
     * Date picture created
     */
    private Date mDateShot;

    /**
     * Original 'local' filename
     */
    private String mOriginalFilename;

    /**
     * Remote path (if applicable)
     */
    private String mRemotePath;

    /**
     * Filename as stored on remote filesystem
     */
    private String mRemoteFilename;

    /**
     * Image width in pixels
     */
    private Integer mWidthPixels;

    /**
     * Image height in Pixels
     */
    private Integer mHeightPixels;

    /**
     * MIME-Type of file
     */
    private String mContentType;

    /**
     * Number of bits per channel
     */
    private Integer mPrecision;

    /**
     * Number of channels (e.g. 3 for RGB)
     */
    private Integer mChannelCount;

    /**
     * Image source orientation
     */
    private Integer mOrientation;

    /**
     * Lat/long of image
     */
    private Wgs84Coordinate mCoordinate;

    /**
     * Origin of image
     */
    private ImageSource mImageSource;


    public String getId() {
        return mId;
    }


    public void setId(String id) {
        mId = id;
    }


    public String getTitle() {
        return mTitle;
    }


    public void setTitle(String title) {
        mTitle = title;
    }


    public String getDescription() {
        return mDescription;
    }


    public void setDescription(String description) {
        mDescription = description;
    }


    public Date getDateCreated() {
        return mDateCreated;
    }


    public void setDateCreated(Date dateCreated) {
        mDateCreated = dateCreated;
    }


    public Date getDateShot() {
        return mDateShot;
    }


    public void setDateShot(Date dateShot) {
        mDateShot = dateShot;
    }


    public String getOriginalFilename() {
        return mOriginalFilename;
    }


    public void setOriginalFilename(String originalFilename) {
        mOriginalFilename = originalFilename;
    }


    public String getRemotePath() {
        return mRemotePath;
    }


    public void setRemotePath(String remotePath) {
        mRemotePath = remotePath;
    }


    public String getRemoteFilename() {
        return mRemoteFilename;
    }


    public void setRemoteFilename(String remoteFilename) {
        mRemoteFilename = remoteFilename;
    }


    public Integer getWidthPixels() {
        return mWidthPixels;
    }


    public void setWidthPixels(Integer widthPixels) {
        mWidthPixels = widthPixels;
    }


    public Integer getHeightPixels() {
        return mHeightPixels;
    }


    public void setHeightPixels(Integer heightPixels) {
        mHeightPixels = heightPixels;
    }


    public String getContentType() {
        return mContentType;
    }


    public void setContentType(String contentType) {
        mContentType = contentType;
    }


    public Integer getPrecision() {
        return mPrecision;
    }


    public void setPrecision(Integer precision) {
        mPrecision = precision;
    }


    public Wgs84Coordinate getCoordinate() {
        return mCoordinate;
    }


    public void setCoordinate(Wgs84Coordinate coordinate) {
        mCoordinate = coordinate;
    }


    public ImageSource getImageSource() {
        return mImageSource;
    }


    public void setImageSource(ImageSource imageSource) {
        mImageSource = imageSource;
    }


    public Integer getOrientation() {
        return mOrientation;
    }


    public Integer getChannelCount() {
        return mChannelCount;
    }


    public void setChannelCount(Integer channelCount) {
        mChannelCount = channelCount;
    }


    public void setOrientation(Integer orientation) {
        mOrientation = orientation;
    }
}
