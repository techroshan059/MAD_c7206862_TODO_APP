package com.example.eziketobenna.todoapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "entry")
public class Entries {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private Date updatedAt;

    @Ignore
    public Entries(String title, String content, Date updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public Entries(int id, String title, String content, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}


