package com.smarttersstudio.rescuenation;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;



public class TermsDialog extends Dialog {
    Button btn_agree;
    Context c;
    public TermsDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.modal_add);
        c=context;
        btn_agree = findViewById(R.id.modal_agree);
    }
}
