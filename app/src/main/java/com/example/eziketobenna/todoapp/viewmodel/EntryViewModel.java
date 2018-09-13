package com.example.eziketobenna.todoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.eziketobenna.todoapp.database.AppDatabase;
import com.example.eziketobenna.todoapp.model.Entries;

public class EntryViewModel extends ViewModel{

    private LiveData<Entries> entry;

    public EntryViewModel(AppDatabase database, int entryId) {
        entry = database.appDao().loadEntryById(entryId);
    }

    public LiveData<Entries> getEntry() {
        return entry;
    }
}
