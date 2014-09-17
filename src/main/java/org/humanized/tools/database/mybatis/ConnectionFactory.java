package org.humanized.tools.database.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ConnectionFactory {

    private static final String PROP_KEY_MYBATIS_XML_FILE = "database.mybatis.config_file";

    private final SqlSessionFactory mSqlSessionFactory;


    public ConnectionFactory(final Properties properties) {
        final String resource = properties.getProperty(PROP_KEY_MYBATIS_XML_FILE);
        if (resource == null || resource.isEmpty()) {
            throw new IllegalStateException("Missing key " + PROP_KEY_MYBATIS_XML_FILE);
        }

        try {
            final Reader reader = Resources.getResourceAsReader(resource);
            mSqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SqlSessionFactory getSqlSessionFactory() {
        return mSqlSessionFactory;
    }

}
