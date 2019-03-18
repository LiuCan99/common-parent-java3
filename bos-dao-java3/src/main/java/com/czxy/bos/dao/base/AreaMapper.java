package com.czxy.bos.dao.base;

import com.czxy.bos.domain.base.Area;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


@org.apache.ibatis.annotations.Mapper
public interface AreaMapper extends Mapper<Area> {

    @Select("SELECT * FROM t_area WHERE province=#{province} AND city =#{city} AND district=#{district}")
    public Area selectByArea(Area area);
}
