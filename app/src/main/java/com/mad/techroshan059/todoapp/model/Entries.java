package com.mad.techroshan059.todoapp.model;

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
    private String dueDate;
    private String dueTime;

    private Date updatedAt;

    @Ignore
    public Entries(String title, String content, String dueDate, String dueTime, Date updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.dueDate = dueDate;
        this.dueTime = dueTime;

    }

    public Entries(int id, String title, String content, String dueDate, String dueTime, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}


