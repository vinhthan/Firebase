package com.example.navigationdrawertoolbarfragment.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String name;
    private String address;
    private Job job;

    public User() {
    }

    public User(String name, String address, Job job) {
        this.name = name;
        this.address = address;
        this.job = job;
    }

    public User(int id, String name, Job job) {
        this.id = id;
        this.name = name;
        this.job = job;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", job=" + job +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("name", name);
        result.put("job/name", job.getName());

        return result;
    }
}
