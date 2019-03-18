package com.czxy.bos.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mytest")

public class TestController {

    @GetMapping
    public String demo(){
        System.out.println("get");
        return  null;
    }
    @PostMapping
    public String demo2(){
        System.out.println("post");
        return null;
    }

    /**
     * http://localhost:8086/mytest
     * 需要注意：请求方式必须是put
     * @return
     */
    @PutMapping
    public String demo3(){
        System.out.println("put");
        return null;
    }
    /**
     * http://localhost:8086/mytest
     * 需要注意：请求方式必须是delete
     * @return
     */
    @DeleteMapping
    public String demo4(){
        System.out.println("delete");
        return null;
    }
    @GetMapping(value="/{id}")
    public String demo5(@PathVariable("id") Integer id ){
        System.out.println("id : " + id);
        return null;
    }}
