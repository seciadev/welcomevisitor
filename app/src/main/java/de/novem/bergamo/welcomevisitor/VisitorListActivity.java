package de.novem.bergamo.welcomevisitor;

import android.support.v4.app.Fragment;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new VisitorListFragment();
    }

}
