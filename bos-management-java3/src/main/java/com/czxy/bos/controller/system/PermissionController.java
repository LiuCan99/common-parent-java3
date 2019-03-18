package com.czxy.bos.controller.system;

import com.czxy.bos.domain.system.Permission;
import com.czxy.bos.service.system.PermissionService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lenovo on 2018/10/8.
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<EasyUIResult<Permission>> findAll( Integer page, Integer rows){

        PageInfo<Permission> pageInfo = permissionService.findAll(page, rows);
        EasyUIResult<Permission> result=new EasyUIResult<>(pageInfo.getTotal(),pageInfo.getList());
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<String> savePermission(Permission permission){
        permissionService.savePermission(permission);
        return new ResponseEntity<String>("权限添加成功",HttpStatus.OK);
    }
    /**
     * 查询所有
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Permission>> findAllPermissionList(){
        //查询所有
        List<Permission> list =  this.permissionService.findAllPermissionList();
        //封装
        return new ResponseEntity<List<Permission>>( list , HttpStatus.OK);
    }

}
