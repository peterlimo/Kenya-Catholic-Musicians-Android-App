package com.example.kcmav1.learn;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kcmav1.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultDialog {
FirebaseFirestore db;
    public void showDialog(Activity activity, String total, String passed, String failed){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.results_dialog);
        TextView tv_total =  dialog.findViewById(R.id.tv_total);
        TextView tv_passed =  dialog.findViewById(R.id.tv_passed);
        TextView tv_failed =  dialog.findViewById(R.id.tv_failed);
        Button done = dialog.findViewById(R.id.d_done);
        tv_total.setText(total);
        tv_failed.setText(failed);
        tv_passed.setText(passed);

        dialog.setCancelable(true);
        dialog.show();
    }
}
