package com.wentaodemos.flashsale.component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_done", consumerGroup = "pay_done_group")
public class PayDoneConsumer implements RocketMQListener<MessageExt> {

    @Resource
    private FlashSaleActivityDao activityDao;

    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("received order creation request: ");
        FlashSaleOrder order = JSON.parseObject(message, FlashSaleOrder.class);
        activityDao.deductStock(order.getFlashsaleActivityId());
    }
}
