package org.humanized.tools.database.services;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.humanized.tools.database.mapper.ImageMapper;
import org.humanized.tools.model.GUID;
import org.humanized.tools.model.Image;

public class ImageService {

    private final SqlSessionFactory mSqlSessionFactory;


    public ImageService(final SqlSessionFactory sqlSessionFactory) {
        mSqlSessionFactory = sqlSessionFactory;
    }


    public Image getById(final String imageId) {
        assert imageId != null && GUID.isValid(imageId);

        try (SqlSession session = mSqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);
            return mapper.getById(imageId);
        }
    }


    public int deleteImage(final String imageId) {
        assert imageId != null && GUID.isValid(imageId);

        try (SqlSession session = mSqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);
            return mapper.delete(imageId);
        }
    }


    public void storeImage(final Image image) {
        assert image != null;

        if (image.getId() == null || image.getId().trim().isEmpty()) {
            image.setId(GUID.generate());
        }

        try (SqlSession session = mSqlSessionFactory.openSession(true)) {
            final ImageMapper mapper = session.getMapper(ImageMapper.class);
            mapper.store(image);
        }
    }
}
