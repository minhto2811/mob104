package com.example.mob104_app.Activities;

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
    private int INDEX = 0;
    private final int CART = 1;
    private final int BILL = 2;
    private final int HOME = 3;
    private final int SETTINGS = 5;
    private int EXIT = 0;
    private MeowBottomNavigation bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.show(HOME, true);
        bottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.widgets));
        bottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.shopping_cart));
        bottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.local_shipping));
        bottomNavigationView.add(new MeowBottomNavigation.Model(5, R.drawable.settings));

        bottomNavigationView.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    replaceFragment(new CartFragment(), CartFragment.TAG, CART);
                    break;

                case 2:
                    replaceFragment(new BillFragment(), BillFragment.TAG, BILL);
                    break;

                case 3:
                    replaceFragment(new HomeFragment(), HomeFragment.TAG, HOME);

                    break;


                case 5:
                    replaceFragment(new SettingsFragment(), SettingsFragment.TAG, SETTINGS);
                    break;
                default:
                    break;
            }
            return null;
        });

        replaceFragment(new HomeFragment(), HomeFragment.TAG, 3);
    }

    private void replaceFragment(Fragment fragment, String name, int ID) {
        if(INDEX != ID) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (INDEX < ID) {
                fragmentTransaction.setCustomAnimations(R.anim.next_enter, R.anim.next_exit);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.prev_enter, R.anim.prev_exit);
            }
            Fragment existingFragment = fragmentManager.findFragmentByTag(name);
            if (existingFragment != null && (ID!=CART || ID!=BILL)) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cart =  getIntent().getIntExtra("cart",-1);
        int bill =  getIntent().getIntExtra("bill",-1);
        if(cart == CART){
            bottomNavigationView.show(CART, true);
            replaceFragment(new CartFragment(), CartFragment.TAG, CART);
            getIntent().removeExtra("cart");
        }else if(bill==BILL){
            bottomNavigationView.show(BILL, true);
            replaceFragment(new BillFragment(), BillFragment.TAG, BILL);
            getIntent().removeExtra("bill");
        }


    }
}