package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskList;
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
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import org.apache.commons.lang3.StringUtils;
import cc.mrbird.febs.common.entity.FebsConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *  Service实现
 *
 * @author MrBird
 * @date 2020-06-03 18:41:31
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaTaskListServiceImpl extends ServiceImpl<RpaTaskListMapper, RpaTaskList> implements IRpaTaskListService {

    private final RpaTaskListMapper rpaTaskListMapper;

    /**
     *  更具记录is 获取运行记录信息  rpa server 端使用
     *
     * @param id
     * @return
     */
    @Override
    public RpaTaskListExcel findById(String id) {
        return this.baseMapper.findById(id);
    }

    /**
     *  更具记录is 获取运行记录信息  rpa server 端使用
     *
     * @param id
     * @return
     */
    @Override
    public int getTaskTotal(String keyword) {
        return 0;
    }

    /**
     * 获取运行列表分页数据
     *  1. rpa 客户端插件分页使用
     *
     * @param offset
     * @param pageSizeInt
     * @param keyword
     * @param startTime
     * @param endTime
     * @return 用户
     */
    @Override
    public List<RpaTaskList> findByPage(String offset, int pageSizeInt, String keyword, String  startTime, String endTime, String status, HttpServletRequest request, QueryRequest qq) {
        LambdaQueryWrapper<RpaTaskList> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        int curOffset = 0;
        try {
            curOffset = Integer.valueOf(offset).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Page<cc.mrbird.febs.api.entity.RpaTaskList> page = new Page<>(curOffset, pageSizeInt);

        Boolean isHave = false;
        // 任务名称
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like(RpaTaskList::getProjectName, keyword);
            isHave = true;
        }

        // 任务状态
        if (StringUtils.isNotBlank(status)) {
            if (isHave == true) {
                queryWrapper.and(  q -> q.eq(RpaTaskList::getStatus, status));
            } else {
                queryWrapper.eq(RpaTaskList::getStatus, status);
            }
            isHave = true;
        }

        // 任务创建时间
        if (StringUtils.isNotBlank(startTime)) {
            if (isHave == true) {
                queryWrapper.and(q -> q.ge(RpaTaskList::getCreateat, startTime));
            } else {
                queryWrapper.ge(RpaTaskList::getCreateat, startTime);
            }
            isHave = true;
        }

        // 任务创建时间
        if (StringUtils.isNotBlank(endTime)) {
            if (isHave == true) {
                queryWrapper.and(q -> q.le(RpaTaskList::getCreateat, startTime));
            } else {
                queryWrapper.le(RpaTaskList::getCreateat, startTime);
            }
            isHave = true;
        }

        cc.mrbird.febs.common.utils.SortUtil.handlePageSort(qq, page, "id", FebsConstant.ORDER_DESC, false);
        // return this.page(page, queryWrapper);  // 原始的分页格式数据  客户端不能用 (mybatisplus)
        IPage<RpaTaskList> page_re = this.page(page, queryWrapper);
        return page_re.getRecords();
    }

    /**
     * server 分页   服务端管理后台使用
     * @param request QueryRequest
     * @param request QueryRequest
     * @return
     */
    @Override
    public IPage<RpaTaskList> findRpaTaskLists(String name, String status,  String startTime, String endTime, HttpServletRequest request, QueryRequest qq) {
        LambdaQueryWrapper<RpaTaskList> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<cc.mrbird.febs.api.entity.RpaTaskList> page = new Page<>(qq.getPageNum(), qq.getPageSize());

        Boolean isHave = false;
        // 任务名称
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(RpaTaskList::getProjectName, name);
            isHave = true;
        }

        // 任务状态
        if (StringUtils.isNotBlank(status)) {
            if (isHave == true) {
                queryWrapper.and(  q -> q.eq(RpaTaskList::getStatus, status));
            } else {
                queryWrapper.eq(RpaTaskList::getStatus, status);
            }
            isHave = true;
        }

        // 任务创建时间
        if (StringUtils.isNotBlank(startTime)) {
            if (isHave == true) {
                queryWrapper.and(q -> q.ge(RpaTaskList::getCreateat, startTime));
            } else {
                queryWrapper.ge(RpaTaskList::getCreateat, startTime);
            }
            isHave = true;
        }

        // 任务创建时间
        if (StringUtils.isNotBlank(endTime)) {
            if (isHave == true) {
                queryWrapper.and(q -> q.le(RpaTaskList::getCreateat, startTime));
            } else {
                queryWrapper.le(RpaTaskList::getCreateat, startTime);
            }
            isHave = true;
        }

        cc.mrbird.febs.common.utils.SortUtil.handlePageSort(qq, page, "id", FebsConstant.ORDER_DESC, false);
        return this.page(page, queryWrapper);
    }

    /**
     * server excel 报表
     * @param param   String
     * @param request QueryRequest
     * @return
     */
    @Override
    public List<RpaTaskListExcel> findAllRpaTaskLists(String name, String status,  String startTime, String endTime, QueryRequest request) {

		return this.rpaTaskListMapper.selectExcelList(name,status,startTime,endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRpaTaskList(RpaTaskList rpaTaskList) {
        this.save(rpaTaskList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRpaTaskList(RpaTaskList rpaTaskList) {
        this.saveOrUpdate(rpaTaskList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRpaTaskList(String id) {
        LambdaQueryWrapper<RpaTaskList> wrapper = new LambdaQueryWrapper<>();

	    // TODO 设置删除条件
        if (StringUtils.isNotBlank(id)) {
            wrapper.inSql(RpaTaskList::getId, id);
        } else {
            return;
        }

	    this.remove(wrapper);
	}
}
