package laboratorio.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.databinding.ActivityMainBinding;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.fragments.SignInFragment;
import laboratorio.app.fragments.UserProfileFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.R;
import laboratorio.app.fragments.CartFragment;
import laboratorio.app.fragments.CategoryListFragment;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.viewmodels.ApplicationViewModel;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FragmentLoader {
    public static final String ARG_NAV_ELEMENT_ID_SELECTED = "NAV_ELEMENT_ID_SELECTED";

    private static final int NAV_ELEMENT_ID_SELECTED_DEFAULT = R.id.store_nav;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();

        initListeners();

        initBottomNavigationView();
    }

    private void initDataBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ApplicationViewModel appViewmodel = new ViewModelProvider(this).get(ApplicationViewModel.class);

        binding.setViewmodel(appViewmodel);
        binding.setLifecycleOwner(this);
    }

    private void initListeners() {
        ApplicationViewModel appViewmodel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        appViewmodel.errorEvent.observe(this, aVoid -> replaceFragmentOnMainContainer(new ErrorFragment()));

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

                    boolean isUserLoggedIn = ApiSession.instance.isUserLoggedIn(getApplicationContext());
                    fragment = isUserLoggedIn ? new UserProfileFragment() : new SignInFragment();

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