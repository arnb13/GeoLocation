package com.example.orderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapterProduct extends RecyclerView.Adapter<RecyclerAdapterProduct.ViewHolder> {
    private ArrayList<ProductObject> arrayList_product;
    private Context context;

    private OnProductListener onProductListener;

    public RecyclerAdapterProduct(ArrayList<ProductObject> arrayList_product, OnProductListener onProductListener, Context context) {
        this.arrayList_product = arrayList_product;
        this.context = context;
        this.onProductListener = onProductListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_product, parent, false);
        ViewHolder holder = new ViewHolder(view, onProductListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.product_name.setText(arrayList_product.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return arrayList_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView product_name;
        LinearLayout parentLayout;
        OnProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            product_name = itemView.findViewById(R.id.text_product_name);
            parentLayout = itemView.findViewById(R.id.product_parent_layout);
            this.onProductListener = onProductListener;

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onProductListener.OnProductClick(getAdapterPosition());

        }
    }

    public interface OnProductListener {
        void OnProductClick(int position);
    }

}
