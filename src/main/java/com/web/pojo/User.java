package com.web.pojo;

public class User {
    private Long user_id;
    private String user_name;
    private Integer user_age;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public User(Long user_id, String user_name, Integer user_age) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_age = user_age;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_age=" + user_age +
                '}';
    }
}
