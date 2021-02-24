package laboratorio.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.databinding.ActivityMainBinding;
import laboratorio.app.fragments.DeliveryOrdersListFragment;
import laboratorio.app.fragments.DeliveryProfileFragment;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.fragments.PurchaseListFragment;
import laboratorio.app.fragments.SignInFragment;
import laboratorio.app.fragments.UserProfileFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.viewmodels.ApplicationViewModel;

public class DeliveryActivity extends AppCompatActivity implements FragmentLoader {

    public static final String ARG_NAV_ELEMENT_ID_SELECTED = "NAV_ELEMENT_ID_SELECTED";

    private static final int NAV_ELEMENT_ID_SELECTED_DEFAULT = R.id.purchase_nav;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        initBottomNavigationView();
    }

    private void initBottomNavigationView() {
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int navElementSelected = getIntent().getIntExtra(
                ARG_NAV_ELEMENT_ID_SELECTED,
                NAV_ELEMENT_ID_SELECTED_DEFAULT);

        navigation.setSelectedItemId(navElementSelected);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.purchase_nav:
                    toolbar.setTitle("Pedidos");
                    fragment = new DeliveryOrdersListFragment();
                    replaceFragmentOnMainContainer(fragment);
                    return true;
                case R.id.account_nav:
                    toolbar.setTitle(R.string.account_nav_title);

                    boolean isUserLoggedIn = ApiSession.instance.isUserLoggedIn(getApplicationContext());
                    fragment = isUserLoggedIn ? new DeliveryProfileFragment() : new SignInFragment();

                    replaceFragmentOnMainContainer(fragment);
                    return true;
            }

            return false;
        }
    };

    @Override
    public void replaceFragmentOnMainContainer(Fragment fragment) {
        replaceFragment(fragment, R.id.app_container, null);
    }

    @Override
    public void replaceFragment(Fragment fragment, int layout, String backStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(backStack);
        loadFragment(fragment, layout, transaction);
    }

    @Override
    public void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment).commit();
    }

    @Override
    public void replaceFragment(Fragment fragment, int layout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        loadFragment(fragment, layout, transaction);
    }

    private void loadFragment(Fragment fragment, int layout, FragmentTransaction transaction) {
        transaction.replace(layout, fragment);
        transaction.commit();
    }

}