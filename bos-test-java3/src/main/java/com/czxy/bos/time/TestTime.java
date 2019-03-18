package com.czxy.bos.time;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by 音老怪 on 2018/9/19.
 */
@Component
public class TestTime {
    @Scheduled(cron = "0/3* * * ?")
    public void demo(){
        System.out.println(System.currentTimeMillis());
    }


}
