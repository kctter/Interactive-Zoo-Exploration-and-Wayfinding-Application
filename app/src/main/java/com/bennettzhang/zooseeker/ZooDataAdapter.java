package com.bennettzhang.zooseeker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZooDataAdapter extends RecyclerView.Adapter<ZooDataAdapter.ViewHolder> {
    private List<Exhibit> zooData = Collections.emptyList();
    private BiConsumer<Exhibit, String> onTextEditedHandler;
    private Consumer<Exhibit> onDeletedBoxClicked;
    private ArrayList<Exhibit> exhibits;

    public void setZooData(List<Exhibit> newZooData) {
        this.zooData.clear();
        this.zooData = newZooData;
        this.exhibits = (ArrayList<Exhibit>) newZooData.stream().
                filter(e -> e.kind.toString().equals("EXHIBIT"))
                .collect(Collectors.toList());

        notifyDataSetChanged();
    }

    public void setOnDeletedBoxClicked(Consumer<Exhibit> onDeletedBoxClicked) {
        this.onDeletedBoxClicked = onDeletedBoxClicked;
    }

    public void setOnTextEditedHandler(BiConsumer<Exhibit, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }

    public ArrayList<Exhibit> getExhibits() {
        return this.exhibits;
    }

    /*
    public void setOnDeletedBoxClicked(Consumer<TodoListItem> onDeletedBoxClicked) {
        this.onDeletedBoxClicked = onDeletedBoxClicked;
    }
    */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plan_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setZooData(zooData.get(position));
    }

    @Override
    public int getItemCount() {
        return zooData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Exhibit zooData;
        private final ImageButton delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.planned_exhibit);
            this.delete_btn = itemView.findViewById(R.id.deleteImageView);

            this.delete_btn.setOnClickListener(view -> {
                if (onDeletedBoxClicked == null) {
                    return;
                }
                onDeletedBoxClicked.accept(zooData);
            });
        }

        public Exhibit getZooData() {
            return zooData;
        }

        public void setZooData(Exhibit zooData) {
            this.zooData = zooData;
            this.textView.setText(zooData.name);

            //CheckBox checkBox = textView.findViewById(R.id.completed);
            //checkBox.setChecked(todoItem.completed);
        }

        public String getZooDataName() {
            return zooData.name;
        }

        public Exhibit.Kind getZooDataKind() {
            return zooData.kind;
        }
    }
}
