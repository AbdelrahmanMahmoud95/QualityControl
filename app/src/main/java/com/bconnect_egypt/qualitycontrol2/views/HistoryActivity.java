package com.bconnect_egypt.qualitycontrol2.views;


import android.app.ProgressDialog;
import android.content.Intent;
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

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView pharmaciesRecyclerView;
    GridLayoutManager layoutManager;
    PharmaciesAdapter pharmaciesAdapter;
    List<PharmacyClass> list;
    ImageView search;
    EditText searchText;
    TextView totalVisit;
    String branch_code;
    String visit_date;
    String visit_type;
    SharedPreferences dataSaver;
    int flag = 0;
    String emp_code, visits, search_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pharmaciesRecyclerView = findViewById(R.id.pharmacies_recycler);
        totalVisit = findViewById(R.id.total_visit);
        layoutManager = new GridLayoutManager(this, 1);
        pharmaciesRecyclerView.setLayoutManager(layoutManager);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        searchText = findViewById(R.id.search_text);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        branch_code = dataSaver.getString(AppKeys.BRANCH_CODE, "");
        visit_type = dataSaver.getString(AppKeys.VISIT_TYPE, "");
        visit_date = dataSaver.getString(AppKeys.VISIT_DATE, "");
        emp_code = dataSaver.getString(AppKeys.EMP_CODE, "");
        Log.e("PharmaciesNotVisit", "visit_type " + visit_type);
        Log.e("PharmaciesNotVisit", "visit_date " + visit_date);
        Intent intent = getIntent();
        visits = intent.getStringExtra("visits");
        if (visit_type.equals("visitByBranch")) {
            getVisitPhy();
        } else if (visit_type.equals("allVisits")) {
            getTotalVisits();
        } else if (visit_type.equals("visitByDate")) {
            getVisitByDate();
        }
    }


    public void search() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        search_txt = searchText.getText().toString();
        Log.e("search_txt", "search_txt " + search_txt);
        String newString = "'%" + search_txt + "%'";
        Log.e("newString", "newString " + newString);

        String query = "Select * from client_master where (motaheda_code like " + newString + " or phar_name like " + newString + ") and (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in(Select code from definitions where scode='sbranch' and qa=" + emp_code + ") and code in (Select phy_code FROM QA_visit)";
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
                        pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                        pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(HistoryActivity.this, "error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(HistoryActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(HistoryActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getTotalVisits() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        String query2 = "Select a.*,b.visit_date,b.staff_report from client_master a inner join QA_visit b on b.phy_code=a.code where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and a.motaheda_branch in (Select code from definitions where scode='sbranch' and qa=" + emp_code + ")";
        Service.FetcherXml.getInstance().getTotalPharmacies(query2).enqueue(new Callback<String>() {

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
                            pharmacyClass.comment = temp.getString("staff_report");
                            pharmacyClass.date_of_visit = temp.getString("visit_date");
                            list.add(pharmacyClass);
                        }
                        totalVisit.setText("Total Visit: " + list.size());
                        pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                        pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(HistoryActivity.this, "error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(HistoryActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(HistoryActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getVisitByDate() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();

        Log.e("visit_date", "visit_date " + visit_date);
        String newString = "'" + visit_date + "'";
        Log.e("newString", "newString " + newString);

        String query = "SELECT * FROM [QA_visit] where (convert(datetime,convert(varchar,visit_date,103),103) >= convert (datetime," + newString + ",103) AND convert(datetime,convert(varchar,visit_date,103),103) <= convert (datetime," + newString + ",103)) AND phy_code in (Select code from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch' and qa=" + emp_code + "))";
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
                            pharmacyClass.pharmacyName = temp.getString("phy_name");
                            pharmacyClass.code_string = temp.getString("phy_code");
                            list.add(pharmacyClass);
                        }
                        totalVisit.setText("Total Visit: " + list.size());
                        pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                        pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(HistoryActivity.this, "error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(HistoryActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(HistoryActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getVisitPhy() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.FetcherXml.getInstance().getVisitPhy(branch_code, emp_code).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    list = new ArrayList<>();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_NO_VIST_PHY_EMP");
                        for (int i = 0; i < results.length(); i++) {

                            PharmacyClass pharmacyClass = new PharmacyClass();
                            JSONObject temp = results.getJSONObject(i);
                            pharmacyClass.pharmacyName = temp.getString("phy_name");
                            pharmacyClass.code = temp.getDouble("code");
                            pharmacyClass.date_of_visit = temp.getString("visit_date");
                            pharmacyClass.comment = temp.getString("staff_report");

                            Log.e("code ", "code " + pharmacyClass.code);
                            list.add(pharmacyClass);

                        }

                        totalVisit.setText("Total Visit: " + list.size());
                        pharmaciesAdapter = new PharmaciesAdapter(HistoryActivity.this, list);
                        pharmaciesRecyclerView.setAdapter(pharmaciesAdapter);
                        pharmaciesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(HistoryActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(HistoryActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

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
