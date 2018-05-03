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

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Count> countList = new ArrayList<>();
    private MainAdapter adapter;
    private ImageView imageView;
    private int in=0,out=0,plan=0;
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
                int i=cursor1.getInt(cursor1.getColumnIndex("input"));
                int o=cursor1.getInt(cursor1.getColumnIndex("output"));
                in=in+i;
                out=out+o;
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        Cursor cursor2=DataSupport.findBySQL("select * from Count");
        if (cursor2.moveToFirst()){
            plan=cursor2.getCount();
        }
        cursor2.close();
        collapsingToolbarLayout.setTitle("共支出："+String.valueOf(out-in)+"，"+"理论消费："+String.valueOf(plan*40));
        Glide.with(this).load(R.drawable.timg).into(imageView);


        Cursor cursor= DataSupport.findBySQL("select * from Count");
        if(cursor.moveToFirst()==false){
            Toast.makeText(MainActivity.this,"没有任何账目 ",Toast.LENGTH_SHORT).show();
        }else {
            List<Count> counts=DataSupport.findAll(Count.class);
            for (Count count:counts){
                Count count1 = new Count(count.getId(),count.getInput(),count.getOutput(),count.getContent(),count.getTime());
                countList.add(count1);

                //Log.d("QaActivity",status);
            }
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
