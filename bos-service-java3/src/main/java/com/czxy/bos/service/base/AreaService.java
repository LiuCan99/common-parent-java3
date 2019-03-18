package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.AreaMapper;
import com.czxy.bos.domain.base.Area;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class AreaService {
      @Resource
    private AreaMapper areaMapper;

      public Integer saveAreas(List<Area>list){
          int count = 0;
          for (Area area:list) {
              //TODO 没有校验是否重复，如果重复忽略，方法返回值整数（本次添加个数）
              Area temp = areaMapper.selectByPrimaryKey(area.getId());
              if(temp != null){
                  continue;
              }
              count++;
              areaMapper.insert(area);
          }
          return  count;
      }

     //查询
     public PageInfo<Area> queryAreaListByPage(Area area, Integer page, Integer rows) {
         Example example = new Example(Area.class);
         Example.Criteria criteria = example.createCriteria();
         //添加条件
         if( area != null ){
             if(StringUtils.isNotBlank(area.getProvince())){
                 criteria.andLike("province", "%"+area.getProvince()+"%");
             }
             if(StringUtils.isNotBlank(area.getCity())){
                 criteria.andLike("city", "%"+area.getCity()+"%");
             }
             if(StringUtils.isNotBlank(area.getDistrict())){
                 criteria.andLike("district", "%"+area.getDistrict()+"%");
             }
         }

        //分页
         PageHelper.startPage(page, rows);


         List<Area> areas = this.areaMapper.selectByExample(example);// 传查询条件
         // 封装分页信息对象
         return new PageInfo<>(areas);
     }

       //增加区块

    public int save(Area area){
          return  areaMapper.insert(area);
    }
       //删除、
      public Integer delectArea(String[] ids){
        for(String id:ids){
         Area area = areaMapper.selectByPrimaryKey(id);
         if(area==null){
             throw new BosException("该操作对象不存在");
         }
        areaMapper.deleteByPrimaryKey(area);
        }
        return  ids.length;
      }
         //修改
    public int update(Area area){
return  areaMapper.updateByPrimaryKey(area);
    }


    //通过省市县查询地区
    public Area selectByArea(Area area){
        return areaMapper.selectByArea(area);
    }







}
