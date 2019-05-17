package com.mad.techroshan059.todoapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mad.techroshan059.todoapp.R;
import com.mad.techroshan059.todoapp.database.AppDatabase;
import com.mad.techroshan059.todoapp.model.Entries;
import com.mad.techroshan059.todoapp.utils.AppExecutors;
import com.mad.techroshan059.todoapp.viewmodel.EntryViewModel;
import com.mad.techroshan059.todoapp.viewmodel.EntryViewModelFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @BindView(R.id.editText_dueDate)
    EditText dueDate;

    @BindView(R.id.editText_dueTime)
    EditText dueTime;

    DatePickerDialog picker;
    ImageView popUpCalendar,popUpClock;

    CheckBox priorityCheckBox;


    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

         priorityCheckBox = findViewById(R.id.checkbox_priority);


        Date date = Calendar.getInstance().getTime();
        DateFormat currentDate = new SimpleDateFormat("EEE, MMM d, yyyy");
        DateFormat currentTime = new SimpleDateFormat("h:mm a");
        String todayDate = currentDate.format(date);
        String todayTime = currentTime.format(date);


        popUpCalendar = findViewById(R.id.popUpCalendar); popUpClock = findViewById(R.id.popUpClock);

       this.dueDate.setText(todayDate);
        this.dueTime.setText(todayTime);


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

        popUpCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                              Calendar calendar = Calendar.getInstance();
                              calendar.set(year,monthOfYear,dayOfMonth);

                              SimpleDateFormat frm = new SimpleDateFormat("EEE, MMM d, yyyy");
                              String selectedDate = frm.format(calendar.getTime());
                              dueDate.setText(selectedDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        popUpClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(EntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String time = selectedHour + ":" + selectedMinute;

                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time );
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime=fmtOut.format(date);

                        dueTime.setText(formattedTime);
                    }
                }, hour, minute, false);//No 24 hour time
                mTimePicker.show();
            }
        });
    }

    private void populateUI(Entries entry) {
        if (entry == null) {
            return;
        }
        title.setText(entry.getTitle());
        content.setText(entry.getContent());
        dueDate.setText(entry.getDueDate());
        dueTime.setText(entry.getDueTime());
        priorityCheckBox.setChecked(entry.getPriority()==1?true:false);


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



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSaveButtonClicked() {
        String entryTitle = title.getText().toString();
        String entryContent = content.getText().toString();
        String entryDueDate = dueDate.getText().toString();
        String entryDueTime = dueTime.getText().toString();

        Date date = new Date();
        final Entries entry = new Entries(entryTitle, entryContent, entryDueDate, entryDueTime, priorityCheckBox.isChecked()?1:0,0,  date);
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

        }
    }



    public  void openCalModal(){
        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
    }

}
