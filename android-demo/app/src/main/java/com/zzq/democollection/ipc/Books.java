package com.zzq.democollection.ipc;

public class Books {
    public int id;
    public String author;
    public float price;
    public int pages;
    public String name;

    @Override
    public String toString(){
        return "id = " + id
                + ", author = " + author
                + ", price = " + price
                + ", pages = " + pages
                + ", name = " + name;
    }
}
