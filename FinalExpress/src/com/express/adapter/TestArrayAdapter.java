package com.express.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TestArrayAdapter extends ArrayAdapter<String> {
  private Context mContext;
    private String [] mStringArray;
  public TestArrayAdapter(Context context, String[] stringArray) {
    super(context, android.R.layout.simple_spinner_item, stringArray);
    mContext = context;
    mStringArray=stringArray;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    //�޸�Spinnerչ�����������ɫ
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent,false);
    }

    //�˴�text1��SpinnerĬ�ϵ�������ʾ���ֵ�TextView
    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
    tv.setText(mStringArray[position]);
    tv.setTextSize(19f);
    tv.setTextColor(Color.GRAY);
    //tv.setTextColor(Color.RED);

    return convertView;

  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // �޸�Spinnerѡ�������������ɫ
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
    }

    //�˴�text1��SpinnerĬ�ϵ�������ʾ���ֵ�TextView
    TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
    tv.setText(mStringArray[position]);
    tv.setTextSize(19f);
    tv.setTextColor(Color.GRAY);
    //tv.setTextColor(Color.BLUE);
    return convertView;
  }

}
