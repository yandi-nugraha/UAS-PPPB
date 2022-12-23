package com.example.uas_pppb1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    private final ArrayList<News> values;
    private final LayoutInflater inflater;

    public BookmarkAdapter(Context context, ArrayList<News> values) {
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new BookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        News news = values.get(position);
        holder.txtReleaseDate.setText(news.getReleaseDate());
        holder.txtTitle.setText(news.getTitle());
        holder.txtCategory.setText(news.getCategory());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int key = news.getId();
                BookmarkActivity.databaseHelper.deleteProduct(key);
                BookmarkActivity.bookmarkList.remove(news);
                BookmarkActivity.bookmarkAdapter.notifyItemRemoved(holder.getAdapterPosition());
                Toast.makeText(view.getContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtReleaseDate;
        TextView txtTitle;
        TextView txtCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReleaseDate = itemView.findViewById(R.id.txt_releaseDate);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtCategory = itemView.findViewById(R.id.txt_category);
        }
    }
}
