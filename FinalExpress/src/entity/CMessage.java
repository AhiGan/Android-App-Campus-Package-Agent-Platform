package entity;

import java.io.Serializable;

public class CMessage implements Serializable {
	private User sender;//������
	private User receiver;//������
	private String msgType;//��Ϣ����
	private Object obj;//��Ϣ����
	private String time; //������Ϣ��ʱ��
	private byte[] avatar= null;//ͷ��
	

	public CMessage() {
		super();
	}
	public CMessage(String content, String type) {
		// TODO Auto-generated constructor stub
		this.obj =content;
		this.msgType = type;
	}
	public CMessage(User sender, User receiver, String msgType, Object obj, String time) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.msgType = msgType;
		this.obj = obj;
		this.time = time;
	}
	
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	//ͷ��
	public byte[] getAvatar()
	{
		return avatar;
	}
	public void setAvatar(byte[] avatar)
	{
		this.avatar = avatar;
	}
}
