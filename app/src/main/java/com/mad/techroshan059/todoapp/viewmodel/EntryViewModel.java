package com.mad.techroshan059.todoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mad.techroshan059.todoapp.database.AppDatabase;
import com.mad.techroshan059.todoapp.model.Entries;

public class EntryViewModel extends ViewModel{

    private LiveData<Entries> entry;

    public EntryViewModel(AppDatabase database, int entryId) {
        entry = database.appDao().loadEntryById(entryId);
    }

    public LiveData<Entries> getEntry() {
        return entry;
    }
}
