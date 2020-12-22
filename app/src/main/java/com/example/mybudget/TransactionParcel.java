package com.example.mybudget;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionParcel implements Parcelable {

    private String category;
    private String date;
    private int amount;

    public TransactionParcel(String category, String date, int amount) {
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    protected TransactionParcel(Parcel in) {
        category = in.readString();
        date = in.readString();
        amount = in.readInt();
    }

    public final Creator<TransactionParcel> CREATOR = new Creator<TransactionParcel>() {
        @Override
        public TransactionParcel createFromParcel(Parcel in) {
            return new TransactionParcel(in);
        }

        @Override
        public TransactionParcel[] newArray(int size) {
            return new TransactionParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(date);
        dest.writeInt(amount);
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }
}
