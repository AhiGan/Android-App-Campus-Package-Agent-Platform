package com.server.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import com.server.dao.UserDAO;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class SServer {
	public SServer()
	{
		ServerSocket ss = null;
		try
		{
			 ss = new ServerSocket(8890);
			 System.out.println("�����������������ڼ���8888�˿�...");
			
			 while(true)
			 {
				//���ܿͻ��˵�socket����
				 Socket s = ss.accept();
				//���ܿͻ���������Ϣ
				 ObjectInputStream ois = new ObjectInputStream(s.getInputStream());		 
				 CMessage msg = (CMessage)ois.readObject();
				 User sender = msg.getSender();     //�������й��ܶ����õ�sender�������ݿ�
				 
				 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				 
				 //�жϿͻ��˵Ĳ��������Ƿ�Ϊ����½����
				 if(msg.getMsgType().equals(MessageType.LOGIN))
				 {
					 ServerCallable call = new ServerCallable(MessageType.LOGIN, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 //���̷߳����̳߳���,��¼����ִ�н����󱣳��̣߳���ͬһ���û����Գ�ע��������������ô��߳�
					 thread.start();
					 
					 User u = (User)task.get(); //ִ�е�¼�������صĽ����UserDao�еķ��ؽ��
					 if(u != null)
					 {
						 System.out.println("["+u.getUsername()+"]������!");
						 msg.setMsgType(MessageType.LOGIN_SUCCESS);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 msg.setMsgType(MessageType.LOGIN_FAILURE);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
					 }
					 thread.interrupt();
					
				 }
				 else if(msg.getMsgType().equals(MessageType.REGISTER))
				 {
					 ServerCallable call = new ServerCallable(MessageType.REGISTER,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1")
						 msg.setMsgType(MessageType.REGISTER_1); //���ݿ��Ѵ��ڴ���
				     if(str=="2")
						 msg.setMsgType(MessageType.REGISTER_2);  //δ����ɹ�
					 if(str=="3")
						 msg.setMsgType(MessageType.REGISTER_3);  //ע��ɹ�
					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //ע�����ɱ���߳�
					 
				 }else if(msg.getMsgType().equals(MessageType.MYFRIEND)){
					 ServerCallable call = new ServerCallable(MessageType.MYFRIEND,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } else if(msg.getMsgType().equals(MessageType.ADDFRIENDIF)){
					 ServerCallable call = new ServerCallable(MessageType.ADDFRIENDIF,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } else if(msg.getMsgType().equals(MessageType.ORDER_REFRESH)){
					 ServerCallable call = new ServerCallable(MessageType.ORDER_REFRESH, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 
					 List<Order> orders = (List<Order>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 if(orders != null)
					 {
						 System.out.println("���ֿ�ݳɹ���");
						 msg.setMsgType(MessageType.ORDER_REFRESH_SUCCESS);
						 msg.setObj(orders);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("���ֿ��ʧ�ܣ�");
						 msg.setMsgType(MessageType.ORDER_REFRESH_FAILURE);
						 msg.setObj(orders);
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }
				 else if(msg.getMsgType().equals(MessageType.NEW_FRIENDS))
				 {
					 ServerCallable call = new ServerCallable(MessageType.NEW_FRIENDS, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<User> newFriends = (List<User>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 if(newFriends != null)
					 {
						 System.out.println("������ʶ���˳ɹ���");
						 msg.setObj(newFriends);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("������ʶ����ʧ�ܣ�");
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }
				 else if(msg.getMsgType().equals(MessageType.SET_AVATAR))
				 {
					 ByteArrayInputStream in = new ByteArrayInputStream((byte[])msg.getObj());
					 BufferedImage image = ImageIO.read(in);
					 File newFile = new File("C://avatar//"+sender.getStudentid()+".jpg");
					 boolean b = ImageIO.write(image, "JPEG", newFile);
					 msg.setReceiver(sender);
					 if (b == true)//ͷ�񱣴�ɹ�
					 {
						 System.out.println("����ͷ��ɹ���");
						 msg.setObj(true);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("����ͷ��ʧ�ܣ�");
						 msg.setObj(false);
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }else if(msg.getMsgType().equals(MessageType.PUBLISH)){
					 ServerCallable call = new ServerCallable(MessageType.PUBLISH,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.RECEIVE)){
					 ServerCallable call = new ServerCallable(MessageType.RECEIVE,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.ADDFRIEND)){
					 System.out.println("msg1"+msg.getObj().toString());
					 ServerCallable call = new ServerCallable(MessageType.ADDFRIEND,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();
				 } else if(msg.getMsgType().equals(MessageType.ORDERPUBLISH))
				 {
					 ServerCallable call = new ServerCallable(MessageType.ORDERPUBLISH,msg.getObj());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1")
						 msg.setMsgType(MessageType.ORDERPUBLISH_SUCCESS); //�����ɹ�
				     if(str=="0")
						 msg.setMsgType(MessageType.ORDERPUBLISH_FAILURE);  //����ʧ��
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //ע�����ɱ���߳�
				 }
				 else if(msg.getMsgType().equals(MessageType.CHANGEPW))
				 {
					 System.out.println("3");
					 ServerCallable call = new ServerCallable(MessageType.CHANGEPW,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1-1")
						 msg.setMsgType(MessageType.CHANGEPW_SUCCESS); //�޸ĳɹ�
				     if(str=="1-0")
						 msg.setMsgType(MessageType.CHANGEPW_FAILURE);  //����ʧ��
				     if(str=="0")
				    	 msg.setMsgType(MessageType.PW_WRONG);//���벻��
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //ע�����ɱ���߳�
				 } 
				 else if(msg.getMsgType().equals(MessageType.SENDMESSAGE))
				 {
					 ServerCallable call = new ServerCallable(MessageType.SENDMESSAGE,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1-1")
						 msg.setMsgType(MessageType.CHANGEPW_SUCCESS); //�޸ĳɹ�
				     if(str=="1-0")
						 msg.setMsgType(MessageType.CHANGEPW_FAILURE);  //����ʧ��
				     if(str=="0")
				    	 msg.setMsgType(MessageType.PW_WRONG);//���벻��
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //ע�����ɱ���߳�
				 }else if(msg.getMsgType().equals(MessageType.CONFIRM)){
					 ServerCallable call = new ServerCallable(MessageType.CONFIRM,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 //��ȡ�û���¼���߳�
					 //ServerThreadMgr.get(sender).start();
					 msg.setReceiver(sender);
					 msg.setObj((Integer)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();
				 }else if(msg.getMsgType().equals(MessageType.FINDPASSWORD))
				 {
					 ServerCallable call = new ServerCallable(MessageType.FINDPASSWORD, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 //���̷߳����̳߳���,��¼����ִ�н����󱣳��̣߳���ͬһ���û����Գ�ע��������������ô��߳�
					 thread.start();
					 
					 User u = (User)task.get(); //ִ�е�¼�������صĽ����UserDao�еķ��ؽ��
					 if(u != null)
					 {
						 System.out.println("["+u.getUsername()+"]�ҵ���!");
						 msg.setMsgType(MessageType.FINDPASSWORD_SUCCESS);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 msg.setMsgType(MessageType.FINDPASSWORD_FAILURE);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
					 }
					 thread.interrupt();
					
				 }
				 else if(msg.getMsgType().equals(MessageType.RANKING)){
					 ServerCallable call = new ServerCallable(MessageType.RANKING,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }
				 else if(msg.getMsgType().equals(MessageType.LOAD_CHAT_RECORDS)){					 
					 ServerCallable call = new ServerCallable(MessageType.LOAD_CHAT_RECORDS, msg.getSender(),msg.getReceiver());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<CMessage> chatrecords = (List<CMessage>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 
					 if(chatrecords != null)
					 {
						 System.out.println("���������¼�ɹ���");
						 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_SUCCESS);
						 for(int i = 0; i<chatrecords.size(); i++)
						 {
							System.out.println("sender: "+chatrecords.get(i).getSender().getStudentid()+ "  receiver: "+ chatrecords.get(i).getReceiver().getStudentid()
									 + "  content: " +(String)chatrecords.get(i).getObj());
							 //System.out.println("content: " +(String)chatrecords.get(i).getObj());
						 }
						 msg.setObj(chatrecords);
					 }
					 else
					 {
						 System.out.println("���������¼ʧ�ܣ�");
						 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_FAILURE);
					 }
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.INSERT_CHAT_RECORDS)){
					 ServerCallable call = new ServerCallable(MessageType.INSERT_CHAT_RECORDS,msg.getObj());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 //System.out.println("msg2"+task.get().toString());
					 
					 Boolean flag =  (Boolean)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 
					 if(flag)
					 {
						 System.out.println("���������¼�ɹ���");
						 msg.setMsgType(MessageType.INSERT_CHAT_RECORDS_SUCCESS);
					 }
					 else
					 {
						 System.out.println("���������¼ʧ�ܣ�");
						 msg.setMsgType(MessageType.INSERT_CHAT_RECORDS_FAILURE);

					 }
					 msg.setObj(flag);
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.CHATER_REFRESH)){
					 
					 ServerCallable call = new ServerCallable(MessageType.CHATER_REFRESH, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<CMessage> chaterList= (List<CMessage>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 User temp = new User();
					 
					 for(int i = 0;i<chaterList.size();i++){
						 if(chaterList.get(i).getReceiver().getStudentid().equals(msg.getSender().getStudentid())){
							 ServerCallable call2 = new ServerCallable(MessageType.FIND_USER, chaterList.get(i).getSender());
							 FutureTask<Object> task2 = new FutureTask<Object>(call2);
							 Thread thread2 = new Thread(task2);
							 thread2.start();
							 temp = (User)task2.get();
							 chaterList.get(i).setSender(temp);
							 thread2.interrupt();
						 }else{
							 ServerCallable call2 = new ServerCallable(MessageType.FIND_USER, chaterList.get(i).getReceiver());
							 FutureTask<Object> task2 = new FutureTask<Object>(call2);
							 Thread thread2 = new Thread(task2);
							 thread2.start();
							 temp = (User)task2.get();
							 chaterList.get(i).setReceiver(temp);
							 thread2.interrupt();
						 }
					 }
					 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_SUCCESS);
					 /*for(int i = 0; i<chaterList.size(); i++)
					 {
						 System.out.println("~~~~~~~");
						 
						System.out.println("sender: "+chaterList.get(i).getSender().getUsername() + "  receiver: "+ chaterList.get(i).getReceiver().getUsername()
								 + "  content: " +(String)chaterList.get(i).getObj());
						 //System.out.println("content: " +(String)chatrecords.get(i).getObj());
					 }*/
					 msg.setObj(chaterList);
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.ADDPOINT)){
					 ServerCallable call = new ServerCallable(MessageType.ADDPOINT,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } 
			 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
