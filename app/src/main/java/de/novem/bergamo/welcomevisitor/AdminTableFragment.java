package de.novem.bergamo.welcomevisitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.novem.bergamo.welcomevisitor.database.VisitorCursorWrapper;

/**
 * Created by gfand on 26/12/2016.
 */

public class AdminTableFragment extends Fragment {


    private static final String DIALOG_FRAGMENT = "DialogFragment";

    private RecyclerView mVisitorRecyclerView;
    private TextView mLastNameLabel;
    private TextView mPurposeLabel;
    private TextView mCompanyLabel;
    private TextView mDateLabel;
    private VisitorAdapter mAdapter;
    private EditText mSearchEditText;
    private static String mQuery;
    //private TextView mVisitorSelectedTextView;
    //private ImageButton mExitVisitors;
    //private Button mCheckOutButton;
    private Button mBackButton;
    private boolean mAscending;
    private FloatingActionButton mFloatingActionButton;
    //private String mCompany;

    //   public int mSelectedVisitors;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCompany = (String) getArguments().getSerializable(ARG_COMPANY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_list, container, false);

        setupUI(view.findViewById(R.id.container_admin_list));

        mVisitorRecyclerView = (RecyclerView) view.findViewById(R.id.visitor_recycler_view);
        mVisitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAscending = true;

