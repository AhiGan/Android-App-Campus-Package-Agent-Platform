package com.express.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;

import com.express.activity.HomePageActivity;
import com.express.activity.LoginActivity;
import com.express.activity.RegisterActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class MyCallable implements Callable<Object> {
	private Socket s;
	/**
     * ���������������
     */
	private String operation;//������
	private Object obj;      //���͵Ķ���
	
	public MyCallable(String operation, Object obj)
	{
		this.operation = operation;
		this.obj = obj;
	}

	@Override
	public Object call() throws Exception {
		Object result = null;  //���ز������
		try
		{
			s = new Socket("223.3.164.112", 8890);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(operation.equals(MessageType.LOGIN))
		{
			result = login((User)obj);
		}
		else if(operation.equals(MessageType.REGISTER))
		{
			result = register((User)obj);
		}else if(operation.equals(MessageType.MYFRIEND))
		{
			result = myfriend((User)obj);
		}
		else if(operation.equals(MessageType.ORDER_REFRESH))
		{
			result = orderRefresh();
		}
		else if(operation.equals(MessageType.NEW_FRIENDS))
		{
			result = newFriends();
		}
		else if(operation.equals(MessageType.SET_AVATAR))
		{
			result = setAvatar();
		}else if(operation.equals(MessageType.PUBLISH))
		{
			result = Publish((User)obj);
		}else if(operation.equals(MessageType.RECEIVE))
		{
			result = Receive((User)obj);
		}else if(operation.equals(MessageType.ADDFRIEND))
		{
			result = AddFriend((CMessage)obj);
		}else if(operation.equals(MessageType.ORDERPUBLISH))
		{
			result=PublishOrder((Order)obj);
		}
		else if(operation.equals(MessageType.CHANGEPW))
		{
			result=ChangePW((User)obj);
     	}else if(operation.equals(MessageType.CONFIRM))
		{
			result=ConfirmExpress((CMessage)obj);
     	}//else if(operation.equals(MessageType.SENDMESSAGE))
//		{
//			result=SendMessge(obj);
//		
		else if(operation.equals(MessageType.LOAD_CHAT_RECORDS))
		{
			result = loadChatRecords();
		}else if(operation.equals(MessageType.UPDATE_CHAT_RECORDS)){
			newChatRecord();
		}else if(operation.equals(MessageType.NEW_CHAT_RECORD)){
			System.out.println("update already");
		}else if(operation.equals(MessageType.INSERT_CHAT_RECORDS)){
			insertChatRecord();
		}else if(operation.equals(MessageType.CHATER_REFRESH)){
			result = chaterRefresh();
		}else if(operation.equals(MessageType.RANKING))
		{
			result = Ranking((User)obj);
		}else if(operation.equals(MessageType.FINDPASSWORD))
		{
			result=FindPassword((User)obj);
		}else if(operation.equals(MessageType.ADDFRIENDIF))
		{
			result=AddFriendIf(obj);
		}else if(operation.equals(MessageType.ADDPOINT))
		{
			result=AddPoint(obj);
		}
		return result;
	}
	


	private void send(CMessage msg)
	{
		//�����������Ϣ
        ObjectOutputStream oos;
		try
		{
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        
	}
	
	private CMessage receive()
	{
		//���շ���˵���Ӧ
		CMessage msg = null;
        ObjectInputStream ois;
		try
		{
			ois = new ObjectInputStream(s.getInputStream());
			msg =  (CMessage)ois.readObject();
		}
		catch (StreamCorruptedException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return msg;
	}
	
	private User login(User user)
	{
		User us;
        CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.LOGIN);
		
		send(msg);
		msg=receive();
		us = msg.getReceiver();
        return us;
	}

	private String register(User user){
		String str = "0";
		//���ѧ�Ų����Ϲ���
		if(user.getStudentid().length()!= 8){
			str = "0";
		}
		else{
			//����ע��ķ����ߣ���Ϣ���ͣ�ע������
			CMessage msg = new CMessage();
			msg.setSender(user);
			msg.setMsgType(MessageType.REGISTER);
			
			send(msg);
			msg = receive();
			
			if(msg.getMsgType().equals(MessageType.REGISTER_1))
	        {
	            str = "1";
	        }
	        if(msg.getMsgType().equals(MessageType.REGISTER_2))
	        {
	        	str = "2";
	        }
	        if(msg.getMsgType().equals(MessageType.REGISTER_3))
	        {
	        	str = "3";
	        }
	        
		}
		return str;
	}
	
	private Object myfriend(User user){
		CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.MYFRIEND);
		
		send(msg);
		msg = receive();
		
		//���������ص�obj����ת��ΪList<User>���洢������Ϣ
		Object users = msg.getObj();
		return users;
	}
	private List<Order> orderRefresh()
	{
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.ORDER_REFRESH);
		send(msg);
		//do{
			msg=receive();
		//}while(!msg.getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid()));
		return (List<Order>)msg.getObj();
	}
	
	private List<User> newFriends()
	{
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.NEW_FRIENDS);
		send(msg);
		//do{
			msg=receive();

		//}while(!msg.getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid()));
		return (List<User>)msg.getObj();
	}
	
	private boolean setAvatar()
	{
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.SET_AVATAR);
		msg.setObj(obj);
		send(msg);
		//do{
			msg=receive();
		//}while(!msg.getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid()));
		return ((Boolean)msg.getObj()).booleanValue();
	}
	
	//��ȡ�ҷ����Ŀ����Ϣ
	private Object Publish(User user){
		CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.PUBLISH);
		
		send(msg);
		msg = receive();

		Object obj = msg.getObj();
		return obj;
	}
	
	//��ȡ�ҵĴ�����Ϣ
	private Object Receive(User user){
		CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.RECEIVE);
		
		send(msg);
		msg = receive();

		Object obj = msg.getObj();
		return obj;
	}
	
	//������
	private Object AddFriend(CMessage message){
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.ADDFRIEND);
		msg.setObj(message.getObj());
		
		send(msg);
		msg = receive();

		Object obj = msg.getObj();
		return obj;
	}
	
	//ȷ�ϼ�����
	private Object AddFriendIf(Object obj){
		CMessage msg = (CMessage)obj;
		
		send(msg);
		msg = receive();

		Object ob = msg.getObj();
		return ob;
	}
    private String PublishOrder(Order order) {
		
		// TODO Auto-generated method stub
		String str = "0";
		//���ѧ�Ų����Ϲ���

		//����ע��ķ����ߣ���Ϣ���ͣ�ע������
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setObj(order);
		msg.setMsgType(MessageType.ORDERPUBLISH);
		
		send(msg);
		msg = receive();
		System.out.println(msg.getMsgType());
		if(msg.getMsgType().equals(MessageType.ORDERPUBLISH_SUCCESS))
        {
            str = "1";
        }
        if(msg.getMsgType().equals(MessageType.ORDERPUBLISH_FAILURE))
        {
        	str = "0";
        }
	        
		return str;
	}
	
	private String ChangePW(User user) {
		System.out.println("2");
		// TODO Auto-generated method stub
		String str = "0";//

		//����ע��ķ����ߣ���Ϣ���ͣ�ע������
		CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.CHANGEPW);
		
		send(msg);
		msg = receive();
		System.out.println(msg.getMsgType());
		if(msg.getMsgType().equals(MessageType.CHANGEPW_SUCCESS))
        {
            str = "1-1";
        }
        if(msg.getMsgType().equals(MessageType.CHANGEPW_FAILURE))
        {
        	str = "1-0";
        }
        if(msg.getMsgType().equals(MessageType.PW_WRONG))
        {
        	str = "0";
        }
	        
		return str;
	}
	private User FindPassword(User user)
	{
		User us;
        CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.FINDPASSWORD);
		
		send(msg);
		msg=receive();
		us = msg.getReceiver();
        return us;
	}
	//���а�
	private Object Ranking(User user){
		CMessage msg = new CMessage();
		msg.setSender(user);
		msg.setMsgType(MessageType.RANKING);
		
		send(msg);
		msg = receive();

		Object obj = msg.getObj();
		return obj;
	}
	
	//ȷ�ϴ��죬�������ݿ��е�order��
	private int ConfirmExpress(CMessage message){
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.CONFIRM);
		msg.setObj(message.getObj());
		
		send(msg);
		msg = receive();

		int updateRow = (Integer)msg.getObj();
		return updateRow;
	}
	//����Ϣ
