package com.app.sample.insta;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.app.sample.insta.model.API;
import com.app.sample.insta.model.APIListener;
import com.app.sample.insta.model.AbstractAPIListener;
import com.app.sample.insta.model.Order;
import com.app.sample.insta.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * data gets saved here about user
 * https://www.youtube.com/watch?v=9rJf0ZzcjAs
 */

public class ModelApi implements Serializable
{
    private static ModelApi sInstance = null;
    private final Application mApplication;
    private API mApi;
    private User mUser;
    private List<Order> mOrder;

    public ModelApi(Application application) {
        mApplication = application;
        mApi = new WebAPI(mApplication,this);
        mOrder = new ArrayList<>();
    }

    public static ModelApi getInstance(Application application) //context object .. only 1 app context ... use for referance
    {
        if(sInstance == null)
        {
            sInstance = new ModelApi(application);
        }

        return sInstance;
    }

    public Application getmApplication()
    {
        return mApplication;
    }

    public void login(String email, String password, APIListener listener)
    {
        mApi.login(email,password, listener);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User User) {
        this.mUser = User;
    }

    public List<Order> getOrders()
    {
        return mOrder;
    }


    public void loadOrders(APIListener APIListener)
    {
        mApi.loadOrders(APIListener);
    }

    //To get QR image for scan
    public void ScanQR(int orderID)
    {
        mApi.ScanQR(orderID);
    }

    public void ReportTemporaryDelay(int routeId, String Reason, long EDT)
    {
        mApi.ReportTempDelay(routeId,Reason,EDT);
    }

    public void ReportPermanentDelay(int routeId, String Reason)
    {
        mApi.ReportPermanentDelay(routeId,Reason);
    }

}
