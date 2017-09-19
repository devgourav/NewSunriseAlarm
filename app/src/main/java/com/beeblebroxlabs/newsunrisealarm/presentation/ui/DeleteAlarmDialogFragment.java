package com.beeblebroxlabs.newsunrisealarm.presentation.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.beeblebroxlabs.newsunrisealarm.R;
import com.beeblebroxlabs.newsunrisealarm.presentation.ui.RepeatAlarmDialogFragment.RepeatDialogFragmentListener;


public class DeleteAlarmDialogFragment extends DialogFragment {

  DeleteDialogFragmentListener listener;
  int position;

  public DeleteAlarmDialogFragment() {
    // Required empty public constructor
  }


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    position = bundle.getInt("position");

    listener = (DeleteDialogFragmentListener) getActivity();

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Delete")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            listener.onClickDeleteDialogListener(Activity.RESULT_OK,position);
            Toast.makeText(getActivity().getApplicationContext(), "Alarm Deleted",
                Toast.LENGTH_SHORT).show();
          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            listener.onClickDeleteDialogListener(Activity.RESULT_CANCELED,position);
          }
        });
    return builder.create();
  }

  public interface DeleteDialogFragmentListener {

    void onClickDeleteDialogListener(int result, int position);
  }
}

