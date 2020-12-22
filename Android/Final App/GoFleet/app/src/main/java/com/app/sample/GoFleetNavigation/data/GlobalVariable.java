package com.app.sample.GoFleetNavigation.data;

import android.app.Application;

import com.app.sample.GoFleetNavigation.model.User;

import java.io.Serializable;

public class GlobalVariable extends Application implements Serializable
{
    private boolean grid_mode = true;

    public void setUserGS(User userG) {
        this.userGS = userG;
    }

    public User getUserGS() {
        return userGS;
    }

    private static User userGS;

    public boolean isGrid_mode() {
        return grid_mode;
    }

    public void setGrid_mode(boolean grid_mode) {
        this.grid_mode = grid_mode;
    }
}