//	private Object SendMessage(Object obj){
//		CMessage msg = (CMessage)obj;
//		send(msg);
//		msg = receive();
//
//		Object obj1 = msg.getObj();
//		return obj1;
//	}
	
	private CMessage insertChatRecord() {
		// TODO Auto-generated method stub
		CMessage msg = new CMessage();
		//msg.setSender(LoginActivity.getUser());
		//msg.setReceiver(MainExpress.getReceiver());
		msg.setObj((CMessage)obj);
		msg.setSender(LoginActivity.getUser());
		msg.setReceiver(HomePageActivity.getReceiver());
		msg.setMsgType(MessageType.INSERT_CHAT_RECORDS);
		send(msg);
		msg=receive();
		return msg;
	}

	private void newChatRecord(){
		// TODO Auto-generated method stub
		CMessage msg = new CMessage();
		//msg.setSender(LoginActivity.getUser());
		//msg.setReceiver(MainExpress.getReceiver());
		msg.setObj((CMessage)obj);
		msg.setSender(LoginActivity.getUser());
		msg.setReceiver(HomePageActivity.getReceiver());
		msg.setMsgType(MessageType.ORIGNAL_CHAT_RECORD);
		send(msg);
		msg=receive();
		/*CMessage temp = (CMessage)msg.getObj();
		if(temp.getReceiver().equals(LoginActivity.getUser())&&temp.getSender().equals(MainExpress.getReceiver())){
			temp.setMsgType(MessageType.CHAT_RECEIVED);	
		}*/
	}

	/*
	 * ���������¼
	 */
	@SuppressWarnings("unchecked")
	private List<CMessage> loadChatRecords()
	{
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setReceiver(HomePageActivity.getReceiver());
		msg.setMsgType(MessageType.LOAD_CHAT_RECORDS);
		send(msg);
		msg=receive();
		return (List<CMessage>)msg.getObj();
	}
	

	/*
	 * �������������б�
	 */
	private List<CMessage> chaterRefresh() {
		// TODO Auto-generated method stub
		CMessage msg = new CMessage();
		msg.setSender(LoginActivity.getUser());
		msg.setMsgType(MessageType.CHATER_REFRESH);
		send(msg);
		msg=receive();
		return (List<CMessage>)msg.getObj();
	}
	
	//ȷ���ջ����ӻ���
	private Object AddPoint(Object obj){
		CMessage msg = (CMessage)obj;
		
		send(msg);
		msg = receive();

		Object ob = msg.getObj();
		return ob;
	}
}
