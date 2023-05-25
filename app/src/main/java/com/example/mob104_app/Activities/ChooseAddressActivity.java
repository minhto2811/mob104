package com.example.mob104_app.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob104_app.Adapter.DistrictAdapter;
import com.example.mob104_app.Adapter.ProvinceAdapter;
import com.example.mob104_app.Adapter.WardAdapter;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Interface.DistrictOnClick;
import com.example.mob104_app.Interface.ProvinceOnClick;
import com.example.mob104_app.Interface.WardOnClick;
import com.example.mob104_app.Models.District;
import com.example.mob104_app.Models.Province;
import com.example.mob104_app.Models.Ward;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ADDRESS;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAddressActivity extends AppCompatActivity {

    private boolean exit;
    private Toolbar toolbar;
    private RecyclerView rcv_province, rcv_district, rcv_ward;
    private TextView tv_district, tv_ward;
    private ProvinceAdapter provinceAdapter;
    private DistrictAdapter districtAdapter;
    private WardAdapter wardAdapter;
    private Button btn_submit_select_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        mapping();
        setToolbar();
        setProvices();
        submit();
        whenGoBack();
    }

    private void whenGoBack() {
        if (ADDRESS.province != null) {
            if (ADDRESS.district != null) {
                setDistrict(true);
                if (ADDRESS.ward != null) {
                    setWard(true);
                }
            }
        }
    }

    private void submit() {
        btn_submit_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", ADDRESS.ward.getPath_with_type());
                setResult(RESULT_OK, intent);
                back();
            }
        });
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_choose_address);
        rcv_province = findViewById(R.id.rcv_choose_province);
        rcv_district = findViewById(R.id.rcv_choose_district);
        rcv_ward = findViewById(R.id.rcv_choose_ward);
        tv_district = findViewById(R.id.tv_district);
        tv_ward = findViewById(R.id.tv_ward);
        btn_submit_select_address = findViewById(R.id.btn_submit_select_address);
    }

    private void setProvices() {
        setLayout(rcv_province);
        provinceAdapter = new ProvinceAdapter(ChooseAddressActivity.this, new ProvinceOnClick() {
            @Override
            public void ItemClick(Province province) {
                if (ADDRESS.province != null) {
                    ADDRESS.province = null;
                    ADDRESS.district = null;
                    ADDRESS.ward = null;
                } else {
                    ADDRESS.province = province;
                }
                setDistrict((ADDRESS.province != null) ? true : false);
                setSubmit(((ADDRESS.province != null) && (ADDRESS.district != null) && (ADDRESS.ward != null)) ? true : false);
                provinceAdapter.notifyDataSetChanged();
            }
        });
        rcv_province.setAdapter(provinceAdapter);
        getListProvince();
    }

    private void getListProvince() {
        ApiService.apiService.getProvinces().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful()) {
                    provinceAdapter.setData(response.body());
                    if (AddAddressActivity.address1 == null) {
                        return;
                    }
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getName().equals(AddAddressActivity.address1.getProvince())) {
                            ADDRESS.province = response.body().get(i);
                            setDistrict(true);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                Toast.makeText(ChooseAddressActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setDistrict(boolean show) {
        if (!show) {
            tv_ward.setVisibility(View.GONE);
            tv_district.setVisibility(View.GONE);
            rcv_ward.setVisibility(View.GONE);
            rcv_district.setVisibility(View.GONE);
            return;
        }
        tv_district.setVisibility(View.VISIBLE);
        rcv_district.setVisibility(View.VISIBLE);
        setLayout(rcv_district);
        districtAdapter = new DistrictAdapter(ChooseAddressActivity.this, new DistrictOnClick() {
            @Override
            public void ItemClick(District district) {
                if (ADDRESS.district != null) {
                    ADDRESS.district = null;
                    ADDRESS.ward = null;
                } else {
                    ADDRESS.district = district;
                }
                setWard((ADDRESS.district != null) ? true : false);
                setSubmit(((ADDRESS.province != null) && (ADDRESS.district != null) && (ADDRESS.ward != null)) ? true : false);
                districtAdapter.notifyDataSetChanged();
            }
        });
        rcv_district.setAdapter(districtAdapter);
        getListDistrict();
    }

    private void setWard(boolean show) {
        if (!show) {
            tv_ward.setVisibility(View.GONE);
            rcv_ward.setVisibility(View.GONE);
            return;
        }
        tv_ward.setVisibility(View.VISIBLE);
        rcv_ward.setVisibility(View.VISIBLE);
        setLayout(rcv_ward);
        wardAdapter = new WardAdapter(ChooseAddressActivity.this, new WardOnClick() {
            @Override
            public void ItemClick(Ward ward) {
                if (ADDRESS.ward != null) {
                    ADDRESS.ward = null;
                } else {
                    ADDRESS.ward = ward;
                }
                setSubmit(((ADDRESS.province != null) && (ADDRESS.district != null) && (ADDRESS.ward != null)) ? true : false);
                wardAdapter.notifyDataSetChanged();
            }
        });
        rcv_ward.setAdapter(wardAdapter);

        getListWard();
    }

    private void getListWard() {
        ApiService.apiService.getWards(ADDRESS.district.getCode()).enqueue(new Callback<List<Ward>>() {
            @Override
            public void onResponse(Call<List<Ward>> call, Response<List<Ward>> response) {
                if (response.isSuccessful()) {
                    wardAdapter.setData(response.body());
                    if (AddAddressActivity.address1 == null) {
                        return;
                    }
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getName().equals(AddAddressActivity.address1.getWards())) {
                            ADDRESS.ward = response.body().get(i);
                            setSubmit(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Ward>> call, Throwable t) {
                Toast.makeText(ChooseAddressActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSubmit(boolean show) {
        if (!show) {
            btn_submit_select_address.setVisibility(View.GONE);
            return;
        }
        btn_submit_select_address.setVisibility(View.VISIBLE);
    }

    private void getListDistrict() {
        ApiService.apiService.getDistricts(ADDRESS.province.getCode()).enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                if (response.isSuccessful()) {
                    districtAdapter.setData(response.body());
                    if (AddAddressActivity.address1 == null) {
                        return;
                    }
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getName().equals(AddAddressActivity.address1.getDistrict())) {
                            ADDRESS.district = response.body().get(i);
                            setWard(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {
                Toast.makeText(ChooseAddressActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLayout(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChooseAddressActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(ChooseAddressActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_choose_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }


    @Override
    public void onBackPressed() {
        if (ADDRESS.province == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseAddressActivity.this);
            builder.setTitle("Nếu bạn thoát, vị trí được chọn sẽ không được lưu lại.");
            builder.setPositiveButton("Xác nhận thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ADDRESS.ward = null;
                    ADDRESS.district = null;
                    ADDRESS.province = null;
                    exit = true;
                }
            });

            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
            if (exit) {
                super.onBackPressed();
                back();
            }
        } else {
            super.onBackPressed();
            back();
        }
    }
}