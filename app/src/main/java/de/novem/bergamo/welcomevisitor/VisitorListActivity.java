package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorListActivity extends SingleFragmentActivity {

    private static final String EXTRA_COMPANY = "de.novem.bergamo.welcomevisitor.company";

    public static Intent newIntent(Context packageContext, String company){
        Intent intent = new Intent(packageContext, VisitorListActivity.class);
        intent.putExtra(EXTRA_COMPANY, company);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        String company = (String) getIntent().getSerializableExtra(EXTRA_COMPANY);
        return VisitorListFragment.newInstance(company);
    }

}
