package com.bconnect_egypt.qualitycontrol2.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bconnect_egypt.qualitycontrol2.views.PharmacyDetailsActivity;
import com.bconnect_egypt.qualitycontrol2.model.PharmacyClass;
import com.bconnect_egypt.qualitycontrol2.R;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.PharmaciesViewHolder> {

    List<PharmacyClass> list;
    Context context;
    SharedPreferences dataSaver;
    String choose_report, branch_name, visit_type;

    public PharmaciesAdapter(Context context, List<PharmacyClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PharmaciesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout2, viewGroup, false);
        PharmaciesViewHolder pharmaciesViewHolder = new PharmaciesViewHolder(view);
        return pharmaciesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PharmaciesViewHolder holder, final int i) {
        dataSaver = getDefaultSharedPreferences(context);
        holder.pharmacyName.setText(list.get(i).pharmacyName);
        holder.date.setText(list.get(i).date_of_visit);
        branch_name = dataSaver.getString(AppKeys.BRANCH_NAME, "");
        choose_report = dataSaver.getString(AppKeys.CHOOSE_REPORT, "");
        visit_type = dataSaver.getString(AppKeys.VISIT_TYPE, "");
        if (choose_report.equals("ALL_PH") || choose_report.equals("HOLDS")) {
            holder.date.setVisibility(View.GONE);
            holder.totalPharmacies.setText(branch_name);
        } else if (choose_report.equals("VISITS")) {
            holder.date.setVisibility(View.VISIBLE);
            holder.totalPharmacies.setText(branch_name);
        }

        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double x = list.get(i).code;
                int x1 = (int) x;
                if (visit_type.equals("visitByDate")) {
                    dataSaver.edit()
                            .putString(AppKeys.PH_CODE, list.get(i).code_string)
                            .apply();
                } else {
                    dataSaver.edit()
                            .putString(AppKeys.PH_CODE, String.valueOf(x1))
                            .apply();
                }

                dataSaver.edit()
                        .putString(AppKeys.REPORT, list.get(i).comment)
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.DATE_OF_VISIT, list.get(i).date_of_visit)
                        .apply();
                Intent intent = new Intent(context, PharmacyDetailsActivity.class);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PharmaciesViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName, date, totalPharmacies;
        RelativeLayout relative;

        public PharmaciesViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);
            date = itemView.findViewById(R.id.date);
            totalPharmacies = itemView.findViewById(R.id.total);

        }
    }
}
