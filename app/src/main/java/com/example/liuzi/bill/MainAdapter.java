package com.example.liuzi.bill;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public abstract class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<Count> mCountList;
    private Context mContext;
    private int i;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView input,output,content,time,id;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            input=(TextView)itemView.findViewById(R.id.input);
            output=(TextView) itemView.findViewById(R.id.output);
            content=(TextView)itemView.findViewById(R.id.content);
            time=(TextView)itemView.findViewById(R.id.time);
            id=(TextView)itemView.findViewById(R.id.id);
        }
    }

    public  MainAdapter(List<Count> countList){
        mCountList=countList;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item,parent,false);
        final MainAdapter.ViewHolder holder=new MainAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Count count=mCountList.get(position);
//                Intent intent=new Intent(mContext,Exam2tActivity.class);
//                intent.putExtra(Exam2tActivity.EXAMEXAM,exam.getExamId());
//                intent.putExtra(Exam2tActivity.EXAMCOUNT,String.valueOf(i));
//                intent.putExtra(Exam2tActivity.EXAMNAME,exam.getName());
//                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position){
        Count count=mCountList.get(position);
        holder.content.setText(count.getContent());
        holder.output.setText(String.valueOf(count.getOutput()));
        holder.input.setText(String.valueOf(count.getInput()));
        holder.time.setText(count.getTime());
        holder.id.setText(String.valueOf(count.getId()));

    }



    public int getItemCout(){
        return mCountList.size();
    }
}

