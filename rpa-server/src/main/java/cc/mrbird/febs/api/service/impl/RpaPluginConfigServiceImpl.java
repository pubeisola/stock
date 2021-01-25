package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.api.entity.RpaPluginView;
import cc.mrbird.febs.api.mapper.RpaPluginConfigMapper;
import cc.mrbird.febs.api.service.IRpaPluginConfigService;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.entity.Menu;
import cc.mrbird.febs.system.entity.User;
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
 * @date 2020-05-27 19:12:10
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaPluginConfigServiceImpl extends ServiceImpl<RpaPluginConfigMapper, RpaPluginConfig> implements IRpaPluginConfigService {

    private final RpaPluginConfigMapper rpaPluginConfigMapper;

    /**
     * 根据插件关键字可安装对的插件记录
     * @return
     */
    @Override
    public List<RpaPluginView> findByPage(String offset, String keyword) {
        return rpaPluginConfigMapper.findByPage(offset, keyword);
    }

    /**
     * 获取可下载插件的总记录数
     * @param keyword String  搜索关键子
     * @return
     */
    @Override
    public int getPluginTotal(String keyword) {
        return rpaPluginConfigMapper.getPluginTotal(keyword);
    }

    /**
     * 获取可下载插件的记录信息
     * @param id String  插件记录id
     * @return
     */
    @Override
    public RpaPluginConfig findById(String id) {
        return this.baseMapper.findById(id);
    }

    @Override
    public IPage<RpaPluginConfig> findRpaPluginConfigs(String username, String status, QueryRequest request, RpaPluginConfig rpaPluginConfig) {
        LambdaQueryWrapper<RpaPluginConfig> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<RpaPluginConfig> page = new Page<>(request.getPageNum(), request.getPageSize());


        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            // 都为空  查询全部数据
        } else if (StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username);
        } else if (!StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            queryWrapper.eq(RpaPluginConfig::getPluginState, status);
        } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            //queryWrapper;
            // queryWrapper.eq(RpaPluginConfig::getPluginState, status)
            // , queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username)
        }


        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<RpaPluginConfig> findRpaPluginConfigs(String username, String status, RpaPluginConfig rpaPluginConfig) {
	    LambdaQueryWrapper<RpaPluginConfig> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件

        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            // 都为空  查询全部数据
        } else if (StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(status)) {
            queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username);
        } else if (!StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            queryWrapper.eq(RpaPluginConfig::getPluginState, status);
        } else if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(status)) {
            //queryWrapper;
            // queryWrapper.eq(RpaPluginConfig::getPluginState, status)
            // , queryWrapper.like(RpaPluginConfig::getPluginName, username).or().like(RpaPluginConfig::getPluginDescription, username)
        }

		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRpaPluginConfig(RpaPluginConfig rpaPluginConfig) {
        this.save(rpaPluginConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRpaPluginConfig(RpaPluginConfig rpaPluginConfig) {
        this.saveOrUpdate(rpaPluginConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRpaPluginConfig(String id) {
        LambdaQueryWrapper<RpaPluginConfig> wrapper = new LambdaQueryWrapper<>();
	    // 要删除的记录id

        if (StringUtils.isNotBlank(id)) {
            wrapper.inSql(RpaPluginConfig::getId, id);
        } else {
            return;
        }

	    this.remove(wrapper);
	}
}
