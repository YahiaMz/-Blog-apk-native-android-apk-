package com.example.blog;

import com.android.volley.Network;

public class Constants {
    public static final String Url = "http://192.168.1.38:5000";
    public  static  final  String Home = Url + "/api";
    public  static  final  String login = Home + "/login";
    public  static  final  String logout = Home + "/logout";
    public  static  final  String regester = Home + "/regester";
    public  static  final  String Posts = Home + "/posts/";
  public  static  final  String userInfo = Home+"/userInfo";
    public static final String EDIT_POST = Posts + "edit";
    public static final String DELETE_POST = Posts + "delete";
    public static final String COMMENTS_OF_POST = Home+ "/commentsOF";
    public static final String NEW_COMMENT = Home+"/comments/create";
    public static final String DELETE_COMMENT = Home+"/comments/delete";
    public static final String LOG_OUT = Home + "/logout";

    public static String like = Url+"/api/like";
    public static String create_Post = Posts + "create";
    public static String isLike = Home+"/islike";
}
