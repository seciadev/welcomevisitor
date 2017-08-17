package de.novem.bergamo.welcomevisitor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class ScreenSlidePageFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    private static final String ARG_GUEST_NUM = "guest_num";
    private int mGuestNum;
    private static String mPurpose;
    private static String mCompany;
    private static String mEmployee;
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
                        if (mCompany==null||mCompany.equals("")) {
                            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                            mVisitorCompany.startAnimation(shake);
                        } else {
                            sendMessage(mPosition + 1);

                        }
                    }

                });

                break;

            case 1:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_visitor_slide_page_2, container, false);
                Spinner spinner = (Spinner) rootView.findViewById(R.id.purpose_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.purpose_choice, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        mPurpose = (String)parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final EditText mEmployeeVisit = (EditText)rootView.findViewById(R.id.employee_edit_text);
                mEmployeeVisit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mEmployee=s.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //
                    }
                });

                Button mNextButton1 = (Button)rootView.findViewById(R.id.next_button);
                mNextButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                            sendMessage(mPosition + 1);
                    }

                });

                Button mBackButton1 = (Button)rootView.findViewById(R.id.back_button);
                mBackButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        sendMessage(mPosition-1);
                    }

                });

                break;


            case 2:
                rootView = (ViewGroup) inflater.inflate(
                        R.layout.fragment_visitor_slide_page_3, container, false);

                mVisitorRecyclerView = (RecyclerView) rootView.findViewById(R.id.visitor_same_company_recycler_view);
                mVisitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mGuestNum=1;
                mVisitors = new ArrayList<>();
                final Visitor newVis = new Visitor();
                mVisitors.add(newVis);
                updateUI();



                ImageButton mAddVisitorButton = (ImageButton) rootView.findViewById(R.id.add_visitor_button);
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

                ImageButton mRemoveVisitorButton = (ImageButton) rootView.findViewById(R.id.remove_visitor_button);
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

                Button mBackButton2 = (Button)rootView.findViewById(R.id.back_button);
                mBackButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        sendMessage(mPosition-1);
                    }

                });

                mVisitorLab = VisitorLab.get(getContext());
                final Button mOkEnterButton = (Button) rootView.findViewById(R.id.ok_enter);
                mOkEnterButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        try {

                            for (Visitor vis : mVisitors) {
                                if (vis.getLast_name() == null || mCompany.equals("")) {
                                    throw new EmptyStackException();
                                }
                                vis.setCompany(mCompany);
                                vis.setPurpose(mPurpose);
                                vis.setEmployeeVisit(mEmployee);
                                mVisitorLab.addVisitor(vis);
                            }
                            Context context = getContext();
                            Resources res = getResources();
                            CharSequence text = res.getText(R.string.check_in_toast);
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            NavUtils.navigateUpFromSameTask(getActivity());
                        } catch (EmptyStackException e){
                            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                            mVisitorRecyclerView.startAnimation(shake);
                        }

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