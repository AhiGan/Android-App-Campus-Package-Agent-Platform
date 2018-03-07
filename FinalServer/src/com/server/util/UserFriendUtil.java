package com.server.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.server.db.DbHelper;

import entity.Order;
import entity.User;

public class UserFriendUtil {
	
	public static List<User> getMyFriends(ResultSet rs) throws SQLException{
		List<User> users = new ArrayList<User>();
		String messageType = "SECONDQUERY";
		
		//rsΪuser�ĺ��Ѽ��ϣ���ѯÿ�����ѵ�userinfo������Ϣ
		while(rs.next()){
			try {
				String sql = "select * from userinfo where studentid=?";
				String[] paras = new String[1];
				paras[0] = rs.getString("friendid");			
				//��ÿ�����õĻ�����Ϣ����users��
				List<User> us = new DbHelper().sqlQuery(sql, paras,messageType);
				users.addAll(us);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}
	
	//��ȡ�ҷ����Ŀ����Ϣ
	public static List<Order> getUserRecord(ResultSet rs) throws SQLException{
		List<Order> receive = new ArrayList<Order>();
		
		while(rs.next()){
			try {
				Order order = new Order();
				order.setOrderId(rs.getInt(1));
				order.setPublishUserId(rs.getString(2));
				order.setReceiveUserId(rs.getString(3));
				order.setExpressDescribe(rs.getString(4));
				if(rs.getString(5).equals("F")){
					order.setAccepted(false);
				}
				if(rs.getString(5).equals("T")){
					order.setAccepted(true);
				}
				if(rs.getString(6).equals("F")){
					order.setReceived(false);
				}
				if(rs.getString(6).equals("T")){
					order.setReceived(true);
				}
				order.setPublishDate(rs.getString(7));
				order.setPublishName(rs.getString(8));
				receive.add(order);
				
				System.out.println("receive   "+order.getPublishUserId()+"  "+order.getReceiveUserId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return receive;
	}

}