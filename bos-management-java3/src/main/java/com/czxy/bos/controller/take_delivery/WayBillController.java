package com.czxy.bos.controller.take_delivery;

import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.es.domain.ESWayBill;
import com.czxy.bos.service.take_delivery.WayBillService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.apache.http.protocol.HTTP;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/wayBill")
public class WayBillController {
    @Resource
    private WayBillService wayBillService;

    /**
     * 运单快速导入（含分页）
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/pageQuickQuery")
    public ResponseEntity<EasyUIResult<WayBill>> pageQuickQuery(Integer page, Integer rows){
        //1 查询
        PageInfo<WayBill> pageInfo = this.wayBillService.pageQuickQuery(page ,rows);
        //2 封装datagrid
        EasyUIResult result = new EasyUIResult( pageInfo.getTotal() , pageInfo.getList());
        //3 封装状态码
        return new ResponseEntity<EasyUIResult<WayBill>>(result , HttpStatus.OK);
    }

    /**
     * 运单快速录入
     * @param wayBill
     * @return
     */
    @RequiresPermissions("waybill : add")
    @PostMapping
    public ResponseEntity<String> saveWayBill(WayBill wayBill){
        //1 数据处理
        // 1.1 初始化状态值
        wayBill.setSignStatus(1);
        //2 保存
        wayBillService.saveWayBill(wayBill);

        //3 封装提示
        return new ResponseEntity<String>("录入成功!" , HttpStatus.OK);
    }

    /**
     *运单更新
     * @param wayBill
     * @return
     */
    @PutMapping
    public ResponseEntity<String> editWayBill(WayBill wayBill){
        //TODO 需要完成更新操作
        System.out.println(wayBill);
        //3 封装提示
        return new ResponseEntity<String>("快速更新成功!" , HttpStatus.OK);
    }

    /**
     * 通过运单号查询详情（运单录入）
     * 根据运单号离焦信息自动补全
     * @param wayBillNum
     * @return
     */
    @GetMapping("/findByWayBillNum")
    public ResponseEntity<WayBill> findByWayBillNum(String wayBillNum){
        //1 查询
        WayBill wayBill = this.wayBillService.findByWayBillNum(wayBillNum);
        //2 封装
        if(wayBill != null) {
            return new ResponseEntity<WayBill>(wayBill , HttpStatus.OK);
        }
        return new ResponseEntity<WayBill>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 运单管理（含分页）
     * @param wayBill
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/pageQuery")
    public ResponseEntity<EasyUIResult<ESWayBill>>pageQuery(ESWayBill wayBill ,Integer page ,Integer rows){
        //查询
        Page<ESWayBill> pageBean = this.wayBillService.pageQuery( wayBill , page , rows);
        //封装datagrid
        EasyUIResult<ESWayBill> result = new EasyUIResult<>( pageBean.getTotalElements() , pageBean.getContent());
        //状态
        return  new ResponseEntity<>(result,HttpStatus.OK);

    }

}
