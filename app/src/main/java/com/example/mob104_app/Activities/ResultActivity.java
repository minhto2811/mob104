package com.example.mob104_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob104_app.R;

public class ResultActivity extends AppCompatActivity {

    private ImageView imv_image;
    private TextView tv_result;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mapping();
        show();
        back();
    }

    private void back() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_back.setVisibility(View.VISIBLE);
            }
        }, 2000);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void show() {
        int choose = getIntent().getIntExtra("result", -1);
        switch (choose) {
            case -1:
                imv_image.setImageResource(R.drawable.fail);
                tv_result.setText("Đặt hàng thất bại");
                btn_back.setBackgroundResource(R.color.red);
                break;
            case 0:
                imv_image.setImageResource(R.drawable.successful);
                tv_result.setText("Đặt hàng thành công");
                btn_back.setBackgroundResource(R.color.green);
                break;
            case 4:
                imv_image.setImageResource(R.drawable.successful);
                tv_result.setText("Hủy đơn thành công");
                btn_back.setBackgroundResource(R.color.green);
                break;
            case 5:
                imv_image.setImageResource(R.drawable.fail);
                tv_result.setText("Hủy đơn thất bại");
                btn_back.setBackgroundResource(R.color.red);
                break;
            default:break;
        }
    }

    private void mapping() {
        imv_image = findViewById(R.id.imv_image);
        tv_result = findViewById(R.id.tv_result);
        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
        finish();
    }
}