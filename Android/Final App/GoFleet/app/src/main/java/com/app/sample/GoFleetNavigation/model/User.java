package com.app.sample.GoFleetNavigation.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * User class
 * Keep track of user email, name and token for request
 */

public class User implements Serializable
{
    private String name;
    private String surname;
    private String email;
    private String token;
    private int ID;

    public User(int id,String name, String surname, String email, String token)
    {
        this.ID = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.token = token;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static User setUser(JSONObject jsonObject) throws JSONException //takes json object and given back user object
    {
      int id = jsonObject.getInt("emp_Id");
      String name = jsonObject.getString("emp_First_Name");
      String surname = jsonObject.getString("emp_Last_Name");
      String email = jsonObject.getString("emp_Email");
      String token = jsonObject.getString("token");
      User user = new User(id,name,surname,email,token);

      return user;

    }

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;

        if(obj != null && obj instanceof User)
        {
            User that = (User) obj;

            if(this.email.equalsIgnoreCase(that.email))
            {
                result = true;
            }

        }
        return result;
    }

    @Override
    public String toString()
    {
        return this.name + "(" + this.email + ")";
    }

}
