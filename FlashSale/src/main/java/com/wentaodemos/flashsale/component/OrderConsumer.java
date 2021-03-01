package com.wentaodemos.flashsale.component;

import com.alibaba.fastjson.JSON;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleOderDao;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "flashsale_order", consumerGroup = "flashsale_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private FlashSaleOderDao oderDao;
    @Resource
    private FlashSaleActivityDao activityDao;


    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        /*
         * Parse the order message
         */
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        System.out.println("Message received by Consumer");
        FlashSaleOrder order = JSON.parseObject(message, FlashSaleOrder.class);
        System.out.println("Message parse by consumer");
        order.setCreateTime(new Date());
        /*
         * Deduct stock
         */
        boolean lockStockResult = activityDao.lockStock(order.getFlashsaleActivityId());
        if (lockStockResult) {
            // order status: 1-succeed, 0-failed
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(0);
        }
        log.info("Ready to receive payment.");
        oderDao.insertOrder(order);
    }
}
