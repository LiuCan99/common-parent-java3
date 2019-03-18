package com.czxy.bos.controller.base;

import com.czxy.bos.domain.base.Vehicle;
import com.czxy.bos.service.base.VehicleService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2018/10/19.
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Resource
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<EasyUIResult<Vehicle>> find(Integer page ,Integer rows){
        PageInfo<Vehicle> pageInfo=vehicleService.find(page, rows);
        EasyUIResult result=new EasyUIResult<>(pageInfo.getTotal(),pageInfo.getList());
        return  new ResponseEntity<EasyUIResult<Vehicle>>(result, HttpStatus.OK);
    }

}
