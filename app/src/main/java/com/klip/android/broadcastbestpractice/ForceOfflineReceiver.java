package com.klip.android.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

public class ForceOfflineReceiver extends BroadcastReceiver {
    private Context mContext;

    public ForceOfflineReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.warning));
        builder.setMessage(context.getString(R.string.warning_massage));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
                Intent intent = new Intent(context, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });
        builder.show();
    }
}
