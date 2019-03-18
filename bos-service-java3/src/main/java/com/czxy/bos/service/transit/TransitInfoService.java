package com.czxy.bos.service.transit;

import com.czxy.bos.dao.take_delivery.WayBillMapper;
import com.czxy.bos.dao.transit.TransitInfoMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.bos.es.domain.ESWayBill;
import com.czxy.bos.es.repository.WayBillRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 开启中转配送
 * Created by lenovo on 2018/10/9.
 */
@Service
@Transactional
public class TransitInfoService {

    @Resource
    private TransitInfoMapper transitInfoMapper;

    @Resource
    private WayBillMapper wayBillMapper;

    @Resource
    private WayBillRepository wayBillRepository;

    /**
     * 根据运单信息，创建“运输配送信息”
     * @param wayBillIds
     */
    public void createTransitInfo(String wayBillIds){

        //使用逗号拆分，并遍历
        String[] ids = wayBillIds.split(",");
        for(String wayBillId : ids){

            //通过id查询运单
            WayBill wayBill = this.wayBillMapper.selectByPrimaryKey(wayBillId);

            System.out.println("jjjjjj"+wayBill.getSignStatus());

            //如果状态为1继续操作
            if(wayBill.getSignStatus() == 1){
                //创建“运输配送信息”
                TransitInfo transitInfo = new TransitInfo();
                transitInfo.setWayBillId(Integer.parseInt(wayBillId));
                transitInfo.setStatus("出入库中转");
                transitInfoMapper.insert(transitInfo);

                //修改运单状态2（派送中）
                wayBill.setSignStatus(2);
                wayBillMapper.updateByPrimaryKey( wayBill );

                //将数据同步到ES
                ESWayBill esWayBill = new ESWayBill();
                BeanUtils.copyProperties(wayBill , esWayBill);
                wayBillRepository.save(esWayBill);
            }

        }

    }

    /**
     * 查询运输配送信息
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<TransitInfo> pageQuery(Integer page ,Integer rows){
        //1 设置分页
        PageHelper.startPage( page , rows);
        //2 查询所有
        List<TransitInfo> list = this.transitInfoMapper.selectAll();
        // 遍历所有，通过wayBillId查询对应的运单
        for(TransitInfo transitInfo : list){
            WayBill wayBill = this.wayBillMapper.selectByPrimaryKey( transitInfo.getWayBillId() );
            transitInfo.setWayBill(wayBill);
        }
        //3 封装
        return new PageInfo<>( list );
    }

}
