package com.eldisprojectexpert.sqlitemasterclass;

public class TodoModel {
    private long id;
    private String title;
    private String content;

    public TodoModel(){
        super();
    }

    public TodoModel(long id, String title){
        this.id = id;
        this.title = title;
    }

    public TodoModel(long id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
