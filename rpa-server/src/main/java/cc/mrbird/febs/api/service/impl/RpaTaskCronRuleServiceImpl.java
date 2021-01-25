package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskCronRule;
import cc.mrbird.febs.api.mapper.RpaTaskCronRuleMapper;
import cc.mrbird.febs.api.service.IRpaTaskCronRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  Service实现
 *
 * @author MrBird
 * @date 2020-06-03 18:41:43
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaTaskCronRuleServiceImpl extends ServiceImpl<RpaTaskCronRuleMapper, RpaTaskCronRule> implements IRpaTaskCronRuleService {

    private final RpaTaskCronRuleMapper rpaTaskCronRuleMapper;

    @Override
    public IPage<RpaTaskCronRule> findRpaTaskCronRules(QueryRequest request, RpaTaskCronRule rpaTaskCronRule) {
        LambdaQueryWrapper<RpaTaskCronRule> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<RpaTaskCronRule> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<RpaTaskCronRule> findRpaTaskCronRules(RpaTaskCronRule rpaTaskCronRule) {
	    LambdaQueryWrapper<RpaTaskCronRule> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule) {
        this.save(rpaTaskCronRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule) {
        this.saveOrUpdate(rpaTaskCronRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule) {
        LambdaQueryWrapper<RpaTaskCronRule> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
