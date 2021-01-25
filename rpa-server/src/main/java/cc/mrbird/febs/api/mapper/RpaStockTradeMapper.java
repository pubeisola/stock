package cc.mrbird.febs.api.mapper;

import cc.mrbird.febs.api.entity.RpaStockTradeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.annotation.SqlParser;

/**
 *  Mapper
 *
 * @author chenggaofeng
 * @date 2021-01-20 18:41:31
 */
public interface RpaStockTradeMapper extends BaseMapper<RpaStockTradeList> {
        /**
     * 更具id 获取插件配置记录
     *
     * @param id
     * @return 用户
     */
     int insert(RpaStockTradeList rpaStockTradeList);

    /**
     * 获取运行列表分页数据
     *
     * @param String tradeSerId
     * @return List<RpaStockTradeList>
     */
    List<RpaStockTradeList> findById(@Param("tradeSerId") String tradeSerId);

    /**
     * 获取某交易员员的最后一次交易数据记录
     *
     * @param String tradeSerId
     * @return List<RpaStockTradeList>
     */
    List<RpaStockTradeList> findTradeLast(@Param("tradeId") Long tradeId);

    @SqlParser(filter = true)
    void delAll();
}
