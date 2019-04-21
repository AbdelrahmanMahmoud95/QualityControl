package com.bconnect_egypt.qualitycontrol2.views;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bconnect_egypt.qualitycontrol2.DateDialog;
import com.bconnect_egypt.qualitycontrol2.R;
import com.bconnect_egypt.qualitycontrol2.controllers.AppKeys;
import com.bconnect_egypt.qualitycontrol2.controllers.Service;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class ContractActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup replicationGroup, problemSolvingGroup, monthly_fees_group, static_group,
            recommend_group, customer_service_group, internet_group, network_group,
            data_base_group, anti_virus_group, ts_evaluation_group, company_products_group,
            android_app_group, bconnect_website_group, egypttec_group, re_group, help_desk_group, facebook_group;

    LinearLayout replicationLinear, averageDaysLinear, monthly_fees_linear,
            monthly_fees_linear1, down_recommend, down_arrow, static_linear;

    TextView date_fees;

    EditText code_text, ts_name, sn_text, ph_name_text, address_text,
            ph_manager_text, owner_text, mobile_text, land_text, ucp_text, date_text,
            soft_name_text, soft_version_text, pc_number, comment_text, available_text,
            total_space_text, from, to, fees, serial, service, comment_fees,
            customer_suggestions_text, other_fees_text, os, name_recommend_client, tel_recommend_client;

    String replication, problemSolving, monthly_fees, staticInternet,
            recommend, customer_service, internet, network,
            data_base, anti_virus, ts_evaluation, re_evaluation, company_products,
            android_app, bconnect_website, egypttec, help_desk, facebook;

    String ph_name, ph_code, ph_owner, ph_addrees, ph_tele, ph_phone,em_name;
    String report;
    Button clear_signature, finish_visit;
    SignaturePad signaturePad;
    SharedPreferences dataSaver;

    Geocoder geocoder;
    List<Address> addresses;

    double lat, lon;
    LatLng p1 = null;
    String address = "";
    String latt = "", lonn = "";
    String signatureByteArray = "";
    String rate_recommend = "0", rate_customer_service = "0";
    FusedLocationProviderClient fusedLocationProviderClient;
    static final int External_Permission_Request_code = 0505;
    static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    static final String Fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    static final String coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    boolean mExternalPermissionGranted = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        getExternalPermission();
        findById();
        dataSaver = getDefaultSharedPreferences(getApplicationContext());
        ph_code = dataSaver.getString(AppKeys.PH_CODE, "");
        ph_name = dataSaver.getString(AppKeys.PH_NAME, "");
        ph_addrees = dataSaver.getString(AppKeys.PH_ADDRESS, "");
        ph_owner = dataSaver.getString(AppKeys.PH_OWNER, "");
        ph_phone = dataSaver.getString(AppKeys.PH_MOBILE,"");
        ph_tele = dataSaver.getString(AppKeys.PH_TELE,"");
        em_name = dataSaver.getString(AppKeys.EMP_NAME,"");
        owner_text.setText(ph_owner);
        mobile_text.setText(ph_phone);
        ph_name_text.setText(ph_name);
        land_text.setText(ph_tele);
        code_text.setText(ph_code);
        owner_text.setText(ph_owner);
        ts_name.setText(em_name);
        Log.e("ContractActivity", "ph_name" + ph_name);
        Log.e("ContractActivity", "ph_code" + ph_code);

        down_arrow.setOnClickListener(this);
        down_recommend.setOnClickListener(this);
        clear_signature.setOnClickListener(this);
        date_fees.setOnClickListener(this);
        finish_visit.setOnClickListener(this);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                getDeviceLocation();
            }

            @Override
            public void onClear() {

            }
        });
        intilizeRadioGroup();
