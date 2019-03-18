package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.StandardMapper;
import com.czxy.bos.domain.base.Standard;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class StandardService {
    @Resource
    private StandardMapper standardMapper;

    public PageInfo<Standard> queryStandardByPage(int page,int rows){
        //分页
        PageHelper.startPage(page,rows);
        List<Standard> list = standardMapper.selectAll();
        return  new PageInfo<>(list);
    }
      //添加
    public Integer saveStandard(Standard standard){
        return  standardMapper.insert(standard);
    }
     //修改
    public int update(Standard standard){
        return  standardMapper.updateByPrimaryKey(standard);

    }
    //删除

    public void deleteStandard(String [] ids){
        for (String idStr:ids) {
            //TODO 查询“收派标准”是否关联快递员
            // throw new BosException("不能删除，管理快递员");
            Integer id = Integer.parseInt(idStr);
            standardMapper.deleteByPrimaryKey(id);
        }
    }
    //查询所有快递员
    public List<Standard> findAll() {
      return  standardMapper.selectAll();

    }

    }
