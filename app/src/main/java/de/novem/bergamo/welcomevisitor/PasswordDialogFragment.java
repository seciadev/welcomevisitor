package de.novem.bergamo.welcomevisitor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by gfand on 29/08/2017.
 */

public class PasswordDialogFragment extends DialogFragment {

    private static final String PASSWORD = "reception";
    private static String mPassword;
    private EditText mPasswordEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_password,null);

        mPasswordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword=s.toString();
                //controlla
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.password_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        if (mPassword.equals(PASSWORD)){
                            Intent intent = new Intent(getContext(), AdminTableActivity.class);
                            startActivity(intent);
                        }else{
                            mPasswordEditText.setText("");
                        }
                    }
                })
                .create();
    }
}
