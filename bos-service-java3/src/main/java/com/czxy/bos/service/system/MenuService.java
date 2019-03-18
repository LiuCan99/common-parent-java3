package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.MenuMapper;
import com.czxy.bos.domain.system.Menu;
import com.czxy.bos.domain.system.User;
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
public class MenuService {

    @Resource
    private MenuMapper menuMapper;
    /**
     * 查询菜单（含分页）
     */
    public PageInfo<Menu> findAll(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        List<Menu> list = menuMapper.selectAll();
        return new PageInfo<>(list);
    }


    /**
     * 查询所有
     * @return
     */
    public List<Menu> findAll(){

        return menuMapper.selectAll();
    }

    /**
     * 添加菜单
     * @param menu
     */
    public  void  add(Menu menu){
        //如果没有设置优先级priority,将使用当前分类中最大值+1
        if(menu.getPriority()==null){
            //当前父类id 可能为null 也可能有值
            Example example=new Example(Menu.class);
            Example.Criteria criteria=example.createCriteria();

            if(menu.getPid()==null){
                //如果pid（父id）为null 则查询为空
                criteria.andIsNull("pid");
            }else {
                //pid不为空 则判断查询相等的
                criteria.andEqualTo("pid",menu.getPid());

            }

            //添加条件
            List<Menu> list=menuMapper.selectByExample(example);
            menu.setPriority(list.size()+1);
        }
        //添加
        menuMapper.insert(menu);
    }

    /**
     * 查询用户对应的所有菜单，如果是admin就查询所有
     * @param user
     * @return
     */
    public  List<Menu> findByUser(User user){
        if("admin".equals(user.getUsername())){
            return  menuMapper.selectAll();
        }
        return menuMapper.findByUser(user.getId());
    }

}
