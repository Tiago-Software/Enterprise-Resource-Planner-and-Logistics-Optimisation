package com.app.sample.GoFleetNavigation.model;

public interface API
{
    void login(String email, String Password,  APIListener listener);
    void loadOrders(APIListener apiListener);

    void loadPastOrders(APIListener apiListener);

    void ScanQR(int orderID);
    void ReportTempDelay(int routeId, String Reason, long EDT);
    void ReportPermanentDelay(int routeId, String Reason);
    void ActivateRoute(int routeID);
    void CompleteRoute(int routeID);
    void ReportDelayedOrder(int orderID, String Reason);

    void PostLocation(int DriverID, Double longitude, Double Latitude);
}
