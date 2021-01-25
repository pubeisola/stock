package cc.mrbird.febs;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;


import javax.annotation.Resource;
import java.util.UUID;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.Calendar;

import static java.lang.System.currentTimeMillis;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;
import cc.mrbird.febs.api.service.impl.message.StockMsg;
import cc.mrbird.febs.api.service.IRpaRocketMQService;
import cc.mrbird.febs.api.service.IRpaStockPositionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.redis.core.RedisTemplate;

import cc.mrbird.febs.api.entity.RpaStockPositionList;
import cc.mrbird.febs.api.service.IRpaStockTradeService;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FebsShiroApplication.class)
@NoArgsConstructor
@WebAppConfiguration
@TestMethodOrder(OrderAnnotation.class)
public class StockServiceTests {
    @Autowired
    public IRpaRocketMQService ems;

    @Autowired
    public IRpaStockPositionService sps;

    @Autowired
    public IRpaStockTradeService sts;

    @Resource
    public RedisTemplate<String, Object> redisTemplate;


    //@Resource
    @Order(1)
    @Test
    public void contextLoads() {
    }

    @BeforeAll
    public void setUp() {

    }

    //@Resource
    @Order(2)
    @Test
    public void testStockLog0() {
        // 每一次测试前清理数据库
        sps.delAll();  // 股票头寸
        sts.delAll();  // 交易记录

        try {
            //等待消息队列后台任务启动成功   后台异步启动
            Thread.sleep(90000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(3)
    @Test
    public void testStockLog1() throws Throwable {
        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(1L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(1);
        sm.setVersion(1);
        sm.setSecurityCode("REL");
        sm.setQuantity(50);
        sm.setAction("INSERT");
        sm.setBuyOrSell("Buy");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog1======" + sr);
        //Assertions.assertEquals(accountMapper.getTotalDeposit(),
                //BigDecimal.valueOf(10000).setScale(2, RoundingMode.HALF_UP));
        // 判断是否发送成功
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 50));

        System.out.println("8888888888888888888888888888888888888888888888===1===end===");
    }

    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(4)
    @Test
    public void testStockLog2() throws Throwable {

        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(2L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(2);
        sm.setVersion(1);
        sm.setSecurityCode("ITC");
        sm.setQuantity(40);
        sm.setAction("INSERT");
        sm.setBuyOrSell("Sell");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog2======" + sr);
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 50));
        RpaStockPositionList rr_itc = result.get(1);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr_itc.getSecurityCode().compareToIgnoreCase("ITC") == 0) && (rr_itc.getQuantity() == -40));


