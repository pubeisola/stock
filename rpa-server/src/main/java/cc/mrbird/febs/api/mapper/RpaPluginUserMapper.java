package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaPluginUser;
import cc.mrbird.febs.api.entity.RpaPluginView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *  Mapper
 *
 * @author MrBird
 * @date 2020-05-27 19:56:26
 */
public interface RpaPluginUserMapper extends BaseMapper<RpaPluginUser> {
    /**
     * 更具id 获取插件配置记录
     *
     * @param id
     * @return 用户
     */
    RpaPluginUser findById(String id);

    /**
     * 根据关键字 获取某用户的下载的插件记录   关键字为插件id
     * 没有传插件id 时获取所有我的插件
     *
     * @param id
     * @return 含部分字段的插件信息记录列表
     */
    List<RpaPluginView> findUserAllPlugin(String user_id, String keyword);
}
