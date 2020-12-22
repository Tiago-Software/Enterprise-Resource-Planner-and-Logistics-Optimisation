package com.app.sample.insta.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable
{
    private String ID;
    private String street;
    private String suburb;
    private String sequenceNumber;
    private String placeID;
    private double longitude;
    private double latitude;
    private boolean delivered;
    private String route_ID;
    private long eta_Sec;
    private String client_Business_Name;

    public Order()
    {

    }

    public Order(String ID, String street, String subrub, String sequenceNumber, String placeID, double longitude, double latitude, boolean delivered, String route_ID, long eta_Sec ,String client_Business_Name)
    {
        this.ID = ID;
        this.street = street;
        this.suburb = subrub;
        this.sequenceNumber = sequenceNumber;
        this.placeID = placeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.delivered = delivered;
        this.route_ID = route_ID;
        this.eta_Sec = eta_Sec;
        this.client_Business_Name = client_Business_Name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String subrub) {
        this.suburb = subrub;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isDelivered()
    {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getRoute_ID() {
        return route_ID;
    }

    public void setRoute_ID(String route_ID) {
        this.route_ID = route_ID;
    }

    public long getEta_Sec() {
        return eta_Sec;
    }

    public void setEta_Sec(long eta_Sec) {
        this.eta_Sec = eta_Sec;
    }

    public String getClient_Business_Name() {
        return client_Business_Name;
    }

    public void setClient_Business_Name(String client_Business_Name) {
        this.client_Business_Name = client_Business_Name;
    }


    public static Order getOrder(JSONObject jsonObject, JSONObject jsonObject2, JSONObject jsonObject3, JSONObject jsonObject4) throws JSONException //takes json object and given back user object
    {
        String id = jsonObject.getString("order_Id");
        String street = jsonObject3.getString("location_Street");
        String subrub = jsonObject3.getString("location_Suburb");
        String sequence_number = jsonObject.getString("order_Sequence_Number");
        String place_id = jsonObject3.getString("location_Places_Id");
        double location_longtitude = jsonObject3.getDouble("location_Longitude");
        double location_latitude = jsonObject3.getDouble("location_Latitude");;

        boolean order_delivered = jsonObject.getBoolean("order_Delivered");

        String routeID = jsonObject4.getString("route_Id");
        long eta_Sec = jsonObject4.getLong("route_ETA_In_Seconds");
        String clientBN = jsonObject2.getString("client_Business_Name");

        Order order = new Order(id,street,subrub,sequence_number,place_id,location_longtitude,location_latitude,order_delivered,routeID,eta_Sec,clientBN);

        return order;

    }

    public static List<Order> getOrders(JSONObject jsonObject) throws  JSONException
    {
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i != jsonObject.getJSONArray("orders").length(); i++)
        {
            JSONArray jsonObject1 = jsonObject.getJSONArray("orders"); //first get json array out of object

            JSONObject jsonObject2 = jsonObject1.getJSONObject(i); // retrieve json object out of json array ... entire json object ... Order details

            JSONObject jsonObject3 = jsonObject2.getJSONObject("client"); //target json object ... 1 order object at a time

            JSONObject jsonObject4 = jsonObject2.getJSONObject("location"); //target json object ... 1 order object at a time

            Order order = Order.getOrder(jsonObject2,jsonObject3,jsonObject4, jsonObject);

            // jsonObject 2 - Order details ... jsonObject 3 - client details ... jsonobject 4 - location ... jSon Object - order deets

            orders.add(order);

        }

        return orders;
    }

    public void checkOrderComplete(String orderID)
    {

    }




}
