package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.DeliveryInfo;
import com.czxy.bos.service.transit.DeliveryInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2018/10/12.
 */
@RestController
@RequestMapping("/deliveryInfo")
public class DeliveryInfoController {

    @Resource
    private DeliveryInfoService deliveryInfoService;

    public ResponseEntity<String> save(String transitInfoId , DeliveryInfo deliveryInfo){
        //保存
        deliveryInfoService.save(transitInfoId,deliveryInfo);

        //提示
        return  new ResponseEntity<String>("配送信息设置成功", HttpStatus.OK);


    }
}
