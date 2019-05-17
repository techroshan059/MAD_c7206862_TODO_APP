package com.mad.techroshan059.todoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.techroshan059.todoapp.R;
import com.mad.techroshan059.todoapp.model.Entries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomView> {

    private Context context;
    private List<Entries> entries;
    private ItemClickListener itemClickListener;
    private static final String DATE_FORMAT = "dd-MM-yyy, hh:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public CustomAdapter(Context context, ItemClickListener listener) {
        this.context = context;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.todo_layout, viewGroup, false);
        return new CustomView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomView customView, int i) {
        Entries entry = entries.get(i);
        String title = entry.getTitle();
        String content = entry.getContent();
        String updatedAt = dateFormat.format(entry.getUpdatedAt());

        Date date = new Date();

        String[] timeA = entry.getDueTime().toString().split(" ");




        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
            Date selectedDateParse = formatter.parse(entry.getDueDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String CurrentDataString = sdf.format(date);


            String selecteddateString = formatter.format(selectedDateParse);
            Date date2 = sdf.parse(CurrentDataString);

            String[] splitSelectedDate = selecteddateString.split(",");
            String[] splitSelectedMonthday = splitSelectedDate[1].split(" ");

            if(selectedDateParse.compareTo(date2)==0){
                customView.datetime3.setText("Today");
                customView.datetime1.setText(timeA[0]);
                customView.datetime2.setText(timeA[1]);
            }

            else{
                customView.date.setText(selecteddateString);

                customView.datetime1.setText(splitSelectedMonthday[2]);
                customView.datetime2.setText(splitSelectedMonthday[1]);
                customView.datetime3.setText(splitSelectedDate[2]);
            }

            customView.content.setText(content);
            customView.date.setText("Added Date: "+ updatedAt);


        } catch (ParseException e) {
            e.printStackTrace();
        }






        customView.title.setText(title);



    }

    @Override
    public int getItemCount() {
        if (entries == null) {
            return 0;
        }
        return entries.size();
    }

    public List<Entries> getEntries() {
        return entries;
    }

    public void setEntries(List<Entries> journalEntries) {
        entries = journalEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title_view)
        TextView title;
        @BindView(R.id.content_view)
        TextView content;
        @BindView(R.id.date_view)
        TextView date;
        @BindView(R.id.datetime1)
        TextView datetime1;
        @BindView(R.id.datetime2)
        TextView datetime2;
        @BindView(R.id.datetime3)
        TextView datetime3;

        CustomView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = entries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(elementId);
        }
    }
}
