package de.novem.bergamo.welcomevisitor;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gfand on 07/01/2017.
 */

public class ExitVisitorFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.exit_dialog,null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.exit_visitor_title)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }
}
