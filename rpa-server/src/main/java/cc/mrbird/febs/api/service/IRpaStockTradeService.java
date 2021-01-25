package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaTaskList;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaStockTradeList;
import javax.servlet.http.HttpServletRequest;
import cc.mrbird.febs.api.service.impl.*;
/**
 *  Service接口
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
public interface IRpaStockTradeService extends IService<RpaStockTradeList> {

    /**
     * 获取交易记录是否存在  rocketMQ 去重
     *
     * @param id String
     * @return IPage<RpaStockTradeList>
     */
    java.util.List<RpaStockTradeList> findByTradeSerId(String id);

    /**
     * 新增
     *
     * @param RpaStockTradeList rpaStockTradeList
     */
    int create(RpaStockTradeList raStockTradeList);

    /**
     * 新增
     *
     * @param RpaStockTradeList rpaStockTradeList
     */
    Result trade_create(RpaStockTradeList raStockTradeList);

    /**
     * 清楚数据库数据 用于测试
     *
     */
    void  delAll();
}
