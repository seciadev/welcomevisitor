package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorListFragment extends Fragment {

    private static final String DIALOG_EXIT = "DialogExit";
    private static final String ARG_COMPANY = "company";

    private RecyclerView mVisitorRecyclerView;
    private VisitorAdapter mAdapter;
    private TextView mVisitorSelectedTextView;
    private ImageButton mExitVisitors;
    private Button mCheckOutButton;
    private Button mBackButton;
    private String mCompany;

    //   public int mSelectedVisitors;

    public static VisitorListFragment newInstance(String company) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMPANY, company);

        VisitorListFragment fragment = new VisitorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompany = (String) getArguments().getSerializable(ARG_COMPANY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visitor_list, container, false);

        mVisitorRecyclerView = (RecyclerView) view.findViewById(R.id.visitor_recycler_view);
        mVisitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mVisitorSelectedTextView = (TextView) view.findViewById(R.id.list_item_count_selected);

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
        });

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

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = null;
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_visitor_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_visitor:
                /*Visitor visitor = new Visitor();
                VisitorLab.get(getActivity()).addVisitor(visitor);
                Intent intent = VisitorActivity.newIntent(getActivity(), visitor.getId());
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), ScreenSlidePagerActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        VisitorLab visitorLab = VisitorLab.get(getActivity());
        List<Visitor> visitors = visitorLab.getUncompletedVisitorsFromSameCompany(mCompany);

        if (mAdapter == null) {

            mAdapter = new VisitorAdapter(visitors);
            mVisitorRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setVisitors(visitors);
            mAdapter.notifyDataSetChanged();
        }

        SparseBooleanArray mSelectedVisitors = mAdapter.mCheckedVisitors;
        if (mSelectedVisitors.size() > 0) {
            mVisitorSelectedTextView.setVisibility(View.VISIBLE);
            //mExitVisitors.setVisibility(View.VISIBLE);
            mCheckOutButton.setEnabled(true);
            Resources res = getResources();
            String visitorsNotification = res.getQuantityString(R.plurals.numberOfVisitorsCounted, mSelectedVisitors.size(), mSelectedVisitors.size());
            mVisitorSelectedTextView.setText(visitorsNotification);
        } else {
            mVisitorSelectedTextView.setVisibility(View.GONE);
            //mExitVisitors.setVisibility(View.GONE);
        }
    }

    private class VisitorHolder extends RecyclerView.ViewHolder {

        private Visitor mVisitor;

        private TextView mLastNameTextView;
        private TextView mTimeEnterTextView;
        private CheckBox mIsSelectedCheckBox;
        private RelativeLayout mItemVisitorRelativeLayout;

        public VisitorHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            mItemVisitorRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_visitor_relativelayout);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.last_name_textview);
            mTimeEnterTextView = (TextView) itemView.findViewById(R.id.time_enter_textview);
            mIsSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.visitor_item_select);

        }

        public void bindVisitor(Visitor visitor) {
            mVisitor = visitor;
            DateFormat formatDate = new SimpleDateFormat("HH:mm");
            Date date = mVisitor.getArrivalDate();
            String formattedDate = formatDate.format(date);
            mTimeEnterTextView.setText(formattedDate);
            String name;
            if(mVisitor.getFirst_name()!=null){
                name = mVisitor.getLast_name() + " " + mVisitor.getFirst_name();
            }  else {
                name = mVisitor.getLast_name();
            }
            mLastNameTextView.setText(name);
        }

        /*@Override
        public void onClick(View v) {
            Intent intent = VisitorActivity.newIntent(getActivity(), mVisitor.getId());
            startActivity(intent);
        }*/
    }

    private class VisitorAdapter extends RecyclerView.Adapter<VisitorHolder> {
        private List<Visitor> mVisitors;
        public SparseBooleanArray mCheckedVisitors = new SparseBooleanArray();


        public VisitorAdapter(List<Visitor> visitors) {
            mVisitors = visitors;
        }

        @Override
        public VisitorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_visitor, parent, false);
            return new VisitorHolder(view);
        }

        @Override
        public void onBindViewHolder(final VisitorHolder holder, final int position) {
            final Visitor visitor = mVisitors.get(position);
            holder.bindVisitor(visitor);
            holder.mIsSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //  mSelectedVisitors++;
                        mCheckedVisitors.append(position, true);
                        holder.mItemVisitorRelativeLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));

                    } else {
                        //  mSelectedVisitors--;
                        mCheckedVisitors.delete(position);
                        holder.mItemVisitorRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
                    }
                    updateUI();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVisitors.size();
        }

        public void setVisitors(List<Visitor> visitors){
            mVisitors = visitors;
        }

        public void setCompletedVisitor(int key) {
            mVisitors.get(key).setCompleted(true);
            Date date = new Date();
            mVisitors.get(key).setDepartureDate(date);
            VisitorLab.get(getActivity()).updateVisitor(mVisitors.get(key));
            // mVisitors.remove(key);
            // notifyItemRemoved(key);
        }
    }

}
