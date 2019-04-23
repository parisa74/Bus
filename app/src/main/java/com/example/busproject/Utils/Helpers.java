package com.example.busproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.busproject.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

import omidi.mehrdad.moalertdialog.MoAlertDialog;

public class Helpers {

    public static AsyncHttpClient client = new AsyncHttpClient();

    public static String baseUrl = "http://admin.idpz.ir/api/";  //todo"http://admin.idpz.ir/api/";

    public static Gson gson = new Gson();

  //  public static List<Taxi> taxis;

    public static String user_id;

    public static String mobile;

    public static String name;

    //public static MyServices myServices = ApiClient.getClient().create(MyServices.class);

    public static String getUser_id() {

        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("TAXI_ID", 0);
        return sharedPreferences.getString("taxi_id", "");
    }

    public static void setUser_id(String taxi_id) {

        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("TAXI_ID", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("taxi_id", taxi_id);
        editor.commit();
        Helpers.user_id = taxi_id;
    }

    public static String getMobile() {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("mobile", 0);
        return sharedPreferences.getString("m", "");
    }

    public static void setMobile(String mobile) {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("mobile", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("m", mobile);
        editor.commit();
    }

    public static String getName() {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("Name", 0);
        return sharedPreferences.getString("name", "");
    }

    public static void setName(String name) {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("Name", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.commit();
    }

    public static String getgender() {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("G", 0);
        return sharedPreferences.getString("gender", "");
    }

    public static void setgender(int gender) {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("G", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("gender", String.valueOf(gender));
        editor.commit();
    }


    public static String getEmail() {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("E", 0);
        return sharedPreferences.getString("email", "");
    }

    public static void setEmail(String name) {
        SharedPreferences sharedPreferences = AppController.getAppContext().getSharedPreferences("E", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("emali", name);
        editor.commit();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void noInternetDialog() {
        try {
            Typeface face = Typeface.createFromAsset(AppController.getAppContext().getAssets(),
                    "fonts/IRANSans(FaNum).ttf");

            MoAlertDialog dialog = new MoAlertDialog(AppController.getAppContext());

            dialog.showSuccessDialog("عدم اتصال به اینترنت ", "لطفا اتصال خود را با اینترنت بررسی نمایید.");

            dialog.setTypeface(face);

            dialog.setDilogIcon(R.drawable.ic_no_internet);
            dialog.setDialogButtonText("باشه!");
        } catch (Exception e) {
        }
    }

    public static void addToSharePrf(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext()).edit()
                .putString(key, value).apply();
    }


    public static String getSharePrf(String key) {
        return PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext()).getString(key, "");
    }
}
