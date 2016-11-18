package com.ictwsn.dao;

import org.junit.runner.RunWith;  
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.transaction.annotation.Transactional;  
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
  
/** 
* 配置文件载入类  
* @ClassName: BaseSpringTestCase  
* @Description: 要想实现Spring自动注入，必须继承此类 
* 
 */  
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:src/main/resources/springmvc-servlet.xml"})  
  
// 添加注释@Transactional 回滚对数据库操作    
@Transactional  
public class JunitDaoBase {  
      
}  
