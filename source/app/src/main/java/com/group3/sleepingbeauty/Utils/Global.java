package com.group3.sleepingbeauty.Utils;

import android.content.Context;
import android.media.Ringtone;
import android.widget.Toast;

/**
 * Created by Lam Le Thanh The on 7/5/2018.
 */

public class Global {
    public static final int REQ_CODE_RINGTONE_SYS = 1;

    public static void notImplementedPrompt(Context context) {
        Toast.makeText(context, "This feature will be available soon!\nSorry for this inconvenience!", Toast.LENGTH_LONG).show();
    }
}
