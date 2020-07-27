package dev.ddzmitry.studenttracker.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dzmitrydubarau on 7/26/20.
 */

public class Alerting extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("param"), Toast.LENGTH_SHORT).show();
    }
}