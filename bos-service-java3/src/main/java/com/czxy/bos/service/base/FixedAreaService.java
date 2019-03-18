package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.CourierMapper;
import com.czxy.bos.dao.base.FixedAreaCourierMapper;
import com.czxy.bos.dao.base.FixedAreaMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.FixedArea;
import com.czxy.bos.domain.base.FixedAreaCourier;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class FixedAreaService {
    @Resource
    private FixedAreaMapper fixedAreaMapper;
    @Resource
    private CourierMapper courierMapper;

    @Resource
    private FixedAreaCourierMapper fixedAreaCourierMapper;

    public Integer saveFixedArea(FixedArea fixedArea) {
        Example nameExample = new Example(FixedArea.class);
        Example.Criteria nameCriteria = nameExample.createCriteria();
        nameCriteria.andEqualTo("fixedAreaName", fixedArea.getFixedAreaName());
        FixedArea nameFixedArea = fixedAreaMapper.selectOneByExample(nameExample);
        if (nameFixedArea != null) {
            throw new BosException("定区名称已存在");
        }
        Example leaderExample = new Example(FixedArea.class);
        Example.Criteria leaderCriteria = leaderExample.createCriteria();
        leaderCriteria.andEqualTo("fixedAreaLeader", fixedArea.getFixedAreaLeader());
        FixedArea leaderFixedArea = fixedAreaMapper.selectOneByExample(leaderExample);
        if (leaderFixedArea != null) {
            throw new BosException("定区的负责人已存在");

        }
        fixedArea.setId(UUID.randomUUID().toString());

        //添加
        return fixedAreaMapper.insert(fixedArea);
    }

    //查询  模糊查询
    public PageInfo<FixedArea> findAllByPage(FixedArea fixedArea, Integer page, Integer rows) {
        Example example = new Example(FixedArea.class);
        Example.Criteria criteria = example.createCriteria();
        if (fixedArea != null) {
            if(StringUtils.isNotBlank(fixedArea.getId())){
                criteria.andLike("id","%"+fixedArea.getId()+"%");
            }
            if(StringUtils.isNotBlank(fixedArea.getCompany())){
                criteria.andLike("company","%"+fixedArea.getCompany()+"%");
            }

        }
        PageHelper.startPage(page,rows);
        List<FixedArea> fixedAreas = this.fixedAreaMapper.selectByExample(example);
        return  new PageInfo<>(fixedAreas);
    }
    //修改
    public int update(FixedArea fixedArea){
        return  fixedAreaMapper.updateByPrimaryKey(fixedArea);
    }

//关联的快递员
    public void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId) {
        //1 更新快递员的收派时间
        // 1.1 快递员查询
        Courier courier = courierMapper.selectByPrimaryKey(courierId);
        //1.2 设置收派时间
        courier.setTakeTimeId(takeTimeId);
        //1.3 更新快要
        courierMapper.updateByPrimaryKey(courier);

        //2 快递员定区表中添加一条关联数据
        FixedAreaCourier fixedAreaCourier = new FixedAreaCourier();
        fixedAreaCourier.setCourierId(courierId);
        fixedAreaCourier.setFixedAreaId(fixedAreaId);
        fixedAreaCourierMapper.insert(fixedAreaCourier);

    }

}
