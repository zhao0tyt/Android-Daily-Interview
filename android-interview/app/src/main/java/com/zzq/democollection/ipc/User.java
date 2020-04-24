package com.zzq.democollection.ipc;

public class User {
    public int id;
    public String name;
    public int age;
    public int isMale;

    @Override
    public String toString(){
        return "id = " + id
                + ", name = " + name
                + ", age = " + age
                + ", isMale = " + isMale;
    }
}
