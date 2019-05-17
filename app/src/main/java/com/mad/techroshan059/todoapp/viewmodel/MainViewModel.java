package com.mad.techroshan059.todoapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.mad.techroshan059.todoapp.database.AppDatabase;
import com.mad.techroshan059.todoapp.model.Entries;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    // member variable for a list of JournalEntry objects wrapped in LiveData
    private LiveData<List<Entries>> entries;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the entries from the DataBase");
        entries = database.appDao().loadAllEntries();
    }

    // getter for the entries variable
    public LiveData<List<Entries>> getEntries() {
        return entries;
    }
}
