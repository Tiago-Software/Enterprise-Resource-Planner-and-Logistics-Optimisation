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

    public Order()
    {

    }

    public Order(String ID, String street, String subrub, String sequenceNumber, String placeID, double longitude, double latitude)
    {
        this.ID = ID;
        this.street = street;
        this.suburb = subrub;
        this.sequenceNumber = sequenceNumber;
        this.placeID = placeID;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public static Order getOrder(JSONObject jsonObject, JSONObject jsonObject2) throws JSONException //takes json object and given back user object
    {
        String id = jsonObject.getString("order_Id");
        String street = jsonObject2.getString("location_Street");
        String subrub = jsonObject2.getString("location_Suburb");
        String sequence_number = jsonObject.getString("order_Sequence_Number");
        String place_id = jsonObject2.getString("location_Places_Id");
        double location_longtitude = Double.parseDouble(jsonObject2.getString("location_Longitude"));
        double location_latitude = Double.parseDouble(jsonObject2.getString("location_Latitude"));

        Order order = new Order(id,street,subrub,sequence_number,place_id,location_longtitude,location_latitude);

        return order;

    }

    public  static List<Order> getOrders(JSONArray jsonArray) throws  JSONException
    {
        List<Order> orders = new ArrayList<>();

        for(int i = 0; i != jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JSONObject jsonObject2 = jsonObject.getJSONObject("location");

            Order order = Order.getOrder(jsonObject,jsonObject2);

            orders.add(order);

        }

        return orders;
    }




}
