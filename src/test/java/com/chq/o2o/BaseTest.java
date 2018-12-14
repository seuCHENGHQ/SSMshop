package com.chq.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 用于配置Spring和junit整合，junit启动时加载springIoC容器
 * @author CHQ
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)

//告诉JUnit spring配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class BaseTest {

}
