package com.wentaodemos.flashsale.service;

import com.alibaba.fastjson.JSON;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleCommodityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleOderDao;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import com.wentaodemos.flashsale.entity.FlashSaleCommodity;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import com.wentaodemos.flashsale.service.util.RedisService;
import com.wentaodemos.flashsale.service.util.RocketMQService;
import com.wentaodemos.flashsale.service.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class FlashSaleActivityService {

    @Resource
    private RedisService redisService;

    @Resource
    private FlashSaleActivityDao activityDao;

    @Resource
    private FlashSaleCommodityDao commodityDao;

    @Resource
    private RocketMQService mqService;

    @Resource
    private FlashSaleOderDao orderDao;


    private SnowFlake snowFlake = new SnowFlake(2,1);

    public boolean flashSaleStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

    public void pushFlashSaleActivityInfoToRedis (long activityId) {
        FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(activityId);
        redisService.setValue("flashSaleActivity:" + activityId, JSON.toJSONString(activity));
        log.info("redis activity injected：" + JSON.toJSONString(redisService.getValue("flashSaleActivity:" + activityId)));

        FlashSaleCommodity commodity = commodityDao.queryFlashSaleCommodityById(activity.getCommodityId());
        redisService.setValue("flashSaleCommodity:" + activity.getCommodityId(), JSON.toJSONString(commodity));
        log.info("redis commodity injected：" + JSON.toJSONString(redisService.getValue("flashSaleCommodity:" + activity.getCommodityId())));
    }

    public FlashSaleOrder createOrder(long activityId, long userId) throws Exception{
        /*
         * Create Order object
         */
        FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(activityId);
        FlashSaleOrder order = new FlashSaleOrder();
        /*
         * Identify order
         */
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setFlashsaleActivityId(activityId);
        order.setUserId(userId);
        order.setOrderAmount(activity.getSalePrice().longValue());
        /*
         * Send Message to mq
         */
        log.info("Order object created, ready to send message, order No: " + order.getOrderNo());
        mqService.sendMessage("flashsale_order", JSON.toJSONString(order));
        mqService.sendDelayedMessage("pay_check", JSON.toJSONString(order), 3);
        return order;
    }

    public void payOrderProcess(String orderNo) throws Exception{
        log.info("Payment Page Started");
        FlashSaleOrder order = orderDao.query(orderNo);

        /*
         * check order payed and valid
         */
        if(order == null){
            log.error("Order No not exist. Requested Order No:" + orderNo);
            return;
        } else if(order.getOrderStatus() != 1){
            log.error("Invalid order (Time out!)" + orderNo);
            return;
        }

        order.setPayTime(new Date());

        order.setOrderStatus(2);
        orderDao.updateOder(order);

        try{
            mqService.sendMessage("pay_done", JSON.toJSONString(order));
        } catch (Exception e){
            log.error("Failed to send message to Pay_done broker: check message: " + e.toString());
        }

    }
}
