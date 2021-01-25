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
@TableName("stock_transaction_log")
public class RpaStockTradeList {

    /**
     * rpa 主建
     */
    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Integer transactionId;

    /**
     *  rocketMQ 去重 记录id
     */
    @TableField("trade_ser_id")
    private String tradeSerId;

    /**
     * 交易员的id
     */
    @TableField("trade_id")
    private Long tradeId;

    /**
     * 股票交易历史版本
     */
    @TableField("version")
    private Integer version;

    /**
     * 股票代码
     */
    @TableField("security_code")
    private String securityCode;

    /**
     * 股票交易数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 股票交易动作
     */
    @TableField("action")
    private String action;

    /**
     * 股票交易类型
     */
    @TableField("buy_or_sell")
    private String buyOrSell;

    /**
     *  交易记录时间戳
     */
    @TableField("create_time")
    private Long createTime;
}
