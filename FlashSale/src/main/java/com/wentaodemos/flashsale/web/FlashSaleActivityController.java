package com.wentaodemos.flashsale.web;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.fastjson.JSON;
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleCommodityDao;
import com.wentaodemos.flashsale.db.dao.FlashSaleOderDao;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import com.wentaodemos.flashsale.entity.FlashSaleCommodity;
import com.wentaodemos.flashsale.entity.FlashSaleOrder;
import com.wentaodemos.flashsale.service.FlashSaleActivityService;
import com.wentaodemos.flashsale.service.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class FlashSaleActivityController {

    /*
     * Daos
     */
    @Resource
    private FlashSaleActivityDao activityDao;
    @Resource
    private FlashSaleCommodityDao commodityDao;
    @Autowired
    private FlashSaleOderDao orderDao;
    @Resource
    private RedisService redisService;

    /*
     * services
     */
    @Resource
    private FlashSaleActivityService activityService;

    /*
     * Non Spring fields
     */
    @RequestMapping("/flashsale/addactivity")
    public String addFlashSaleActivity(){
        return "add_activity";
    }

    @RequestMapping("/flashsale")
    public String activitiesPage(Map<String, Object> resultMap) {

        try (Entry entry = SphU.entry("flashsale")){
            List<FlashSaleActivity> activities = activityDao.queryFlashSaleActivitiesByStatus(1);
            resultMap.put("FlashSaleActivity", activities);
            System.out.println("flashsale page succeed" + activities.toString());
            return "flashsale_activity";
        } catch (Exception e){
            log.error("System busy Exception: " + e.toString());
            return "wait";
        }

    }

    @RequestMapping("/flashsale/item/{flashSaleActivityId}")
    public String itemPage(Map<String, Object> resultMap, @PathVariable long flashSaleActivityId) {
        FlashSaleActivity activity;
        FlashSaleCommodity commodity;

        String activityInfo = redisService.getValue("flashSaleActivity:" + flashSaleActivityId);
        if(StringUtils.isNotEmpty(activityInfo)){
            log.info("redis activity data: " + activityInfo);
            activity = JSON.parseObject(activityInfo, FlashSaleActivity.class);
        } else {
            activity = activityDao.queryFlashSaleActivityById(flashSaleActivityId);
        }

        String commodityInfo = redisService.getValue("flashSaleCommodity:" + activity.getCommodityId());
        if (StringUtils.isNotEmpty(commodityInfo)){
            log.info("redis commodity data: " + commodityInfo);
            commodity = JSON.parseObject(commodityInfo, FlashSaleCommodity.class);
        } else {
            commodity = commodityDao.queryFlashSaleCommodityById(activity.getCommodityId());
        }

//        FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(flashSaleActivityId);
//        System.out.println("Current Activity Name: " + activity.getName());
//        FlashSaleCommodity commodity = commodityDao.queryFlashSaleCommodityById(activity.getCommodityId());
        resultMap.put("flashSaleActivity", activity);
        resultMap.put("flashSaleCommodity", commodity);
        resultMap.put("salePrice", activity.getSalePrice());
        resultMap.put("fullPrice", activity.getFullPrice());
        resultMap.put("commodityId", activity.getCommodityId());
        resultMap.put("commodityName", commodity.getCommodityName());
        resultMap.put("commodityDesc", commodity.getCommodityDesc());

        return "flashsale_item";
    }

    @RequestMapping("/flashsale/addactivity/result")
    public String addFlashSaleActivityAction(
            @RequestParam("name") String name,
            @RequestParam("commodity_id") long commodityId,
            @RequestParam("sale_price") BigDecimal salePrice,
            @RequestParam("full_price") BigDecimal fullPrice,
            @RequestParam("amount") long amount,
            @RequestParam("start_time") String startTime,
            @RequestParam("end_time") String endTime,
            Map<String, Object> resultMap
    ) throws ParseException
    {
        startTime = startTime.substring(0, 10) + startTime.substring(11);
        endTime = endTime.substring(0, 10) + endTime.substring(11);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        FlashSaleActivity activity = new FlashSaleActivity();
        activity.setName(name);
        activity.setCommodityId(commodityId);
        activity.setSalePrice(salePrice);
        activity.setFullPrice(fullPrice);
        activity.setTotalStock(amount);
        activity.setAvailableStock(new Integer("" + amount));
        activity.setLockStock(0L);
        activity.setActivityStatus(1);
        activity.setStartTime(format.parse(startTime));
        activity.setEndTime(format.parse(endTime));
        activityDao.insertFlashSaleActivity(activity);
        resultMap.put("FlashSaleActivity", activity);
        return "add_success";
    }

    /**
     * @param userId
     * @param activityId
     * @return
     */
    @RequestMapping("/flashsale/buy/{userId}/{activityId}")
    public ModelAndView purchaseControl(@PathVariable long userId, @PathVariable long activityId) {
        log.info("order creating page");
        boolean stockValidate = false;
        ModelAndView modelAndView = new ModelAndView();
        try {
            /*
             * check if user bought before
             */
            if(redisService.isInLimitMember(activityId, userId)){
                modelAndView.addObject("resultInfo",
                        "Sorry, you already bought it before, have a look on other deals");
                modelAndView.setViewName("flashsale_result");
                return modelAndView;
            }

            /*
             * check available stock
             */
            stockValidate = activityService.flashSaleStockValidator(activityId);
            System.out.println("Stock validated: " + stockValidate);
            if(stockValidate){
                FlashSaleOrder order = activityService.createOrder(activityId, userId);
                modelAndView.addObject("resultInfo", "Succeed, we are creating your order"
                        + order.getOrderNo());
                log.info("Success, We are creating your order");
                modelAndView.addObject("orderNo", order.getOrderNo());
                //Add user to limited Member
                redisService.addLimitMember(activityId, userId);
            } else {
                log.info("No available stock");
                modelAndView.addObject("resultInfo", "No available stock!");
            }
        } catch (Exception e) {
            log.error("System error, please check error message: " + e.toString());
            modelAndView.addObject("resultInfo", "Oops! Something went wrong");
        }
        modelAndView.setViewName("flashsale_result");
        return modelAndView;
    }

    /**
     * @param orderNo
     * @return
     */


    @RequestMapping("/flashsale/order/{orderNo}")
    public ModelAndView orderQuery(@PathVariable String orderNo){
        log.info("Order Query Page");
        FlashSaleOrder order = orderDao.query(orderNo);
        ModelAndView modelAndView = new ModelAndView();

        if(order != null){
            modelAndView.setViewName("order");
            modelAndView.addObject("order", order);
            FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(order.getFlashsaleActivityId());
            modelAndView.addObject("activity", activity);
        } else {
            modelAndView.setViewName("order_wait");
        }
        return modelAndView;
    }

    @RequestMapping("/flashsale/order/pay/{orderNo}")
    public String payOrder(@PathVariable String orderNo) throws Exception {
        activityService.payOrderProcess(orderNo);
        return "redirect:/flashsale/order/" + orderNo;
    }

    @ResponseBody
    @RequestMapping("/flashsale/getSystemTime")
    public String getSystemTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        return date;
    }
}





































