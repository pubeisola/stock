package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import cc.mrbird.febs.api.mapper.RpaTaskLisuserMapper;
import cc.mrbird.febs.api.service.IRpaTaskLisuserService;
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
 * @author chenggaofeng
 * @date 2020-06-18 15:05:56
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaTaskLisuserServiceImpl extends ServiceImpl<RpaTaskLisuserMapper, RpaTaskLisuser> implements IRpaTaskLisuserService {

    private final RpaTaskLisuserMapper rpaTaskLisuserMapper;

    /**
     *  根据记录is 获取运行记录信息  rpa server 端使用
     *
     * @param id
     * @return
     */
    @Override
    public RpaTaskLisuser findById(String id) {
        return this.rpaTaskLisuserMapper.findById(id);
    }


    @Override
    public IPage<RpaTaskLisuser> findRpaTaskLisusers(QueryRequest request, RpaTaskLisuser rpaTaskLisuser) {
        LambdaQueryWrapper<RpaTaskLisuser> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<RpaTaskLisuser> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<RpaTaskLisuser> findRpaTaskLisusers(RpaTaskLisuser rpaTaskLisuser) {
	    LambdaQueryWrapper<RpaTaskLisuser> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser) {
        this.save(rpaTaskLisuser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser) {
        this.saveOrUpdate(rpaTaskLisuser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser) {
        LambdaQueryWrapper<RpaTaskLisuser> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
