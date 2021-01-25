package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.api.entity.RpaPluginView;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author MrBird
 * @date 2020-05-27 19:12:10
 */
public interface IRpaPluginConfigService extends IService<RpaPluginConfig> {

    /**
     * 获取可下载插件的总记录数
     *
     * @return 插件记录数
     */
    int getPluginTotal(String keyword);

    /**
     * 根据关键字获取可下载的插件
     *
     * @return 插件记录数
     */
    List<RpaPluginView> findByPage(String offset, String  keyword);

    /**
     * 根据id 获取记录信息
     *
     * @param id
     * @return 用户
     */
    RpaPluginConfig findById(String id);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param rpaPluginConfig rpaPluginConfig
     * @return IPage<RpaPluginConfig>
     */
    IPage<RpaPluginConfig> findRpaPluginConfigs(String username, String status, QueryRequest request, RpaPluginConfig rpaPluginConfig);

    /**
     * 查询（所有）
     *
     * @param rpaPluginConfig rpaPluginConfig
     * @return List<RpaPluginConfig>
     */
    List<RpaPluginConfig> findRpaPluginConfigs(String username, String status, RpaPluginConfig rpaPluginConfig);

    /**
     * 新增
     *
     * @param rpaPluginConfig rpaPluginConfig
     */
    void createRpaPluginConfig(RpaPluginConfig rpaPluginConfig);

    /**
     * 修改
     *
     * @param rpaPluginConfig rpaPluginConfig
     */
    void updateRpaPluginConfig(RpaPluginConfig rpaPluginConfig);

    /**
     * 删除
     * @param id
     */
    void deleteRpaPluginConfig(String id);
}
