package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaTaskList;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import cc.mrbird.febs.api.entity.RpaTaskList;
import javax.servlet.http.HttpServletRequest;

/**
 *  Service接口
 *
 * @author MrBird
 * @date 2020-06-03 18:41:31
 */
public interface IRpaTaskListService extends IService<RpaTaskList> {

    /**
     * 运行任务详细信息
     *
     * @param id String
     * @return IPage<RpaTaskList>
     */
    RpaTaskListExcel findById(String id);

    /**
     * 获取运行列表分页数据
     *
     * @param offset
     * @param pageSizeInt
     * @param keyword
     * @param startTime
     * @param endTime
     * @return 用户
     */
    public List<RpaTaskList> findByPage(String offset, int pageSizeInt, String keyword, String  startTime, String endTime, String status, HttpServletRequest request ,QueryRequest qq);

    /**
     * 获取可下载插件的总记录数
     * @param keyword String  搜索关键子
     * @return
     */
    public int getTaskTotal(String keyword);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param rpaTaskList rpaTaskList
     * @return IPage<RpaTaskList>
     */
    IPage<RpaTaskList> findRpaTaskLists(String username, String status,  String startTime, String endTime, HttpServletRequest request ,QueryRequest qq);

    /**
     * 查询（所有）
     *
     * @param rpaTaskList rpaTaskList
     * @return List<RpaTaskList>
     */
    List<RpaTaskListExcel> findAllRpaTaskLists(String username, String status,  String startTime, String endTime, QueryRequest request);

    /**
     * 新增
     *
     * @param rpaTaskList rpaTaskList
     */
    void createRpaTaskList(RpaTaskList rpaTaskList);

    /**
     * 修改
     *
     * @param rpaTaskList rpaTaskList
     */
    void updateRpaTaskList(RpaTaskList rpaTaskList);

    /**
     * 删除
     *
     * @param id String
     */
    void deleteRpaTaskList(String id);
}
