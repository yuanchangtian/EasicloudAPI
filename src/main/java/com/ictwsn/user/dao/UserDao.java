package com.ictwsn.user.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ictwsn.bean.UserBean;

/**
 * 
 * @author 黄若然 MyBatis操作用户
 * 
 */

public interface UserDao {

	// 创建用户
	@Insert("insert into tbl_user(user_name,nick_name,user_password,user_key,user_role,user_phone,user_email) "
			+ "values(#{user_name},#{nick_name},#{user_password},#{user_key},#{user_role},#{user_phone},#{user_email})")
	public int createUser(UserBean userBean);

	// 获取用户id
	@Select("select user_id from tbl_user where user_key = #{user_key}")
	public int getUserId(String user_key);

	// 删除用户
	@Delete("delete from tbl_user where user_id = #{user_id}")
	public int deleteUser(int user_id);

	// 通过user_id获取用户
	@Select("select * from tbl_user where user_id = #{0}")
	public UserBean getUser(int user_id);

	// 修改用户
	@Update("update tbl_user set nick_name=#{nick_name},user_phone=#{user_phone},user_email=#{user_email} where user_id=#{user_id}")
	public int updateUser(UserBean userBean);

	// 通过user_id获取用户密码
	@Select("select user_password from tbl_user where user_id=#{user_id}")
	public String getPassword(int user_id);

	// 修改密码
	@Update("update tbl_user set user_password=#{0} where user_id=#{1}")
	public int changePassword(String new_password, int user_id);

	// 通过user_name获取用户所有信息
	@Select("select * from tbl_user where user_name =#{user_name}")
	public UserBean getUserByName(String user_name);

	// 通过user_name获取用户密码
	@Select("select user_password from tbl_user where user_name =#{user_name}")
	public String getPasswordByName(String user_name);

	// 查找邮箱号是否已经存在
	@Select("select count(*) from tbl_emailverify where email=#{0} and verification=#{1}")
	public int getEmailCount(String email, int verification);

	// 插入邮箱号和验证码
	@Insert("insert into tbl_emailverify (email,email_code,verification,deadline) values (#{0},#{1},0,#{2})")
	public int insertEmailVerify(String email, String email_code, Date deadline);

	// 判断邮箱号是否过期
	@Select("select count(*) from tbl_emailverify where email=#{0} and deadline>=#{1}")
	public int judgeEmailDatetime(String email, Date deadline);

	// 更变邮箱号为已验证
	@Update("update tbl_emailverify set verification=1 where email = #{0}")
	public int updateEmailVerify(String email);

	// 更变邮箱号最新过期时间
	@Update("update tbl_emailverify set deadline=#{1},email_code=#{2} where email = #{0}")
	public int updateEmailVerifyDateCode(String email, Date deadline,
			String emailCode);

	// 获取邮箱验证码
	@Select("select email_code from tbl_emailverify where email=#{0}")
	public String getEmailCode(String email);


}
