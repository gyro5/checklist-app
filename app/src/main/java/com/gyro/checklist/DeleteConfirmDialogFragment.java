package com.gyro.checklist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmDialogFragment extends DialogFragment {
    /**
     * An interface to let the activity decides what happens
     * when each button is clicked. The activity that calls this
     * dialog needs to implement this interface.
     */
    public interface DeleteDialogListener {
        void onDeleteDialogClick();
    }

    DeleteDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDialogListener) context;
        }
        catch (ClassCastException cce) {
            Log.e("ERROR", "Activity must implement the DeleteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this checklist?")
                .setPositiveButton("DELETE", (dialog, which) -> listener.onDeleteDialogClick())
                .setNegativeButton("CANCEL", (dialog, which) -> {});
        return builder.create();
    }
}
