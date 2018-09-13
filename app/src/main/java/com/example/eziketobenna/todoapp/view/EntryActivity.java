package com.example.eziketobenna.todoapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eziketobenna.todoapp.R;
import com.example.eziketobenna.todoapp.database.AppDatabase;
import com.example.eziketobenna.todoapp.model.Entries;
import com.example.eziketobenna.todoapp.utils.AppExecutors;
import com.example.eziketobenna.todoapp.viewmodel.EntryViewModel;
import com.example.eziketobenna.todoapp.viewmodel.EntryViewModelFactory;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity {

    private static final String TAG = EntryActivity.class.getSimpleName();
    public static final String INSTANCE_ENTRY_ID = "instanceEntryId";
    private static final int DEFAULT_ENTRY_ID = -1;
    private int mEntryId = DEFAULT_ENTRY_ID;
    public static String EXTRA_ENTRY_ID = "entry_id";
    @BindView(R.id.editText_title)
    EditText title;
    @BindView(R.id.editText_content)
    EditText content;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ENTRY_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_ENTRY_ID, DEFAULT_ENTRY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ENTRY_ID)) {
            if (mEntryId == DEFAULT_ENTRY_ID) {
                mEntryId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);
                EntryViewModelFactory factory = new EntryViewModelFactory(mDb, mEntryId);
                final EntryViewModel viewModel = ViewModelProviders.of(this, factory).get(EntryViewModel.class);
                viewModel.getEntry().observe(this, new Observer<Entries>() {
                    @Override
                    public void onChanged(@Nullable Entries entry) {
                        viewModel.getEntry().removeObserver(this);
                        Log.d(TAG, "Receiving database updates from LiveData");
                        populateUI(entry);
                    }
                });
            }
        }
    }

    private void populateUI(Entries entry) {
        if (entry == null) {
            return;
        }
        title.setText(entry.getTitle());
        content.setText(entry.getContent());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_ENTRY_ID, mEntryId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                onSaveButtonClicked();
                return true;
            case R.id.cancel_action:
                onNavigateUpFromChild(EntryActivity.this);
                return true;
            case R.id.homeAsUp:
                onSaveButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSaveButtonClicked() {
        String entryTitle = title.getText().toString();
        String entryContent = content.getText().toString();
        Date date = new Date();
        final Entries entry = new Entries(entryTitle, entryContent, date);
        if (!(entryTitle.isEmpty()) && !(entryContent.isEmpty())) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mEntryId == DEFAULT_ENTRY_ID) {
                        mDb.appDao().insertEntry(entry);
                    } else {
                        entry.setId(mEntryId);
                        mDb.appDao().updateEntry(entry);
                    }
                    finish();
                }
            });
            Toast.makeText(EntryActivity.this, "Your note is saved", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
