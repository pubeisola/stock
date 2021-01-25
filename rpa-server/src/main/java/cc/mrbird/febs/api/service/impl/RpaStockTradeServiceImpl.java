package cc.mrbird.febs.api.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaStockTradeList;
import cc.mrbird.febs.api.mapper.RpaStockTradeMapper;
import cc.mrbird.febs.api.service.IRpaStockTradeService;
import cc.mrbird.febs.api.service.IRpaStockPositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import java.util.Calendar;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import cc.mrbird.febs.api.entity.RpaStockPositionList;
import org.apache.commons.lang3.StringUtils;
import cc.mrbird.febs.common.entity.FebsConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.function.BiPredicate;

/**
 *  Service实现
 *
 * @author chenggaofeng
 * @date 2020-01-20 18:41:31
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RpaStockTradeServiceImpl extends ServiceImpl<RpaStockTradeMapper, RpaStockTradeList> implements IRpaStockTradeService {



    private final RpaStockTradeMapper rpaStockTradeMapper;
    private final IRpaStockPositionService rpasPs;
    /**
     * 获取交易记录是否存在  rocketMQ 去重
     *
     * @param id String
     */
    @Override
    public java.util.List<RpaStockTradeList> findByTradeSerId(String id) {
        return this.rpaStockTradeMapper.findById(id);
    }

    /**
     * 新增 股票交易记录
     *
     * @param RpaStockTradeList raStockTradeList
     */
    @Override
    public int create(RpaStockTradeList raStockTradeList) {
        return this.rpaStockTradeMapper.insert(raStockTradeList);
    }

    /**
     * 新增 股票交易记录
     *
     * @param RpaStockTradeList raStockTradeList
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Result trade_create(RpaStockTradeList raStockTradeList)  {

        // 锁定stock_position 为了更新  获取悲观锁  获取到针对本交易员对本股票的交易版本 insert version=1
        // cance为最后一个版本号
        RpaStockPositionList rpaStockPositionList = new RpaStockPositionList();
        rpaStockPositionList.setVersion(raStockTradeList.getVersion());
        rpaStockPositionList.setSecurityCode(raStockTradeList.getSecurityCode());
        rpaStockPositionList.setTradeId(raStockTradeList.getTradeId());
        Long currentTime = Calendar.getInstance().getTimeInMillis()/1000;   // 记录添加时间
        rpaStockPositionList.setUpdateTime(currentTime);

        // 更新股票头寸
        try {

            // 获取当前交易状态的最后一条记录
            List<RpaStockTradeList> currenttradeLoatRecord = this.rpaStockTradeMapper.findTradeLast(raStockTradeList.getTradeId()); // 当前交易员的最后一条记录

            if (currenttradeLoatRecord != null) {
                if (currenttradeLoatRecord.size() <=0 ) {
                    // 没有历史记录 直接处理当前记录
                    // 没有交易记录  直接插入 inset  update  cancel记录
                    String meAction = raStockTradeList.getAction();
                    String meBuy = raStockTradeList.getBuyOrSell();
                    if (meAction.compareToIgnoreCase("insert") == 0) {
                        if (meBuy.compareToIgnoreCase("Buy") == 0) {
                            rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                        } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                            rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                        }

                        rpaStockPositionList.setVersion(1);
                        // 插入交易日志
                        this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                    } else if (meAction.compareToIgnoreCase("update") == 0) {
                        if (meBuy.compareToIgnoreCase("Buy") == 0) {
                            rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                        } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                            rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                        }

                        rpaStockPositionList.setVersion(1);
                        // 插入交易日志
                        this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                    } else if (meAction.compareToIgnoreCase("cancel") == 0) {
                        if (meBuy.compareToIgnoreCase("Buy") == 0) {
                            rpaStockPositionList.setQuantity(0);       // 代表添加
                        } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                            rpaStockPositionList.setQuantity(0);  // 代表减去
                        }

                        rpaStockPositionList.setVersion(1);  // 因为是本交易的第一条记录 因此是版本1
                        // 插入交易日志
                        this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                    }
                } else {
                     // 有历史记录直接处理历史记录  和 本交易最后一条历史记录比对做处理
                     // 有交易记录 根据最后一条记录的情况  记录状态计算后续记录的计算状态
                    RpaStockTradeList curHaveLastTradeRecord = currenttradeLoatRecord.get(0);  // 本次交易的最后一条记录  后续记录根据这条记录更新
                    if (curHaveLastTradeRecord != null) {
                        // 本交易批次存在交易的记录
                        String meAction = curHaveLastTradeRecord.getAction();
                        String meBuy = curHaveLastTradeRecord.getBuyOrSell();
                        if (meAction.compareToIgnoreCase("insert") == 0) {  // 前一条记录状态
                            // 最后一条记录是插入
                            String curAction = raStockTradeList.getAction(); // 当前要更新的记录状态
                            String curBuy = raStockTradeList.getBuyOrSell();
                            // 当前要插入的记录是  insert   区分买和卖 raStockTradeList
                            // 当前要插入的记录是  update   区分买和卖
                            // 当前要插入的记录是  cancel   区分买和卖
                            if (curAction.compareToIgnoreCase("insert") == 0) {
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                                }

                                rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                // 插入交易日志
                                this.rpasPs.updateRpaTaskList(rpaStockPositionList);   // 在原始记录上加或者减
                            } else if (curAction.compareToIgnoreCase("update") == 0) {
                                // 如果变更了安全代码(股票代码)  原始交易清零  增加新的股票的交易
                                int plus = 1;
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    plus = 1;
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    plus = -1;
                                }

                                String meSecurityCode = curHaveLastTradeRecord.getSecurityCode();
                                String curSecurityCode = raStockTradeList.getSecurityCode();
                                if (meSecurityCode.compareToIgnoreCase(curSecurityCode) == 0) {
                                    // 同一个股票 直接更新
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * plus);       // 代表添加
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 插入交易日志
                                    this.rpasPs.update(rpaStockPositionList);
                                } else if (meSecurityCode.compareToIgnoreCase(curSecurityCode) != 0) {
                                    // 取消原来的 增加新赠的  直接更新原始交易记录
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * plus);    //新增的
                                    // 根据交易id(trade_id) 直接更新对应 stock_position 对应的记录
                                    this.rpasPs.update(rpaStockPositionList); // 不存在插入 存在直接更新
                                }
                            } else if (curAction.compareToIgnoreCase("cancel") == 0) {
                                // 取消是最后一条记录 直接清零   不管是买和卖直接清零

                                int plus = 1;
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    plus = 1;
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    plus = -1;
                                }

                                String meSecurityCode = curHaveLastTradeRecord.getSecurityCode();
                                String curSecurityCode = raStockTradeList.getSecurityCode();
                                if (meSecurityCode.compareToIgnoreCase(curSecurityCode) == 0) {
                                    // 同一个股票 直接更新
                                    rpaStockPositionList.setQuantity(0 * plus);       // 代表添加
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 插入交易日志
                                    this.rpasPs.update(rpaStockPositionList);
                                } else if (meSecurityCode.compareToIgnoreCase(curSecurityCode) != 0) {
                                    // 取消原来的 增加新赠的
                                    rpaStockPositionList.setQuantity(0 * plus);    //新增的   取消记录清凉
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 根据交易id(trade_id) 直接更新对应 stock_position 对应的记录
                                    this.rpasPs.update(rpaStockPositionList); // 不存在插入 存在直接更新
                                }
                            }
                        } else if (meAction.compareToIgnoreCase("update") == 0) {
                            // 最后一条记录是更新
                            String curAction = raStockTradeList.getAction(); // 当前要更新的记录状态
                            String curBuy = raStockTradeList.getBuyOrSell();
                            // 当前要插入的记录是  insert   区分买和卖 raStockTradeList
                            // 当前要插入的记录是  update   区分买和卖
                            // 当前要插入的记录是  cancel   区分买和卖
                            if (curAction.compareToIgnoreCase("insert") == 0) {
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                                }

                                rpaStockPositionList.setVersion(1); // 插入的版本总是1
                                // 插入交易日志
                                this.rpasPs.updateRpaTaskList(rpaStockPositionList);   // 在原始记录上加或者减
                            } else if(curAction.compareToIgnoreCase("update") == 0) {
                                int plus = 1;
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    plus = 1;
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    plus = -1;
                                }

                                String meSecurityCode = curHaveLastTradeRecord.getSecurityCode();
                                String curSecurityCode = raStockTradeList.getSecurityCode();
                                if (meSecurityCode.compareToIgnoreCase(curSecurityCode) == 0) {
                                    // 同一个股票 直接更新
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * plus);       // 代表添加
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 插入交易日志
                                    this.rpasPs.update(rpaStockPositionList);
                                } else if (meSecurityCode.compareToIgnoreCase(curSecurityCode) != 0) {
                                    // 取消原来的 增加新赠的  直接更新原始交易记录
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * plus);    //新增的
                                    // 根据交易id(trade_id) 直接更新对应 stock_position 对应的记录
                                    this.rpasPs.update(rpaStockPositionList); // 不存在插入 存在直接更新
                                }
                            } else if (meAction.compareToIgnoreCase("cancel") == 0) {
                                // 前一步是更新  后一步是取消  本交易直接取消   不管股票代码变化没  本次交易都清零
                                int plus = 1;
                                if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                    plus = 1;
                                } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                    plus = -1;
                                }

                                String meSecurityCode = curHaveLastTradeRecord.getSecurityCode();
                                String curSecurityCode = raStockTradeList.getSecurityCode();
                                if (meSecurityCode.compareToIgnoreCase(curSecurityCode) == 0) {
                                    // 同一个股票 直接更新
                                    rpaStockPositionList.setQuantity(0 * plus);       // 代表添加
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 插入交易日志
                                    this.rpasPs.update(rpaStockPositionList);
                                } else if (meSecurityCode.compareToIgnoreCase(curSecurityCode) != 0) {
                                    // 取消原来的 增加新赠的
                                    rpaStockPositionList.setQuantity(0 * plus);    //新增的   取消记录清凉
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 根据交易id(trade_id) 直接更新对应 stock_position 对应的记录
                                    this.rpasPs.update(rpaStockPositionList); // 不存在插入 存在直接更新
                                }
                            }
                        } else if (meAction.compareToIgnoreCase("cancel") == 0) {
                            // 最后一条记录是取消 新的记录可能是重新买入
                            String curAction = raStockTradeList.getAction(); // 当前要更新的记录状态
                            String curBuy = raStockTradeList.getBuyOrSell();

                            int plus = 1;
                            if (curBuy.compareToIgnoreCase("Buy") == 0) {
                                plus = 1;
                            } else if (curBuy.compareToIgnoreCase("sell") == 0) {
                                plus = -1;
                            }

                            String curAction2 = raStockTradeList.getAction(); // 当前要更新的记录状态
                            String curBuy2 = raStockTradeList.getBuyOrSell();
                            // 当前要插入的记录是  insert   区分买和卖 raStockTradeList
                            // 当前要插入的记录是  update   区分买和卖
                            // 当前要插入的记录是  cancel   区分买和卖
                            if (curAction2.compareToIgnoreCase("insert") == 0) {
                                if (curBuy2.compareToIgnoreCase("Buy") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                                } else if (curBuy2.compareToIgnoreCase("sell") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                                }

                                rpaStockPositionList.setVersion(1); // 插入的版本总是1
                                // 插入交易日志
                                this.rpasPs.update(rpaStockPositionList);   // 在原始记录上加或者减
                            }  else if (curAction2.compareToIgnoreCase("update") == 0) {
                                if (curBuy2.compareToIgnoreCase("Buy") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                                } else if (curBuy2.compareToIgnoreCase("sell") == 0) {
                                    rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                                }

                                rpaStockPositionList.setVersion(1); // 插入的版本总是1
                                // 插入交易日志
                                this.rpasPs.update(rpaStockPositionList);   // 在原始记录上加或者减
                            } else if (curAction2.compareToIgnoreCase("cancel") == 0) {
                                int plus2 = 1;
                                if (curBuy2.compareToIgnoreCase("Buy") == 0) {
                                    plus2 = 1;
                                } else if (curBuy2.compareToIgnoreCase("sell") == 0) {
                                    plus2 = -1;
                                }

                                String meSecurityCode = curHaveLastTradeRecord.getSecurityCode();
                                String curSecurityCode = raStockTradeList.getSecurityCode();
                                if (meSecurityCode.compareToIgnoreCase(curSecurityCode) == 0) {
                                    // 同一个股票 直接更新
                                    rpaStockPositionList.setQuantity(0 * plus2);       // 代表添加
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 插入交易日志
                                    this.rpasPs.update(rpaStockPositionList);
                                } else if (meSecurityCode.compareToIgnoreCase(curSecurityCode) != 0) {
                                    // 取消原来的 增加新赠的
                                    rpaStockPositionList.setQuantity(0 * plus2);    //新增的   取消记录清凉
                                    rpaStockPositionList.setVersion(curHaveLastTradeRecord.getVersion() + 1);
                                    // 根据交易id(trade_id) 直接更新对应 stock_position 对应的记录
                                    this.rpasPs.update(rpaStockPositionList); // 不存在插入 存在直接更新
                                }
                            }
                        }
                    } else {
                        // 列表中有一条为null 的记录
                        // 没有交易记录  直接插入 inset  update  cancel记录
                        String meAction = raStockTradeList.getAction();
                        String meBuy = raStockTradeList.getBuyOrSell();
                        if (meAction.compareToIgnoreCase("insert") == 0) {
                            if (meBuy.compareToIgnoreCase("Buy") == 0) {
                                rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                            } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                                rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                            }

                            rpaStockPositionList.setVersion(1);
                            // 插入交易日志
                            this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                        } else if (meAction.compareToIgnoreCase("update") == 0) {
                            if (meBuy.compareToIgnoreCase("Buy") == 0) {
                                rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                            } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                                rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                            }

                            rpaStockPositionList.setVersion(1);
                            // 插入交易日志
                            this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                        } else if (meAction.compareToIgnoreCase("cancel") == 0) {
                            if (meBuy.compareToIgnoreCase("Buy") == 0) {
                                rpaStockPositionList.setQuantity(0);       // 代表添加
                            } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                                rpaStockPositionList.setQuantity(0);  // 代表减去
                            }

                            rpaStockPositionList.setVersion(1);  // 因为是本交易的第一条记录 因此是版本1
                            // 插入交易日志
                            this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                        }
                    }
                }
            } else {
                // 没有交易记录  直接插入 inset  update  cancel记录
                String meAction = raStockTradeList.getAction();
                String meBuy = raStockTradeList.getBuyOrSell();
                if (meAction.compareToIgnoreCase("insert") == 0) {
                    if (meBuy.compareToIgnoreCase("Buy") == 0) {
                        rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                    } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                        rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                    }

                    rpaStockPositionList.setVersion(1);
                    // 插入交易日志
                    this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                } else if (meAction.compareToIgnoreCase("update") == 0) {
                    if (meBuy.compareToIgnoreCase("Buy") == 0) {
                        rpaStockPositionList.setQuantity(raStockTradeList.getQuantity());       // 代表添加
                    } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                        rpaStockPositionList.setQuantity(raStockTradeList.getQuantity() * -1);  // 代表减去
                    }

                    rpaStockPositionList.setVersion(1);
                    // 插入交易日志
                    this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                } else if (meAction.compareToIgnoreCase("cancel") == 0) {
                    if (meBuy.compareToIgnoreCase("Buy") == 0) {
                        rpaStockPositionList.setQuantity(0);       // 代表添加
                    } else if (meBuy.compareToIgnoreCase("sell") == 0) {
                        rpaStockPositionList.setQuantity(0);  // 代表减去
                    }

                    rpaStockPositionList.setVersion(1);  // 因为是本交易的第一条记录 因此是版本1
                    // 插入交易日志
                    this.rpasPs.updateRpaTaskList(rpaStockPositionList);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.FAILED;
        }

        // 插入交易日志
        try {
            // 如果为插入总为版本1
            raStockTradeList.setVersion(rpaStockPositionList.getVersion());
            this.rpaStockTradeMapper.insert(raStockTradeList);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.FAILED;
        }

        return Result.SUCCESS;
    }

    /**
     * 删除数据库数据用于测试
     *
     */
    @Override
    public void  delAll() {
        this.rpaStockTradeMapper.delAll();
        return;
    }

}
