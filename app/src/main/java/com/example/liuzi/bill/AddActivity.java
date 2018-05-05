package com.example.liuzi.bill;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText bill,content;
    private TextView time,main;
    private Button add;
    private String date;
    private static String tbill,tcontent;
    Spinner put;
    String[] strs;
    String[] puts={"支出","收入"};
    private String uput;
    private int order;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        date=sdf.format( new Date());
        init();
    }

    private void init(){
        bill=(EditText)findViewById(R.id.bill);
        content=(EditText)findViewById(R.id.content);
        time=(TextView)findViewById(R.id.time);
        time.setOnClickListener(this);
        main=(TextView)findViewById(R.id.main);
        main.setOnClickListener(this);
        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(this);
        put=(Spinner)findViewById(R.id.put);
        order=Integer.parseInt(sdf1.format( new Date()));
        time.setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                add();
                break;
            case R.id.main:
                back();
                break;
            case R.id.time:
                getDate();
            default:
        }
    }

    public void getDate() {
        String[]  strs=date.split("-");
        for(int i=0,len=strs.length;i<len;i++){
            System.out.println(strs[i].toString());
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                KLog.d(String.format("%d%d%d",i,i1+1,i2));
                order= Integer.parseInt(String.format("%d%d%d",i,i1+1,i2));
                KLog.d(String.valueOf(order+1));
                date=String.format("%d-%d-%d",i,i1+1,i2);
                time.setText(date);
            }
        },Integer.parseInt(strs[0].toString()),Integer.parseInt(strs[1].toString())-1,Integer.parseInt(strs[2].toString())).show();
    }

    private void add(){
        tbill=bill.getText().toString().trim();
        tcontent=content.getText().toString().trim();
        if (TextUtils.isEmpty(tbill)) {  //当手机号没有输入时
            Toast.makeText(this, "金额不能为空！", Toast.LENGTH_SHORT).show();
            bill.requestFocus();//使输入框失去焦点
            return;
        }else if(bill.length()>=6){      //用户名小于6位
            Toast.makeText(this, "金钱数额不能超过99999！", Toast.LENGTH_SHORT).show();
            bill.requestFocus();//使输入框失去焦点
            return;
        }else if(bill.equals("0")){      //用户名小于6位
            Toast.makeText(this, "金钱数额不能为0！", Toast.LENGTH_SHORT).show();
            bill.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(tcontent)) {//当注册密码没有输入时
            Toast.makeText(this, "用途不能为空！", Toast.LENGTH_SHORT).show();
            content.requestFocus();//使输入框失去焦点
            return;
        }else {
            showd();
        }
    }
    private void showd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置警告对话框的标题
        builder.setTitle("记录");
        //设置警告显示的图片
//    builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage("确定添加此项记录吗？"+"\n"+"在"+date+"添加一笔内容为     "+tcontent+"\n"+"金额为     "+tbill+"\n"+"的款项。");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sureadd();
            }
        });
        //设置“反面”按钮，及点击事件
        builder.setNegativeButton("再等等看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bill.requestFocus();
            }
        });
        //显示对话框
        builder.show();
    }

    private void back(){
        AddActivity.this.finish();;
    }


    private void sureadd(){
        int s=put.getSelectedItemPosition();
        KLog.d(String.valueOf(s));
        String spinnerbill=puts[s];
        KLog.d(spinnerbill);
        Cursor cursor= DataSupport.findBySQL("select * from Count where time = ? ",date);
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            Double input1=cursor.getDouble(cursor.getColumnIndex("input"));
            Double ouput1=cursor.getDouble(cursor.getColumnIndex("output"));
            String content1=cursor.getString(cursor.getColumnIndex("content"));
            if (spinnerbill.equals("支出")){
                ContentValues values=new ContentValues();
                Double value =new BigDecimal(ouput1+Double.parseDouble(tbill)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //double精度四舍五入
                values.put("output",value);
                values.put("content",content1+"、"+tcontent);
                DataSupport.updateAll(Count.class,values,"time = ?",date);
            }else {
                ContentValues values=new ContentValues();
                Double value =new BigDecimal(input1+Double.parseDouble(tbill)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //double精度四舍五入
                values.put("input",value);
                values.put("content",content1+"、"+tcontent);
                DataSupport.updateAll(Count.class,values,"time = ?",date);
            }
        }else{
            if (spinnerbill.equals("支出")) {
                Count count = new Count(1, 0.0,Double.parseDouble(tbill),tcontent,date,order);
                count.save();
            }else{
                Count count = new Count(1, Double.parseDouble(tbill),0.0,tcontent,date,order);
                count.save();
            }
        }
        Toast.makeText(this,"记录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){         //给spinner数据定位
        uput=String.valueOf(puts[position]);
    }
    public void onNothingSelected(AdapterView<?> parent){
    }
}
