package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */
@org.apache.ibatis.annotations.Mapper
public interface PermissionMapper extends Mapper<Permission> {


    @Select("SELECT * FROM t_permission p " +
            "INNER JOIN t_role_permission rp ON p.id = rp.permission_id\n" +
            "INNER JOIN t_role r ON rp.role_id = r.id " +
            "INNER JOIN t_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    public List<Permission> findByUser(@Param("userId") Integer userId);
}
