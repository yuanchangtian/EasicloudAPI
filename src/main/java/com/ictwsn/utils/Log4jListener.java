package com.ictwsn.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Log4jListener  implements ServletContextListener {  
    public static final String log4jdirkey = "log4jdir";  
    public void contextDestroyed(ServletContextEvent servletcontextevent) {  
        System.getProperties().remove(log4jdirkey);  
    }  
    public void contextInitialized(ServletContextEvent servletcontextevent) {  
    String log4jdir = servletcontextevent.getServletContext().getRealPath("/")+"logs\\";  
    System.out.println("log4jdir:"+log4jdir);  
    System.setProperty(log4jdirkey, log4jdir);  
    }  
}  
