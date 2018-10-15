package com.filmfan.filmfan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Widgets {
    private Context context;
    private AlertDialog.Builder builder;
    public Widgets(Context context) {
        this.context = context;
    }

    public void displayToast(String msg){
        Toast.makeText(context,String.valueOf(msg),Toast.LENGTH_LONG).show();
    }

    public void showAlertDialog(boolean status,String title,String message){
        builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if(status){
            builder.setCancelable(false);
            builder.show();
        }else{
            builder.setCancelable(true);
        }
    }
    public void dismissAlertDialog(){
        if(builder!=null){
            builder.setCancelable(true);
        }
    }
}