        System.out.println("8888888888888888888888888888888888888888888888==2 ==end=====");
    }

    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(5)
    @Test
    public void testStockLog3() throws Throwable {

        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(3L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(3);
        sm.setVersion(1);
        sm.setSecurityCode("INF");
        sm.setQuantity(70);
        sm.setAction("INSERT");
        sm.setBuyOrSell("Buy");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog3======" + sr);
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 50));
        RpaStockPositionList rr_itc = result.get(1);
        Assertions.assertNotNull(rr_itc);
        Assertions.assertTrue((rr_itc.getSecurityCode().compareToIgnoreCase("ITC") == 0) && (rr_itc.getQuantity() == -40));

        RpaStockPositionList rr_inf = result.get(2);
        Assertions.assertNotNull(rr_inf);
        Assertions.assertTrue((rr_inf.getSecurityCode().compareToIgnoreCase("INF") == 0) && (rr_inf.getQuantity() == 70));

        System.out.println("8888888888888888888888888888888888888888888888====3==end===");
    }


    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(6)
    @Test
    public void testStockLog4() throws Throwable {

        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(1L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(4);
        sm.setVersion(2);
        sm.setSecurityCode("REL");
        sm.setQuantity(60);
        sm.setAction("UPDATE");
        sm.setBuyOrSell("Buy");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog4======" + sr);
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 60));
        RpaStockPositionList rr_itc = result.get(1);
        Assertions.assertNotNull(rr_itc);
        Assertions.assertTrue((rr_itc.getSecurityCode().compareToIgnoreCase("ITC") == 0) && (rr_itc.getQuantity() == -40));

        RpaStockPositionList rr_inf = result.get(2);
        Assertions.assertNotNull(rr_inf);
        Assertions.assertTrue((rr_inf.getSecurityCode().compareToIgnoreCase("INF") == 0) && (rr_inf.getQuantity() == 70));

        System.out.println("8888888888888888888888888888888888888888888888===4====end==");
    }


    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(7)
    @Test
    public void testStockLog5() throws Throwable {
        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(2L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(5);
        sm.setVersion(2);
        sm.setSecurityCode("ITC");
        sm.setQuantity(30);
        sm.setAction("CANCEL");
        sm.setBuyOrSell("Buy");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog5======" + sr);
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 60));
        RpaStockPositionList rr_itc = result.get(1);
        Assertions.assertNotNull(rr_itc);
        Assertions.assertTrue((rr_itc.getSecurityCode().compareToIgnoreCase("ITC") == 0) && (rr_itc.getQuantity() == 0));

        RpaStockPositionList rr_inf = result.get(2);
        Assertions.assertNotNull(rr_inf);
        Assertions.assertTrue((rr_inf.getSecurityCode().compareToIgnoreCase("INF") == 0) && (rr_inf.getQuantity() == 70));


        System.out.println("8888888888888888888888888888888888888888888888=====5 end====");
    }


    /**
     * 测试日志记录   获取股票寸头断言
     */
    @Order(8)
    @Test
    public void testStockLog6() throws Throwable {

        redisTemplate.opsForValue().set("redisTemplate", "hello redisTemplate , i will use u as a tool");
        System.err.println(redisTemplate.opsForValue().get("redisTemplate"));

        StockMsg  sm = new StockMsg();
        sm.setTradeId(4L);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        sm.setUuid(uuid);
        sm.setTransactionId(6);
        sm.setVersion(1);
        sm.setSecurityCode("INF");
        sm.setQuantity(20);
        sm.setAction("INSERT");
        sm.setBuyOrSell("Sell");
        sm.setCreateTime((Long)Calendar.getInstance().getTimeInMillis()/1000);
        SendResult sr = ems.syncSendString(sm);
        System.out.println("8888888888888888888888888888888888888888888888===testStockLog6======" + sr);
        Assertions.assertEquals(sr.getSendStatus(), SendStatus.SEND_OK);
        try {
            //等待消息队列处理完
            Thread.sleep(10000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断本步骤的结果
        RpaStockPositionList  pp = new RpaStockPositionList();
        List<RpaStockPositionList> result = sps.findAll(pp);
        Assertions.assertNotNull(result);

        RpaStockPositionList rr = result.get(0);
        Assertions.assertNotNull(rr);
        Assertions.assertTrue((rr.getSecurityCode().compareToIgnoreCase("REL") == 0) && (rr.getQuantity() == 60));
        RpaStockPositionList rr_itc = result.get(1);
        Assertions.assertNotNull(rr_itc);
        Assertions.assertTrue((rr_itc.getSecurityCode().compareToIgnoreCase("ITC") == 0) && (rr_itc.getQuantity() == 0));

        RpaStockPositionList rr_inf = result.get(2);
        Assertions.assertNotNull(rr_inf);
        Assertions.assertTrue((rr_inf.getSecurityCode().compareToIgnoreCase("INF") == 0) && (rr_inf.getQuantity() == 50));

        System.out.println("8888888888888888888888888888888888888888888888===6===end ===");
    }


    //@Resource
    @Order(9)
    @Test
    public void testStockLog7() {
        try {
            //等待消息队列处理完成
            Thread.sleep(300000);  // 测试程序暂停 5分钟 等待后台消息消费完
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}