# stock
stock 面试代码实现


一、方案设计   
1)运用 RocketMQ 消息队列保证提交的股票交易的顺序提交

2)代码运用spring boot 实现  运用rocketmq-spring-boot-starter 

生产端同步生产，运用 RocketMQ 的生产端的select特性保证RocketMQ同一个交易员的交易的顺序性，不同交易员的交易不需要保证交易的顺序性

3)运用rocketmq-spring-boot-starter 自带的MessageListenerOrderly 同一个队列的单线程消费保证同一个交易员的交易的顺序性

   根据交易量设置RocketMQ 集群大小和启动时主题的默认队列数目
   
   消费端采用一队列一线程消费实现某交易员的某只股票的顺序交易进入mysql 数据库
   
4）交易流水存储入mysql 数据库(海量数据可以使用TiDB 代替) 

5）最终的每个交易员的某个股票交易头寸通过股票的最终交易表存储

6）界面显示的股票头寸直接查询 本部分第五步设计的最终交易表直接查询

7）用到的主要中间件RocketMQ    业务和消息消费通过spring boot 实现

8）RocketMq  在消费端的去重通过生产交易记录时生成uuid 存储入mysql 

       消费端在查询到重复记录时丢弃本记录，然后插入可以插入的记录
       
       同时更新最终交易表
       
       
9） 本案例相关的mysql 表实现

        数据库是 127.0.0.1 的rpa
        
        DROP TABLE IF EXISTS `stock_position`;
        
CREATE TABLE `stock_position` (

  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主建',
  
  `security_code` varchar(248) DEFAULT '',
  
  `quantity` bigint(20) DEFAULT '0' COMMENT ' 交易的股票数',
  
  `update_time` int(11) DEFAULT '0' COMMENT ' 记录更新时间',
  
  `trade_id` int(11) DEFAULT '0' COMMENT '交易员id',
  
  
  `version` int(11) DEFAULT '0' COMMENT '交易最后版本号',
  
  PRIMARY KEY (`id`),
  
  UNIQUE KEY `index` (`trade_id`,`security_code`) USING BTREE
  
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `stock_transaction_log`;

CREATE TABLE `stock_transaction_log` (

  `transaction_id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主建',
  
  `trade_ser_id` char(32) DEFAULT '' COMMENT ' 唯一交易id 用于rocketMQ 重复记录去重',
  
  `trade_id` int(12) DEFAULT '0' COMMENT '交易员的id',
  
  `version` int(12) DEFAULT '1' COMMENT '同一个交易员同一只股票的交易版本号',
  
  `security_code` varchar(248) DEFAULT '' COMMENT '股票代码',
  
  `quantity` int(12) DEFAULT '0' COMMENT '交易股数',
  
  `action` char(6) DEFAULT '' COMMENT '交易动作 // insert update cancel',
  
  `buy_or_sell` char(4) DEFAULT '0' COMMENT '交易的类型  // buy: 买 Sell 卖   1 :   买  -1 卖',
  
  `create_time` int(12) DEFAULT '0' COMMENT '创建记录时间',
  
  PRIMARY KEY (`transaction_id`),
  
  KEY `index_trade_ser_id` (`trade_ser_id`) USING BTREE
  
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;




10）
        
二、Java 代码实现      功能代码实现

    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/entity/RpaStockPositionList.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/entity/RpaStockTradeList.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/mapper/RpaStockPositionMapper.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/mapper/RpaStockTradeMapper.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/IRpaRocketMQService.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/IRpaStockPositionService.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/IRpaStockTradeService.java
       
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/impl/RpaRocketMQBackServiceImpl.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/impl/RpaRocketMQServiceImpl.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/impl/RpaStockPositionServiceImpl.java
       
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/java/cc/mrbird/febs/api/service/impl/RpaStockTradeServiceImpl.java
       
       
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/resources/mapper/api/RpaStockPositionMapper.xml
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/resources/mapper/api/RpaStockTradeMapper.xml
       
    相关配置文件 
       
   https://github.com/pubeisola/stock/blob/master/rpa-server/pom.xml
   配置文件
   https://github.com/pubeisola/stock/blob/master/rpa-server/src/main/resources/application-dev.yml
       
   测试环境  要安装  mysql 5.7 数据库     安装数据库初始化 sql在
   测试环境  要安装   RocketMQ 4.8.0 单机环境测试 作为接受交易记录的消息队列：
   RocketMQ 4.8.0 win环境案例 安装案例:  https://www.cnblogs.com/coder-lzh/p/9006048.html
               
三、测试代码实现     本功能的测试代码实现代码
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/test/java/cc/mrbird/febs/StockServiceTests.java
    https://github.com/pubeisola/stock/blob/master/rpa-server/src/test/java/cc/mrbird/febs/SpringDemoApplicationTests.java
    测试结果写入  目录：   maven 编译本项目后的   项目目录的  /target/surefire-reports/cc.mrbird.febs.StockServiceTests.txt    
                                                       cc.mrbird.febs.SpringDemoApplicationTests.txt  文件以及连带的 xml 文件




相关整体实现代码在附带代码包中

