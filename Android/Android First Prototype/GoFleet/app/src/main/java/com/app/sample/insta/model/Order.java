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

    public Order()
    {

    }

    public Order(String ID, String street, String subrub)
    {
        this.ID = ID;
        this.street = street;
        this.suburb = subrub;
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

    public static Order getOrder(JSONObject jsonObject, JSONObject jsonObject2) throws JSONException //takes json object and given back user object
    {
        String id = jsonObject.getString("order_Id");
        String street = jsonObject2.getString("location_Street");
        String subrub = jsonObject2.getString("location_Suburb");

        Order order = new Order(id,street,subrub);

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
