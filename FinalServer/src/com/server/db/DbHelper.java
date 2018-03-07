package com.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.server.util.UserDAOUtil;
import com.server.util.UserFriendUtil;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class DbHelper {
	static String drivername = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/expressdb";
	static String username = "root";
	static String password = "";
	static
	{
		try
		{
			Class.forName(drivername);//��������
			System.out.println("���������ɹ���");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("�������ݿ�ɹ���");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void free(ResultSet rs,Connection conn, Statement stmt)
	{
		try
		{
			if(rs != null)
				rs.close();//�رս����
		}
		catch(SQLException e)
		{
			System.out.println("�ر�ResultSetʧ�ܣ�");
			e.printStackTrace();
		}
		finally{
			try
			{
				if (conn != null)
					conn.close();//�ر�����
			}
			catch(SQLException e)
			{
				System.out.println("�ر�Connectionʧ�ܣ�");
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(stmt != null)
						stmt.close();
				}
				catch(SQLException e)
				{
					System.out.println("�ر�Statementʧ�ܣ�");
					e.printStackTrace();
				}
			}
		}
	}
		
	//ִ�в�ѯ���
	public List<User> sqlQuery(String sql,String []paras,String messageType)
	{
		List<User> users = null;
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//ִ�в���
			rs=pstmt.executeQuery();
			//users = UserDAOUtil.ResultSet2List(rs);
			
			if(messageType == MessageType.LOGIN || messageType == MessageType.REGISTER 
					||messageType == MessageType.SECONDQUERY ||messageType==MessageType.CHANGEPW){
				users = UserDAOUtil.ResultSet2List(rs);	
			}else if(messageType == MessageType.MYFRIEND){
				users = UserFriendUtil.getMyFriends(rs);
			}
					
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return users;
	}
	
	public List<Order> sqlQueryRecord(String sql,String []paras)
	{
		List<Order> publish = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			rs=pstmt.executeQuery();
			
		    publish = UserFriendUtil.getUserRecord(rs);
					
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return publish;
	}
	
	public List<String> sqlQueryFriends(String sql,String []paras)
	{
		List<String> friends = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//ִ�в���
			rs=pstmt.executeQuery();
			friends = UserDAOUtil.ResultSet2FriendIds(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return friends;
	}
	
	public List<Order> sqlQueryOrders(String sql,String []paras)
	{
		List<Order> orders = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//ִ�в���
			rs=pstmt.executeQuery();
			orders = UserDAOUtil.ResultSet2Orders(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return orders;
	}
	
	public List<CMessage> sqlQueryChatRecords(String sql, String[] paras) {
		// TODO Auto-generated method stub
		List<CMessage> listmsg = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//ִ�в���
			rs=pstmt.executeQuery();
			listmsg = UserDAOUtil.ResultSetChatRecords(rs);	
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return listmsg;
	}
	
	public static int executeUpdate(String sql,Object[]params)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int rows=-1;
		try
		{
			conn = DbHelper.getConnection();
			pstmt = conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<params.length;i++)
			{
				pstmt.setObject(i+1, params[i]);
			}
			rows = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println("ʹ��Ԥ�������������ݲ������������쳣");
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return rows;
	}
	

	public User sqlQuery(String sql, String[]params) {
		// TODO Auto-generated method stub
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//��pstmt���ʺŸ�ֵ
			for(int i=0;i<params.length;i++){
				pstmt.setString(i+1, params[i]);
			}
			//ִ�в���
			rs=pstmt.executeQuery();
			user = UserDAOUtil.ResultUser(rs);	
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//�ر���Դ
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return user;
	}
}
