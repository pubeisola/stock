package cc.mrbird.febs.api.service.impl.message;

import lombok.Data;

@Data
public class StockMsg {

    public static final String TOPIC = "StockMsg";

    /**
     * 编号
     */
    private String uuid;
    private Integer transactionId;
    private Integer tradeSerId;
    private Long tradeId;

    /**
     * 股票交易历史版本
     */
    private Integer version;

    /**
     * 股票代码
     */
    private String securityCode;

    /**
     * 股票交易数量
     */
    private Integer quantity;

    /**
     * 股票交易动作
     */
    private String action;

    /**
     * 股票交易类型
     */
    private String buyOrSell;

    /**
     *  交易记录时间戳
     */
    private Long createTime;
    public StockMsg setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "StockMsg{" +
                "uuid=" + uuid +
                '}';
    }

}