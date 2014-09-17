package org.humanized.tools.importers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.apache.commons.lang3.text.WordUtils;
import org.humanized.tools.database.Database;
import org.humanized.tools.helpers.FileUtils;
import org.humanized.tools.model.GUID;
import org.humanized.tools.model.Image;
import org.humanized.tools.model.ImageSource;
import org.humanized.tools.model.Wgs84Coordinate;
import org.humanized.tools.uploaders.Uploader;
import org.humanized.tools.uploaders.UploaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class JpegImporter {
    private static final Logger mLogger = LoggerFactory.getLogger(JpegImporter.class);
    private static final String DEFAULT_JPG_EXT = ".jpg";
    private static final String MIME_TYPE_JPEG = "image/jpeg";


    public void handleNewJpeg(final File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        // Store
        mLogger.info("JpegImporter: handling file {}", file.getAbsolutePath());
        storeFile(file);
    }


    private void storeFile(File file) {
        try {
            // Construct image metadata
            final Image image = buildImage(file);

            // Try to upload base file
            for (Uploader uploader : UploaderFactory.getUploaders()) {
                uploader.uploadFile(file, image.getContentType(), image.getRemoteFilename());
            }

            // Send it off, John!
            Database.getImageService().storeImage(image);
        } catch (ImageProcessingException e) {
            mLogger.error("storeFile: failed to process file {}: {}", file, e.getMessage());
        } catch (IOException e) {
            mLogger.error("storeFile: I/O error processing file {}: {}", file, e.getMessage());
        }
    }


    private Image buildImage(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        //printImageMeta(metadata);

        // Build image object
        Image image = new Image();
        image.setId(GUID.generate());
        image.setDateCreated(new Date());
        image.setDateShot(getDateShot(metadata));
        image.setTitle(getTitle(file));
        image.setDescription(getDescription(metadata));
        image.setOriginalFilename(file.getName());
        image.setRemoteFilename(image.getId() + DEFAULT_JPG_EXT);

        if (image.getDateShot() == null) {
            image.setDateShot(image.getDateCreated());
        }
        // Store meta data in DB

        // Visual
        image.setWidthPixels(getWidth(metadata));
        image.setHeightPixels(getHeight(metadata));
        image.setPrecision(getPrecision(metadata));
        image.setChannelCount(getChannelCount(metadata));
        image.setContentType(MIME_TYPE_JPEG);
        image.setOrientation(getOrientation(metadata));

        // Coords
        image.setCoordinate(getCoordinate(metadata));

        // Source
        ImageSource source = new ImageSource();
        source.setMake(getMake(metadata));
        source.setModel(getModel(metadata));
        image.setImageSource(source);
        return image;
    }


    /**
     * Convenience function to 'prettify' (ahem..) the title from a filename
     *
     * @param file file from which to extract name. Must not be null
     * @return prettified file name
     */
    private String getTitle(final File file) {
        assert file != null;

        final String name = file.getName().replace("_", " ").replace("-", " ").replace("  ", " ");
        return WordUtils.capitalizeFully(FileUtils.removeExtension(name));
    }


    private Wgs84Coordinate getCoordinate(final Metadata metadata) {
        assert metadata != null;

        Wgs84Coordinate coordinate = null;
        GpsDirectory directory = metadata.getDirectory(GpsDirectory.class);
        if (directory != null &&
                directory.containsTag(GpsDirectory.TAG_GPS_LATITUDE) &&
                directory.containsTag(GpsDirectory.TAG_GPS_LONGITUDE)) {

            Double latitude = null;
            Double longitude = null;
            Rational[] components;

            // https://josm.openstreetmap.de/changeset/6128/josm/trunk

            components = directory.getRationalArray(GpsDirectory.TAG_GPS_LONGITUDE);
            if (components != null) {
                double deg = components[0].doubleValue();
                double min = components[1].doubleValue();
                double sec = components[2].doubleValue();
                if (Double.isNaN(deg) && Double.isNaN(min) && Double.isNaN(sec)) {
                    throw new IllegalArgumentException();
                }
                longitude = (Double.isNaN(deg) ? 0 : deg + (Double.isNaN(min) ? 0 : (min / 60)) + (Double.isNaN(sec) ? 0 : (sec / 3600)));
                if (directory.getString(GpsDirectory.TAG_GPS_LONGITUDE_REF).charAt(0) == 'W') {
                    longitude = -longitude;
                }
            } else {
                latitude = directory.getDoubleObject(GpsDirectory.TAG_GPS_LONGITUDE);
            }

            components = directory.getRationalArray(GpsDirectory.TAG_GPS_LATITUDE);
            if (components != null) {
                double deg = components[0].doubleValue();
                double min = components[1].doubleValue();
                double sec = components[2].doubleValue();
                if (Double.isNaN(deg) && Double.isNaN(min) && Double.isNaN(sec)) {
                    throw new IllegalArgumentException();
                }
                latitude = (Double.isNaN(deg) ? 0 : deg + (Double.isNaN(min) ? 0 : (min / 60)) + (Double.isNaN(sec) ? 0 : (sec / 3600)));
                if (Double.isNaN(latitude)) {
                    throw new IllegalArgumentException();
                }

                if (directory.getString(GpsDirectory.TAG_GPS_LATITUDE_REF).charAt(0) == 'S') {
                    latitude = -latitude;
                }
            } else {
                longitude = directory.getDoubleObject(GpsDirectory.TAG_GPS_LATITUDE);
            }

            if (latitude != null && longitude != null) {
                coordinate = new Wgs84Coordinate(latitude, longitude);
            }
        }
        return coordinate;
    }


    private Date getDateShot(final Metadata metadata) {
        return getExif0DateValue(metadata, ExifIFD0Directory.TAG_DATETIME);
    }


    private String getMake(final Metadata metadata) {
        return getExif0StringValue(metadata, ExifIFD0Directory.TAG_MAKE);
    }


    private String getModel(final Metadata metadata) {
        return getExif0StringValue(metadata, ExifIFD0Directory.TAG_MODEL);
    }


    private String getDescription(final Metadata metadata) {
        return getExif0StringValue(metadata, ExifIFD0Directory.TAG_IMAGE_DESCRIPTION);
    }


    private Integer getWidth(final Metadata metadata) {
        assert metadata != null;

        Integer value = null;
        ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
        if (directory != null && directory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH)) {
            value = directory.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
        } else {
            JpegDirectory dir = metadata.getDirectory(JpegDirectory.class);
            if (dir != null && dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_WIDTH)) {
                value = dir.getInteger(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
            }
        }
        return value;
    }


    private Integer getHeight(final Metadata metadata) {
        Integer value = null;
        ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
        if (directory != null && directory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
            value = directory.getInteger(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
        } else {
            JpegDirectory dir = metadata.getDirectory(JpegDirectory.class);
            if (dir != null && dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
                value = dir.getInteger(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
            }
        }
        return value;
    }


    private Integer getOrientation(final Metadata metadata) {
        return getExif0IntegerValue(metadata, ExifIFD0Directory.TAG_ORIENTATION);
    }


    private Integer getPrecision(final Metadata metadata) {
        Integer value = null;
        JpegDirectory directory = metadata.getDirectory(JpegDirectory.class);
        if (directory != null && directory.containsTag(JpegDirectory.TAG_JPEG_DATA_PRECISION)) {
            value = directory.getInteger(JpegDirectory.TAG_JPEG_DATA_PRECISION);
        }
        return value;
    }


    private Integer getChannelCount(final Metadata metadata) {
        Integer value = null;
        JpegDirectory directory = metadata.getDirectory(JpegDirectory.class);
        if (directory != null && directory.containsTag(JpegDirectory.TAG_JPEG_NUMBER_OF_COMPONENTS)) {
            value = directory.getInteger(JpegDirectory.TAG_JPEG_NUMBER_OF_COMPONENTS);
        }
        return value;
    }


    /**
     * Debugging: print everything we know from the metadata directories
     * @param metadata metadata dirs. Must not be null.
     */
    private void printImageMeta(Metadata metadata) {
        assert metadata != null;

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                mLogger.debug(tag.toString());
            }
        }
    }


    /**
     * Helper methods: try to get Date object from EXIF IFD0 directory.
     *
     * @param metadata input metadata. Must not be null.
     * @param tag      tag to fetch
     * @return value or null
     */
    private Date getExif0DateValue(Metadata metadata, int tag) {
        assert metadata != null;

        ExifIFD0Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
        Date value = null;
        if (directory != null) {
            if (directory.containsTag(tag)) {
                value = directory.getDate(tag);
            }
        }
        return value;
    }


    /**
     * Helper methods: try to get Integer object from EXIF IFD0 directory.
     *
     * @param metadata input metadata
     * @param tag      tag to fetch
     * @return value or null
     */
    private Integer getExif0IntegerValue(Metadata metadata, int tag) {
        assert metadata != null;

        ExifIFD0Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
        Integer value = null;
        if (directory != null) {
            if (directory.containsTag(tag)) {
                value = directory.getInteger(tag);
            }
        }
        return value;
    }


    /**
     * Helper methods: try to get String object from EXIF IFD0 directory.
     *
     * @param metadata input metadata
     * @param tag      tag to fetch
     * @return value or null
     */
    private String getExif0StringValue(Metadata metadata, int tag) {
        assert metadata != null;

        ExifIFD0Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
        String value = null;
        if (directory != null) {
            if (directory.containsTag(tag)) {
                value = directory.getString(tag);
            }
        }
        return value;
    }
}
