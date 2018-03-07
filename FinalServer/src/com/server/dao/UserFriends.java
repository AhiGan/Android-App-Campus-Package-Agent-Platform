package com.server.dao;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.List;

import com.server.db.DbHelper;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class UserFriends {

	public static String Register(User user) {

		String messageType = "REGISTER";
		String str = "1";       //�洢���ؽ��
		
		//��ѯsutdentid�Ƿ��Ѿ������ݿ���
		String sql1 = "select * from userinfo where studentid =?";
		String[] paras1 = new String[1];
		paras1[0] = user.getStudentid();	

		List<User> users = new DbHelper().sqlQuery(sql1, paras1,messageType);
		//����1��ʾ���ݿ����Ѿ��д���
		if (users != null && users.size() > 0) {
			str = "1";   
		}
		
		//������ݿ���û�д���
        else{
			//ִ�в������
			String sql2 = "Insert into userinfo (studentid, username, password, department, dormitory) values(?,?,?,?,?)";
			String[] paras2 = new String[5];
			paras2[0] = user.getStudentid();
			paras2[1] = user.getUsername();
			paras2[2] = user.getPassword();
			paras2[3] = user.getDepartment();		
			paras2[4] = user.getDormitory();
			new DbHelper();
			int row = DbHelper.executeUpdate(sql2, paras2);
			if(row == -1){
				str = "2";    //����ʧ��
			}
			else{
				str = "3";    //����ɹ�
			}			
		}
		return str;	
	}
	
	public static List<User> MyFriends(User user){
		
		String messageType = "MYFRIEND";
		String sql = "select * from friend where studentid =?";
		String[] paras = new String[1];
		paras[0] = user.getStudentid();
		
		List<User> users = new DbHelper().sqlQuery(sql, paras, messageType);
		for(int i=0;i<users.size();i++){
			if(users.get(i).getStudentid().equals(paras[0])){
				users.remove(i);
			}
		}
		return users;
	}

	public static List<Order> getPublish(User user){	
		String sql = "select * from expressorder where publisher =?";
		String[] paras = new String[1];
		paras[0] = user.getStudentid();
		
		List<Order> publish = new DbHelper().sqlQueryRecord(sql, paras);
		
		return publish;
	}
	public static List<Order> getReceive(User user){	
		String sql = "select * from expressorder where receiver =?";
		String[] paras = new String[1];
		paras[0] = user.getStudentid();
		
		List<Order> publish = new DbHelper().sqlQueryRecord(sql, paras);
		
		return publish;
	}
	
	//������
	public static List<User> getFriend(CMessage msg){	
		String messagetype1 = "LOGIN";
		String str = (String)msg.getObj();
		User user = new User();
		user = msg.getSender();
		
		//��ѯ���ݿ����������������Ƶ���
		String sql = "select * from userinfo where username like '%"+str+"%'";
		String[] paras = new String[0];
		List<User> friends1 = new DbHelper().sqlQuery(sql,paras,messagetype1);
		for(int i=0;i<friends1.size();i++){
			System.out.println("1friends"+i+":  "+friends1.get(i).getUsername());
		}
		
		//��ȥ�Ѿ����ҵĺ��ѵ���
		String messageType2 = "MYFRIEND";
		String sql2 = "select * from friend where studentid =?";
		String[] paras2 = new String[1];
		paras2[0] = user.getStudentid();
		List<User> friends2 = new DbHelper().sqlQuery(sql2,paras2,messageType2);
		
		for(int i=0;i<friends2.size();i++){
			System.out.println("2friends"+i+":  "+friends2.get(i).getUsername());
		}
		for(int i=0;i<friends1.size();i++){
			for(int j=0;j<friends2.size();j++){
				if(friends1.get(i).getStudentid().equals(friends2.get(j).getStudentid())){
					friends1.remove(i);
					break;
				}
			}
		}		
		return friends1;
	}
	//ȷ�ϼӺ���
	public static int AddFriend(CMessage msg) {

		User friendSend = msg.getSender();
		User friendRece = (User)msg.getObj();
		
		String insertSql = "Insert into friend (studentid, friendid)values(?,?)";
		
		String[] paras = new String[2];
		paras[0] = friendSend.getStudentid();
		paras[1] = friendRece.getStudentid();

		int row = DbHelper.executeUpdate(insertSql, paras);
		return row;
	}
	
	//ȷ�ϴ����ݣ��������ݿ�
	public static int ConfirmExpress(CMessage msg){
		Order order = (Order)msg.getObj();
		User user = (User)msg.getSender();
		String updateSql = "update expressorder set receiver=?,accepted=?,received =? where publisher =? and  date =?";
		String[] paras = new String[5];
		paras[0] = user.getStudentid();
		paras[1] = "T";
		paras[2] = "F";
		paras[3] = order.getPublishUserId();
		paras[4] = order.getPublishDate();

		int updateRow = new DbHelper().executeUpdate(updateSql, paras);
		
		return updateRow;
	}
	
	public static String insertOrder(Order order) {
		// TODO Auto-generated method stub
		String messageType = "OrderPublish";
		String str = "0";       //�洢���ؽ��
		String sql2 = "insert into  expressorder (publisher,description,accepted,received,date,publishername) values (?,?,?,?,?,?);";
		String[] paras2 = new String[6];
		paras2[0] =order.getPublishUserId();
		paras2[1] = order.getExpressDescribe();
		paras2[2]="F";
		paras2[3]="F";
		paras2[4] = order.getPublishDate();
		paras2[5]=order.getPublishName();
		new DbHelper();
		int row = DbHelper.executeUpdate(sql2, paras2);
		if(row == -1){
			str = "0";    //����ʧ��
		}
		else{
			str = "1";    //����ɹ�
		}			

		return str;	
	}

	public static String changePw(User user) {
		// TODO Auto-generated method stub
		System.out.println("5");
		String messageType = "CHANGEPW";
		String str = "0";       //�洢���ؽ��
		//�����¾�����
		String password=user.getPassword();
		String pw[]=password.split(",");
		System.out.println(pw[0]);//��
		System.out.println(pw[1]);//��
		String sql = "select * from userinfo where studentid =?";
		String[] paras = new String[1];
		paras[0] = user.getStudentid();
		
		List<User> users = new DbHelper().sqlQuery(sql, paras, messageType);

		if(users.size()==1)
		{
			User nowUser=users.get(0);
			if(nowUser.getPassword().equals(pw[0]))
				str="1";
			String sql2="update userinfo set password=? where studentid =?";
			String[] paras2 = new String[2];
			paras2[0] = pw[1];
			paras2[1] = nowUser.getStudentid();
			
			int row = DbHelper.executeUpdate(sql2, paras2);
			if(row == -1){
				str = "1-0";    //�޸�ʧ��
			}
			else{
				str = "1-1";    //�޸ĳɹ�
			}	
		}
		return str;	
	}
	public static List<User> Ranking(User user){
		String messagetype = "LOGIN";
		String sql = "select * from userinfo order by points desc";
		String[] paras = new String[0];
		
		List<User> ranking = new DbHelper().sqlQuery(sql,paras,messagetype);
		List<User> result=new ArrayList<User>();
		for(int i=0;i<5;i++)
		{
			result.add(ranking.get(i));
		}
		return result;
	}
	
	//ȷ���ջ����ӻ���
		public static int AddPoint(CMessage msg) {

			User friendSend = msg.getSender();
			Order order = (Order)msg.getObj();
			
			//����expressorder���е�T
			String updateSql = "update expressorder set received=? where publisher =? and date =?";			
			String[] paras = new String[3];
			paras[0] = "T";
			paras[1] = order.getPublishUserId();
			paras[2] = order.getPublishDate();
			int row1 = DbHelper.executeUpdate(updateSql, paras);
			
			String updateSql2 = "update userinfo set points=? where studentid =?";
			String[] paras2 = new String[2];
			
			//��ѯuserinfo�е�order.getReceiveUserId()���󣬼ӻ���
			String sql = "select * from userinfo where studentid = ? ";
			String[] paras3 = new String[1];
			paras3[0]=order.getReceiveUserId();
			String messageType = "LOGIN";
			List<User> user0 = new DbHelper().sqlQuery(sql, paras3, messageType);
			
			//����order.getReceiveUserId()��point����1
			int point = user0.get(0).getPoint()+1;
			paras2[0] = ""+point; 
			paras2[1] = order.getReceiveUserId(); 
			int row2 = DbHelper.executeUpdate(updateSql2, paras2);
			return row2;
		}
		
}
