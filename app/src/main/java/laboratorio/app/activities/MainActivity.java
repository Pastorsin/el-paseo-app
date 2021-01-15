package laboratorio.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import laboratorio.app.fragments.SignInFragment;
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

        navigation.setSelectedItemId(R.id.store_nav);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.store_nav:
                    toolbar.setTitle(R.string.store_nav_title);
                    fragment = new CategoryListFragment();
                    replaceFragmentOnMainContainer(fragment);
                    return true;
                case R.id.cart_nav:
                    toolbar.setTitle(R.string.cart_nav_title);
                    fragment = new CartFragment();
                    replaceFragmentOnMainContainer(fragment);
                    return true;
                case R.id.account_nav:
                    toolbar.setTitle("Mi cuenta");
                    fragment = new SignInFragment();
                    replaceFragmentOnMainContainer(fragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    public void replaceFragmentOnMainContainer(Fragment fragment) {
        replaceFragment(fragment, R.id.app_container);
    }

    @Override
    public void replaceFragment(Fragment fragment, int layout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}