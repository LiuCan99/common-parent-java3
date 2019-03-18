package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.VehicleMapper;
import com.czxy.bos.domain.base.Vehicle;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lenovo on 2018/10/19.
 */
@Service
@Transactional
public class VehicleService {

    @Resource
    private VehicleMapper vehicleMapper;

    public PageInfo<Vehicle> find(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        List<Vehicle> list=vehicleMapper.selectAll();
        return new PageInfo<>(list);
    }
}
