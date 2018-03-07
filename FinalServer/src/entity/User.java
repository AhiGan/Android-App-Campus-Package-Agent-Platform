package entity;

import java.io.Serializable;

public class User implements Serializable {
	private String studentid;  //ѧ��
	private String username;   //����
	private String password;   //����
	private String department; //Ժϵ
	private String dormitory;  //������
	private byte[] avatar = null;//ͷ��
	private int point;

	//ѧ��
	public String getStudentid()
	{
		return studentid;
	}
	public void setStudentid(String studentid)
	{
		this.studentid = studentid;
	}
	
	//����
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	//����
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	//Ժϵ
	public String getDepartment(){
		return department;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	
	//������
	public String getDormitory(){
		return dormitory;
	}
	public void setDormitory(String dormitory){
		this.dormitory = dormitory;
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
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}