//        getLocationFromAddress("51 Aman, Ad Doqi, Giza Governorate, Egypt");
//        double Latitude = p1.latitude;
//        double longitude = p1.longitude;
//        Log.e("Latitude", "Latitude " + Latitude);
//        Log.e("longitude", "longitude " + longitude);

    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;

    }

    public void findById() {
        code_text = findViewById(R.id.code_text);
        ts_name = findViewById(R.id.ts_name);
        sn_text = findViewById(R.id.sn_text);
        ph_name_text = findViewById(R.id.ph_name_text);
        address_text = findViewById(R.id.address_text);
        ph_manager_text = findViewById(R.id.ph_manager_text);
        owner_text = findViewById(R.id.owner_text);
        mobile_text = findViewById(R.id.mobile_text);
        os = findViewById(R.id.ob_text);
        land_text = findViewById(R.id.land_text);

        date_text = findViewById(R.id.date_text);
        soft_name_text = findViewById(R.id.soft_name_text);
        soft_version_text = findViewById(R.id.soft_version_text);
        pc_number = findViewById(R.id.pc_number);
        comment_text = findViewById(R.id.comment_text);
        static_group = findViewById(R.id.static_group);
        available_text = findViewById(R.id.available_text);
        total_space_text = findViewById(R.id.total_space_text);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        fees = findViewById(R.id.fees);
        serial = findViewById(R.id.serial);
        service = findViewById(R.id.service);
        comment_fees = findViewById(R.id.comment_fees);
        customer_suggestions_text = findViewById(R.id.customer_suggestions_text);
        other_fees_text = findViewById(R.id.other_fees_text);
        tel_recommend_client = findViewById(R.id.tel_recommend_client);
        name_recommend_client = findViewById(R.id.name_recommend_client);

        network_group = findViewById(R.id.network_group);
        data_base_group = findViewById(R.id.data_base_group);
        anti_virus_group = findViewById(R.id.anti_virus_group);
        ts_evaluation_group = findViewById(R.id.ts_evaluation_group);
        company_products_group = findViewById(R.id.company_products_group);
        android_app_group = findViewById(R.id.android_app_group);
        bconnect_website_group = findViewById(R.id.bconnect_website_group);
        egypttec_group = findViewById(R.id.egypttec_group);
        help_desk_group = findViewById(R.id.help_desk_group);
        facebook_group = findViewById(R.id.facebook_group);
        replicationGroup = findViewById(R.id.replication_group);
        problemSolvingGroup = findViewById(R.id.problem_solving_group);
        monthly_fees_group = findViewById(R.id.monthly_fees_group);
        recommend_group = findViewById(R.id.recommend_group);
        customer_service_group = findViewById(R.id.customer_service_group);
        internet_group = findViewById(R.id.internet_group);
        re_group = findViewById(R.id.re_group);


        averageDaysLinear = findViewById(R.id.average_days);
        replicationLinear = findViewById(R.id.replication_linear);
        monthly_fees_linear = findViewById(R.id.monthly_fees_linear);
        monthly_fees_linear1 = findViewById(R.id.monthly_fees_linear1);
        date_fees = findViewById(R.id.date_fees);
        finish_visit = findViewById(R.id.finish_visit);
        clear_signature = findViewById(R.id.clear_signature);
        down_recommend = findViewById(R.id.down_arrow2);
        down_arrow = findViewById(R.id.down_arrow);
        static_linear = findViewById(R.id.static_linear);
        signaturePad = findViewById(R.id.signaturePad);
    }

    public void intilizeRadioGroup() {

        replicationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.replication_yes:
                        replication = "y";
                        replicationLinear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.replication_no:
                        replication = "n";
                        replicationLinear.setVisibility(View.GONE);
                        break;

                }
            }
        });
        network_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.network_yes:
                        network = "y";
                        break;

                    case R.id.network_no:
                        network = "n";
                        break;

                }
            }
        });
        data_base_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.data_base_yes:
                        data_base = "y";
                        break;

                    case R.id.data_base_no:
                        data_base = "n";
                        break;

                }
            }
        });
        problemSolvingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.problem_solving_yes:
                        problemSolving = "y";
                        break;

                    case R.id.problem_solving_no:
                        problemSolving = "n";
                        break;

                }
            }
        });
        anti_virus_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.anti_virus_yes:
                        anti_virus = "y";
                        break;

                    case R.id.anti_virus_no:
                        anti_virus = "n";
                        break;
                }
            }
        });
        ts_evaluation_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.excellent:
                        ts_evaluation = "excellent";
                        break;

                    case R.id.good:
                        ts_evaluation = "good";
                        break;

                    case R.id.weak:
                        ts_evaluation = "weak";
                        break;

                    case R.id.fair:
                        ts_evaluation = "fair";
                        break;
                }
            }
        });

        re_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.excellent_re:
                        re_evaluation = "excellent";
                        break;

                    case R.id.good_re:
                        re_evaluation = "good";
                        break;

                    case R.id.weak_re:
                        re_evaluation = "weak";
                        break;

                    case R.id.fair_re:
                        re_evaluation = "fair";
                        break;
                }
            }
        });


        company_products_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.company_products_yes:
                        company_products = "y";
                        break;

                    case R.id.company_products_no:
                        company_products = "n";
                        break;

                }
            }
        });
        bconnect_website_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bconnect_website_yes:
                        bconnect_website = "y";
                        break;

                    case R.id.bconnect_website_no:
                        bconnect_website = "n";
                        break;

                }
            }
        });
        android_app_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.android_app_yes:
                        android_app = "y";
                        break;

                    case R.id.android_app_no:
                        android_app = "n";
                        break;

                }
            }
        });
        egypttec_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.egypttec_yes:
                        egypttec = "y";
                        break;

                    case R.id.egypttec_no:
                        egypttec = "n";
                        break;

                }
            }
        });
        help_desk_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.help_desk_yes:
                        help_desk = "y";
                        break;

                    case R.id.help_desk_no:
                        help_desk = "n";
                        break;

                }
            }
        });
        facebook_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.facebook_yes:
                        facebook = "y";
                        break;

                    case R.id.facebook_no:
                        facebook = "n";
                        break;
                }
            }
        });

        monthly_fees_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.monthly_fees_yes:
                        monthly_fees = "y";
                        monthly_fees_linear1.setVisibility(View.VISIBLE);
                        monthly_fees_linear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.monthly_fees_no:
                        monthly_fees = "n";
                        monthly_fees_linear1.setVisibility(View.GONE);
                        monthly_fees_linear.setVisibility(View.GONE);
                        break;

                }
            }
        });

        recommend_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.recommend_yes:
                        recommend = "y";
                        down_recommend.setVisibility(View.VISIBLE);
                        break;

                    case R.id.recommend_no:
                        recommend = "n";
                        down_recommend.setVisibility(View.GONE);
                        break;

                }
            }
        });

        customer_service_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.customer_service_yes:
                        customer_service = "y";
                        down_arrow.setVisibility(View.VISIBLE);
                        break;

                    case R.id.customer_service_no:
                        customer_service = "n";
                        down_arrow.setVisibility(View.GONE);
                        break;

                }
            }
        });

        internet_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.internet_yes:
                        internet = "y";
                        static_linear.setVisibility(View.VISIBLE);
                        break;

                    case R.id.internet_no:
                        internet = "n";
                        static_linear.setVisibility(View.GONE);
                        break;

                }
            }
        });
        static_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.static_yes:
                        staticInternet = "y";
                        break;

                    case R.id.static_no:
                        staticInternet = "n";
                        break;

                }
            }
        });
    }

    public void selectPopupMenu() {
        final PopupMenu popup = new PopupMenu(ContractActivity.this, down_arrow, Gravity.CENTER);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
                    rate_recommend = "1";
                    Toast.makeText(ContractActivity.this, "تم اختيار 1", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.two) {
                    rate_recommend = "2";
                    Toast.makeText(ContractActivity.this, "تم اختيار 2", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.three) {
                    rate_recommend = "3";
                    Toast.makeText(ContractActivity.this, "تم اختيار 3", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.four) {
                    rate_recommend = "4";
                    Toast.makeText(ContractActivity.this, "تم اختيار 4", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.five) {
                    rate_recommend = "5";
                    Toast.makeText(ContractActivity.this, "تم اختيار 5", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.six) {
                    rate_recommend = "6";
                    Toast.makeText(ContractActivity.this, "تم اختيار 6", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.seven) {
                    rate_recommend = "7";
                    Toast.makeText(ContractActivity.this, "تم اختيار 7", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.eight) {
                    rate_recommend = "8";
                    Toast.makeText(ContractActivity.this, "تم اختيار 8", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.nine) {
                    rate_recommend = "9";
                    Toast.makeText(ContractActivity.this, "تم اختيار 9", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.ten) {
                    rate_recommend = "10";
                    Toast.makeText(ContractActivity.this, "تم اختيار 10", Toast.LENGTH_SHORT).show();
                    return true;

                }

                return false;
            }
        });
        popup.show();

    }

    public void selectPopupMenu2() {
        final PopupMenu popup = new PopupMenu(ContractActivity.this, down_arrow, Gravity.CENTER);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
                    rate_customer_service = "1";
                    Toast.makeText(ContractActivity.this, "تم اختيار 1", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.two) {
                    rate_customer_service = "2";
                    Toast.makeText(ContractActivity.this, "تم اختيار 2", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.three) {
                    rate_customer_service = "3";
                    Toast.makeText(ContractActivity.this, "تم اختيار 3", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.four) {
                    rate_customer_service = "4";
                    Toast.makeText(ContractActivity.this, "تم اختيار 4", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.five) {
                    rate_customer_service = "5";
                    Toast.makeText(ContractActivity.this, "تم اختيار 5", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.six) {
                    rate_customer_service = "6";
                    Toast.makeText(ContractActivity.this, "تم اختيار 6", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.seven) {
                    rate_customer_service = "7";
                    Toast.makeText(ContractActivity.this, "تم اختيار 7", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.eight) {
                    rate_customer_service = "8";
                    Toast.makeText(ContractActivity.this, "تم اختيار 8", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.nine) {
                    rate_customer_service = "9";
                    Toast.makeText(ContractActivity.this, "تم اختيار 9", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (item.getItemId() == R.id.ten) {
                    rate_customer_service = "10";
                    Toast.makeText(ContractActivity.this, "تم اختيار 10", Toast.LENGTH_SHORT).show();
                    return true;

                }

                return false;
            }
        });
        popup.show();

    }

    public void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lon = location.getLongitude();
                        lat = location.getLatitude();
                        lonn = String.valueOf(lon);
                        latt = String.valueOf(lat);
                        Log.e("TAG", " lat " + lat + " lon " + lon);
                        getAddress();
                    } else {
                        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        // startActivity(intent);
                        Log.e("TAG", " lat " + lat + " lon " + lon);
                    }
                }
            });
        } catch (SecurityException e) {
            e.getMessage();
        }
    }

    public void getAddress() {
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        Log.e("address", "address " + address);
    }

    private void getExternalPermission() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                coarse_Location) == PackageManager.PERMISSION_GRANTED) {
            mExternalPermissionGranted = true;
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    READ_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mExternalPermissionGranted = true;

                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mExternalPermissionGranted = true;
                    if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                            Fine_location) == PackageManager.PERMISSION_GRANTED) {
                        mExternalPermissionGranted = true;
                    } else {
                        ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
                }
            } else {
                ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, External_Permission_Request_code);
        }
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        ContractActivity.this.sendBroadcast(mediaScanIntent);
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        Log.e("SignaturePad", "file.getPath() " + file.getPath());
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public String imageFileToByte(File file) {

        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void addJpgSignatureToGallery(Bitmap signature) {

        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d" + "abdo" + ".jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            imageFileToByte(photo);
            signatureByteArray = imageFileToByte(photo);
            Log.e("signatureByteArray", "signatureByteArray " + signatureByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postComment() {

        final AlertDialog builder = new AlertDialog.Builder(this).create();

        final View v = LayoutInflater.from(this).inflate(R.layout.comment_layout, null);

        Button submit = v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText commentText = v.findViewById(R.id.comment);
                report = commentText.getText().toString();
                finishVisit();
                builder.dismiss();

            }
        });
        builder.setView(v);
        // dialog.setCancelable(false);
        builder.show();

    }

    public void finishVisit() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("برجاء الانتظار ...");
        progressDialog.show();

        String os_ = os.getText().toString();
        Log.e("ContractActivity", "os_ " + os_);

        String soft_name = soft_name_text.getText().toString();
        Log.e("ContractActivity", "soft_name " + soft_name);

        String soft_version = soft_version_text.getText().toString();
        Log.e("ContractActivity", "soft_version " + soft_version);

        String pc_n = pc_number.getText().toString();
        Log.e("ContractActivity", "pc_n " + pc_n);

        String comment_t = comment_text.getText().toString();
        Log.e("ContractActivity", "comment_t " + comment_t);

        String fees_ = fees.getText().toString();
        Log.e("ContractActivity", "fees_ " + fees_);

        String free_s = available_text.getText().toString();
        Log.e("ContractActivity", "free_s " + free_s);

        String monthly_date = date_fees.getText().toString();
        Log.e("ContractActivity", "monthly_date " + monthly_date);

        String fee_ver = serial.getText().toString();
        Log.e("ContractActivity", "fee_ver " + fee_ver);

        String fee_comment = comment_fees.getText().toString();
        Log.e("ContractActivity", "fee_comment " + fee_comment);

        String fee_other = other_fees_text.getText().toString();
        Log.e("ContractActivity", "fee_other " + fee_other);

        String name_recommend_clie = name_recommend_client.getText().toString();
        Log.e("ContractActivity", "name_recommend_clie " + name_recommend_clie);

        String tel_recommend_clie = tel_recommend_client.getText().toString();
        Log.e("ContractActivity", "tel_recommend_clie " + tel_recommend_clie);

        String customer_suggestions_te = customer_suggestions_text.getText().toString();
        Log.e("ContractActivity", "customer_suggestions_te " + customer_suggestions_te);
        Log.e("ContractActivity", "rate_customer_service " + rate_customer_service);
        Log.e("ContractActivity", "rate_recommend " + rate_recommend);

        String s = " AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\n" +
                "    AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHCA/wDASIA\n" +
                "    AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\n" +
                "    AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\n" +
                "    ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\n" +
                "    p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\n" +
                "    AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\n" +
                "    BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\n" +
                "    U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\n" +
                "    uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD+/Cii\n" +
                "    igAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKK\n" +
                "    ACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAo";

        Service.FetcherXml.getInstance().saveVisit(ph_code, ph_name, os_, soft_name, soft_version,
                replication, internet, pc_n, network, comment_t, data_base, anti_virus, free_s,
                problemSolving, fees_, monthly_date, fee_ver, fee_comment, fee_other, ts_evaluation, re_evaluation,
                company_products, android_app, bconnect_website, egypttec, help_desk, rate_customer_service, rate_recommend, name_recommend_clie, tel_recommend_clie,
                s, customer_suggestions_te, report, latt, lonn).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String res = response.body();
                    progressDialog.dismiss();
                    Log.e("res", "res " + res);
                    Log.e("isSuccessful", "isSuccessful");
                    Toast.makeText(ContractActivity.this, "تمت الزيارة بنجاح", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ContractActivity.this, PharmaciesActivity.class);
                    startActivity(intent);
                    finish();

                    try {
                        JSONObject places = new JSONObject(res);
                        JSONArray results = places.getJSONArray("QA_SAVE_VISIT");
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject temp = results.getJSONObject(i);
                            String SUCCESS_SAVE = temp.getString("SUCCESS_SAVE");

                            Log.e("SUCCESS_SAVE", "SUCCESS_SAVE " + SUCCESS_SAVE);

                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ContractActivity.this, "املأ كل البيانات ",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(ContractActivity.this, "حاول مره اخرى", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == down_arrow) {
            selectPopupMenu2();
        }
        if (view == down_recommend) {
            selectPopupMenu();
        }
        if (view == clear_signature) {
            signaturePad.clear();
        }
        if (view == date_fees) {
            DateDialog dateDialog = new DateDialog(view);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            dateDialog.show(fragmentTransaction, "Date Picker");
        }
        if (view == finish_visit) {
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
            addJpgSignatureToGallery(signatureBitmap);
            postComment();
        }
    }
}