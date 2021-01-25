package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaTaskLisuser;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author chenggaofeng
 * @date 2020-06-18 15:05:56
 */
public interface IRpaTaskLisuserService extends IService<RpaTaskLisuser> {
    /**
     *  根据记录is 获取运行记录信息  rpa server 端使用
     *
     * @param id
     * @return RpaTaskLisuser
     */
    RpaTaskLisuser findById(String id);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param rpaTaskLisuser rpaTaskLisuser
     * @return IPage<RpaTaskLisuser>
     */
    IPage<RpaTaskLisuser> findRpaTaskLisusers(QueryRequest request, RpaTaskLisuser rpaTaskLisuser);

    /**
     * 查询（所有）
     *
     * @param rpaTaskLisuser rpaTaskLisuser
     * @return List<RpaTaskLisuser>
     */
    List<RpaTaskLisuser> findRpaTaskLisusers(RpaTaskLisuser rpaTaskLisuser);

    /**
     * 新增
     *
     * @param rpaTaskLisuser rpaTaskLisuser
     */
    void createRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser);

    /**
     * 修改
     *
     * @param rpaTaskLisuser rpaTaskLisuser
     */
    void updateRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser);

    /**
     * 删除
     *
     * @param rpaTaskLisuser rpaTaskLisuser
     */
    void deleteRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser);
}
