package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.InOutStorageInfo;
import com.czxy.bos.service.transit.InOutStorageInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 出入库
 * Created by lenovo on 2018/10/11.
 */
@RestController
@RequestMapping("/inoutstore")
public class InOutStorageInfoController {
    @Resource
    private InOutStorageInfoService inOutStorageInfoService;


    @PostMapping
    public ResponseEntity<String> save(InOutStorageInfo inOutStorageInfo){
        //保存
        inOutStorageInfoService.save(inOutStorageInfo);
        //提示
        return  new ResponseEntity<String>("出入库操作成功", HttpStatus.OK);
    }

}
