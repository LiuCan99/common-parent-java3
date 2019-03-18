package com.czxy.bos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {


    @RequestMapping("/{page}")
    public String home(@PathVariable("page") String page ){
        return page;
    }


    @RequestMapping("/pages/{dir}/{page}")
    public String pages(@PathVariable("dir") String dir , @PathVariable("page") String page ){
        return "/pages/"+dir+"/" + page;
    }
}
