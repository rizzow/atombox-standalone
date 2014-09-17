package org.humanized.tools.database.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.humanized.tools.model.Image;

public interface ImageMapper {

    @Select("SELECT * FROM images WHERE id = #{id}")
    Image getById(@Param("id") final String imageId);

    @Delete("DELETE FROM images WHERE id = #{id}")
    int delete(@Param("id") final String imageId);

    int store(Image image);
}
