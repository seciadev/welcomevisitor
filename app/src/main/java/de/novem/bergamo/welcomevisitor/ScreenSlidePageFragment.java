package de.novem.bergamo.welcomevisitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScreenSlidePageFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    private static final String ARG_GUEST_NUM = "guest_num";
    private int mGuestNum;
    private static String mCompany;
    private VisitorLab mVisitorLab;
    private RecyclerView mVisitorRecyclerView;
    private VisitorAdapter mAdapter;
    private ArrayList<Visitor> mVisitors;
    //ArrayList<VisitorFromSameCompany> mCompanyVisitors;

    public static ScreenSlidePageFragment newInstance(int position){
        Bundle args = new Bundle();
        args.putSerializable(ARG_POSITION, position);
        //args.putSerializable(ARG_GUEST_NUM, guest_num);
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPosition = (int)getArguments().getSerializable(ARG_POSITION);
        mGuestNum=1;
        //mGuestNum = (int)getArguments().getSerializable(ARG_GUEST_NUM);
        Log.d("onCreate", "in OnCreate: " + mGuestNum);
        
       // mCompanyVisitors = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView;

        final float scale = getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (5 * scale + 0.5f);

        switch (mPosition){
            case 0:
                 rootView= (ViewGroup) inflater.inflate(
                        R.layout.fragment_visitor_slide_page_1, container, false);

                final EditText mVisitorCompany = (EditText)rootView.findViewById(R.id.visitor_company);
                mVisitorCompany.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mCompany=s.toString();
                        //controlla
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //
                    }
                });

                Button mNextButton = (Button)rootView.findViewById(R.id.next_button);
                mNextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        sendMessage(mPosition+1);
                    }

                });

                break;

            case 1:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_visitor_slide_page_2, container, false);

                mVisitorRecyclerView = (RecyclerView) rootView.findViewById(R.id.visitor_same_company_recycler_view);
                mVisitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mGuestNum=1;
                mVisitors = new ArrayList<>();
                final Visitor newVis = new Visitor();
                mVisitors.add(newVis);
                updateUI();



                Button mAddVisitorButton = (Button) rootView.findViewById(R.id.add_visitor_button);
                mAddVisitorButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v){
                        mGuestNum++;
                        Visitor newV = new Visitor();
                        mVisitors.add(newV);
                        mAdapter=null;
                        updateUI();


                    }
                });

                Button mRemoveVisitorButton = (Button) rootView.findViewById(R.id.remove_visitor_button);
                mRemoveVisitorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mGuestNum>1) {
                            mGuestNum--;
                            mVisitors.remove(mGuestNum);
                            mAdapter=null;
                            updateUI();
                        }

                    }
                });

                mVisitorLab = VisitorLab.get(getContext());
                Button mOkEnterButton = (Button) rootView.findViewById(R.id.ok_enter);
                mOkEnterButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        for (Visitor vis : mVisitors) {
                            vis.setCompany(mCompany);
                            mVisitorLab.addVisitor(vis);
                        }
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }
                });


                break;

            default:
                rootView= (ViewGroup) inflater.inflate(
                        R.layout.fragment_visitor_slide_page_1, container, false);
                break;

        }

        return rootView;
    }

    private void sendMessage(int position) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("scroll_page_event");
        // You can also include some extra data.
        intent.putExtra("message", position);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private class VisitorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Visitor mVisitor;

        private EditText mFirstNameEditText;
        private EditText mLastNameEditText;
        private TextView mLabelTextView;

        public  VisitorHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mLabelTextView = (TextView) itemView.findViewById(R.id.label_visitor_text_view);
            mFirstNameEditText = (EditText) itemView.findViewById(R.id.fist_name_edit_text);
            mLastNameEditText = (EditText) itemView.findViewById(R.id.last_name_edit_text);

        }

        public void bindVisitor(Visitor visitor, int position){
            mVisitor = visitor;
            mLabelTextView.setText("Visitor #" + (position+1));
        }

        @Override
        public void onClick(View v){
            //
        }

    }

    private class VisitorAdapter extends RecyclerView.Adapter<VisitorHolder>{
        private List<Visitor> mVisitors;


        public VisitorAdapter(List<Visitor> visitors){
            mVisitors = visitors;
        }

        @Override
        public VisitorHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_enter_new_visitor, parent, false);
            return new VisitorHolder(view);
        }

        @Override
        public void onBindViewHolder(VisitorHolder holder, final int position){
            final Visitor visitor = mVisitors.get(position);
            holder.bindVisitor(visitor, position);
            //mVisitors.get(position).setCompany(mCompany);
            holder.mFirstNameEditText.setText(visitor.getFirst_name());
            holder.mFirstNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mVisitors.get(position).setFirst_name(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.mLastNameEditText.setText(visitor.getLast_name());
            holder.mLastNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mVisitors.get(position).setLast_name(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public int getItemCount(){
            return mVisitors.size();
        }

    }


    private void updateUI(){
        /*Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("add_visitor_event");
        //intent.putExtra("message", guest_num);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);*/

            if(mAdapter == null) {
                mAdapter = new VisitorAdapter(mVisitors);
                mVisitorRecyclerView.setAdapter(mAdapter);
            } else{
                mAdapter.notifyDataSetChanged();
            }
    }
}