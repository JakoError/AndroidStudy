package com.example.androidstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private NotificationManager noteManager;
    public static final String EXTRA_MESSAGE = "com.example.androidstudy.MESSAGE";

    private TextView top;
    private TextView user_name;
    private TextView user_password;
    private ProgressBar pb;
    private Button click;
    private Button add;
    private Button search;

    private int progress;
    private boolean auto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationManager();
        setAllViews();
    }

    private void setAllViews() {
        top = findViewById(R.id.top);
        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        pb = findViewById(R.id.progress);
        click = findViewById(R.id.click);
        add = findViewById(R.id.add);
        search = findViewById(R.id.search);

        add.setOnLongClickListener(v -> {
            autoIncrease();
            return true;
        });
    }

    protected boolean createNotificationManager(){
        noteManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notificationChannel", "通知",
                    NotificationManager.IMPORTANCE_HIGH);
            noteManager.createNotificationChannel(channel);
            return true;
        }else return false;
    }
    private Notification createNotification(String ChannelId, String Title,String Text) {
        return new NotificationCompat.Builder(this, ChannelId)
                .setContentTitle(Title)
                .setContentText(Text)
                .setSmallIcon(R.drawable.ic_baseline_person_24)
                .build();
    }
    public void jump_next(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        TextView passwordTextView = findViewById(R.id.user_password);
        String password = passwordTextView.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, password);
        startActivity(intent);
    }

    public void disappearPb(View view) {
        if (pb.getVisibility() == View.GONE) {
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.GONE);
            pb.setProgress(0);
            auto = false;
        }
    }

    public void addPb(View view) {
        pb.setProgress(pb.getProgress() + 1);
        if (auto) auto = false;
    }
    /**
     *实现自我增加
     */
    private void autoIncrease() {
        progress = pb.getProgress();
        auto = true;
        new Thread(() -> {
            while (progress <= 100 && auto) {
                progress += 1;
                if (progress > 100)
                    progress = 0;
                runOnUiThread(() -> pb.setProgress(progress));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void searchUser(View view) {
        String name_str = user_name.getText().toString();
        String result;
        if (name_str.length() == 0){
            top.setText(getResources().getString(R.string.user_hint));
            return;
        }
        if (name_str.equals("JakoError")) {
            top.setText(name_str + getResources().getString(R.string.top_positive_text));
            top.setTextColor(getResources().getColor(R.color.green));
            result = getResources().getString(R.string.top_positive_text);
        } else {
            top.setText(name_str + getResources().getString(R.string.top_negative_text));
            top.setTextColor(getResources().getColor(R.color.red));
            result = getResources().getString(R.string.top_negative_text);
        }
        top.requestFocus();
        noteManager.notify(1, createNotification("notificationChannel",
                name_str+"核酸检测结果:"+result,
                String.format("新冠在即请 %s 认真对待",name_str)));
    }
}