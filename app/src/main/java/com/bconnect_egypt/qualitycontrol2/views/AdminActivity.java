package com.bconnect_egypt.qualitycontrol2.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AdminAdapter;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;
import com.bconnect_egypt.qualitycontrol2.model.AdminClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView adminRecyclerView;
    GridLayoutManager layoutManager;
    AdminAdapter adminAdapter;
    List<AdminClass> list;
    TextView username, totalNotVisit, goals, target;
    ImageView logOut;
    int targetNum, goalsNum;
    SharedPreferences dataSaver;
    String admin_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminRecyclerView = findViewById(R.id.admin_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        adminRecyclerView.setLayoutManager(layoutManager);
        username = findViewById(R.id.username);
        goals = findViewById(R.id.goals2);
        target = findViewById(R.id.target2);
        totalNotVisit = findViewById(R.id.total_not_visit);
        logOut = findViewById(R.id.log_out);
        dataSaver = getDefaultSharedPreferences(this);
        admin_name = dataSaver.getString(AppKeys.ADMIN_NAME, "");
        username.setText("Welcome: " + admin_name);
        Log.e("admin_name ", "admin_name " + admin_name);
        logOut.setOnClickListener(this);
        getEmployees();
        getTotalTarget();
        getTotalGoals();
    }

    public void getTotalTarget() {
        String query = "Select count(*)  from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch') and code in (Select phy_code FROM QA_visit)";
        Service.FetcherXml.getInstance().getTotalPharmacies(query).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    Log.e("isSuccessful", "isSuccessful query");
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_REPORTS");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject temp = results.getJSONObject(i);
                            targetNum = temp.getInt("Column1");
                            target.setText(String.valueOf(targetNum));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(AdminActivity.this, "error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AdminActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(AdminActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getTotalGoals() {
        String query = "Select count(*)  from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch')";
        Service.FetcherXml.getInstance().getTotalPharmacies(query).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    Log.e("isSuccessful", "isSuccessful query");
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_REPORTS");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject temp = results.getJSONObject(i);
                            goalsNum = temp.getInt("Column1");
                            goals.setText(String.valueOf(goalsNum));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(AdminActivity.this, "error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(AdminActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(AdminActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getEmployees() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.FetcherXml.getInstance().getEmployees().enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_LIST_EMP");
                        for (int i = 0; i < results.length(); i++) {

                            AdminClass adminClass = new AdminClass();
                            JSONObject temp = results.getJSONObject(i);
                            adminClass.emp_name = temp.getString("emp_name");
                            adminClass.emp_code = temp.getString("emp_code");
                            Log.e("emp_name", "emp_name " + temp.getString("emp_name"));
                            Log.e("emp_code", "emp_code " + temp.getString("emp_code"));
                            list.add(adminClass);
//                            List<BarEntry> entries = new ArrayList<>();
//                            for (AdminClass aClass : list) {
//                                entries.add(new BarEntry(aClass.totalNotVisit, aClass.totalNotVisit));
//
//                            }
                        }

                        adminAdapter = new AdminAdapter(list, AdminActivity.this);
                        adminRecyclerView.setAdapter(adminAdapter);
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AdminActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(AdminActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void logOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dataSaver.edit()
                        .putString(AppKeys.EMP_CODE, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.ADMIN_NAME, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_ADMIN, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_LOGIN, "")
                        .apply();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        dialog.setTitle("تسجيل الخروج");
        dialog.setMessage("هل انت متأكد انك تريد تسجيل الخروج؟");

        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if (view == logOut) {
            logOut();
        }
    }
}