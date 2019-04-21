package com.bconnect_egypt.qualitycontrol2.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PharmacyDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView pharmacyName, pharmacyCode, pharmacyAddress,
            pharmacyPhone, pharmacyTele, pharmacyOwner, pharmacySubscription,
            pharmacyGoal, pharmacyBranch, pharmacyContactType, commentText, visitDate, motahedaCode;
    Button finishVisit;
    String ownerName, address, phone, tele,
            subscription, pharmacy_name, branch, motaheda_code;

    String ph_code, choose_report, date_of_visit, report;
    SharedPreferences dataSaver;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pharmacy);
        pharmacyAddress = findViewById(R.id.pharmacy_address);
        pharmacyCode = findViewById(R.id.pharmacy_code);
        pharmacyName = findViewById(R.id.pharmacy_name);
        pharmacyPhone = findViewById(R.id.pharmacy_phone);
        pharmacyContactType = findViewById(R.id.pharmacy_contact_type);
        pharmacyBranch = findViewById(R.id.pharmacy_branch);
        pharmacyGoal = findViewById(R.id.pharmacy_goal);
        pharmacyOwner = findViewById(R.id.pharmacy_owner);
        pharmacySubscription = findViewById(R.id.pharmacy_subscription);
        pharmacyTele = findViewById(R.id.pharmacy_tele);
        finishVisit = findViewById(R.id.finish_visit);
        motahedaCode = findViewById(R.id.motaheda_code);
        commentText = findViewById(R.id.comment);
        visitDate = findViewById(R.id.visit_date);
        cardView = findViewById(R.id.card);

        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        ph_code = dataSaver.getString(AppKeys.PH_CODE, "");
        branch = dataSaver.getString(AppKeys.BRANCH_NAME, "");
        date_of_visit = dataSaver.getString(AppKeys.DATE_OF_VISIT, "");
        report = dataSaver.getString(AppKeys.REPORT, "");
        choose_report = dataSaver.getString(AppKeys.CHOOSE_REPORT, "");
        Log.e("NotVisitDetails", "ph_code " + ph_code);
        Log.e("NotVisitDetails", "choose_report " + choose_report);
        if (choose_report.equals("ALL_PH") || choose_report.equals("VISITS")) {
            finishVisit.setVisibility(View.GONE);
        }
        if (choose_report.equals("ALL_PH") || choose_report.equals("HOLDS")) {
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);

        }
        visitDate.setText(" تاريخ الزيارة: " + date_of_visit);
        commentText.setText(" التقرير: " + report);
        finishVisit.setOnClickListener(this);
        getPhyInfo();

    }

    public void getPhyInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل...");
        progressDialog.show();
        Service.FetcherXml.getInstance().getPhyInfo(ph_code).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("0");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject temp = results.getJSONObject(i);
                            address = temp.getString("address");
                            pharmacy_name = temp.getString("phar_name");
                            ownerName = temp.getString("manager_name");
                            motaheda_code = temp.getString("motaheda_code");
                            phone = temp.getString("mob1");
                            tele = temp.getString("tel1");
                            subscription = temp.getString("monthly_fee");
                            dataSaver.edit()
                                    .putString(AppKeys.PH_NAME, pharmacy_name)
                                    .apply();
                            dataSaver.edit()
                                    .putString(AppKeys.PH_ADDRESS, address)
                                    .apply();
                            dataSaver.edit()
                                    .putString(AppKeys.PH_MOBILE, phone)
                                    .apply();
                            dataSaver.edit()
                                    .putString(AppKeys.PH_TELE, tele)
                                    .apply();
                            dataSaver.edit()
                                    .putString(AppKeys.PH_OWNER, ownerName)
                                    .apply();
                            pharmacyName.setText(" اسم الصيدلية: " + pharmacy_name);
                            pharmacyAddress.setText(" العنوان: " + address);
                            motahedaCode.setText(" كود المتحدة: " + motaheda_code);
                            pharmacyPhone.setText(" تليفون: " + phone + "-" + tele);
                            pharmacySubscription.setText(" قيمة الاشتراك: " + subscription);
                            pharmacyOwner.setText(" اسم المالك: " + ownerName);
                            pharmacyBranch.setText(" اسم الفرع: " + branch);
                            pharmacyCode.setText(" كود الصيدلية: " + ph_code);
                        }

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PharmacyDetailsActivity.this, "خطأ ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(PharmacyDetailsActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();

            }
        });
    }

    //    app:layout_constraintCircle="@id/view_id"
//    app:layout_constraintCircleAngle="angle_0_360"
//    app:layout_constraintCircleRadius="raduis_in_dp"
//https://medium.com/mindorks/learn-constraintlayout-constraintlayout-tutorial-for-android-b43ef6b92f55
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(PharmacyDetailsActivity.this, ContractActivity.class);
        startActivity(intent);
    }
}