package com.example.navigationdrawertoolbarfragment.model;

import java.util.HashMap;
import java.util.Map;

public class UserList {
    private int id;
    private String name;

    public UserList() {
    }

    public UserList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);

        return result;
    }
}
