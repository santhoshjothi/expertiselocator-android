package com.example.expertiselocator.model;

public class MenuModel {

    private String menuTitle, menuBadgeCount;
    private int menuIcon;
    private boolean isBadgeAvailable;

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuBadgeCount() {
        return menuBadgeCount;
    }

    public void setMenuBadgeCount(String menuBadgeCount) {
        this.menuBadgeCount = menuBadgeCount;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public boolean isBadgeAvailable() {
        return isBadgeAvailable;
    }

    public void setBadgeAvailable(boolean badgeAvailable) {
        isBadgeAvailable = badgeAvailable;
    }
}
