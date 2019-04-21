package com.bconnect_egypt.qualitycontrol2.controllers;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("QA_LOGIN_DATA")
    Call<String> userLogin(@Query("v_user_name") String username, @Query("v_user_pass") String password);

    @GET("QA_LIST_EMP")
    Call<String> getEmployees();

    @GET("QA_BRANCH_EMP")
    Call<String> getEmployeeBranches(@Query("v_emp_code") String v_emp_code);

    @GET("QA_PHY_BY_BRANCH")
    Call<String> getPhyByBranch(@Query("BRANCH_CODE") String BRANCH_CODE);

    @GET("QA_NO_VIST_PHY_EMP")
    Call<String> getNoVisitPhy(@Query("v_br_code") String v_br_code, @Query("v_emp_code") String v_emp_code);

    @GET("QA_VIST_PHY_EMP")
    Call<String> getVisitPhy(@Query("v_br_code") String v_br_code, @Query("v_emp_code") String v_emp_code);

    @GET("QA_PHY_INFO")
    Call<String> getPhyInfo(@Query("PHY_CODE") String PHY_CODE);

    @GET("QA_REPORTS")
    Call<String> getTotalPharmacies(@Query("v_SQL_QUARY") String v_SQL_QUARY);

    @GET("QA_SAVE_VISIT")
    Call<String> saveVisit(@Query("v_phy_code") String v_phy_code
            , @Query("v_phy_name") String v_phy_name
            , @Query("v_os") String v_os
            , @Query("v_soft_name") String v_soft_name
            , @Query("v_soft_ver") String v_soft_ver
            , @Query("v_replication") String v_replication
            , @Query("v_internt") String v_internt
            , @Query("v_pc_no") String v_pc_no
            , @Query("v_network") String v_network
            , @Query("v_comment") String v_comment
            , @Query("v_db_check") String v_db_check
            , @Query("v_anti_vir") String v_anti_vir
            , @Query("v_free_space") String v_free_space
            , @Query("v_problem_solve") String v_problem_solve
            , @Query("v_monthly_fee") String v_monthly_fee
            , @Query("v_monthly_fee_date") String v_monthly_fee_date
            , @Query("v_monthly_fee_serial") String v_monthly_fee_serial
            , @Query("v_monthly_fee_comm") String v_monthly_fee_comm
            , @Query("v_monthly_fee_other") String v_monthly_fee_other
            , @Query("v_tech_evaluation") String v_tech_evaluation
            , @Query("v_tech_respond_evaluation") String v_tech_respond_evaluation
            , @Query("v_oth_product") String v_oth_product
            , @Query("v_android_app") String v_android_app
            , @Query("v_bc_web") String v_bc_web
            , @Query("v_egyptec") String v_egyptec
            , @Query("v_helpdesk") String v_helpdesk
            , @Query("v_cs_tell") String v_cs_tell
            , @Query("v_recomend_bc") String v_recomend_bc
            , @Query("v_recomend_client_name") String v_recomend_client_name
            , @Query("v_recomend_client_no") String v_recomend_client_no
            , @Query("v_signature") String v_signature
            , @Query("v_client_notes") String v_client_notes
            , @Query("v_staff_report") String v_staff_report
            , @Query("v_lat") String v_lat
            , @Query("v_lang") String v_lang);


    String BASE_URL = "http://41.187.17.242/QAWS/QAWS.asmx/";
    String BASE_URL2 = "http://172.16.0.2/QAWS/QAWS.asmx/";
    String BASE_URL3 = "http://172.16.0.20/QAWS.asmx/";

    class FetcherXml {

        private static Service service = null;
        final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        public static Service getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                service = retrofit.create(Service.class);
            }
            return service;
        }
    }
}
