package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaStockPositionList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.annotation.SqlParser;

/**
 *  Mapper
 *
 * @author chenggaofeng
 * @date 2021-01-20 18:41:31
 */
public interface RpaStockPositionMapper extends BaseMapper<RpaStockPositionList> {
   /**
     * 更新 股票数量 累加模式
     *
     * @param RpaStockPositionList rpaStockPositionList
     * @return int
     */
     @SqlParser(filter = true)
     int insertOrUpdate(RpaStockPositionList rpaStockPositionList);

    /**
     * 更新 股票数量 直接更新为某数据
     *
     * @param RpaStockPositionList rpaStockPositionList
     * @return void
     */
     @SqlParser(filter = true)
     public void update(RpaStockPositionList rpaStockPositionList);

    /**
     * 更新 股票数量 直接更新为某数据
     *
     * @param RpaStockPositionList rpaStockPositionList
     * @return void
     */
    @SqlParser(filter = true)
    List<RpaStockPositionList>  findAll(RpaStockPositionList rpaStockPositionList);

    /**
     * 清理数据库数据
     *
     * @param RpaStockPositionList rpaStockPositionList
     * @return void
     */
    @SqlParser(filter = true)
    void delAll();

    /**
     * 获取运行列表分页数据
     *
     * @param offset
     * @param pageSizeInt
     * @return 用户
     */
    List<RpaStockPositionList> findByPage(@Param("name") String offset, @Param("name") int pageSizeInt);
}
