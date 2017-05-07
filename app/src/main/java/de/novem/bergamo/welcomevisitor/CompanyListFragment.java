package de.novem.bergamo.welcomevisitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by gfand on 28/04/2017.
 */

public class CompanyListFragment extends Fragment {

    private RecyclerView mCompanyRecyclerView;
    private CompanyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_list, container, false);

        mCompanyRecyclerView = (RecyclerView) view.findViewById(R.id.company_recycler_view);
        mCompanyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter=null;
        updateUI();
    }



    private class CompanyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String mCompany;

        private TextView mCompanyTextView;
        private TextView mVisitorNumberTextView;

        public  CompanyHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mCompanyTextView = (TextView) itemView.findViewById(R.id.item_company);
            mVisitorNumberTextView = (TextView) itemView.findViewById(R.id.item_visitor_number);

        }

        //QUALCHE ERRORE QUI
        public void bindCompany(String company, int numVis){
            mCompanyTextView.setText(company);
            mCompany = company;
            /*VisitorLab visitorLab = VisitorLab.get(getActivity());
            List<Visitor> openVis = visitorLab.getUncompletedVisitorsFromSameCompany(company);*/
            String visNumber = String.valueOf(numVis);
            mVisitorNumberTextView.setText(visNumber);
        }

        @Override
        public void onClick(View v){
            Intent intent = VisitorListActivity.newIntent(getActivity(), mCompany);
            startActivity(intent);
        }
    }

    private class CompanyAdapter extends RecyclerView.Adapter<CompanyHolder>{
        private Map<String, Integer> mCompaniesVisitors;
        private String[] mCompanies;
        private Integer[] mNumVisitors;
        //public SparseBooleanArray mCheckedVisitors = new SparseBooleanArray();



        public CompanyAdapter(Map<String,Integer> compVis){

            mCompaniesVisitors = (HashMap) compVis;
            mCompanies = mCompaniesVisitors.keySet().toArray(new String[mCompaniesVisitors.size()]);
            mNumVisitors = mCompaniesVisitors.values().toArray(new Integer[mCompaniesVisitors.size()]);

        }

        @Override
        public CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_company, parent, false);
            return new CompanyHolder(view);
        }

        @Override
        public void onBindViewHolder(CompanyHolder holder, final int position){
            final String company = mCompanies[position];
            final int numVis = mNumVisitors[position];
            holder.bindCompany(company, numVis);
        }

        @Override
        public int getItemCount(){
            return mCompaniesVisitors.size();
        }

    }


    private void updateUI() {
        VisitorLab visitorLab = VisitorLab.get(getActivity());
        List<String> companies = visitorLab.getUncompletedCompanies();
        Map<String, Integer> compVis = new HashMap<>();

        for (String company : companies){
            int numVis = visitorLab.getUncompletedVisitorNumberFromSameCompany(company);
            compVis.put(company, numVis);
        }

        if (mAdapter == null) {

            mAdapter = new CompanyAdapter(compVis);
            mCompanyRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
}
