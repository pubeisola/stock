package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaTaskList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *  Mapper
 *
 * @author MrBird
 * @date 2020-06-03 18:41:31
 */
public interface RpaTaskListMapper extends BaseMapper<RpaTaskList> {
        /**
     * 更具id 获取插件配置记录
     *
     * @param id
     * @return 用户
     */
     RpaTaskListExcel findById(String id);

    /**
     * 更具id 获取插件配置记录
     *
     * @param id
     * @return 用户
     */
    List<RpaTaskListExcel> selectExcelList(@Param("name") String name, @Param("name") String status, @Param("name") String startTime, @Param("name") String endTime);


    /**
     * 获取运行列表分页数据
     *
     * @param offset
     * @param pageSizeInt
     * @param keyword
     * @param startTime
     * @param endTime
     * @return 用户
     */
    List<RpaTaskList> findByPage(@Param("name") String offset, @Param("name") int pageSizeInt, @Param("name") String keyword, @Param("name") String  startTime, @Param("name") String endTime, @Param("name") String status);
}
