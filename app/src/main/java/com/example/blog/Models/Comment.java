package com.example.blog.Models;

public class Comment {

    private UserModel user;
    private  String comment;
    private String date;
    private int id;


   public  Comment ( ){}
    public Comment(UserModel user, String comment, String date, int id) {
        this.user = user;
        this.comment = comment;
        this.date = date;
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
