package com.beeblebroxlabs.newsunrisealarm.presentation.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beeblebroxlabs.newsunrisealarm.R;

public class RepeatAlarmDialogFragment extends DialogFragment {

  RepeatDialogFragmentListener dialogFragmentListener;
  public RepeatAlarmDialogFragment() {}

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    dialogFragmentListener = (RepeatDialogFragmentListener) getActivity();

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Repeat Alarm")
        .setPositiveButton("Repeat", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialogFragmentListener.onClickDialogListener(Activity.RESULT_OK);
          }
        })
        .setNegativeButton("Only Once", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialogFragmentListener.onClickDialogListener(Activity.RESULT_CANCELED);
          }
        });
    return builder.create();
  }

  public interface RepeatDialogFragmentListener {
    void onClickDialogListener(int result);
  }
}

