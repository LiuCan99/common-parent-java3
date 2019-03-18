package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.TransitInfo;
import com.czxy.bos.service.transit.TransitInfoService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 开启中转配送
 * Created by lenovo on 2018/10/9.
 */
@RestController
@RequestMapping("/transit")
public class TransitController {

    @Resource
    private TransitInfoService transitInfoService;

    /**
     * 开始配送
     * @param wayBillIds
     * @return
     */
    @PostMapping
    public ResponseEntity<String> createTransitInfo(String wayBillIds){
        //保存
        this.transitInfoService.createTransitInfo( wayBillIds );
        //提示
        return new ResponseEntity<String>("开始中转配送", HttpStatus.OK);
    }

    /**
     * 查询运输配送信息列表
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/pageQuery")
    public ResponseEntity<EasyUIResult<TransitInfo>> pageQuery(Integer page , Integer rows){
        PageInfo<TransitInfo> pageInfo = this.transitInfoService.pageQuery( page ,rows);
        EasyUIResult result = new EasyUIResult( pageInfo.getTotal() , pageInfo.getList() );
        return new ResponseEntity<EasyUIResult<TransitInfo>>( result , HttpStatus.OK );
    }

}
