package com.bconnect_egypt.qualitycontrol2.views;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.PharmaciesAdapter;
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

public class PharmaciesActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView totalRecycler;
    GridLayoutManager layoutManager1;
    PharmaciesAdapter pharmaciesAdapter;
    List<PharmacyClass> list;
    TextView totalPharmacies;
    EditText searchText;
    ImageView search;
    String branch_code, search_txt;
    int total_pharmacies;
    String emp_code;
    SharedPreferences dataSaver;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);
        totalPharmacies = findViewById(R.id.total_pharmacies);
        totalRecycler = findViewById(R.id.total_recycler);
        layoutManager1 = new GridLayoutManager(this, 1);
        totalRecycler.setLayoutManager(layoutManager1);
        search = findViewById(R.id.search);
        searchText = findViewById(R.id.search_text);
        search.setOnClickListener(this);
        searchText.setOnClickListener(this);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_code = dataSaver.getString(AppKeys.EMP_CODE, "");
        Log.e("PharmaciesNotVisit", "emp_code " + emp_code);
        branch_code = dataSaver.getString(AppKeys.BRANCH_CODE, "");
        Log.e("allPharmacies", "branch_code " + branch_code);
        getPhyByBranch();
    }

    public void search() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        search_txt = searchText.getText().toString();
        Log.e("search_txt", "search_txt " + search_txt);
        String newString = "'%" + search_txt + "%'";
        Log.e("newString", "newString " + newString);

        String query = "Select * from client_master where (motaheda_code like " + newString + " or phar_name like " + newString + ") and (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in(Select code from definitions where scode='sbranch' and qa=" + emp_code + ")";
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
                            PharmacyClass pharmacyClass = new PharmacyClass();
                            JSONObject temp = results.getJSONObject(i);
                            pharmacyClass.pharmacyName = temp.getString("phar_name");
                            pharmacyClass.code = temp.getDouble("code");
                            list.add(pharmacyClass);
                        }
                        pharmaciesAdapter = new PharmaciesAdapter(PharmaciesActivity.this, list);
                        totalRecycler.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(PharmaciesActivity.this, "error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(PharmaciesActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(PharmaciesActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getPhyByBranch() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.FetcherXml.getInstance().getPhyByBranch(branch_code).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_PHY_BY_BRANCH");
                        for (int i = 0; i < results.length(); i++) {

                            PharmacyClass pharmacyClass = new PharmacyClass();
                            JSONObject temp = results.getJSONObject(i);
                            pharmacyClass.pharmacyName = temp.getString("phy_name");
                            pharmacyClass.code = temp.getDouble("code");
                            list.add(pharmacyClass);

                            total_pharmacies = list.size();
                            totalPharmacies.setText("Total Pharmacies: " + total_pharmacies);

                        }

                        pharmaciesAdapter = new PharmaciesAdapter(PharmaciesActivity.this, list);
                        totalRecycler.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PharmaciesActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(PharmaciesActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            searchText.setVisibility(View.VISIBLE);

            if (flag != 0) {
                search();
            }
            flag++;
        }
    }
}