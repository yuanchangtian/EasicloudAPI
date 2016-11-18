package com.ictwsn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class VerificationEMail {
	
	public static Logger logger = LoggerFactory.getLogger(VerificationEMail.class);

	
	public static void main(String[] args) {
		String verification = "";
		for(int i=0;i<6;i++)
			verification += (int)(Math.random()*10);
		String userEamil = "376951732@qq.com";
		VerificationEMail ve = new VerificationEMail();
		ve.mail(verification, userEamil);
	}
	
	public static void mail(String verification,String userEamil) {
        String smtphost = "smtp.163.com"; // 发送邮件服务器
        String user = "ruoranhuang@163.com"; // 邮件服务器登录用户名
        String password = "HeroVagabond2016";  // 邮件服务器登录密码
        String from = "ruoranhuang@163.com"; // 发送人邮件地址
        
        
        String to[] = new String [1];
//        to[0] = "376951732@qq.com";
        to[0] = userEamil;
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
		String date = df.format(new Date());
		
        String subject = ""+date+"Easicloud邮箱验证码"; // 邮件标题
        
        
//        System.out.println("SQL:"+sql2);
        
        
        String body = "您好,\n\n"+"Easicloud系统"+date+"验证码信息如下：\n\n"+"邮箱"+to[0]+"的验证码为："+verification+"\n\n"+"祝好,\n\n中科院计算技术研究所\n传感器网络实验室";
        
        for(int i = 0;i<1;i++)
        {
        	Send(smtphost,user,password,from,to[i],subject,body);
        }
    }
	
	
    
    public static void Send(String smtphost, String user, String password, String from,  String to, 
			   String subject, String body)
    {
    	 try {
    		 System.out.println("开始发送");
	            Properties props = new Properties();
	            props.put("mail.smtp.host", smtphost);
	            props.put("mail.smtp.auth","true");
	            Session ssn = Session.getInstance(props, null);

	            MimeMessage message = new MimeMessage(ssn);

	            InternetAddress fromAddress = new InternetAddress(from);
	            message.setFrom(fromAddress);
	            InternetAddress toAddress = new InternetAddress(to);
	            message.addRecipient(Message.RecipientType.TO, toAddress);
	            message.setSubject(subject);
	            message.setText(body);

	            Transport transport = ssn.getTransport("smtp");
	            transport.connect(smtphost, user, password);
	            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
	            //transport.send(message);
	            transport.close();
	            System.out.println("已经发送");
	            
	        } catch(Exception m) {
	            m.printStackTrace();
	        }
    }
}
