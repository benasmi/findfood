package com.example.benas.findfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;

/**
 * Created by Benas on 8/14/2016.
 */
public class CheckingUtils {

  //Scaling bitmaps
    public static Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int maxWidth = 1920;
        int maxHeight = 1080;
        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


    //Checking network connection
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public static ProgressDialog progressDialog(Context context, String message){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

        return progressDialog;
    }

    //No gps dialog
    public static void buildAlertMessageNoGps(String message,final Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    //Dialog box for logout
    public static void buildAlertMessageLogout(String message, final Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
            new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage("Do you want to logout?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", null);
                            editor.putString("password", null);
                            editor.putBoolean("rememberPassword", false);
                            editor.commit();

                            context.startActivity(new Intent(context, LoginOrRegister.class));
                            TabActivityLoader.isChecked = false;
                            ((Activity) context).finish();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                final SharedPreferences sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                new AlertDialog.Builder(context)
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", null);
                                editor.putString("password", null);
                                editor.putBoolean("rememberPassword", false);
                                editor.commit();

                                context.startActivity(new Intent(context, LoginOrRegister.class));
                                TabActivityLoader.isChecked = false;
                                ((Activity) context).finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }
    public static void dialogBoxForReport(String message, final Context context, final String intent_truck_name){
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(message)
                    .setPositiveButton("REPORT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new ServerManager(context).execute("REPORT", intent_truck_name);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })

                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton("REPORT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new ServerManager(context).execute("REPORT", intent_truck_name);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })

                    .show();
        }
    }



    public static void createErrorBox(String message, Context context){

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

}
