package com.bconnect_egypt.qualitycontrol2.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;
import com.bconnect_egypt.qualitycontrol2.model.PharmacyClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    Button login;
    String currentVersion, latestVersion;
    Dialog dialog;
    SharedPreferences dataSaver;
    String emp_code;
    String is_login;
    String is_admin;
    String welcome_back, pass, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        is_login = dataSaver.getString(AppKeys.IS_LOGIN, "");
        is_admin = dataSaver.getString(AppKeys.IS_ADMIN, "");
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        getCurrentVersion();

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
            username.setText(name);
            password.setText(pass);
        } else {
            Log.e("TAG", "welcome");
        }

        Log.e("is_login", "is_login " + is_login);
        Log.e("is_admin", "is_admin " + is_login);

        if (is_login.equals("Y") && is_admin.equals("Y") && (currentVersion.equals(latestVersion) || latestVersion == null)) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();

        } else if (is_login.equals("Y") && is_admin.equals("N") && (currentVersion.equals(latestVersion) || latestVersion == null)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("50", "welcome back");
        outState.putString("user_name", username.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        welcome_back = savedInstanceState.getString("50");
        name = savedInstanceState.getString("user_name");
        pass = savedInstanceState.getString("password");
    }

    @Override
    public void onClick(View view) {
        if (view == login) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("جاري تسجيل الدخول ...");
            progressDialog.show();
            final String name = username.getText().toString();
            final String pass = password.getText().toString();
            if (name.equals("") || pass.equals("")) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "ادخل الاسم وكلمة السر",
                        Toast.LENGTH_SHORT).show();
            } else {
                Service.FetcherXml.getInstance().userLogin(name, pass).enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if (response.isSuccessful()) {

                            String res = response.body();
                            Log.e("isSuccessful", "isSuccessful");

                            try {
                                JSONObject places = new JSONObject(res);
                                JSONArray results = places.getJSONArray("LOGIN_DATA");
                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject temp = results.getJSONObject(i);
                                    emp_code = temp.getString("emp_code");
                                    is_login = temp.getString("login");
                                    is_admin = temp.getString("admin");
                                    Log.e("is_login", "is_login1 " + temp.getString("login"));
                                    Log.e("is_admin", "is_admin1 " + temp.getString("admin"));
                                    Log.e("emp_code", "emp_code " + temp.getString("emp_code"));
                                    dataSaver.edit()
                                            .putString(AppKeys.IS_LOGIN, is_login)
                                            .apply();
                                    dataSaver.edit()
                                            .putString(AppKeys.IS_ADMIN, is_admin)
                                            .apply();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                            if (is_login.equals("Y") && is_admin.equals("Y")) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                        Toast.LENGTH_SHORT).show();
                                dataSaver.edit()
                                        .putString(AppKeys.EMP_CODE, emp_code)
                                        .apply();

                                dataSaver.edit()
                                        .putString(AppKeys.ADMIN_NAME, name)
                                        .apply();

                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (is_login.equals("Y") && is_admin.equals("N")) {
                                progressDialog.dismiss();
                                Log.e("is-login", "is-login " + is_login);
                                Toast.makeText(LoginActivity.this, "تم تسجيل الدخول",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                dataSaver.edit()
                                        .putString(AppKeys.EMP_CODE, emp_code)
                                        .apply();

                                dataSaver.edit()
                                        .putString(AppKeys.EMP_NAME, name)
                                        .apply();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "مستخدم غير موجود",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "خطأ ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("TAG", "onFailure " + t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        Log.e("currentVersion ", "currentVersion " + currentVersion);
        new GetLatestVersion().execute();
    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.bconnect_egypt.qualitycontrol2&hl=en_US").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.e("latestVersion", "latestVersion " + latestVersion);
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) {
                        showUpdateDialog();
                    }
                }
            } else
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.bconnect_egypt.qualitycontrol2")));
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        dialog = builder.show();
    }
}