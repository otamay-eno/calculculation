package com.example.calcalculation.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcalculation.R;

import java.util.List;

public class CustomCardViewAdapter extends RecyclerView.Adapter<CustomCardViewAdapter.ViewHolder> {
    private List<String> data;
    private OnItemClickListener onItemClickListener;

    // クリックリスナーのインターフェースを定義
    public interface OnItemClickListener {
        void onItemClick(String itemData);
    }

    // クリックリスナーをセットするメソッド
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public CustomCardViewAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemData = data.get(position);
        // データの設定
        holder.text1.setText(itemData);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.cardTextView);

            // アイテムがクリックされたときの処理を追加
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String itemData = data.get(position);
                            onItemClickListener.onItemClick(itemData);
                        }
                    }
                }
            });
        }
    }
}
