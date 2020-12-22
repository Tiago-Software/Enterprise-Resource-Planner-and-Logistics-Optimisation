package com.app.sample.insta.model;

import java.util.List;

/**
 * https://www.youtube.com/watch?v=9rJf0ZzcjAs
 */
public interface APIListener {
    void onLogin(User user);
    void onOrdersLoaded(List<Order> order);
    void ScanQR(int orderID);
}
