package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.CourierMapper;
import com.czxy.bos.dao.base.SubAreaMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.SubArea;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class CourierService {
    @Resource
    private CourierMapper courierMapper;

    @Resource
    private SubAreaMapper subAreaMapper;

    //查询  所有快递员
    public PageInfo<Courier> queryCourierListByPage(Integer pageNum, Integer pageSize) {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Courier> list = courierMapper.findCourierByPage();

        return new PageInfo<>(list);

    }

    //添加快递员
    public int save(Courier courier) {


        Example example = new Example(Courier.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("courierNum", courier.getCourierNum());
        Courier findCourier = courierMapper.selectOneByExample(example);
        if (findCourier != null) {
            throw new BosException("快递员账号已存在");
        }
        return courierMapper.insert(courier);
    }

    //删除
    public Integer delectCourier(Integer[] ids) {
        for (Integer id : ids) {
            Courier courier = courierMapper.selectByPrimaryKey(id);
            if (courier == null) {
                throw new BosException("操作对象不存在");
            }
            //修改标记
            courier.setDeltag('1');//1代表作废
            //更新操作
            courierMapper.updateByPrimaryKey(courier);
        }
        return ids.length;
    }

    //还原
    public int restore(Integer[] ids) {
        for (Integer id : ids) {
            Courier courier = courierMapper.selectByPrimaryKey(id);
            courier.setDeltag(null);
            courierMapper.updateByPrimaryKey(courier);
        }
        return ids.length;

    }


    //查询   未关联的快递员
    public List<Courier> findNoAssociation() {
        List<Courier> list = courierMapper.findNoAssociation();
        for (Courier c : list) {
            c.setInfo(c.getName() + "(" + c.getCompany() + ")");
        }
        return list;
    }

    /**
     * 查询定区关联的快递员
     *
     * @param fixedAreaId
     * @return
     */
    public List<Courier> findAssociationCourier(String fixedAreaId) {
        return courierMapper.findAssociationCourier(fixedAreaId);
    }

    public List<SubArea> associationSubarea(String fixedAreaId) {
        List<SubArea> list =  subAreaMapper.findAll(fixedAreaId);

        return list;
    }
}