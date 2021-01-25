package cc.mrbird.febs.api.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import javax.servlet.http.HttpServletRequest;
/**
 *  Entity
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
@Data
@TableName("stock_position")
public class RpaStockPositionList {

    /**
     * rpa 任务列表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户 用户  //uiauto
     */
    @TableField("security_code")
    private String securityCode;

    /**
     * 股票交易数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 股票交易历史版本
     */
    @TableField("version")
    private Integer version;

    /**
     *  记录更新时间戳
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     *  交易员id
     */
    @TableField("trade_id")
    private Long tradeId;
}
