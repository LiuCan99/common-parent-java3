package com.czxy.bos.dao.base;

import com.czxy.bos.domain.base.SubArea;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@org.apache.ibatis.annotations.Mapper
public interface SubAreaMapper extends Mapper<SubArea>{

    @Select("select * from t_sub_area where FIXEDAREA_ID=#{fixedAreaId}")
    @Results({
            @Result(property="id",column="ID"),
            @Result(property="startNum",column="START_NUM"),
            @Result(property="endNum",column="ENDNUM"),
            @Result(property="single",column="SINGLE"),
            @Result(property="keyWords",column="KEY_WORDS"),
            @Result(property="address",column="ADDRESS"),
            @Result(property="assistKeyWords",column="ASSIST_KEY_WORDS"),
            @Result(property="area",column="AREA_ID",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey"))


    })
    public List<SubArea> findAll(String fixedAreaId);

}
