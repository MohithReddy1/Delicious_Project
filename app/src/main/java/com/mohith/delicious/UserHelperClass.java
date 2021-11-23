package com.mohith.delicious;

public class UserHelperClass {

    private String email,mobile,password,address,id,name, image;

    public UserHelperClass(String email, String mobile, String password, String userId, String image){
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.image = image;
        this.address = "";
        this.id = userId;
        this.name = "User";
    }

    public UserHelperClass(String email, String mobile, String password, String address, String id, String name, String image) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.address = address;
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
