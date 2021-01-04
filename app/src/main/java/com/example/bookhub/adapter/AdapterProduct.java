package com.example.bookhub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookhub.R;
import com.example.bookhub.activity.AddProductActivity;
import com.example.bookhub.activity.DetailProductActivity;
import com.example.bookhub.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.HolderProduct>  {
    private Context ctx;
    private List<ProductModel> listProduct;
    private String id;

    public AdapterProduct(Context ctx, List<ProductModel> listProduct) {
        this.ctx = ctx;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public HolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        HolderProduct holder = new HolderProduct(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProduct holder, int position) {
        ProductModel pm = listProduct.get(position);
        holder.tvId.setText(String.valueOf(pm.getId()));
        holder.tvName.setText(pm.getName());
        holder.tvAuthor.setText(pm.getAuthor());
        holder.tvPrice.setText("Rp. "+String.valueOf(pm.getPrice()));
        Picasso.get().load(pm.getImage()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class HolderProduct extends RecyclerView.ViewHolder{
        TextView tvId,tvName, tvAuthor, tvPrice;
        ImageView ivImage;

        public HolderProduct(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvPrice = itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = tvId.getText().toString();
                    Intent i = new Intent(ctx, DetailProductActivity.class);
                    i.putExtra("idProduct", id);
                    ctx.startActivity(i);
                }
            });
        }
    }
}
