package com.express.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.MsgAdapter;
import com.express.service.MyCallable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class ChatActivity extends Activity {
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private TextView titleText;	
	private ImageButton back;
	private List<CMessage> msgList = new ArrayList<CMessage>();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private int i = 0;
	boolean stopThread=false;
	private final static int updateRecords_ID = 1;
    public Timer mTimer = new Timer();// ��ʱ��
	public Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 1:  

            	// Handler����UI�߳���,���½���Ĳ����ڴ˴�ִ��  */
            	//System.out.println("handler" + i);
            	adapter.notifyDataSetChanged(); // ��������Ϣʱ��ˢ��ListView�е���ʾ
				msgListView.setSelection(msgList.size()); // ��ListView��λ�����һ��
                break; 
            case 2:
            	break; 
            case 3:  
                mTimer.cancel();//  
                mTimer=null;  
            }  
            super.handleMessage(msg);  
        }  
    };  
      
    //public Timer mTimer = new Timer();// ��ʱ��
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		back = (ImageButton)findViewById(R.id.chat_return_publish);
		titleText = (TextView)findViewById(R.id.chat_title_text);
		msgList = initMsgs(); // ��ʼ����Ϣ����
		adapter = new MsgAdapter(ChatActivity.this, msgList);
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		msgListView.setAdapter(adapter);
		msgListView.setSelection(msgList.size()); // ��ListView��λ�����һ��	
		
		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String content = inputText.getText().toString();				
				Date curDate = new Date(System.currentTimeMillis()); //��ȡ��ǰʱ��
				String time = formatter.format(curDate);
				if (!"".equals(content)) {
					CMessage msg = new CMessage(LoginActivity.getUser(),HomePageActivity.getReceiver(),MessageType.CHAT_SENT,(Object)content,time);
					insertMsgList(msg);	
					//chatClient.sendMsg(msg);
					msgList.add(msg);
					adapter.notifyDataSetChanged(); // ��������Ϣʱ��ˢ��ListView�е���ʾ
					msgListView.setSelection(msgList.size()); // ��ListView��λ�����һ��			
					inputText.setText(""); // ���������е�����
				}
			}		
		});	
		
		titleText.setText(HomePageActivity.getReceiver().getUsername());
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		timerTask(); // ��ʱִ�� 		
	}
	
    public void timerTask() {  
        //������ʱ�߳�ִ�и�������  
        mTimer.schedule(new TimerTask() {  
            @Override  
            public void run() {  
            	Message msgTemp =new Message(); 
                if(!stopThread){  
                	List<CMessage> temp = initMsgs();
                	if(temp.size()==msgList.size()){
                		msgTemp.what = 2;
                	}else{
                		for(int j = msgList.size();j<temp.size();j++){
                			msgList.add(temp.get(j));
                		}
                		
                		msgTemp.what = updateRecords_ID;
                	}
					
					System.out.println("task" + i++);
					System.out.println(msgList.size());
					mHandler.sendMessage(msgTemp); 
                }else{  
                	msgTemp.what = 3;
                	mHandler.sendMessage(msgTemp);// ��Handler������Ϣֹͣ����ִ��  
                }   
            }  
        }, 3000, 3000);// ��ʱ����  
    } 

	protected void onDestroy() {
		System.out.println("-----------onDestroy------");
		stopThread = true;
		super.onDestroy();
	};
		
	protected void insertMsgList(CMessage msg) {
		// TODO Auto-generated method stub
		MyCallable call = new MyCallable(MessageType.INSERT_CHAT_RECORDS, msg);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		try {
			msg = (CMessage)task.get();
			//System.out.println("!r: "+msg.getReceiver().getStudentid()+"!s: "+msg.getSender().getStudentid()+"!t: "+msg.getMsgType()+"!c: "+(String)msg.getObj());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
	}

	//������Ϣ��������Ϣ�б�
	private void updateMsgList(CMessage msg) {
		MyCallable call = new MyCallable(MessageType.UPDATE_CHAT_RECORDS, msg);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		try {
			msg = (CMessage)task.get();
			System.out.println("!r: "+msg.getReceiver().getStudentid()+"!s: "+msg.getSender().getStudentid()+"!t: "+msg.getMsgType()+"!c: "+(String)msg.getObj());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
	}
	
	private List<CMessage> initMsgs() {
		List<CMessage> msgListTemp = null;
		MyCallable call = new MyCallable(MessageType.LOAD_CHAT_RECORDS, null);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();

		try {
			msgListTemp = (List<CMessage>)task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		return msgListTemp;
	}
	
    @Override  
    protected void onStop() {  
        mTimer.cancel();// �����˳�ʱcancel timer  
        super.onStop();  
    } 
}
