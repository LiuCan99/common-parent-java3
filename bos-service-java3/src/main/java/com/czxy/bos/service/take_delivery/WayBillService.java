package com.czxy.bos.service.take_delivery;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.domain.report.HighChart;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.es.domain.ESWayBill;
import com.czxy.bos.es.repository.WayBillRepository;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;



@Service
@Transactional
public class WayBillService {
    @Resource
    private WayBillMapper wayBillMapper;
    @Resource
    private WayBillRepository wayBillRepository;


    public PageInfo<WayBill> pageQuickQuery(Integer page , Integer rows){
        //1 设置分页
        PageHelper.startPage(page ,rows);
        //2 查询
        List<WayBill> list = this.wayBillMapper.selectAll();
        //3 封装
        return new PageInfo<>(list);
    }

    /**
     * 如果运单id存放，表示为快速录入的数据，此处进行的是更新操作
     * 如果运单id不存在，表示通过订单直接录入的运单新数据，为添加操作。
     * @param wayBill
     */
    public void saveWayBill(WayBill wayBill){
        if(wayBill.getId() != null ){
            //更新
            wayBillMapper.updateByPrimaryKey( wayBill );
        } else {
            //添加
            //1 校验：查询订单号不能重复
            Example example = new Example(WayBill.class);
            example.createCriteria().andEqualTo("wayBillNum",wayBill.getWayBillNum());
            WayBill temp = wayBillMapper.selectOneByExample(example);
            if(temp != null){
                throw new BosException("运单号已存在");
            }
            //2 保存
            wayBillMapper.insert(wayBill);
        }
        //将WayBill运单中的所有数据--> ESWayBill，最后将ESWayBill添加到elasticsearch
        ESWayBill esWayBill = new ESWayBill();
        BeanUtils.copyProperties(wayBill, esWayBill);
        this.wayBillRepository.save( esWayBill );

    }
    /**
     * 通过运单号查询运单
     * @param wayBillNum
     * @return
     */
    public WayBill findByWayBillNum(String wayBillNum){
        //1 条件
        Example example = new Example(WayBill.class);
        example.createCriteria()
                .andEqualTo("wayBillNum",wayBillNum);
        //2 查询
        return this.wayBillMapper.selectOneByExample( example );
    }



    public Page<ESWayBill>  pageQuery(ESWayBill wayBill , Integer page , Integer rows){
        //条件封装对象
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        //处理运单号
        if(StringUtils.isNotBlank( wayBill.getWayBillNum() )){
            queryBuilder.must( QueryBuilders.matchPhraseQuery("wayBillNum" , wayBill.getWayBillNum()));
        }
        //发货地，模糊查询
        if(StringUtils.isNotBlank(wayBill.getSendAddress())){
            queryBuilder.must(QueryBuilders.matchQuery("sendAddress",wayBill.getSendAddress()));
        }

        //收货地
        if(StringUtils.isNotBlank(wayBill.getRecAddress())){
            queryBuilder.must(QueryBuilders.matchQuery("recAddress",wayBill.getSendAddress()));
        }

        //速运类型
        if(StringUtils.isNotBlank(wayBill.getSendProNum())){
            queryBuilder.must(QueryBuilders.matchPhraseQuery("sendProNum",wayBill.getSendProNum()));
        }

        //签到状态
        if(wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0){
            queryBuilder.must(QueryBuilders.matchPhraseQuery("signStatus",wayBill.getSignStatus()));
        }
        //2 搜索封装对象
        SearchQuery searchQuery = new NativeSearchQuery( queryBuilder );
        //2.1 设置分页
        searchQuery.setPageable(PageRequest.of( page-1 , rows));


        //3 查询
        return this.wayBillRepository.search( searchQuery );
    }

    /**
     * 查询所有
     * @return
     */
    public  List<WayBill> findAll(){
        return this.wayBillMapper.selectAll();
    }
    public List<HighChart> chartWayBill() {
        return wayBillMapper.findBySignStatus();
    }


}
