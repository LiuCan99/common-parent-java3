package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.UserMapper;
import com.czxy.bos.domain.system.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;


@Service
@Transactional
public class UserService {
     @Resource
    private UserMapper userMapper;
     //通过用户名  查找用户
    public User findUserByUsername(String username){
        //条件
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username",username);

        //查询
        return  userMapper.selectOneByExample( example );

    }

}
