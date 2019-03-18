package com.czxy.crm.controller;

import com.czxy.crm.domain.Customer;
import com.czxy.crm.service.CrmCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 音老怪 on 2018/9/10.
 */
@RestController
@RequestMapping("/crmCustomer")
public class CrmCustomerController {
    @Resource
    private CrmCustomerService crmCustomerService;

    //查询没有关联的定区客户
    @GetMapping("/findNoAssociationCustomers")
    public ResponseEntity<List<Customer>> findNoAssociationCustomers() {

        //查询
        List<Customer> list = crmCustomerService.findNoAssociationCustomers();
        //封装状态码
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/findHasAssociationFixedAreaCustomers")
    public ResponseEntity<List<Customer>> findHasAssociationFixedAreaCustomers(@RequestParam("fixedAreaId") String fixedAreaId) {

        //调用service层查询
        List<Customer> list = crmCustomerService.findHasAssociationFixedAreaCustomers(fixedAreaId);
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @PostMapping("/associationCustomersToFixedArea")
    public ResponseEntity<String> associationCustomersToFixedArea(String fixedAreaId, String customerIds) {
        //操作
        crmCustomerService.associationCustomersToFixedArea(fixedAreaId, customerIds);
        //提示
        return new ResponseEntity<String>("操作成功", HttpStatus.OK);

    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customer) {

        try {
            //通过service进行操作
            System.out.println(customer.getTelephone() + "---------------");
            crmCustomerService.saveCustomer(customer);
            return new ResponseEntity<String>("注册成功", HttpStatus.CREATED); //201
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.ALREADY_REPORTED); //208
        }
    }

    @GetMapping("findByTelephone")
    public ResponseEntity<Customer> findByTelephone(String telephone) {
        //1查询
        Customer customer = crmCustomerService.findByTelephone(telephone);
        //2 封装
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);

    }

    //激活
    @GetMapping("/updateType")
    public ResponseEntity<String> updateType(String telephone) {

        try {
            //1 更新
            crmCustomerService.updateType(telephone);
            //2 提示
            return new ResponseEntity<String>("账号激活成功", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("账号激活失败", HttpStatus.ALREADY_REPORTED);
        }

    }
      //根据   手机号码   和密码   查询
    @GetMapping("/findCustomerTelephoneAndPassword")
    public ResponseEntity<Customer> findCustomerTelephoneAndPassword(String telephone , String password){
       // 1.查询
        Customer customer = this.crmCustomerService.findCustomerByTelephoneAndPassword(telephone,password);
        //2.封装
        return  new ResponseEntity<Customer>(customer,HttpStatus.OK);




    }

     //通过id和地址   查询客户
    // http://localhost:8090/crmCustomer/findFixedAreaIdByAddressAndId?address=北京天安门&customerId=1

    @GetMapping("/findFixedAreaIdByAddressAndId")
    public ResponseEntity<String> findFixedAreaIdByAddressAndId(String address , String customerId){
        //查询
        String fixedAreaId = crmCustomerService.findFixedAreaIdByAddressAndId(address,Integer.parseInt(customerId));
        //封装
        return  new ResponseEntity<String>(fixedAreaId,HttpStatus.OK);
    }


}