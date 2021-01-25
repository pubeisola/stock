package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaTaskCronRule;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author MrBird
 * @date 2020-06-03 18:41:43
 */
public interface IRpaTaskCronRuleService extends IService<RpaTaskCronRule> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param rpaTaskCronRule rpaTaskCronRule
     * @return IPage<RpaTaskCronRule>
     */
    IPage<RpaTaskCronRule> findRpaTaskCronRules(QueryRequest request, RpaTaskCronRule rpaTaskCronRule);

    /**
     * 查询（所有）
     *
     * @param rpaTaskCronRule rpaTaskCronRule
     * @return List<RpaTaskCronRule>
     */
    List<RpaTaskCronRule> findRpaTaskCronRules(RpaTaskCronRule rpaTaskCronRule);

    /**
     * 新增
     *
     * @param rpaTaskCronRule rpaTaskCronRule
     */
    void createRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule);

    /**
     * 修改
     *
     * @param rpaTaskCronRule rpaTaskCronRule
     */
    void updateRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule);

    /**
     * 删除
     *
     * @param rpaTaskCronRule rpaTaskCronRule
     */
    void deleteRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule);
}
