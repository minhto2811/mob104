package com.example.mob104_app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.mob104_app.R;
import com.example.mob104_app.Tools.LIST;
import com.example.mob104_app.Tools.TOOLS;
import com.example.mob104_app.UI.BillFragment;
import com.example.mob104_app.UI.CartFragment;
import com.example.mob104_app.UI.HomeFragment;
import com.example.mob104_app.UI.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    public static int INDEX = 0;
    public static final int CART = 2;
    public static final int BILL = 3;
    public static final int HOME = 1;
    private final int SETTINGS = 4;
    private int EXIT = 0;
    public static MeowBottomNavigation bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        setBottomNavigationView();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void setBottomNavigationView(){
        bottomNavigationView.show(HOME, true);
        bottomNavigationView.add(new MeowBottomNavigation.Model(HOME, R.drawable.widgets));
        bottomNavigationView.add(new MeowBottomNavigation.Model(CART, R.drawable.shopping_cart));
        bottomNavigationView.add(new MeowBottomNavigation.Model(BILL, R.drawable.local_shipping));
        bottomNavigationView.add(new MeowBottomNavigation.Model(SETTINGS, R.drawable.settings));

        bottomNavigationView.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case CART:
                    replaceFragment(new CartFragment(), CartFragment.TAG, CART, this);
                    break;

                case BILL:
                    replaceFragment(new BillFragment(), BillFragment.TAG, BILL, this);
                    break;


                case SETTINGS:
                    replaceFragment(new SettingsFragment(), SettingsFragment.TAG, SETTINGS, this);
                    break;

                case HOME:
                default:
                    replaceFragment(new HomeFragment(), HomeFragment.TAG, HOME, this);
                    break;
            }
            return null;
        });
        replaceFragment(new HomeFragment(), HomeFragment.TAG, HOME, this);
    }

    public static void replaceFragment(Fragment fragment, String name, int ID, Context context) {
        if (INDEX != ID) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (INDEX < ID) {
                fragmentTransaction.setCustomAnimations(R.anim.next_enter, R.anim.next_exit);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.prev_enter, R.anim.prev_exit);
            }

            Fragment existingFragment = fragmentManager.findFragmentByTag((ID != CART && ID != BILL)?name:null);
            if (existingFragment != null ) {
                fragmentTransaction.replace(R.id.container_content, existingFragment,name);
            } else {
                TOOLS.checkAllCarts = false;
                LIST.listBuyCart.clear();
                fragmentTransaction.replace(R.id.container_content, fragment,name);
            }
            fragmentTransaction.addToBackStack(name);
            fragmentTransaction.commit();
            INDEX = ID;
        }
    }

    @Override
    public void onBackPressed() {
        if(!HomeFragment.searchView.isIconified()){
            HomeFragment.searchView.setIconified(true);
            return;
        }
        EXIT++;
        if (EXIT < 2) {
            Toast.makeText(this, "Nhấn 1 lần nữa để thoát.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> EXIT = 0, 2000);
        } else {
            moveTaskToBack(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_extend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.toolbar_notify) {
            Intent intent = new Intent(MainActivity.this, NotifyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("cart", -1) == CART) {
            bottomNavigationView.show(CART, true);
            replaceFragment(new CartFragment(), CartFragment.TAG, CART, this);
            getIntent().removeExtra("cart");
        } else if (getIntent().getIntExtra("bill", -1) == BILL) {
            bottomNavigationView.show(BILL, true);
            replaceFragment(new BillFragment(), BillFragment.TAG, BILL, this);
            getIntent().removeExtra("bill");
        }
    }


}