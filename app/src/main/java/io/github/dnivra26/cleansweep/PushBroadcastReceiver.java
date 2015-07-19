package io.github.dnivra26.cleansweep;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;

import io.github.dnivra26.cleansweep.models.Message;

public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        String jsonString = intent.getExtras().get("com.parse.Data").toString();

        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson(jsonString, Message.class);

        String[] split = message.getAlert().split(":");
        Intent intent1 = new Intent(context, ConfirmActivity_.class);

        if (split.length > 1) {
            intent1.putExtra("task_id", split[1]);
        }


        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }
}
