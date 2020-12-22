package com.app.sample.insta.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class qrModel implements Serializable
{

    private byte[] qrBytes;

    public qrModel(byte[] qrBytes)
    {
        this.qrBytes = qrBytes;
    }

    public byte[] getQrBytes() {
        return qrBytes;
    }

    public void setQrBytes(byte[] qrBytes) {
        this.qrBytes = qrBytes;
    }

    public static qrModel getQr(JSONObject jsonObject) throws JSONException //takes json object and given back user object
    {
        byte[] QRCode = (byte[]) jsonObject.get("order_Id");

        qrModel qrModel = new qrModel(QRCode);

        return qrModel;

    }

}
