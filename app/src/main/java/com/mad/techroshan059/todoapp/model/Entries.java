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
    private int priority;
    private int status;
    private Date updatedAt;

    @Ignore
    public Entries(String title, String content, String dueDate, String dueTime,int priority,int status, Date updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = priority;
        this.status = status;

    }

    public Entries(int id, String title, String content, String dueDate, String dueTime, int priority, int status, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.updatedAt = updatedAt;
        this.priority = priority;
        this.status = status;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}


