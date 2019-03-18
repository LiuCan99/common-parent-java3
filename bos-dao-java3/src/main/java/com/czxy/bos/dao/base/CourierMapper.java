package com.czxy.bos.dao.base;

import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.SubArea;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@org.apache.ibatis.annotations.Mapper
public interface CourierMapper extends Mapper<Courier> {
    @Select("select * from T_COURIER")
    @Results({
            @Result(id=true,property="id",column="ID"),
        @Result(property="courierNum",column="COURIER_NUM"),
        @Result(property="name",column="NAME"),
        @Result(property="telephone",column="TELEPHONE"),
        @Result(property="pda",column="PDA"),
        @Result(property="deltag",column="DELTAG"),
        @Result(property="checkPwd",column="CHECK_PWD"),
        @Result(property="type",column="TYPE"),
        @Result(property="company",column="COMPANY"),
        @Result(property="vehicleType",column="VEHICLE_TYPE"),
        @Result(property="vehicleNum",column="VEHICLE_NUM"),
        @Result(property="standardId",column="STANDARD_ID"),
        @Result(property="takeTimeId",column="TAKETIME_ID"),
            @Result(property = "standard", column = "STANDARD_ID",
                    one = @One(select = "com.czxy.bos.dao.base.StandardMapper.selectByPrimaryKey")
            )
    })
    public List<Courier> findCourierByPage();

    @Select("SELECT * FROM t_courier WHERE id NOT IN (SELECT courier_id FROM t_fixedarea_courier)")
    List<Courier> findNoAssociation();






    /**
     * 查询指定定区管理的快递员
     * @return
     */
    @Select("SELECT * FROM t_courier c , t_fixedarea_courier fc " +
            "WHERE c.id = fc.courier_id AND fc.fixed_area_id = #{fixedAreaId}")
    @Results({
            @Result(property="standard",column="standard_id",one=@One(select="com.czxy.bos.dao.base.StandardMapper.selectByPrimaryKey")),
            @Result(property="takeTime",column="taketime_id",one=@One(select="com.czxy.bos.dao.base.TakeTimeMapper.selectByPrimaryKey"))
    }
    )
    List<Courier> findAssociationCourier(@Param("fixedAreaId") String fixedAreaId);





    @Select("select * from t_sub_area where FIXEDAREA_ID=#{fixedAreaId}")
        @Results({
                @Result(property="id",column="ID"),
                @Result(property="startNum",column="START_NUM"),
                @Result(property="endNum",column="ENDNUM"),
                @Result(property="single",column="SINGLE"),
                @Result(property="keyWords",column="KEY_WORDS"),
                @Result(property="address",column="ADDRESS"),
                @Result(property="assistKeyWords",column="ASSIST_KEY_WORDS"),
                @Result(property="area",column="FIXEDAREA_ID",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey"))


        })

         /*   @Result(property="province",column="PROVINCE",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey")),
          @Result(property="city",column="CITY",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey")),
            @Result(property="district",column="DISTRICT",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey")),*/


        //    @Result(property="Area",column="areaId",one=@One(select="com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey"))
    public List<SubArea> associationSubarea(String fixedAreaId);

}


