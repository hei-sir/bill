package com.example.liuzi.bill;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Count> countList = new ArrayList<>();
    private MainAdapter adapter;
    private ImageView imageView;
    private Double in=0.0,out=0.0,plan=0.0,value;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_tollbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
//        Count count2=new Count(2,1,15,"吃饭",sdf.format( new Date()));
//        count2.save();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(countList) {
            @Override
            public int getItemCount() {
                Cursor cursor= DataSupport.findBySQL("select * from Count ");
                return cursor.getCount();
            }        //sql语句，返回查询的行数，实现数目统一
        };
        imageView=(ImageView)findViewById(R.id.image_view);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Cursor cursor1=DataSupport.findBySQL("select * from Count");
        if (cursor1.moveToFirst()){
            cursor1.moveToFirst();
            do{
                Double i=cursor1.getDouble(cursor1.getColumnIndex("input"));
                Double o=cursor1.getDouble(cursor1.getColumnIndex("output"));
                in=in+i;
                out=out+o;
                KLog.d(String.valueOf(in)+"+++"+String.valueOf(out));
                KLog.d(String.valueOf(out-in));
                value =new BigDecimal(out-in).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //double精度四舍五入
                KLog.d(String.valueOf(value));
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        Cursor cursor2=DataSupport.findBySQL("select * from Count");
        if (cursor2.moveToFirst()){
            Double c=(double)cursor2.getCount();
            plan=c*40;
        }
        cursor2.close();
        collapsingToolbarLayout.setTitle("共支出："+String.valueOf(value)+"，"+"理论消费："+String.valueOf(plan));
        Glide.with(this).load(R.drawable.timg).into(imageView);


        Cursor cursor= DataSupport.findBySQL("select * from Count order by orders");
        if(cursor.moveToFirst()==false){
            Toast.makeText(MainActivity.this,"没有任何账目 ",Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();
            do{

                Count count1 = new Count(cursor.getInt(cursor.getColumnIndex("id")),cursor.getDouble(cursor.getColumnIndex("input")),
                        cursor.getDouble(cursor.getColumnIndex("output")),cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("time")),cursor.getInt(cursor.getColumnIndex("orders")));
                countList.add(count1);

                //Log.d("QaActivity",status);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void change(){

    }
}
