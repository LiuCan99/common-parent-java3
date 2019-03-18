package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */

@org.apache.ibatis.annotations.Mapper
public interface RoleMapper extends Mapper<Role>{

    //角色权限管理
    @Select("SELECT r.* FROM t_role r , t_user_role ur " +
            "WHERE r.id = ur.role_id AND ur.user_id = #{userId}")
    public List<Role> findByUser(@Param("userId") Integer userId);
}
