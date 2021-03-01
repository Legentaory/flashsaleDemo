package com.wentaodemos.flashsale.service;

//delete after
import com.wentaodemos.flashsale.db.dao.FlashSaleActivityDao;
import com.wentaodemos.flashsale.entity.FlashSaleActivity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SimpleOverSellService {

    @Resource
    private FlashSaleActivityDao activityDao;

    public String processRequest(long activityId) {
        FlashSaleActivity activity = activityDao.queryFlashSaleActivityById(activityId);
        int availableStock = activity.getAvailableStock();
        String result;
        if(availableStock > 0){
            result = "Congratulation, you have successfully placed the order";
            System.out.println(result);
            availableStock -= 1;
            activity.setAvailableStock(availableStock);
            activityDao.updateFlashSaleActivity(activity);
        } else {
            result = "Come earlier next time!";
            System.out.println(result);
        }
        //????
        return result;
    }
}
