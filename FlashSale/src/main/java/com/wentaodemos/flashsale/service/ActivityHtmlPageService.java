package com.wentaodemos.flashsale.service;

import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleCommodityDao;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import com.wentaodemos.flashsale.entity.FlashSaleCommodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ActivityHtmlPageService {
    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private FlashSaleActivityDao activityDao;

    @Resource
    private FlashSaleCommodityDao commodityDao;

    public void createActivityHtml(long activityId) {
        PrintWriter writer = null;
        try{
            FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(activityId);
            FlashSaleCommodity commodity = commodityDao.queryFlashSaleCommodityById(activity.getCommodityId());

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("flashSaleActivity", activity);
            resultMap.put("flashSaleCommodity", commodity);
            resultMap.put("salePrice", activity.getSalePrice());
            resultMap.put("fullPrice", activity.getFullPrice());
            resultMap.put("commodityId", activity.getCommodityId());
            resultMap.put("commodityName", commodity.getCommodityName());
            resultMap.put("commodityDesc", commodity.getCommodityDesc());

            Context context = new Context();
            context.setVariables(resultMap);

            File file = new File("src/main/resources/templates/" + "item_page_" + activityId + ".html");
            writer = new PrintWriter(file);
            templateEngine.process("flashsale_item", context, writer);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("Page Static failed" + activityId);
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
