package de.novem.bergamo.welcomevisitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by gfand on 26/12/2016.
 */

public class VisitorListFragment extends Fragment{

    private static final String DIALOG_EXIT = "DialogExit";
    private static final String ARG_COMPANY = "company";

    private RecyclerView mVisitorRecyclerView;
    private VisitorAdapter mAdapter;
    private TextView mVisitorSelectedTextView;
    private ImageButton mExitVisitors;
    private String mCompany;

 //   public int mSelectedVisitors;

    public static VisitorListFragment newInstance(String company){
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMPANY, company);

        VisitorListFragment fragment = new VisitorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mCompany = (String) getArguments().getSerializable(ARG_COMPANY);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_visitor_list,container,false);

        mVisitorRecyclerView = (RecyclerView) view.findViewById(R.id.visitor_recycler_view);
        mVisitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mVisitorSelectedTextView = (TextView)  view.findViewById(R.id.list_item_count_selected);

        mExitVisitors = (ImageButton) view.findViewById(R.id.exit_button);
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
        });


        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter=null;
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_visitor_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    private void updateUI(){
        VisitorLab visitorLab = VisitorLab.get(getActivity());
        List<Visitor> visitors = visitorLab.getUncompletedVisitorsFromSameCompany(mCompany);

        if(mAdapter == null) {

            mAdapter = new VisitorAdapter(visitors);
            mVisitorRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }

        SparseBooleanArray mSelectedVisitors = mAdapter.mCheckedVisitors;
        if(mSelectedVisitors.size()>0){
            mVisitorSelectedTextView.setVisibility(View.VISIBLE);
            mExitVisitors.setVisibility(View.VISIBLE);
            String visitorsNotification = getString(R.string.visitor_selected_format, mSelectedVisitors.size());
            mVisitorSelectedTextView.setText(visitorsNotification);
        } else {
            mVisitorSelectedTextView.setVisibility(View.GONE);
            mExitVisitors.setVisibility(View.GONE);
        }
    }

    private class VisitorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Visitor mVisitor;

        private TextView mCompanyTextView;
        private TextView mLastNameTextView;
        private CheckBox mIsSelectedCheckBox;

        public  VisitorHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mCompanyTextView = (TextView) itemView.findViewById(R.id.item_company);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.item_field_2);
            mIsSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.visitor_item_select);

        }

        public void bindVisitor(Visitor visitor){
            mVisitor = visitor;
            mCompanyTextView.setText(mVisitor.getCompany());
            mLastNameTextView.setText(mVisitor.getLast_name());
        }

        @Override
        public void onClick(View v){
            Intent intent = VisitorActivity.newIntent(getActivity(), mVisitor.getId());
            startActivity(intent);
        }
    }

    private class VisitorAdapter extends RecyclerView.Adapter<VisitorHolder>{
        private List<Visitor> mVisitors;
        public SparseBooleanArray mCheckedVisitors = new SparseBooleanArray();



        public VisitorAdapter(List<Visitor> visitors){
            mVisitors = visitors;
        }

        @Override
        public VisitorHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_visitor, parent, false);
            return new VisitorHolder(view);
        }

        @Override
        public void onBindViewHolder(VisitorHolder holder, final int position){
            final Visitor visitor = mVisitors.get(position);
                holder.bindVisitor(visitor);
                holder.mIsSelectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                });
        }

        @Override
        public int getItemCount(){
            return mVisitors.size();
        }

        public void setCompletedVisitor(int key){
            mVisitors.get(key).setCompleted(true);
            Date date = new Date();
            mVisitors.get(key).setDepartureDate(date);
           // mVisitors.remove(key);
           // notifyItemRemoved(key);
        }
    }

}
