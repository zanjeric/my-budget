package com.example.mybudget;

public class Transaction {
    String uid, category, date, title;
    int amount;

    public Transaction() {
    }

    public Transaction(String uid, String category, String date, String title, int amount) {
        this.uid = uid;
        this.category = category;
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
