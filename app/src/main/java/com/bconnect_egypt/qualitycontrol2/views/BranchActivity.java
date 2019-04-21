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
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.BranchAdapter;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;
import com.bconnect_egypt.qualitycontrol2.model.PharmacyClass;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class BranchActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView pharmaciesRecyclerView;
    GridLayoutManager layoutManager;
    BranchAdapter branchAdapter;
    List<PharmacyClass> list;
    ImageView logOut;
    TextView branchNumber;
    SharedPreferences dataSaver;
    int branch_number;
    String emp_code, emp_name;
    PharmacyClass pharmacyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        pharmaciesRecyclerView = findViewById(R.id.pharmacies_recycler);
        layoutManager = new GridLayoutManager(this, 1);
        pharmaciesRecyclerView.setLayoutManager(layoutManager);
        logOut = findViewById(R.id.log_out);
        branchNumber = findViewById(R.id.branch_number);
        logOut.setOnClickListener(this);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_code = dataSaver.getString(AppKeys.EMP_CODE, "");
        emp_name = dataSaver.getString(AppKeys.EMP_NAME, "");
        Log.e("BranchActivity", "emp_code " + emp_code);
        Log.e("BranchActivity", "emp_name " + emp_name);
        getBranches();
    }

    public void getBranches() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.FetcherXml.getInstance().getEmployeeBranches(emp_code).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String res = response.body();
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_BRANCH_EMP");
                        for (int i = 0; i < results.length(); i++) {

                            pharmacyClass = new PharmacyClass();
                            JSONObject temp = results.getJSONObject(i);
                            pharmacyClass.branch = temp.getString("br_name");
                            pharmacyClass.branchCode = temp.getDouble("br_code");
                            list.add(pharmacyClass);

                            branch_number = list.size();
                            branchNumber.setText("Total Branches : " + String.valueOf(branch_number));
                        }

                        branchAdapter = new BranchAdapter(BranchActivity.this, list);
                        pharmaciesRecyclerView.setAdapter(branchAdapter);
                        branchAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(BranchActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(BranchActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

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
                        .putString(AppKeys.EMP_NAME, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_ADMIN, "")
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.IS_LOGIN, "")
                        .apply();
                Intent intent = new Intent(BranchActivity.this, LoginActivity.class);
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