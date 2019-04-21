package com.bconnect_egypt.qualitycontrol2.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.PharmaciesAdapter;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;
import com.bconnect_egypt.qualitycontrol2.model.AdminClass;
import com.bconnect_egypt.qualitycontrol2.model.PharmacyClass;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class RepresentativeDetailsActivity extends AppCompatActivity {
    TextView totalVisitPharmacies, visit_number, phCount, header;
    Button visit;
    DayScrollDatePicker mPicker;
    BarChart barChart;
    double progress = 0, progress1 = 0;
    ProgressBar simpleProgressBar, simpleProgressBar1;
    String emp_name, emp_code;
    List<AdminClass> list;
    int totalNotVisit, total, totalVisit;
    String visit_date;
    int totalPh;
    SharedPreferences dataSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_details);
        totalVisitPharmacies = findViewById(R.id.total_visit_number);
        visit_number = findViewById(R.id.visit_number);
        visit = findViewById(R.id.visit);
        phCount = findViewById(R.id.ph_count);
        simpleProgressBar = findViewById(R.id.circularProgressbar);
        simpleProgressBar1 = findViewById(R.id.circularProgressbar1);
        mPicker = findViewById(R.id.day_date_picker);
        header = findViewById(R.id.header);
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        emp_name = dataSaver.getString(AppKeys.EMP_NAME, "");
        emp_code = dataSaver.getString(AppKeys.EMP_CODE, "");
        header.setText(emp_name);
        barChart = findViewById(R.id.chart1);
        List<BarEntry> entries = new ArrayList<>();
        getTotalNotVisit();
        getTotalVisit();
        totalNotVisit = dataSaver.getInt(AppKeys.TOTAL_NOT_VISIT, 1);
        totalVisit = dataSaver.getInt(AppKeys.TOTAL_VISIT, 1);
//        List<AdminClass> list = new ArrayList<>();
//        how to put list to MPAndroidChart
//        for (int i = 1; i <= list.size(); i++) {
//            float x = (float) list.get(i).totalNotVisit;
//            float y = (float) list.get(i).totalVisit;
//            entries.add(new BarEntry(x, y));
//            //or
//            entries.add(new BarEntry(i, y));
//        }

