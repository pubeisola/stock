package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaTaskList;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaStockPositionList;
import javax.servlet.http.HttpServletRequest;


import cc.mrbird.febs.api.service.impl.message.StockMsg;
import org.apache.rocketmq.client.producer.SendResult;

/**
 *  Service接口
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
public interface IRpaRocketMQService extends IService<RpaTaskList> {

    SendResult syncSendString(StockMsg stockMsg);



}
