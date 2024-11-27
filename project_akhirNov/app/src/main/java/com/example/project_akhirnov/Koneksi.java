package com.example.project_akhirnov;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Koneksi extends SQLiteOpenHelper {
    public Koneksi(@Nullable Context context) {
        super(context, "TopUp", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // untuk create table register
        db.execSQL(
                "create table user (id integer PRIMARY KEY AUTOINCREMENT, username text, email text UNIQUE, password text);"
        );
        // untuk create table history
        db.execSQL(
                "create table history (id integer PRIMARY KEY AUTOINCREMENT, amount text, method text, tanggal date default current_date);"
        );

        // untuk create table balance
        db.execSQL(
                "create table balance (id integer PRIMARY KEY, amount REAL);"
        );
        db.execSQL(
                "insert into balance (id,amount) values(1,0);"
        );

        // table untuk buku yang dibeli
        db.execSQL(
                "create table book_purchase (book_name text PRIMARY KEY, purchased integer);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists history");
        db.execSQL("drop table if exists balance");
        db.execSQL("drop table if exists user");
        onCreate(db);
    }


    public List<String> getAllHistory() {
        List<String> historyList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select amount, method, tanggal from history", null);

        while (cursor.moveToNext()) {
            String history = "amount: " + cursor.getString(0) +
                    "\nmethod:" + cursor.getString(1) +
                    "\ndate" + cursor.getString(2);
            historyList.add(history);
        }
        cursor.close();
        return historyList;
    }

    public Float getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select amount from balance where id = 1", null);
        float balance = 0;
        if (cursor.moveToFirst()) {
            balance = cursor.getFloat(0);
        }
        cursor.close();
        return balance;
    }

    public void UpdateBalance(float updateBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE balance set amount= ? WHERE id=1", new Object[]{updateBalance});
    }

    public void addHistory(float amount, String method) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO history (amount, method) VALUES (?, ?)", new Object[]{amount, method});
    }




    public void addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO user (username,email,password) VALUES (?,?,?)", new Object[]{username,email,password});
    }



    public String getUserbyEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM user WHERE email=?", new String[]{email});

        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            cursor.close();
            return username;
        }
        cursor.close();
        return null;

    }

    public void markPurchased(String book_names) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT or REPLACE INTO book_purchase (book_name, purchased) VALUES  (?,1)", new Object[]{book_names});
    }

    public boolean isBookPurchased(String book_names) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT purchased FROM book_purchase WHERE book_name= ?", new String[]{book_names});
        Boolean purchased = false;

        if (cursor.moveToFirst()) {
            purchased = cursor.getInt(0) == 1;
        }
        cursor.close();
        return purchased;
    }
}
