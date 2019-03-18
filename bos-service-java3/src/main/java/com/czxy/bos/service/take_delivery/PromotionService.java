package com.czxy.bos.service.take_delivery;

import com.czxy.bos.dao.take_delivery.PromotionMapper;
import com.czxy.bos.domain.take_delivery.Promotion;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class PromotionService {
    @Resource
    private PromotionMapper promotionMapper;

    public void save(Promotion promotion){
        promotionMapper.insert(promotion);
    }
    //查询
    public PageInfo<Promotion> queryPromotionListByPage(Integer page, Integer rows) {
        // 使用分页助手开始分页,指定两个参数：当前页码，每页条数
        PageHelper.startPage(page, rows);
        // 然后分页拦截器会自动对接下来的查询进行分页
        List<Promotion> promotions = this.promotionMapper.selectAll();
        // 封装分页信息对象
        return new PageInfo<>(promotions);
    }

        //前段分页
    public List<Promotion>findAll(){

        Example example = new Example(Promotion.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status","1");
        return  this.promotionMapper.selectByExample(example);
    }

    public void updateWithEndDate(){
        promotionMapper.updateWithEndDate();
    }

}
