package com.example.blog.Models;

public class PostModel {
    private  int id ;
  private  int like , comment ;
  private  String desc , date  , photo ;
  private  UserModel user;
    private  boolean selflike;

  public PostModel(int like, int comment, String desc, String date, String photo, UserModel user, boolean selflike) {
    this.like = like;
    this.comment = comment;
    this.desc = desc;
    this.date = date;
    this.photo = photo;
    this.user = user;
    this.selflike = selflike;
  }
 public PostModel ( ){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  public int getLike() {
    return like;
  }

  public void setLike(int like) {
    this.like = like;
  }

  public int getComment() {
    return comment;
  }

  public void setComment(int comment) {
    this.comment = comment;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public UserModel getUser() {
    return user;
  }

  public void setUser(UserModel user) {
    this.user = user;
  }

  public boolean isSelflike() {
    return selflike;
  }

  public void setSelflike(boolean selflike) {
    this.selflike = selflike;
  }



}
