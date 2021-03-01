package com.wentaodemos.flashsale.component;

import java.util.*;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import com.wentaodemos.flashsale.service.util.RedisService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisPreheatRunner implements ApplicationRunner {

    @Resource
    private RedisService redisService;

    @Resource
    FlashSaleActivityDao activityDao;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<FlashSaleActivity> activities = activityDao.queryFlashSaleActivitiesByStatus(1);
        for (FlashSaleActivity activity: activities) {
            redisService.setValue("stock:" + activity.getId(), (long) activity.getAvailableStock());
            System.out.println("stock data injected into redis: " + "stock:" + activity.getId()
                    + redisService.getValue("stock:" + activity.getId()));
        }
    }
}
