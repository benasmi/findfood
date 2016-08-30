package com.mabe.productions.findfood;

/**
 * Created by Benas on 5/16/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;


/**
 * Created by Benas on 4/24/2016.
 */
public class ServerManager extends AsyncTask<String, Void, Void> {

    private int echo_result;

    //Registering
    private String register_username = null;
    private String register_password = null;
    private String mail = null;

    //Logging in
    private String login_username = null;
    private String login_password = null;

    //Sending coords
    private double longtitude;
    private double latitude;


    private AlreadyLoggedIn alreadyLoggedIn;
    private String method;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String rememberPassword;

    public static final String SERVER_ADDRESS = "http://findfood.ax.lt";


    public ServerManager(Context context) {
        this.context = context;

    }

    public ServerManager(Context context, AlreadyLoggedIn alreadyLoggedIn, String method) {
        this.method = method;
        this.alreadyLoggedIn = alreadyLoggedIn;
        this.context = context;
    }

    private ProgressDialog progressDialog;


    @Override
    protected void onPreExecute() {
        progressDialog = CheckingUtils.progressDialog(context, "Please wait...");
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params[0].equals("REGISTRATION")) {
            method = params[0];
            try {
                //Setting values.
                register_username = params[1];
                register_password = params[2];
                mail = params[3];

                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/register.php");

                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", register_username);
                jsonObject.putOpt("password", register_password);
                jsonObject.putOpt("mail", mail);

//                //Entity builder to send value.


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");


            } catch (Exception e) {

                echo_result = 3;
            }

        } else if (params[0].equals("LOGIN")) {
            method = params[0];
            try {


                //Setting values.
                login_username = params[1];
                login_password = params[2];
                rememberPassword = params[3];
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/login.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", login_username);
                jsonObject.putOpt("password", login_password);

                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

//                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");

            } catch (Exception e) {
                echo_result = 3;
            }
        } else if (params[0].equals("SEND CORDINATES")) {
            method = params[0];

            longtitude = Double.parseDouble(params[1]);
            latitude = Double.parseDouble(params[2]);
            String username = params[3];
            String password = params[4];

            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/location.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("longtitude", longtitude);
                jsonObject.putOpt("latitude", latitude);
                jsonObject.putOpt("username", username);
                jsonObject.putOpt("password", password);


                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");
            } catch (Exception e) {
                echo_result = 3;
            }
        } else if (params[0].equals("ACTIVATE")) {
            method = params[0];
            String activation_code = params[1];
            String username = params[2];
            String password = params[3];
            Log.i("TEST", username + " " + password);

            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/activate.php");

                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("activation_code", activation_code);
                jsonObject.putOpt("username", username);
                jsonObject.putOpt("password", password);

                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");
            } catch (Exception e) {
                echo_result = 3;
            }

        } else if (params[0].equals("UPDATE PROFILE")) {
            method = params[0];
            String mail = params[1];
            String username = params[2];
            String password = params[3];
            String is_working = params[4];
            String description = params[5];
            String menu = params[6];
            String number = params[7];
            String offers = params[8];
            String slogan = params[9];
            String truck_name = params[10];
            String monday = params[11];
            String tuesday = params[12];
            String wednesday = params[13];
            String thursday = params[14];
            String friday = params[15];
            String saturday = params[16];
            String sunday = params[17];
            String marker_info = params[18];


            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/updateProfile.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();

                jsonObject.putOpt("username", username);
                jsonObject.putOpt("password", password);
                jsonObject.putOpt("mail", mail);
                jsonObject.putOpt("is_working", is_working);
                jsonObject.putOpt("description", description);
                jsonObject.putOpt("slogan", slogan);
                jsonObject.putOpt("number", number);
                jsonObject.putOpt("special_offer", offers);
                jsonObject.putOpt("truck_name", truck_name);
                jsonObject.putOpt("monday", monday);
                jsonObject.putOpt("tuesday", tuesday);
                jsonObject.putOpt("wednesday", wednesday);
                jsonObject.putOpt("thursday", thursday);
                jsonObject.putOpt("friday", friday);
                jsonObject.putOpt("saturday", saturday);
                jsonObject.putOpt("sunday", sunday);
                jsonObject.putOpt("marker_icon", marker_info);
                jsonObject.putOpt("menu", menu);


                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
                entity.addTextBody("json", jsonObject.toString(), type);
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");
            } catch (Exception e) {
                echo_result = 3;
            }


        } else if (params[0].equals("UPDATE_PICTURE")) {
            method = params[0];
            String username = params[1];
            String password = params[2];
            String path = params[3];


            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost( ServerManager.SERVER_ADDRESS + "/sendPicture.php");

                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", username);
                jsonObject.putOpt("password", password);


                MultipartEntity entity = new MultipartEntity();
                entity.addPart(new FormBodyPart("json", new StringBody(jsonObject.toString())));
                entity.addPart("picture", new FileBody(new File(path)));
                httpPost.setEntity(entity);

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }  else if (params[0].equals("RECOVERY")) {
            method = params[0];
            try {


                //Setting values.
                mail = params[1];
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/forgotPassword.php");

                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("mail", mail);

                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

//                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");

            } catch (Exception e) {
                echo_result = 3;
            }
        } else if (params[0].equals("CHANGE_IS_WORKING")) {
            method = params[0];
            String is_working = params[1];
            String username = params[2];
            String password = params[3];
            Log.i("TEST", username + " " + password);

            try {
                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/workingStatus.php");

                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("is_working", is_working);
                jsonObject.putOpt("username", username);
                jsonObject.putOpt("password", password);

                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");
            } catch (Exception e) {
                echo_result = 3;
            }
        } else if (params[0].equals("REPORT")) {
            method = params[0];
            try {


                //Setting values.
                String username = params[1];

                //Connect to mysql.
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADDRESS + "/report.php");


                //JSON object.
                JSONObject jsonObject = new JSONObject();
                jsonObject.putOpt("username", username);

                EntityBuilder entity = EntityBuilder.create();
                entity.setText(jsonObject.toString());
                httpPost.setEntity(entity.build());

//                //Getting response
                HttpResponse response = httpClient.execute(httpPost);
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseObject = new JSONObject(responseBody);

                echo_result = responseObject.getInt("code");

            } catch (Exception e) {
                echo_result = 3;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        if (method.equals("REGISTRATION")) {
            switch (echo_result) {
                case 0:
                    new AlertDialog.Builder(context)
                            .setMessage("Username or email already exists!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                case 1:

                    sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", register_username);
                    editor.putString("password", register_password);
                    editor.putBoolean("registered", true);
                    editor.commit();
                    context.startActivity(new Intent(context, ActivateAccount.class));
//                        ((Activity) context).finish();
                    break;

                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }

        if (method.equals("ACTIVATE")) {
            switch (echo_result) {
                case 0:
                    context.startActivity(new Intent(context, TabActivityLoader.class));
//                        ((Activity) context).finish();
                    break;
                case 1:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }
        if (method.equals("RECOVERY")) {
            switch (echo_result) {
                case 0:
                    CheckingUtils.createErrorBox("Please, check you e-mail", context);
                    break;
                case 1:
                    CheckingUtils.createErrorBox("This email doesn't exist", context);
                    break;
                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }
        if (method.equals("UPDATE PROFILE")) {
            switch (echo_result) {
                case 0:
                    CheckingUtils.createErrorBox("Successfully changed your profile", context);
                    break;
                case 1:
                    Toast.makeText(context, "Couldn't save, try again", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }
        if (method.equals("LOGIN")) {
            switch (echo_result) {
                case 0:
                    sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", login_username);
                    editor.putString("password", login_password);
                    editor.putBoolean("logged_in", true);
                    if (rememberPassword.equals("1")) {
                        editor.putBoolean("rememberPassword", true);
                    } else {
                        editor.putBoolean("rememberPassword", false);
                    }
                    editor.commit();
                    context.startActivity(new Intent(context, TabActivityLoader.class));
                    ((Activity) context).finish();

                    break;
                case 1:
                    CheckingUtils.createErrorBox("Wrong username or password", context);

                    break;
                case 2:
                    sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putString("username", login_username);
                    editor1.putString("password", login_password);
                    editor1.putBoolean("logged_in", true);
                    editor1.commit();
                    context.startActivity(new Intent(context, ActivateAccount.class));
//                        ((Activity) context).finish();
                    break;
                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }

        }
        if (method.equals("SEND CORDINATES")) {
            switch (echo_result) {
                case 0:
                    Toast.makeText(context, "Your location is successfully changed", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }
        if (method.equals("CHANGE_IS_WORKING")) {
            switch (echo_result) {

                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }
        }
        if (method.equals("UPDATE_PICTURE")) {
            alreadyLoggedIn.startBackgroundfetching();
        }

        super.onPostExecute(aVoid);

    }
}

