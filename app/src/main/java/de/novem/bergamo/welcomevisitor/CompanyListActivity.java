package de.novem.bergamo.welcomevisitor;


import android.support.v4.app.Fragment;


public class CompanyListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment(){
        return new CompanyListFragment();
    }

}
