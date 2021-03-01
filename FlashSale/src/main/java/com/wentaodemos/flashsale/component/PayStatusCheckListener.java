package com.wentaodemos.flashsale.component;

import com.alibaba.fastjson.JSON;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleOderDao;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import com.wentaodemos.flashsale.service.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "pay_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {

    @Resource
    private FlashSaleOderDao orderDao;
    @Resource
    private FlashSaleActivityDao activityDao;
    @Resource
    private RedisService redisService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Received timeout message, checking payment status");
        FlashSaleOrder order = JSON.parseObject(message, FlashSaleOrder.class);
        //query order
        FlashSaleOrder orderInfo = orderDao.query(order.getOrderNo());

        //check payed
        if(orderInfo.getOrderStatus() != 2) {
            log.warn("No payment put, order failed");
            orderInfo.setOrderStatus(99);
            orderDao.updateOder(orderInfo);

            //Payment not received, restore stock and remove user from limit member database
            activityDao.revertStock(order.getFlashsaleActivityId());

            redisService.revertStock("stock:" + order.getFlashsaleActivityId());

            redisService.removeLimitMember(order.getFlashsaleActivityId(), order.getUserId());
        }
        log.info("Check payment succeed.");
    }
}
