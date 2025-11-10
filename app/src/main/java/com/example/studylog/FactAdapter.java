package com.example.studylog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.studylog.R;

public class FactAdapter extends RecyclerView.Adapter<FactAdapter.FactViewHolder> {
    private final List<FactModel> items;

    public FactAdapter(List<FactModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fact, parent, false);
        return new FactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FactViewHolder holder, int position) {
        FactModel fact = items.get(position);
        holder.tvText.setText(fact.getText());
        Picasso.get()
                .load(fact.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgFact);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FactViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFact;
        TextView tvText;

        public FactViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFact = itemView.findViewById(R.id.img_fact);
            tvText = itemView.findViewById(R.id.tv_fact_text);
        }
    }
}
