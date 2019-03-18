package com.czxy.bos.controller.base;

import com.czxy.bos.dao.base.SubAreaMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.SubArea;
import com.czxy.bos.service.base.CourierService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/courier")
public class CourierController {
    @Resource
    private CourierService courierService;

    @Resource
    private SubAreaMapper subAreaMapper;

    @GetMapping
    public ResponseEntity<EasyUIResult<Courier>> findCourierByPage(Integer page, Integer rows) {
        PageInfo<Courier> pageInfo = courierService.queryCourierListByPage(page, rows);
        EasyUIResult<Courier> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());
        return new ResponseEntity<EasyUIResult<Courier>>(result, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Void> save(Courier courier) {
        int count = courierService.save(courier);
        if (count == 1) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //作废   快递员
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam(name = "ids[]") Integer[] ids) {
        //删除
        int count = courierService.delectCourier(ids);
        //提示
        return new ResponseEntity<String>("本对此作废" + count + "条", HttpStatus.OK);//通用删除

    }

    //还原   工作状态
    @PutMapping
    public ResponseEntity<Void> restore(@RequestParam("ids[]") Integer[] ids) {
        courierService.restore(ids);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/findNoAssociation")
    public ResponseEntity<List<Courier>> findNoAssociation() {
        //调用业务层   查询为关联定区的快递员
        List<Courier> result = courierService.findNoAssociation();
        return new ResponseEntity<List<Courier>>(result, HttpStatus.OK);

    }

    //关联快递员
    @GetMapping("/findAssociationCourier")
    public ResponseEntity<List<Courier>> findAssociationCourier(String fixedAreaId) {
        //1 查询
        List<Courier> list = courierService.findAssociationCourier(fixedAreaId);
        //2 封装
        return new ResponseEntity<List<Courier>>(list, HttpStatus.OK);


    }
    /**
     * 查询关联的区域
     */
    @GetMapping("/associationSubarea")
    public ResponseEntity<List<SubArea>> associationSubarea(String fixedAreaId){
        // 调用业务层，查询关联快递员 列表
        List<SubArea> list=courierService.associationSubarea(fixedAreaId);

        return new ResponseEntity<List<SubArea>>(list,HttpStatus.OK);
    }



}