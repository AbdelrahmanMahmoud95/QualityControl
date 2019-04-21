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

import com.bconnect_egypt.qualitycontrol2.model.PharmacyClass;
import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.views.HistoryActivity;
import com.bconnect_egypt.qualitycontrol2.views.PharmaciesActivity;
import com.bconnect_egypt.qualitycontrol2.views.PharmaciesNotVisitActivity;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    List<PharmacyClass> list;
    Context context;
    SharedPreferences dataSaver;
    String choose_report;
    String branchCode;
    int x1;
    double x;

    public BranchAdapter(Context context, List<PharmacyClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout2, viewGroup, false);
        BranchViewHolder pharmaciesViewHolder = new BranchViewHolder(view);
        return pharmaciesViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final BranchViewHolder holder, final int i) {
        dataSaver = getDefaultSharedPreferences(context);
        choose_report = dataSaver.getString(AppKeys.CHOOSE_REPORT, "");
        holder.pharmacyName.setText(list.get(i).branch);
        holder.totalPharmacies.setVisibility(View.GONE);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = list.get(i).branchCode;
                x1 = (int) x;
                branchCode = String.valueOf(x1);
                dataSaver.edit()
                        .putString(AppKeys.BRANCH_CODE, branchCode)
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.BRANCH_NAME, list.get(i).branch)
                        .apply();
                if (choose_report.equals("ALL_PH")) {
                    Intent intent = new Intent(context, PharmaciesActivity.class);
                    context.startActivity(intent);
                } else if (choose_report.equals("VISITS")) {
                    Intent intent = new Intent(context, HistoryActivity.class);
                    dataSaver.edit()
                            .putString(AppKeys.VISIT_TYPE, "visitByBranch")
                            .apply();
                    context.startActivity(intent);
                } else if (choose_report.equals("HOLDS")) {
                    Intent intent = new Intent(context, PharmaciesNotVisitActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName, totalPharmacies;
        RelativeLayout relative;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);
            totalPharmacies = itemView.findViewById(R.id.total);
        }
    }
}