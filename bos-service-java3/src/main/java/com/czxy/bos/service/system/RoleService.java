package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.RoleMapper;
import com.czxy.bos.dao.system.RoleMenuMapper;
import com.czxy.bos.dao.system.RolePermissionMapper;
import com.czxy.bos.domain.system.Role;
import com.czxy.bos.domain.system.RoleMenu;
import com.czxy.bos.domain.system.RolePermission;
import com.czxy.bos.domain.system.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */
@Service
@Transactional

public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Role> findAllRoleList(Integer page ,Integer rows){
        PageHelper.startPage( page ,rows );
        List<Role> list = this.roleMapper.selectAll();
        return new PageInfo<>(list);
    }


    /**
     * 通过用户id查询所有角色（如果是admin查询所有）
     */

    public List<Role> findByUser(User user){
        //1 如果是admin查询所有
        if ("admin".equals(user.getUsername())){
            return  roleMapper.selectAll();
        }

        //如果不是根据user id 查询
        return  roleMapper.findByUser(user.getId());

    }

    /**
     *
     * @param role 角色基本信息
     * @param menuIds 关联所有菜单的id，使用逗号分隔
     * @param permissionIds 关联所有权限的id
     */
    public void saveRole(Role role , String menuIds , String[] permissionIds){
        // 保存角色 ，在JavaBean中设置 @GeneratedValue(generator = "JDBC")  ，保存之后role就可以直接获得id值了
        roleMapper.insert(role);

        // 保存菜单关联，将使用逗号拆分字符串，并遍历
        String[] menuArr = menuIds.split(",");
        for(String menuId : menuArr ){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(Integer.parseInt(menuId));
            roleMenu.setRoleId(role.getId());
            roleMenuMapper.insert(roleMenu);
        }

        // 保存权限管理，遍历处理
        for (String permId: permissionIds ) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId( role.getId() );
            rolePermission.setPermissionId(Integer.parseInt( permId ));
            rolePermissionMapper.insert(rolePermission);
        }
    }
}
