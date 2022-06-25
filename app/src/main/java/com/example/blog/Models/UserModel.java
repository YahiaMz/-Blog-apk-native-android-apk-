package com.example.blog.Models;

public class UserModel {


    private  int id;
    private String name;
    private String lastName;
    private String photo;




    public UserModel( int id, String name, String lastName, String photo) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.photo = photo;
    }

   public UserModel( ) {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
