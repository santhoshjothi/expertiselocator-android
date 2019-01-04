package com.example.expertiselocator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.expertiselocator.R;
import com.example.expertiselocator.main.LoginActivity;
import com.example.expertiselocator.model.MenuModel;
import com.example.expertiselocator.utils.CommonMethods;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MenuModel> menuModels;
    private Context context;
    private CommonMethods commonMethods;
    private LoginActivity loginActivity;

    public MenuAdapter(Context context, ArrayList<MenuModel> menuModels) {
        this.context = context;
        this.menuModels = menuModels;
        commonMethods = new CommonMethods(context);
        loginActivity = (LoginActivity) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewMenu = LayoutInflater.from(context).inflate(R.layout.list_home_menu_square, parent, false);
        return new MenuAdapterHolder(viewMenu);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuModel menuModel = menuModels.get(position);
        MenuAdapterHolder menuAdapterHolder = (MenuAdapterHolder) holder;
        menuAdapterHolder.imgMenuIcon.setImageResource(menuModel.getMenuIcon());
        menuAdapterHolder.tvMenuTitle.setText(menuModel.getMenuTitle());
        menuAdapterHolder.tvMenuBadgeCount.setVisibility(View.GONE);
        if (menuModel.isBadgeAvailable()) {
            if (!menuModel.getMenuBadgeCount().equals("0")) {
                menuAdapterHolder.tvMenuBadgeCount.setVisibility(View.VISIBLE);
                menuAdapterHolder.tvMenuBadgeCount.setText(menuModel.getMenuBadgeCount());
            }else {
                menuAdapterHolder.tvMenuBadgeCount.setVisibility(View.GONE);
            }
        }
        menuAdapterHolder.linearListMenuItems.setOnClickListener(v -> {
            commonMethods.showLog("Position Clicked : " + position);
            loginActivity.onItemClick(v, position);
        });
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    class MenuAdapterHolder extends RecyclerView.ViewHolder {
        LinearLayout linearListMenuItems;
        TextView tvMenuTitle, tvMenuBadgeCount;
        ImageView imgMenuIcon;
        private MenuAdapterHolder(View itemView) {
            super(itemView);
            linearListMenuItems = (LinearLayout) itemView.findViewById(R.id.linearListMenuItems);
            tvMenuTitle = (TextView) itemView.findViewById(R.id.tvMenuTitle);
            tvMenuBadgeCount = (TextView) itemView.findViewById(R.id.tvMenuBadgeCount);
            imgMenuIcon = (ImageView) itemView.findViewById(R.id.imgMenuIcon);
        }
    }
}
