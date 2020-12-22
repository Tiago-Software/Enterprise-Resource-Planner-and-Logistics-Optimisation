package com.app.sample.GoFleetNavigation;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.sample.GoFleetNavigation.model.API;
import com.app.sample.GoFleetNavigation.model.APIListener;
import com.app.sample.GoFleetNavigation.model.Order;
import com.app.sample.GoFleetNavigation.model.User;

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
    public static final String BASE_URL = "https://fa0b9b9b2b67.eu.ngrok.io/";
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

        if(user != null)
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


    }

    @Override
    public void loadPastOrders(APIListener apiListener)
    {
        if(user != null)
        {
            String driverID = String.valueOf(user.getID());

            String url = BASE_URL + "GetPastOrders/" + driverID; //request

            Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>()    //takes json object as parmeter
            {
                @Override
                public void onResponse(JSONArray response)  //Success
                {
                    try
                    {
                        List<Order> order = Order.getPastOrders(response);

                        if(apiListener != null)
                        {
                            apiListener.onPastOrdersLoaded(order);
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
                    Toast.makeText(mApplication,"No Past orders Recorded",Toast.LENGTH_LONG).show();
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
        String url = BASE_URL + "ReportTemporaryDelay/";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("route_Id",routeId);
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
        String url = BASE_URL + "ReportPermanentDelay/";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("route_Id",routeId);
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
    public void ReportDelayedOrder(int orderID, String Reason)
    {
        String url = BASE_URL + "ReportDelayedOrder/";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("order_Id",orderID);
            jsonObject.put("delay_Reason",Reason);

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
    public void ActivateRoute(int routeID)
    {
        String url = BASE_URL + "ActivateRoute/" + routeID;


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

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request); //procceseed in backkground thread of volley ...

    }

    @Override
    public void CompleteRoute(int routeID)
    {
        String url = BASE_URL + "CompleteRoute/" + routeID;


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

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request); //procceseed in backkground thread of volley ...

    }


    @Override
    public void PostLocation(int DriverID, Double longitude, Double Latitude)
    {
        String url = BASE_URL + "PostLocation";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("driver_Id",DriverID);
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",Latitude);

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




}
