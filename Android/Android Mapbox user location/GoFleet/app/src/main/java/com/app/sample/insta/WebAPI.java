package com.app.sample.insta;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.sample.insta.model.API;
import com.app.sample.insta.model.APIListener;
import com.app.sample.insta.model.Order;
import com.app.sample.insta.model.User;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web API class
 * https://www.youtube.com/watch?v=9rJf0ZzcjAs
 */
public class WebAPI implements API, Serializable
{
    public static final String BASE_URL = "https://36051767cbf2.ngrok.io/";
    private final Application mApplication;
    private ModelApi mModel;

    private User user;

    private RequestQueue mRequestQueue;

    public WebAPI(Application application, ModelApi modelApi)
    {
        mApplication = application;
        mRequestQueue = Volley.newRequestQueue(application);
        mModel = modelApi;
    }


    public void login(String email, String password, final APIListener listener)
    {

        String url = BASE_URL + "Authenticate";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("emp_Log_Email",email);
            jsonObject.put("emp_Log_Password",password);


            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>()    //takes json object as parmeter
            {

                @Override
                public void onResponse(JSONObject response)  //Success
                {
                    try
                    {
                        user = User.setUser(response);
                        listener.onLogin(user); //on success call
                    }
                    catch(JSONException e)
                    {
                        Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener()  //listening for error
            {

                @Override
                public void onErrorResponse(VolleyError error)  //failure
                {
                    Toast.makeText(mApplication,"Error response",Toast.LENGTH_LONG).show();

                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener,errorListener); //request being sent to web api
            mRequestQueue.add(request); //procceseed in backkground thread of volley ...

        } catch (JSONException e)
        {
            Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void loadOrders(APIListener apiListener)
    {
        String driverID = String.valueOf(user.getID());

        String url = BASE_URL + "GetDriverOrders/" + driverID; //request

        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>()    //takes json object as parmeter
        {
            @Override
            public void onResponse(JSONArray response)  //Success
            {
                try
                {
                    List<Order> order = Order.getOrders(response);

                    if(apiListener != null)
                    {
                        apiListener.onOrdersLoaded(order);
                    }

                }
                catch(JSONException e)
                {
                    Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()  //listening for error
        {

            @Override
            public void onErrorResponse(VolleyError error)  //failure
            {
                Toast.makeText(mApplication,"Error response",Toast.LENGTH_LONG).show();
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null,successListener,errorListener)
        {
            @Override
            public Map<String,String> getHeaders()
            {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Accept","application/json");
                headers.put("Authorization","Bearer " + mModel.getUser().getToken());

                return headers;
            }
        };

        mRequestQueue.add(request);

    }

//    public void getDriverOrders(int DriverID, final APIListener listener)
//    {
//        String url = BASE_URL + "getDriverOrders/" + DriverID;
//        JSONObject jsonObject = new JSONObject();
//
//
//        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>()    //takes json object as parmeter
//        {
//
//            @Override
//            public void onResponse(JSONObject response)  //Success
//            {
//                try
//                {
//                    Order order = Order.setOrder(response);
//              //      listener.onLogin(order);
//                }
//                catch(JSONException e)
//                {
//                    Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
//                }
//
//
//
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener()  //listening for error
//        {
//
//            @Override
//            public void onErrorResponse(VolleyError error)  //failure
//            {
//                Toast.makeText(mApplication,"Error response",Toast.LENGTH_LONG).show();
//
//            }
//        };
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener,errorListener); //request being sent to web api
//        mRequestQueue.add(request); //procceseed in backkground thread of volley ...
//
//
//    }

}
