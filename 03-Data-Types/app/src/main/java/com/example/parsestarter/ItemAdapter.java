package com.example.parsestarter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<Data> list;
    private Context context;

    public ItemAdapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);

        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Data data = list.get(position);
        holder.type.setText(data.getType());
        holder.value.setText(data.getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ItemHolder extends RecyclerView.ViewHolder {

    public TextView type;
    public TextView value;

    public ItemHolder(@NonNull View itemView) {
        super(itemView);
        type = itemView.findViewById(R.id.type);
        value = itemView.findViewById(R.id.value);
    }
}