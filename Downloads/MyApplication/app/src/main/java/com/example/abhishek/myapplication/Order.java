package com.example.abhishek.myapplication;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Abhishek on 6/9/2018.
 */


@IgnoreExtraProperties
public class Order {

    private String date;
    private String letter_no;
    private String subject;
    private String select_type;
    private String select_dept;



    public Order() {
    }

    public Order(String date, String letter_no, String subject, String select_type, String select_dept) {
        this.date = date;
        this.letter_no = letter_no;
        this.subject = subject;
        this.select_type= select_type;
        this.select_dept = select_dept;

    }


}
