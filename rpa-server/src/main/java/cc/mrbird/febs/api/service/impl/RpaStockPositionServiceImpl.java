package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaStockPositionList;
import cc.mrbird.febs.api.mapper.RpaStockPositionMapper;
import cc.mrbird.febs.api.service.IRpaStockPositionService;
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

/**
 *  Service实现
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaStockPositionServiceImpl extends ServiceImpl<RpaStockPositionMapper, RpaStockPositionList> implements IRpaStockPositionService {

    private final RpaStockPositionMapper rpaStockPositionMapper;

    /**
     * 获取所有股票头寸
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    @Override
    public List<RpaStockPositionList> findAll(RpaStockPositionList rpaStockPositionList) {
        return this.rpaStockPositionMapper.findAll(rpaStockPositionList);
    }

    /**
     * 删除数据库数据用于测试
     *
     */
    @Override
    public void  delAll() {
        this.rpaStockPositionMapper.delAll();
        return;
    }

    /**
     * 存储更新 不存在添加
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    @Override
    public void updateRpaTaskList(RpaStockPositionList rpaStockPositionList) {
        this.rpaStockPositionMapper.insertOrUpdate(rpaStockPositionList);
    }

    /**
     * 存储更新 不存在添加
     *
     * @param RpaStockPositionList rpaStockPositionList
     */
    @Override
    public void update(RpaStockPositionList rpaStockPositionList) {

        this.rpaStockPositionMapper.update(rpaStockPositionList);
    }

    /**
     * 查询（分页）  查询股票头寸 分页
     *
     * @return IPage<RpaStockPositionList>
     */
    @Override
    public IPage<RpaStockPositionList> findRpaStockPositionLists(String offset, int pageSizeInt, HttpServletRequest request ,QueryRequest qq) {
        LambdaQueryWrapper<RpaStockPositionList> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        int curOffset = 0;
        try {
            curOffset = Integer.valueOf(offset).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //queryWrapper.groupBY(RpaStockPositionList::getSecurityCode);
        Page<cc.mrbird.febs.api.entity.RpaStockPositionList> page = new Page<>(curOffset, pageSizeInt);

        cc.mrbird.febs.common.utils.SortUtil.handlePageSort(qq, page, "id", FebsConstant.ORDER_DESC, false);
        // return this.page(page, queryWrapper);  // 原始的分页格式数据  客户端不能用 (mybatisplus)
        IPage<RpaStockPositionList> page_re = this.page(page, new LambdaQueryWrapper<RpaStockPositionList>()
                .groupBy(RpaStockPositionList::getSecurityCode)
                .select(RpaStockPositionList::getSecurityCode, RpaStockPositionList::getTradeId));
        return page_re;
    }

}
