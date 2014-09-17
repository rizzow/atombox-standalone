package org.humanized.tools.database;

import org.humanized.tools.database.mybatis.ConnectionFactory;
import org.humanized.tools.database.services.ImageService;

import java.util.Properties;

public class Database {

    private static ImageService mImageService;


    public static void initialize(final Properties properties) {
        ConnectionFactory factory = new ConnectionFactory(properties);
        mImageService = new ImageService(factory.getSqlSessionFactory());
    }

    ////////////////////////////////////////////
    /// Add database service access methods here


    public static ImageService getImageService() {
        return mImageService;
    }
}
