package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaPluginUser;
import cc.mrbird.febs.api.entity.RpaPluginView;
import cc.mrbird.febs.api.mapper.RpaPluginUserMapper;
import cc.mrbird.febs.api.service.IRpaPluginUserService;
import cc.mrbird.febs.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2020-05-27 19:56:26
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaPluginUserServiceImpl extends ServiceImpl<RpaPluginUserMapper, RpaPluginUser> implements IRpaPluginUserService {

    private final RpaPluginUserMapper rpaPluginUserMapper;


    /**
     * 根据关键字 获取某用户的下载的插件记录   关键字为插件id
     * 没有传插件id 时获取所有我的插件
     *
     * @return 含部分字段的插件信息记录列表
     */
    public List<RpaPluginView> findUserAllPlugin(String user_id, String keyword) {
        return rpaPluginUserMapper.findUserAllPlugin(user_id, keyword);
    }

    /**
     *  获取插件详细信息
     */
    @Override
    public RpaPluginUser findById(String id) {
        return this.baseMapper.findById(id);
    }


    @Override
    public IPage<RpaPluginUser> findRpaPluginUsers(String username, String status, QueryRequest request, RpaPluginUser rpaPluginUser) {
        LambdaQueryWrapper<RpaPluginUser> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<RpaPluginUser> page = new Page<RpaPluginUser>(request.getPageNum(), request.getPageSize());

        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            // 都为空  查询全部数据
        } else if (StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            queryWrapper.like(RpaPluginUser::getCompanyId, username).or().like(RpaPluginUser::getCompanyId, username);
        } else if (!StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            queryWrapper.eq(RpaPluginUser::getPluginState, status);
        } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            //queryWrapper;
            // queryWrapper.eq(RpaPluginConfig::getPluginState, status)
            // , queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username)
        }
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, false);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<RpaPluginUser> findRpaPluginUsers(String username, String status, RpaPluginUser rpaPluginUser) {
	    LambdaQueryWrapper<RpaPluginUser> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件

        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            // 都为空  查询全部数据
        } else if (StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            queryWrapper.like(RpaPluginUser::getCompanyId, username).or().like(RpaPluginUser::getCompanyId, username);
        } else if (!StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            queryWrapper.eq(RpaPluginUser::getPluginState, status);
        } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            //queryWrapper;
            // queryWrapper.eq(RpaPluginConfig::getPluginState, status)
            // , queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username)
        }

		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRpaPluginUser(RpaPluginUser rpaPluginUser) {
        this.save(rpaPluginUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRpaPluginUser(RpaPluginUser rpaPluginUser) {
        this.saveOrUpdate(rpaPluginUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRpaPluginUser(RpaPluginUser rpaPluginUser) {
        LambdaQueryWrapper<RpaPluginUser> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
