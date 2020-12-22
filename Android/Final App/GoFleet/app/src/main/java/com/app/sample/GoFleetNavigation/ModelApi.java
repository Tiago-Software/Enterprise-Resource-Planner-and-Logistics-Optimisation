package com.app.sample.GoFleetNavigation;

import android.app.Application;

import com.app.sample.GoFleetNavigation.model.API;
import com.app.sample.GoFleetNavigation.model.APIListener;
import com.app.sample.GoFleetNavigation.model.Order;
import com.app.sample.GoFleetNavigation.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data gets saved here about user
 *
 * https://www.youtube.com/watch?v=9rJf0ZzcjAs
 */

public class ModelApi implements Serializable
{
    private static ModelApi sInstance = null;
    private final Application mApplication;
    private API mApi;
    private User mUser;
    private List<Order> mOrder;
    private int SequenceNumber;

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


    public int getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public void  UpdateSequenceNumber()
    {
        SequenceNumber++;
    }


    public void loadOrders(APIListener APIListener)
    {
        mApi.loadOrders(APIListener);
    }

    public void loadPastOrders(APIListener APIListener)
    {
        mApi.loadPastOrders(APIListener);
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

    public void ActivateRoute(int routeID)
    {
        mApi.ActivateRoute(routeID);
    }

    public void CompleteRoute(int routeID)
    {
        mApi.CompleteRoute(routeID);
    }

    public void ReportDelayedOrder(int orderID, String Reason)
    {
        mApi.ReportDelayedOrder(orderID,Reason);
    }

     public void PostLocation(int DriverID, Double longitude, Double Latitude)
     {
         mApi.PostLocation(DriverID,longitude,Latitude);
     }

}
