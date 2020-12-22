package com.app.sample.insta;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.google.gson.JsonArray;
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
    public static final String BASE_URL = "https://d9c77e151f52.eu.ngrok.io/";
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

        }
        catch (JSONException e)
        {
            Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void loadOrders(APIListener apiListener)
    {
        String driverID = String.valueOf(user.getID());

        String url = BASE_URL + "GetDriverOrders/" + driverID; //request

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>()    //takes json object as parmeter
        {
            @Override
            public void onResponse(JSONObject response)  //Success
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,successListener,errorListener)
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

    @Override
    public void ScanQR(int orderID)
    {
        String url = BASE_URL + "OrderDelivered/" + orderID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, null,null)
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
        }; //request being sent to web api

        //so volley doesnt do call twice
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request); //procceseed in backkground thread of volley ...

    }

    @Override
public void ReportTempDelay(int routeId, String Reason, long EDT)
{
    String url = BASE_URL + "ReportTemporaryDelay/" + routeId;

    JSONObject jsonObject = new JSONObject();

    try {
        jsonObject.put("route_Delay_Reason",Reason);
        jsonObject.put("route_EDT",EDT);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, null,null)
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
        }; //request being sent to web api

        //so volley doesnt do call twice
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request); //procceseed in backkground thread of volley ...

    }
    catch (JSONException e)
    {
        Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
    }

}

    @Override
    public void ReportPermanentDelay(int routeId, String Reason)
    {
        String url = BASE_URL + "ReportPermanentDelay/" + routeId;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("route_Delay_Reason",Reason);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, null,null)
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
            }; //request being sent to web api

            mRequestQueue.add(request); //procceseed in backkground thread of volley ...

        }
        catch (JSONException e)
        {
            Toast.makeText(mApplication,"JSON exception",Toast.LENGTH_LONG).show();
        }

    }


}
