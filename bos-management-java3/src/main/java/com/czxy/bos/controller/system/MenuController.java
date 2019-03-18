package com.czxy.bos.controller.system;

import com.czxy.bos.domain.system.Menu;
import com.czxy.bos.domain.system.User;
import com.czxy.bos.service.system.MenuService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<EasyUIResult<Menu>>findAll(Integer page,Integer rows){
        PageInfo<Menu> pageInfo=menuService.findAll(page, rows);
        EasyUIResult result=new EasyUIResult<>(pageInfo.getTotal(),pageInfo.getList());
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询父菜单单项
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Menu>> findAll (){
        List<Menu> list = this.menuService.findAll();
        return new ResponseEntity<List<Menu>>( list ,HttpStatus.OK) ;
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public  ResponseEntity<List<Menu>> add(Menu menu){
        List<Menu> list = this.menuService.findAll();
        return new ResponseEntity<List<Menu>>( list ,HttpStatus.OK) ;

    }
    /**
     *
     */
    @GetMapping("/showMenu")
    public  ResponseEntity<List<Menu>> showMenu(){
        //1 获得当前登陆用户 （方式1：从session获得， 方式2：从shiro获得）
        //User user = (User)session.getAttribute("user");
        User user=(User) SecurityUtils.getSubject().getPrincipal();

        //2 查询所有菜单
        List<Menu>list=menuService.findByUser(user);

        //3 封装状态
        return  new ResponseEntity<List<Menu>>(list,HttpStatus.OK);
    }



}
