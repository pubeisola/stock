package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.api.entity.RpaPluginView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.*;

/**
 *  Mapper
 *
 * @author MrBird
 * @date 2020-05-27 19:12:10
 */
public interface RpaPluginConfigMapper extends BaseMapper<RpaPluginConfig> {
    /**
     * 更具id 获取插件配置记录
     *
     * @param id
     * @return 用户
     */
    RpaPluginConfig findById(String username);

    /**
     * 获取可下载插件的总记录数
     * @param keyword String 搜搜关键字  插件 id   等
     * @return 记录数
     */
    int getPluginTotal(String keyword);

    /**
     * 获取可下载插件的总记录数
     *
     * @return 包含部分字段的插件记录
     */
    List<RpaPluginView>  findByPage(String offset, String keyword);

}
