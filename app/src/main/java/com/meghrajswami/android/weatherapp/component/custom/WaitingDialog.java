package com.meghrajswami.android.weatherapp.component.custom;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * waiting dialog extending progress dialog
 * To show waiting message
 */
public class WaitingDialog extends ProgressDialog {

    Context mContext;
    String mMessage;

    public WaitingDialog(Context context, String message) {
        super(context);

        mContext = context;
        mMessage = message;

        //set cancelable to true so that user don't feel UI jammed/blocked
        setCancelable(true);
        setMessage(message);
    }
}
