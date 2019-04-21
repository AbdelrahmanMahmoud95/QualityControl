package com.bconnect_egypt.qualitycontrol2.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bconnect_egypt.qualitycontrol2.model.AdminClass;
import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.views.RepresentativeDetailsActivity;

import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {
    List<AdminClass> list;
    Context context;
    SharedPreferences dataSaver;

    public AdminAdapter(List<AdminClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pharmacies_layout, viewGroup, false);
        AdminViewHolder adminViewHolder = new AdminViewHolder(view);
        return adminViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminViewHolder holder, final int i) {
        dataSaver = getDefaultSharedPreferences(context);
        holder.pharmacyName.setText(list.get(i).emp_name);

        holder.pharmacyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.relative.setBackgroundColor(Color.parseColor("#EFEDED"));
                dataSaver.edit()
                        .putString(AppKeys.EMP_CODE, list.get(i).emp_code)
                        .apply();
                dataSaver.edit()
                        .putString(AppKeys.EMP_NAME, list.get(i).emp_name)
                        .apply();
                Intent intent = new Intent(context, RepresentativeDetailsActivity.class);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName;
        RelativeLayout relative;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacy_name);
            relative = itemView.findViewById(R.id.relative);

        }
    }
}
