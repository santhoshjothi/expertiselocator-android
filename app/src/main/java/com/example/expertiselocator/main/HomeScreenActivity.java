package com.example.expertiselocator.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.MenuAdapter;
import com.example.expertiselocator.interfaces.MenuItemClick;
import com.example.expertiselocator.model.MenuModel;
import com.example.expertiselocator.utils.CommonMethods;

import java.util.ArrayList;
import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener, MenuItemClick {

    CommonMethods commonMethods;

    int[] menuIcon;
    String[] menuTitle, menuBadgeCount;
    boolean[] isBadgeAvailable;

    MenuModel menuModel;
    ArrayList<MenuModel> menuModels = new ArrayList<>();
    int maxGridCount = 3;
    MenuAdapter menuAdapter;
    RecyclerView rvMenu;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        commonMethods = new CommonMethods(this);

        menuIcon = new int[]{R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png};

        menuTitle = new String[]{"Timeline",
                "Messaging",
                "Thanks",
                "Solutions",
                "Collaborations",
                "Awards",
                "Followers",
                "Following",
                "Events",
                "KSPT",
                "My Group",
                "My Communities",
                "Recommended Profiles",
                "Recommended Groups"};

        menuBadgeCount = new String[]{"0", "4", "2", "0", "3", "0", "99+",
                "6", "0", "4", "0", "0", "0", "0"};

        isBadgeAvailable = new boolean[]{false, true, true, true, true, true, true,
                true, true, true, false, false, false, false, false};

        for (int menu = 0; menu < menuTitle.length; menu++) {
            menuModel = new MenuModel();
            menuModel.setMenuIcon(menuIcon[menu]);
            menuModel.setMenuTitle(menuTitle[menu]);
            menuModel.setMenuBadgeCount(menuBadgeCount[menu]);
            menuModel.setBadgeAvailable(isBadgeAvailable[menu]);
            menuModels.add(menuModel);
        }

        menuAdapter = new MenuAdapter(HomeScreenActivity.this, menuModels);
        rvMenu = (RecyclerView) findViewById(R.id.rvMenu);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), maxGridCount);
        rvMenu.setLayoutManager(gridLayoutManager);
        rvMenu.setAdapter(menuAdapter);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            // Timeline
            case 0:
                startActivity(new Intent(HomeScreenActivity.this, TimelineActivity.class));
                break;

            default:
                break;
        }
    }
}