//        String x1 = "17-6-1995";
//        String split[] = x1.split("-");
//         float x = Float.parseFloat(split[0]);
//        Toast.makeText(RepresentativeDetailsActivity.this,String.valueOf(x),Toast.LENGTH_LONG).show();
//        float y = 50f;
//        entries.add(new BarEntry(x, y));

        entries.add(new BarEntry(1, 2));
        entries.add(new BarEntry(2f, 3f));
        entries.add(new BarEntry(3f, 5f));
        entries.add(new BarEntry(4f, 8f));
        entries.add(new BarEntry(5f, 8f));
        entries.add(new BarEntry(6f, 8f));
        entries.add(new BarEntry(7f, 8f));
        entries.add(new BarEntry(8f, 6f));
        entries.add(new BarEntry(9f, 4f));
        entries.add(new BarEntry(10f, 3f));
        entries.add(new BarEntry(11f, 2f));
        entries.add(new BarEntry(12f, 1f));
        entries.add(new BarEntry(13f, 2f));
        entries.add(new BarEntry(14f, 5f));
        entries.add(new BarEntry(15f, 8f));
        entries.add(new BarEntry(16f, 8f));
        entries.add(new BarEntry(17f, 8f));
        entries.add(new BarEntry(18f, 8f));
        entries.add(new BarEntry(19f, 6f));
        entries.add(new BarEntry(20f, 4f));
        entries.add(new BarEntry(21f, 3f));
        entries.add(new BarEntry(22f, 2f));
        entries.add(new BarEntry(23f, 4f));
        entries.add(new BarEntry(24f, 3f));
        entries.add(new BarEntry(25f, 2f));
        entries.add(new BarEntry(26f, 1f));
        entries.add(new BarEntry(27f, 2f));
        entries.add(new BarEntry(28f, 5f));
        entries.add(new BarEntry(29f, 9f));
        entries.add(new BarEntry(30f, 7f));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setValueTextSize(12);
        dataSet.setColor(Color.parseColor("#2E3797"));
        dataSet.setValueTextColor(Color.parseColor("#2E3797"));

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
        barChart.setScaleMinima(5f, 0f);
        mPicker.setStartDate(1, 1, 2019);
        mPicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                if (date != null) {
                    int month = date.getMonth() + 1;
                    visit_date = (date.getDate() + " / " + month + " / " + 2019);
                    Log.e("visit_date", "visit_date " + visit_date);
                    getTotalVisitByDate();
                    phCount.setVisibility(View.VISIBLE);
                }
            }
        });

        phCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phCount.setVisibility(View.GONE);
            }
        });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(RepresentativeDetailsActivity.this, ChooseVisitTypActivity.class);
                startActivity(intent1);
            }
        });
        setProgressValue();
    }

    public void getTotalVisitByDate() {
        String newString = "'" + visit_date + "'";
        Log.e("newString", "newString " + newString);

        String query = "SELECT count(*) FROM [QA_visit] where (convert(datetime,convert(varchar,visit_date,103),103) >= convert (datetime," + newString + ",103) AND convert(datetime,convert(varchar,visit_date,103),103) <= convert (datetime," + newString + ",103)) AND phy_code in (Select code from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch' and qa=" + emp_code + "))";
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
                        JSONObject temp = results.getJSONObject(0);
                        totalPh = temp.getInt("Column1");
                        Log.e("totalPh", "totalPh " + totalPh);
                        phCount.setText(totalPh + " ph");
                        totalPh = 0;
                        Log.e("totalPh", "totalPh " + totalPh);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(RepresentativeDetailsActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(RepresentativeDetailsActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTotalNotVisit() {
        String query2 = "Select count(*)  from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch' and qa=" + emp_code + ")and code not in (Select phy_code FROM QA_visit)";
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
                            JSONObject temp = results.getJSONObject(i);
                            totalNotVisit = temp.getInt("Column1");
                            totalVisitPharmacies.setText(String.valueOf(totalNotVisit));
                            dataSaver.edit()
                                    .putInt(AppKeys.TOTAL_NOT_VISIT, totalNotVisit)
                                    .apply();

                        }

                    } catch (JSONException e) {
                        Toast.makeText(RepresentativeDetailsActivity.this, "error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(RepresentativeDetailsActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(RepresentativeDetailsActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getTotalVisit() {
        String query2 = "Select count(*) from client_master where (approve_flag = 'b' and sent_flag = 'd' and stage_flag = 'l' and activation_flag in('a','c')) and motaheda_branch in (Select code from definitions where scode='sbranch' and qa=" + emp_code + ")and code in (Select phy_code FROM QA_visit)";
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
                            JSONObject temp = results.getJSONObject(i);
                            totalVisit = temp.getInt("Column1");
                            visit_number.setText(String.valueOf(totalVisit));
                            dataSaver.edit()
                                    .putInt(AppKeys.TOTAL_VISIT, totalVisit)
                                    .apply();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(RepresentativeDetailsActivity.this, "error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(RepresentativeDetailsActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                Toast.makeText(RepresentativeDetailsActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setProgressValue() {

        Log.e("totalVisit", "totalVisit " + totalVisit);
        Log.e("totalNotVisit", "totalNotVisit " + totalNotVisit);
        total = totalNotVisit + totalVisit;
        Log.e("total", "total " + total);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress = (totalNotVisit * 100.0) / total;
                progress1 = (totalVisit * 100.0) / total;
                Log.e("progress", "progress " + progress);
                Log.e("progress1", "progress1 " + progress1);
                simpleProgressBar.setProgress((int) Math.round(progress));
                simpleProgressBar1.setProgress((int) Math.round(progress1));
                Log.e("progress", "Math.round(progress) " + Math.round(progress));
                Log.e("progress", "Math.round(progress)1 " + (int) Math.round(progress));

            }
        });
        thread.start();
    }
}