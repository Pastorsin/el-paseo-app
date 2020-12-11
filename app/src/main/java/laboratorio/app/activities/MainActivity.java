package laboratorio.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.R;
import laboratorio.app.fragments.CartFragment;
import laboratorio.app.fragments.CategoryListFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FragmentLoader {
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.store_nav:
                    toolbar.setTitle("Categorías");
                    fragment = new CategoryListFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.cart_nav:
                    toolbar.setTitle("Carrito de compras");
                    fragment = new CartFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.app_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}