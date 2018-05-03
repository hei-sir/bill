package com.example.liuzi.bill;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText bill,content;
    private TextView time,main;
    private Button add;
    Spinner put;
    String[] puts={"支出","收入"};
    private String uput;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
    }

    private void init(){
        bill=(EditText)findViewById(R.id.bill);
        content=(EditText)findViewById(R.id.content);
        time=(TextView)findViewById(R.id.time);
        main=(TextView)findViewById(R.id.main);
        main.setOnClickListener(this);
        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(this);
        put=(Spinner)findViewById(R.id.put);
        time.setText(sdf.format(new Date()));
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
            default:
        }
    }

    private void add(){
        final String tbill=bill.getText().toString().trim();
        final String tcontent=content.getText().toString().trim();
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
            int s=put.getSelectedItemPosition();
            KLog.d(String.valueOf(s));
            String spinnerbill=puts[s];
            KLog.d(spinnerbill);
            Cursor cursor= DataSupport.findBySQL("select * from Count where time = ? ",sdf.format( new Date()));
            if (cursor.moveToFirst()){
                cursor.moveToFirst();
                int input1=cursor.getInt(cursor.getColumnIndex("input"));
                int ouput1=cursor.getInt(cursor.getColumnIndex("output"));
                String content1=cursor.getString(cursor.getColumnIndex("content"));
                if (spinnerbill.equals("支出")){
                    ContentValues values=new ContentValues();
                    values.put("output",ouput1+Integer.parseInt(tbill));
                    values.put("content",content1+"、"+tcontent);
                    DataSupport.updateAll(Count.class,values,"time = ?",sdf.format(new Date()));
                }else {
                    ContentValues values=new ContentValues();
                    values.put("input",input1+Integer.parseInt(tbill));
                    values.put("content",content1+"、"+tcontent);
                    DataSupport.updateAll(Count.class,values,"time = ?",sdf.format(new Date()));
                }
            }else{
                if (spinnerbill.equals("支出")) {
                    Count count = new Count(1, 0,Integer.parseInt(tbill),tcontent,sdf.format(new Date()));
                    count.save();
                }else{
                    Count count = new Count(1, Integer.parseInt(tbill),0,tcontent,sdf.format(new Date()));
                    count.save();
                }
            }
            Toast.makeText(this,"记录成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void back(){
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
