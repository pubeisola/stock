package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 *  Mapper
 *
 * @author chenggaofeng
 * @date 2020-06-18 15:05:56
 */
public interface RpaTaskLisuserMapper extends BaseMapper<RpaTaskLisuser> {
    /**
     *  根据记录is 获取运行记录信息  rpa server 端使用
     *
     * @param id
     * @return RpaTaskLisuser
     */
    RpaTaskLisuser findById(String id);
}
