package cc.mrbird.febs.api.service;

import cc.mrbird.febs.api.entity.RpaPluginUser;
import cc.mrbird.febs.api.entity.RpaPluginView;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author MrBird
 * @date 2020-05-27 19:56:26
 */
public interface IRpaPluginUserService extends IService<RpaPluginUser> {

    /**
     * 根据关键字 获取某用户的下载的插件记录   关键字为插件id
     * 没有传插件id 时获取所有我的插件
     *
     * @param id
     * @return 含部分字段的插件信息记录列表
     */
    List<RpaPluginView> findUserAllPlugin(String user_id, String keyword);

    /**
     * 根据id 获取记录信息
     *
     * @param id
     * @return 用户
     */
    RpaPluginUser findById(String id);

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param rpaPluginUser rpaPluginUser
     * @return IPage<RpaPluginUser>
     */
    IPage<RpaPluginUser> findRpaPluginUsers(String username, String status, QueryRequest request, RpaPluginUser rpaPluginUser);

    /**
     * 查询（所有）
     *
     * @param rpaPluginUser rpaPluginUser
     * @return List<RpaPluginUser>
     */
    List<RpaPluginUser> findRpaPluginUsers(String username, String status, RpaPluginUser rpaPluginUser);

    /**
     * 新增
     *
     * @param rpaPluginUser rpaPluginUser
     */
    void createRpaPluginUser(RpaPluginUser rpaPluginUser);

    /**
     * 修改
     *
     * @param rpaPluginUser rpaPluginUser
     */
    void updateRpaPluginUser(RpaPluginUser rpaPluginUser);

    /**
     * 删除
     *
     * @param rpaPluginUser rpaPluginUser
     */
    void deleteRpaPluginUser(RpaPluginUser rpaPluginUser);
}
