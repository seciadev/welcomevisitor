package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class VisitorActivity extends SingleFragmentActivity {

    private static final String EXTRA_VISITOR_ID = "de.novem.bergamo.welcomevisitor.visitor_id";

    public static Intent newIntent(Context packageContext, UUID visitorId){
        Intent intent = new Intent(packageContext, VisitorActivity.class);
        intent.putExtra(EXTRA_VISITOR_ID, visitorId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID visitorId = (UUID) getIntent().getSerializableExtra(EXTRA_VISITOR_ID);
        return VisitorFragment.newInstance(visitorId);
    }
}
