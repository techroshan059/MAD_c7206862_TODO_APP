package com.example.eziketobenna.todoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eziketobenna.todoapp.R;
import com.example.eziketobenna.todoapp.model.Entries;

import java.text.SimpleDateFormat;
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

        customView.title.setText(title);
        customView.content.setText(content);
        customView.date.setText(updatedAt);

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
