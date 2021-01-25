package cc.mrbird.febs.api.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaStockPositionList;
import javax.servlet.http.HttpServletRequest;

/**
 *  Service接口
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
public interface IRpaStockPositionService extends IService<RpaStockPositionList> {

    /**
     * 存储更新不存在添加
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    void updateRpaTaskList(RpaStockPositionList rpaStockPositionList);

    /**
     * 存储更新不存在添加
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    void update(RpaStockPositionList rpaStockPositionList);

    /**
     * 获取所有股票头寸
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    List<RpaStockPositionList>  findAll(RpaStockPositionList rpaStockPositionList);

    /**
     * 清楚数据库数据 用于测试
     *
     */
    void  delAll();


    /**
     * 查询（分页）  查询股票头寸 分页
     *
     * @return IPage<RpaStockPositionList>
     */
    IPage<RpaStockPositionList> findRpaStockPositionLists(String offset, int pageSizeInt, HttpServletRequest request ,QueryRequest qq);
}