        mLastNameLabel = (TextView) view.findViewById(R.id.last_name_label);
        mLastNameLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLastNameLabel.setBackgroundColor(Color.LTGRAY);
                mPurposeLabel.setBackgroundColor(Color.TRANSPARENT);
                mCompanyLabel.setBackgroundColor(Color.TRANSPARENT);
                mDateLabel.setBackgroundColor(Color.TRANSPARENT);
                VisitorLab visitorLab = VisitorLab.get(getActivity());
                List<Visitor> visitors = visitorLab.getVisitorsSortName(mAscending);
                UpdateUIVisitors(visitors);
            }
        });

        mPurposeLabel = (TextView) view.findViewById(R.id.purpose_label);
        mPurposeLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLastNameLabel.setBackgroundColor(Color.TRANSPARENT);
                mPurposeLabel.setBackgroundColor(Color.LTGRAY);
                mCompanyLabel.setBackgroundColor(Color.TRANSPARENT);
                mDateLabel.setBackgroundColor(Color.TRANSPARENT);
                VisitorLab visitorLab = VisitorLab.get(getActivity());
                List<Visitor> visitors = visitorLab.getVisitorSortPurpose(mAscending);
                UpdateUIVisitors(visitors);
            }
        });

        mCompanyLabel = (TextView) view.findViewById(R.id.company_label);
        mCompanyLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLastNameLabel.setBackgroundColor(Color.TRANSPARENT);
                mPurposeLabel.setBackgroundColor(Color.TRANSPARENT);
                mCompanyLabel.setBackgroundColor(Color.LTGRAY);
                mDateLabel.setBackgroundColor(Color.TRANSPARENT);
                VisitorLab visitorLab = VisitorLab.get(getActivity());
                List<Visitor> visitors = visitorLab.getVisitorSortCompany(mAscending);
                UpdateUIVisitors(visitors);
            }
        });

        mDateLabel = (TextView) view.findViewById(R.id.date_label);
        mDateLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLastNameLabel.setBackgroundColor(Color.TRANSPARENT);
                mPurposeLabel.setBackgroundColor(Color.TRANSPARENT);
                mCompanyLabel.setBackgroundColor(Color.TRANSPARENT);
                mDateLabel.setBackgroundColor(Color.LTGRAY);
                VisitorLab visitorLab = VisitorLab.get(getActivity());
                List<Visitor> visitors = visitorLab.getVisitorSortDate(mAscending);
                UpdateUIVisitors(visitors);
            }
        });

        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        mSearchEditText.addTextChangedListener(new TextWatcher() {

            VisitorLab visitorLab = VisitorLab.get(getActivity());

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQuery=s.toString();
                List<Visitor> visitors = visitorLab.getVisitorQuery(mQuery);
                UpdateUIVisitors(visitors);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_action_share);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                VisitorLab visitorLab = VisitorLab.get(getActivity());
                visitorLab.exportToExcel();
                Toast.makeText(getActivity(),
                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
                /*ExportDialogFragment dialog = new ExportDialogFragment();
                dialog.show(getFragmentManager(), DIALOG_FRAGMENT);*/
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"reception.novem@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Visitor worksheet");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the updated guestbook");
                File root = Environment.getExternalStorageDirectory();
                String pathToMyAttachedFile = "guestbook.xls";
                File file = new File(root, pathToMyAttachedFile);
                if (!file.exists() || !file.canRead()) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));



            }
        });



        /*mVisitorSelectedTextView = (TextView) view.findViewById(R.id.list_item_count_selected);

        mCheckOutButton = (Button) view.findViewById(R.id.check_out_button);
        mCheckOutButton.setEnabled(false);
        mCheckOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedVisitors = mAdapter.mCheckedVisitors;
                for (int i = 0; i < checkedVisitors.size(); i++) {
                    int key = checkedVisitors.keyAt(i);
                    mAdapter.setCompletedVisitor(key);
                }
                mAdapter.mCheckedVisitors.clear();
                mAdapter = null;
                //updateUI();

                Context context = getContext();
                Resources res = getResources();
                CharSequence text = res.getText(R.string.check_out_toast);
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });*/

        mBackButton = (Button) view.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });

        /*mExitVisitors = (ImageButton) view.findViewById(R.id.exit_button);
        mExitVisitors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SparseBooleanArray checkedVisitors = mAdapter.mCheckedVisitors;
                for (int i=0; i<checkedVisitors.size(); i++){
                    int key = checkedVisitors.keyAt(i);
                    mAdapter.setCompletedVisitor(key);
                }
                mAdapter.mCheckedVisitors.clear();
                mAdapter = null;
                updateUI();

            }
        });*/


        updateUI();

        return view;
    }

    private void UpdateUIVisitors(List<Visitor> visitors) {
        mAdapter = null;
        if (mAdapter == null) {
            mAdapter = new VisitorAdapter(visitors);
            mVisitorRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setVisitors(visitors);
            mAdapter.notifyDataSetChanged();
        }
        if(mAscending==true){
            mAscending=false;
        } else {
            mAscending = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = null;
        updateUI();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void updateUI() {
        VisitorLab visitorLab = VisitorLab.get(getActivity());
        List<Visitor> visitors = visitorLab.getVisitors();

        if (mAdapter == null) {
            mAdapter = new VisitorAdapter(visitors);
            mVisitorRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setVisitors(visitors);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class VisitorHolder extends RecyclerView.ViewHolder {

        private Visitor mVisitor;

        private TextView mLastNameTextView;
        private TextView mCompanyTextView;
        private TextView mPurposeTextView;
        private TextView mDateTextView;
        private CheckBox mIsInsideCheckBox;

        public VisitorHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.last_name_label);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_label);
            mCompanyTextView = (TextView) itemView.findViewById(R.id.company_label);
            mPurposeTextView = (TextView) itemView.findViewById(R.id.purpose_label);
            mIsInsideCheckBox = (CheckBox) itemView.findViewById(R.id.is_inside_checkbox);
            //mIsSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.visitor_item_select);

        }

        public void bindVisitor(Visitor visitor) {
            mVisitor = visitor;
            DateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            Date date = mVisitor.getArrivalDate();
            String formattedDate = formatDate.format(date);
            mDateTextView.setText(formattedDate);
            String name;
            if(mVisitor.getFirst_name()!=null){
                name = mVisitor.getLast_name() + " " + mVisitor.getFirst_name();
            }  else {
                name = mVisitor.getLast_name();
            }
            mLastNameTextView.setText(name);
            if(mVisitor.getCompany()!=null){
                mCompanyTextView.setText(mVisitor.getCompany());
            } else {
                mCompanyTextView.setText("");
            }

            mPurposeTextView.setText(mVisitor.getPurpose());

            if(!mVisitor.isCompleted()){
                mIsInsideCheckBox.setChecked(true);
            }
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = VisitorActivity.newIntent(getActivity(), mVisitor.getId());
            startActivity(intent);
        }*/
    }

    private class VisitorAdapter extends RecyclerView.Adapter<VisitorHolder> {
        private List<Visitor> mVisitors;
        //public SparseBooleanArray mCheckedVisitors = new SparseBooleanArray();


        public VisitorAdapter(List<Visitor> visitors) {
            mVisitors = visitors;
        }

        @Override
        public VisitorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_admin, parent, false);
            return new VisitorHolder(view);
        }

        @Override
        public void onBindViewHolder(VisitorHolder holder, final int position) {
            final Visitor visitor = mVisitors.get(position);
            holder.bindVisitor(visitor);
            /*holder.mIsSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //  mSelectedVisitors++;
                        mCheckedVisitors.append(position, true);
                    } else {
                        //  mSelectedVisitors--;
                        mCheckedVisitors.delete(position);
                    }
                    updateUI();
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return mVisitors.size();
        }

        public void setVisitors(List<Visitor> visitors){
            mVisitors = visitors;
        }

        /*public void setCompletedVisitor(int key) {
            mVisitors.get(key).setCompleted(true);
            Date date = new Date();
            mVisitors.get(key).setDepartureDate(date);
            VisitorLab.get(getActivity()).updateVisitor(mVisitors.get(key));
            // mVisitors.remove(key);
            // notifyItemRemoved(key);
        }*/
    }

}
