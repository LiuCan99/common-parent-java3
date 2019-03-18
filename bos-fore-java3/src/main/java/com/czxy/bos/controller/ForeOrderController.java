package com.czxy.bos.controller;
import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.crm.domain.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/foreOrder")
public class ForeOrderController {


    @Resource
    private RestTemplate restTemplate;

    @Resource
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity<String> addOrder(Order order ,String recAreaInfo,String sendAreaInfo){

        //1 处理数据
        //1.1 收获地址 recAreaInfo = "江苏省/宿迁市/沭阳县"
        String[] recArr = recAreaInfo.split("/");
        Area recArea = new Area();
        recArea.setProvince(recArr[0]);
        recArea.setCity(recArr[1]);
        recArea.setDistrict(recArr[2]);
        order.setRecArea(recArea);
        //1.2 发货地址 sendAreaInfo
        String[] sendArr = sendAreaInfo.split("/");
        Area sendArea = new Area();
        sendArea.setProvince(sendArr[0]);
        sendArea.setCity(sendArr[1]);
        sendArea.setDistrict(sendArr[2]);
        order.setSendArea(sendArea);
        //1.3 登陆客户,从session获得登陆用户的信息
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if(customer == null){
            return new ResponseEntity<String>("请先登录", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //设置用户id
        order.setCustomer_id(customer.getId());


        //知己访问bos后端
        String url = "http://localhost:8088/bosOrder";
        System.out.println(order);
        return restTemplate.postForEntity(url ,order ,String.class);
    }

}
