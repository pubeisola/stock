package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskList;
import cc.mrbird.febs.api.mapper.RpaTaskListMapper;
import cc.mrbird.febs.api.service.IRpaRocketMQService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import org.apache.commons.lang3.StringUtils;
import cc.mrbird.febs.common.entity.FebsConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

// rocketMQ  相关
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.mrbird.febs.api.service.impl.message.StockMsg;


/**
 *  Service实现
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaRocketMQServiceImpl extends ServiceImpl<RpaTaskListMapper, RpaTaskList> implements IRpaRocketMQService {

    private final RpaTaskListMapper rpaTaskListMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     *  同步发送有序消息
     *
     * @param String json
     * @return int
     */
    @Override
    public SendResult syncSendString(StockMsg tockMsg) {

        return this.syncSendOrderly(tockMsg);
    }

    public SendResult syncSendOrderly(StockMsg tockMsg) {

        // 同步发送消息
        return rocketMQTemplate.syncSendOrderly(StockMsg.TOPIC, tockMsg, String.valueOf(tockMsg.getTradeId()));
    }

    public void asyncSendOrderly(StockMsg tockMsg, SendCallback callback) {
        // 异步发送消息
        rocketMQTemplate.asyncSendOrderly(StockMsg.TOPIC, tockMsg, String.valueOf(tockMsg.getTradeId()), callback);
    }

    public void onewaySendOrderly(StockMsg tockMsg) {

        // 异步发送消息
        rocketMQTemplate.sendOneWayOrderly(StockMsg.TOPIC, tockMsg, String.valueOf(tockMsg.getTradeId()));
    }

}
