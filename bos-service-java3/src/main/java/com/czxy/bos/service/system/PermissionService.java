package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.PermissionMapper;
import com.czxy.bos.domain.system.Permission;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */

@Service
@Transactional
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询指定用户授权的所有权限（如果是admin用户，将查询所有权限）
     * @return
     */
    public List<Permission> findByUser(User user){
        if("admin".equals(user.getUsername())){
            return permissionMapper.selectAll();
        }
        return this.permissionMapper.findByUser(user.getId());
    }


    /**
     * 权限查询所有（含分页）
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Permission> findAll(Integer page,Integer rows){

        PageHelper.startPage(page,rows);
        List<Permission> list=permissionMapper.selectAll();
        return new PageInfo<>(list);
    }


    /**
     * 添加权限
     * @param permission
     */
    public void savePermission(Permission permission){

        //校验：权限名称
        Example NameExample=new Example(Permission.class);
        Example.Criteria namecriteria=NameExample.createCriteria();
        namecriteria.andEqualTo("name",permission.getName());
         Permission permission1= permissionMapper.selectOneByExample(NameExample);
        if(permission1 !=null){
                throw  new BosException("权限名称已被占用");
        }

        //校验：关键字

        Example keyExample=new Example(Permission.class);
        Example.Criteria keycriteria=keyExample.createCriteria();
        keycriteria.andEqualTo("name",permission.getName());
        Permission permission2= permissionMapper.selectOneByExample(keyExample);
        if(permission2 !=null){
            throw  new BosException("权限关键字已被占用");
        }

        permissionMapper.insert(permission);
    }

    /**
     * 查询所有
     * @return
     */
    public List<Permission> findAllPermissionList(){

        return this.permissionMapper.selectAll();
    }

}
