package com.ictwsn.action;


import org.junit.Assert;  
import org.junit.Test;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.mock.web.MockHttpServletRequest;  
import org.springframework.mock.web.MockHttpServletResponse;  
import org.springframework.web.servlet.ModelAndView;  


/** 
* action测试列子 
* @author fule 
* 
*/  
public class TestAction extends JUnitActionBase {  
	
  @Test  
  public void testUserShow() throws Exception{  
      MockHttpServletRequest request = new MockHttpServletRequest();  
      MockHttpServletResponse response = new MockHttpServletResponse();  
      request.setServletPath("/mvc/user.show");  
      request.addParameter("name", "张三");  
      request.addParameter("password", "123456");  
      request.setMethod("post");  
      request.setAttribute("msg", "测试action成功");  
      final ModelAndView mav = this.excuteAction(request, response);  
      Assert.assertEquals("userManager/userlist", mav.getViewName());  
      String msg=(String)request.getAttribute("msg");  
      System.out.println(msg);  
  }  
}  