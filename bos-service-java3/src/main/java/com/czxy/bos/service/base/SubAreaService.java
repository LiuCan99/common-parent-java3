package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.SubAreaMapper;
import com.czxy.bos.domain.base.SubArea;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class SubAreaService {

    @Resource
    private SubAreaMapper subAreaMapper;
    //通过区域id查询所有的子区域（分区）

    public List<SubArea> findAllByAreaId(String areaId){
        //条件
        Example example = new Example(SubArea.class);
        example.createCriteria().andEqualTo("areaId",areaId);
        //查询
        return  this.subAreaMapper.selectByExample( example );

    }





}
