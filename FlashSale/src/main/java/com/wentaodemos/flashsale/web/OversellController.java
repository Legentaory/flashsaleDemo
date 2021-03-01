package com.wentaodemos.flashsale.web;

import com.wentaodemos.flashsale.service.FlashSaleActivityService;
import com.wentaodemos.flashsale.service.SimpleOverSellService;
import com.wentaodemos.flashsale.service.util.RedisService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class OversellController {

    @Resource
    private SimpleOverSellService simpleOverSellService;
    @Resource
    private FlashSaleActivityService flashSaleActivityService;

    @ResponseBody
    @RequestMapping("/flashsale/simple/{flashSaleActivityId}")
    public String stockReduction(@PathVariable long flashSaleActivityId) {
        return simpleOverSellService.processRequest(flashSaleActivityId);
    }


    @ResponseBody
    @RequestMapping("/flashsale/validate/{flashSaleActivityId}")
    public String flashSaleStockControl(@PathVariable long flashSaleActivityId){
        boolean stockValidationResult = flashSaleActivityService.flashSaleStockValidator(flashSaleActivityId);
        return stockValidationResult ? "Succeed" : "Come earlier next time";
    }
}
