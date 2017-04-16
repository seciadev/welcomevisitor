package de.novem.bergamo.welcomevisitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by gfand on 24/12/2016.
 */

public class VisitorFragment extends Fragment {
    private static final String ARG_VISITOR_ID = "visitor_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Visitor mVisitor;
    private EditText mCompanyField;
    private EditText mLastName;
    private EditText mFirstName;
    private TextView mDateArrival;
    private Button mDateArrivalNowButton;
    private Button mDateArrivalPickButton;
    private EditText mInternalRef;
    private EditText mAim;
   /* private TextView mDateDeparture;
    private Button mDateDepartureNowButton;
    private Button mDateDeparturePickButton;*/

    public static VisitorFragment newInstance(UUID visitorId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_VISITOR_ID, visitorId);

        VisitorFragment fragment = new VisitorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID visitorId = (UUID)getArguments().getSerializable(ARG_VISITOR_ID);
        setHasOptionsMenu(true);

        mVisitor = VisitorLab.get(getActivity()).getVisitor(visitorId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_visitor, container, false);

        mCompanyField = (EditText) v.findViewById(R.id.visitor_company);
        mCompanyField.setText(mVisitor.getCompany());
        mCompanyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mVisitor.setCompany(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLastName = (EditText) v.findViewById(R.id.visitor_last_name);
        mLastName.setText(mVisitor.getLast_name());
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mVisitor.setLast_name(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFirstName = (EditText) v.findViewById(R.id.visitor_first_name);
        mFirstName.setText(mVisitor.getFirst_name());
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mVisitor.setFirst_name(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateArrival = (TextView)v.findViewById(R.id.visitor_arrival_date);
        updateDate();
        mDateArrivalNowButton = (Button)v.findViewById(R.id.visitor_arrival_date_now);
        mDateArrivalNowButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Date date = new Date();
                mVisitor.setArrivalDate(date);
                updateDate();
            }
        });

        mDateArrivalPickButton = (Button)v.findViewById(R.id.visitor_arrival_date_pick);
        mDateArrivalPickButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mVisitor.getArrivalDate());
                dialog.setTargetFragment(VisitorFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        /*mDateDeparture = (TextView)v.findViewById(R.id.visitor_departure_date);
        mDateDeparture.setText( df.format("yyyy-MM-dd hh:mm", mVisitor.getDepartureDate()));

        mDateDepartureNowButton = (Button)v.findViewById(R.id.visitor_depature_date_now);
        mDateDepartureNowButton.setEnabled(false);

        mDateDeparturePickButton = (Button)v.findViewById(R.id.visitor_depature_date_pick);
        mDateDeparturePickButton.setEnabled(false);*/


        mInternalRef = (EditText) v.findViewById(R.id.visitor_internal_ref);
        mInternalRef.setText(mVisitor.getInternalRef());
        mInternalRef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mVisitor.setInternalRef(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAim = (EditText) v.findViewById(R.id.visitor_aim);
        mAim.setText(mVisitor.getAim());
        mAim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mVisitor.setAim(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_visitor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mVisitor.setArrivalDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        DateFormat df = new DateFormat();;
        mDateArrival.setText( df.format("yyyy-MM-dd hh:mm", mVisitor.getArrivalDate()));
    }
}
