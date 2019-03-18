package com.czxy.bos.controller.system;

import com.czxy.bos.domain.system.User;
import com.czxy.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 后台bos登陆
 */
@RestController
@RequestMapping("/user")
public class UserController {
       @Resource
       private UserService userService;

       @GetMapping("/login")
       public ResponseEntity<String> login(User user, HttpSession session){
           //1实用工具  进行登录
           SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsername() ,user.getPassword()));
          //将登录的用户名信息   存放到session里面
           User loginUser =  (User)SecurityUtils.getSubject().getPrincipal();
           session.setAttribute("user" , loginUser );

           //3 提示
           return new ResponseEntity<String>("登录成功", HttpStatus.OK);


       }

}
