package com.express.activity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.TestArrayAdapter;
import com.express.service.MyCallable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import entity.MessageType;
import entity.User;

public class RegisterActivity extends Activity{
	private EditText studentid;
	private EditText username;
	private EditText password;

	private Spinner mSpinner,dSpinner;//mSpinnerΪרҵ����ѡ�dSpinnerΪ��������ѡ��
    private ArrayAdapter<String> mAdapter,dAdapter ;
    private String [] mStringArray,dStringArray;
	private User user = new User();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_layout);
		
		studentid = (EditText)findViewById(R.id.studentid);
		username = (EditText)findViewById(R.id.username);	
		password = (EditText)findViewById(R.id.password);
//		department = (EditText)findViewById(R.id.department);
//		dormitory = (EditText)findViewById(R.id.dormitory);	
		
		mSpinner = (Spinner) findViewById(R.id.department);
		dSpinner = (Spinner) findViewById(R.id.dormitory);
		mStringArray = getResources().getStringArray(R.array.majors);
		dStringArray = getResources().getStringArray(R.array.dormitorys);
        //����ѡ������ArrayAdapter��������
		mAdapter = new TestArrayAdapter(this,mStringArray);
		dAdapter = new TestArrayAdapter(this,dStringArray);
		//mAdapter = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_item);
		
        //���������б�ķ�� 
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //��adapter2 ��ӵ�spinner��
        mSpinner.setAdapter(mAdapter);
        dSpinner.setAdapter(dAdapter);
        //����¼�Spinner�¼�����  
        mSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener1());
        dSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener2());
        //����Ĭ��ֵ
        mSpinner.setVisibility(View.VISIBLE);
        dSpinner.setVisibility(View.VISIBLE);
	}
	
	class SpinnerXMLSelectedListener1 implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	user.setDepartment(arg0.getItemAtPosition(arg2).toString());
        	/* Toast.makeText(arg0.getContext(), 
                   "The planet is "+  arg0.getItemAtPosition(arg2).toString(), 
                     Toast.LENGTH_SHORT).show(); */
        	//System.out.println("ѡ����:"+mStringArray[position]);
            //view2.setText("��ʹ��ʲô�����ֻ���"+adapter2.getItem(arg2));
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
             
        }
	}
	
	class SpinnerXMLSelectedListener2 implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	user.setDormitory(arg0.getItemAtPosition(arg2).toString());
        	/* Toast.makeText(arg0.getContext(), 
                   "The planet is "+  arg0.getItemAtPosition(arg2).toString(), 
                     Toast.LENGTH_SHORT).show(); */
        	//System.out.println("ѡ����:"+mStringArray[position]);
            //view2.setText("��ʹ��ʲô�����ֻ���"+adapter2.getItem(arg2));
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
             
        }
	}
	
	// ע�ᰴť
		public void Register2(View v){ 
			//User user = new User();
			user.setStudentid(studentid.getText().toString());
			user.setUsername(username.getText().toString());  
			user.setPassword(password.getText().toString());
//			user.setDepartment(department.getText().toString());
//			user.setDormitory(dormitory.getText().toString()); 
			
			MyCallable call = new MyCallable(MessageType.REGISTER, user);
			FutureTask<Object> task = new FutureTask<Object>(call);
			Thread thread = new Thread(task);  
			thread.start();  //����call
			
			String s= "0";	
			try
			{
				s = (String)task.get();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			} 
			catch(ExecutionException e)
			{
				e.printStackTrace();
			}
		
			if(s=="0")     
			{
				Toast.makeText(RegisterActivity.this, "�û�����ʽ����ȷ", Toast.LENGTH_LONG).show();
			}
			if(s=="1")      
			{
				Toast.makeText(RegisterActivity.this, "���û��Ѿ�ע��", Toast.LENGTH_LONG).show();
			}
			if(s=="2")     
			{
				Toast.makeText(RegisterActivity.this, "ע��ʧ��", Toast.LENGTH_LONG).show();
			}
			if(s=="3")      //ע��ɹ�
			{
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			thread.interrupt();
		}
}