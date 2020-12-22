package com.app.sample.GoFleetNavigation.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.app.sample.GoFleetNavigation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("ResourceType")
public class Constant {


    public static Resources getStrRes(Context context) {
        return context.getResources();
    }

    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }


/*    public static List<LottoTicket> getTicketData(String[] boards)
    {
        List<LottoTicket> items = new ArrayList<>();

        String s_arr[] = boards;

        for (int i = 0; i < s_arr.length; i++)
        {
            LottoTicket LT = new LottoTicket(s_arr);
            items.add(LT);
        }

        return items;
    }*/


    public static List<Integer> getExplorePhotos(Context ctx) {
        Random r = new Random();
        List<Integer> items = new ArrayList<>();
        TypedArray photo = ctx.getResources().obtainTypedArray(R.array.feed_photos);
        for (int i = 0; i < 50; i++) {
            int idx = getRandomIndex(r, 0, photo.length());
            items.add(photo.getResourceId(idx, -1));
        }
        return items;
    }

    private static int getRandomIndex(Random r, int min, int max) {
        return r.nextInt(max - min) + min;
    }

    private static String[] getLoremArr(Context ctx) {
        String rand_lorem[] = new String[4];
        rand_lorem[0] = ctx.getString(R.string.lorem_ipsum);
        rand_lorem[1] = ctx.getString(R.string.short_lorem_ipsum);
        rand_lorem[2] = ctx.getString(R.string.long_lorem_ipsum);
        rand_lorem[3] = ctx.getString(R.string.middle_lorem_ipsum);
        return rand_lorem;
    }

}
