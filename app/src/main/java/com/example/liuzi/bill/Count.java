package com.example.liuzi.bill;

import org.litepal.crud.DataSupport;

import java.util.Date;


public class Count extends DataSupport {
    private int id;
    private String time;
    private String content;
    private int input;
    private int output;

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

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
    public Count(int id,int input,int output,String content,String time){
        this.id=id;
        this.input=input;
        this.output=output;
        this.content=content;
        this.time=time;
    }
}
