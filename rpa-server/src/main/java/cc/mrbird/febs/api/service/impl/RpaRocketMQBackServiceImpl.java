package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskList;
import cc.mrbird.febs.api.entity.RpaStockTradeList;
import cc.mrbird.febs.api.mapper.RpaTaskListMapper;
import cc.mrbird.febs.api.service.IRpaTaskListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import cc.mrbird.febs.common.entity.FebsConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import cc.mrbird.febs.api.service.impl.message.StockMsg;
import org.springframework.beans.factory.annotation.Autowired;
import cc.mrbird.febs.api.service.IRpaStockTradeService;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import lombok.SneakyThrows;

/**
 * 监听顺序消息，保证顺序缴费
 *
 * 逐条插入日志  汇总入股票头寸
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "StockMsg", consumerGroup = "my-group1",consumeMode = ConsumeMode.ORDERLY)

public class RpaRocketMQBackServiceImpl implements RocketMQListener<StockMsg> {

    @Autowired
    public IRpaStockTradeService sts;  // 交易日志存储 和 股票寸头生成

    @SneakyThrows
    @Override
    public void onMessage(StockMsg message) {
        log.info("consumer 顺序消费，收到消息{}",  message);
        // 为有效消息时处理
        if (message != null) {
            // 构造推送过来的股票消息
            RpaStockTradeList raStockTradeList = new RpaStockTradeList();

            raStockTradeList.setTradeId(message.getTradeId());
            raStockTradeList.setTradeSerId(message.getUuid());
            raStockTradeList.setTransactionId(message.getTransactionId());
            raStockTradeList.setVersion(message.getVersion());
            raStockTradeList.setSecurityCode(message.getSecurityCode());
            raStockTradeList.setQuantity(message.getQuantity());
            raStockTradeList.setAction(message.getAction());
            raStockTradeList.setBuyOrSell(message.getBuyOrSell());
            raStockTradeList.setCreateTime(message.getCreateTime());

            Result ret = sts.trade_create(raStockTradeList);
            if (ret == Result.SUCCESS) {
                // 如果成功处理  提交本地事务
            } else {
                // 如果处理失败  提交后续继续处理  到本地队列 重试
                // 剖出异常 代码会自动重试
                throw new Exception("本消息处理失败要重试处理！");
            }
        }
    }
}
