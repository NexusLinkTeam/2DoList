package com.jacob.www.a2dolist;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FRIEND_VERIFY.
 */
public class FriendVerify {

    private String avatar;
    private String nickName;
    private String hello;
    private String userName;
    private Long id;

    public FriendVerify() {
    }

    public FriendVerify(Long id) {
        this.id = id;
    }

    public FriendVerify(String avatar, String nickName, String hello, String userName, Long id) {
        this.avatar = avatar;
        this.nickName = nickName;
        this.hello = hello;
        this.userName = userName;
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
