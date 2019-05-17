package com.example.eziketobenna.todoapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.eziketobenna.todoapp.R;
import com.example.eziketobenna.todoapp.adapter.CustomAdapter;
import com.example.eziketobenna.todoapp.database.AppDatabase;
import com.example.eziketobenna.todoapp.model.Entries;
import com.example.eziketobenna.todoapp.utils.AppExecutors;
import com.example.eziketobenna.todoapp.viewmodel.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  implements CustomAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fabButton;
    CustomAdapter customAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(this,this);
        recyclerView.setAdapter(customAdapter);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                startActivity(intent);
            }
        });

        mDb = AppDatabase.getInstance(getApplicationContext());
        setUpViewModel();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Entries> entries = customAdapter.getEntries();
                        mDb.appDao().deleteEntry(entries.get(position));
                        showUndoSnackbar("Your todo task has been deleted successfully!");
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<Entries>>() {
            @Override
            public void onChanged(@Nullable List<Entries> entry) {
                Log.d(TAG, "Updating entries from Livedata");
                customAdapter.setEntries(entry);
            }
        });

    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, EntryActivity.class);
        intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, itemId);
        startActivity(intent);
    }

    private void showUndoSnackbar(String message) {
        View view = this.findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);

        snackbar.show();
    }

}
