package com.zzq.democollection.ipc;


import com.zzq.democollection.ipc.Book;

interface IMyAidlInterface {

   void addBook(in String name);
    List<Book> getBookList();
}
