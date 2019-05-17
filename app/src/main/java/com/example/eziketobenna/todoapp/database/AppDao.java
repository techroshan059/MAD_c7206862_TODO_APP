package com.example.eziketobenna.todoapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.eziketobenna.todoapp.model.Entries;

import java.util.List;

@Dao
public interface AppDao {

    @Query("SELECT * FROM entry ORDER BY updatedAt")
    LiveData<List<Entries>> loadAllEntries();

    @Insert
    void insertEntry(Entries entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(Entries entry);

    @Delete
    void deleteEntry(Entries entry);

    @Query("SELECT * FROM entry WHERE id = :id")
    LiveData<Entries> loadEntryById(int id);
}



