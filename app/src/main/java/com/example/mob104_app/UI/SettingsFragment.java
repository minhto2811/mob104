package com.example.mob104_app.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mob104_app.Activities.FavouriteActivity;
import com.example.mob104_app.Activities.LoginActivity;
import com.example.mob104_app.Api.ApiService;
import com.example.mob104_app.Models.User;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.ACCOUNT;
import com.example.mob104_app.Tools.TOOLS;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment {
    public static final String TAG = SettingsFragment.class.getName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.title_settings);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    private Button btn_logout, btn_favourite;
    private CircleImageView civ_avatar;
    private TextView tv_fullname;
    private ImageView imv_bg_settings, imv_change_avatar;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_LOGIN = 2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ACCOUNT.user == null) {
            gotoLogin();
            return;
        }
        mapping(view);
        logout();
        setAvatar();
        changeAvatar();
        favourite();
    }


    private void mapping(View view) {
        btn_favourite = view.findViewById(R.id.btn_favourite);
        btn_logout = view.findViewById(R.id.btn_logout);
        civ_avatar = view.findViewById(R.id.civ_avatar);
        tv_fullname = view.findViewById(R.id.tv_fullname);
        imv_bg_settings = view.findViewById(R.id.imv_bg_settings);
        imv_change_avatar = view.findViewById(R.id.imv_change_avatar);
        Glide.with(requireContext()).asGif().load(R.drawable.settings_bg).into(imv_bg_settings);
    }

    private void favourite() {
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(FavouriteActivity.class);
            }
        });
    }

    private void changeAvatar() {
        imv_change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }


    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    private void openImagePicker() {
        TedBottomPicker.OnImageSelectedListener listener = uri -> {
            File file = new File(uri.getPath());
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageRequestBody);

            ApiService.apiService.changeAvatar(imagePart,ACCOUNT.user.getUsername()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        TOOLS.saveUser(requireContext(),response.body());
                        ACCOUNT.user = response.body();
                        Glide.with(requireContext()).load(TOOLS.doMainDevice + response.body().getImage()).into(civ_avatar);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(requireContext(), "Tải ảnh thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        };

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(requireContext())
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getFragmentManager());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            setAvatar();
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    civ_avatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                setPic();
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private void setAvatar() {
        Glide.with(requireContext()).load(TOOLS.doMainDevice + ACCOUNT.user.getImage()).into(civ_avatar);
        tv_fullname.setText("Hi, " + ACCOUNT.user.getFullname());
    }

    private void gotoActivity(Class aClass) {
        Intent intent = new Intent(getContext(), aClass);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
    }

    private void gotoLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void logout() {

        btn_logout.setOnClickListener(v -> {
            TOOLS.clearUser(getContext());
            ACCOUNT.user = null;
            gotoLogin();
        });
    }

}