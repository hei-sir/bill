package com.example.liuzi.bill;

import org.litepal.crud.DataSupport;

import java.util.Date;


public class Count extends DataSupport {
    private int id;
    private String time;
    private String content;
    private Double input;
    private Double output;
    private int orders;

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getInput() {
        return input;
    }

    public void setInput(Double input) {
        this.input = input;
    }

    public Double getOutput() {
        return output;
    }

    public void setOutput(Double output) {
        this.output = output;
    }
    public Count(int id,Double input,Double output,String content,String time,int orders){
        this.id=id;
        this.input=input;
        this.output=output;
        this.content=content;
        this.time=time;
        this.orders=orders;
    }
}
