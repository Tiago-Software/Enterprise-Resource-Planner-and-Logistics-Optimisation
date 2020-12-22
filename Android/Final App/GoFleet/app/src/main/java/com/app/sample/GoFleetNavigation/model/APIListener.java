package com.app.sample.GoFleetNavigation.model;

import java.util.List;

/**
 * https://www.youtube.com/watch?v=9rJf0ZzcjAs
 */
public interface APIListener {
    void onLogin(User user);
    void onOrdersLoaded(List<Order> order);

    void onPastOrdersLoaded(List<Order> order);

    void ScanQR(int orderID);
    void ReportTemporaryDelay(int routeId, String Reason, long EDT);
    void ReportPermanentDelay(int routeId, String Reason);
    void ActivateRoute(int routeID);
    void CompleteRoute(int routeID);
    void ReportDelayedOrder(int orderID, String Reason);

    void PostLocation(int DriverID, Double longitude, Double Latitude);
}
