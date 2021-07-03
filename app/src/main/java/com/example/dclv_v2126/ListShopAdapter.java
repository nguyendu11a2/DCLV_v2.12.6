package com.example.dclv_v2126;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.colindodd.gradientlayout.GradientLinearLayout;
import net.colindodd.gradientlayout.GradientRelativeLayout;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

public class ListShopAdapter extends RecyclerView.Adapter<ListShopAdapter.ListShopViewHolder>{
    private Context mContext;
    private List<Shop> mListShop;
    public ListShopAdapter(Context mContext){
        this.mContext = mContext;
    }
    private String mColors[] = {
            "#FF416C",
            "#FFC92B",
            "#FF7765",
            "#8A52E9",
            "#21E8AC",
            "#8360c3",
            "#00c3ff",
            "#f79d00",
            "#00d2ff",
            "#FF5F6D",
            "#00C9FF",
            "#FDFC47",
            "#B3FFAB"
    };
    private String mColors2[] = {
            "#FFA72F",
            "#FF7765",
            "#F7A23C",
            "#376DF6",
            "#376DF6",
            "#2ebf91",
            "#ffff1c",
            "#64f38c",
            "#928DAB",
            "#FFC371",
            "#92FE9D",
            "#24FE41",
            "#12FFF7"

    };
    @NonNull
    @NotNull
    @Override
    public ListShopViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop,parent,false);
        return new ListShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListShopViewHolder holder, int position) {
        Shop shop = mListShop.get(position);
        if (shop == null){
            return;
        }
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);
        holder.gradientRelativeLayout.setStartColor(Color.parseColor(mColors[randomNumber]));
        holder.gradientRelativeLayout.setEndColor(Color.parseColor(mColors2[randomNumber]));

        holder.textView1.setText(shop.getName());
        holder.textView2.setText(shop.getDistance());
    }
    public void setData(List<Shop> list){
        this.mListShop = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mListShop.size();
    }

    public class ListShopViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1,textView2;
        private GradientRelativeLayout gradientRelativeLayout;
        public ListShopViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.tv_item_shop1);
            textView2 = itemView.findViewById(R.id.tv_item_shop2);
            gradientRelativeLayout = itemView.findViewById(R.id.gradient);
        }
    }
}
