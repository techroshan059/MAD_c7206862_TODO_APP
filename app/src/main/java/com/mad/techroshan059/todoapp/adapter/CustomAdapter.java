package com.mad.techroshan059.todoapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mad.techroshan059.todoapp.R;
import com.mad.techroshan059.todoapp.model.Entries;
import com.mad.techroshan059.todoapp.view.MainActivity;

import java.sql.Time;
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



        String[] timeA = entry.getDueTime().toString().split(" ");




        try {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
            Date selectedDateParse = formatter.parse(entry.getDueDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeF1 = new SimpleDateFormat("HH:mm");

           SimpleDateFormat timeF2 = new SimpleDateFormat("hh:mm aa");
            String CurrentDataString = sdf.format(date);
            String CurrentTimeString = timeF1.format(date);


            String selecteddateString = formatter.format(selectedDateParse);
            Date date2 = sdf.parse(CurrentDataString);
            Date time2 = timeF1.parse(CurrentTimeString);
            Date selectedTimeParse = timeF1.parse(entry.getDueTime());
           // Date time2 = time.parse(entry.getDueTime());


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

            customView.duedateview.setVisibility(View.GONE);

            if(entry.getStatus()==0){
                customView.statusoflist.setBackground(customView.itemView.getResources().getDrawable(R.drawable.circle_shape_warning));

            }else if(entry.getStatus()==1){
                customView.statusoflist.setBackground(customView.itemView.getResources().getDrawable(R.drawable.circle_shape_done));

            }

            if((selectedDateParse.compareTo(date2)<0 || selectedDateParse.compareTo(date2)==0) && selectedTimeParse.before(time2)){
                customView.statusoflist.setBackground(customView.itemView.getResources().getDrawable(R.drawable.circle_shape_red));
                customView.duedateview.setVisibility(View.VISIBLE);
                customView.duedateview.setText("Due Date");
            }






            if(entry.getPriority()==1){
                customView.priorityview.setVisibility(View.VISIBLE);
                customView.priorityview.setText("Priority");
            }
            else{
                customView.priorityview.setVisibility(View.GONE);
            }



                customView.content.setText(content);
                customView.date.setText(updatedAt);


           // customView.content.setText(Integer.toString(selectedDateParse.compareTo(date2)));







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
        @BindView(R.id.priority_view)
        TextView priorityview;

        @BindView(R.id.dueDate_view)
        TextView duedateview;

        @BindView(R.id.status_of_list)
        RelativeLayout statusoflist;




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
