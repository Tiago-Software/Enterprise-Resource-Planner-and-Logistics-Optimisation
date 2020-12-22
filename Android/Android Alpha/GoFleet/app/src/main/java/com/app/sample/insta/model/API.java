package com.app.sample.insta.model;

public interface API
{
    void login(String email, String Password,  APIListener listener);
    void loadOrders(APIListener apiListener);
    void ScanQR(int orderID);
}
