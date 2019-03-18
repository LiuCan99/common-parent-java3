package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */
@org.apache.ibatis.annotations.Mapper
public interface MenuMapper extends Mapper<Menu> {

    @Select("SELECT * FROM t_menu m " +
            "INNER JOIN t_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN t_role r ON rm.role_id = r.id " +
            "INNER JOIN t_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} ORDER BY m.priority ")
    public List<Menu> findByUser(@Param("userId") Integer userId);
}
