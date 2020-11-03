package com.mally.ch02.waitHater.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mally.ch02.waitHater.R;

public class NotificationsFragment extends Fragment {

    private View notification;
    private ImageView iv_addAlarm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        notification = inflater.inflate(R.layout.fragment_notifications, container, false);

        iv_addAlarm = notification.findViewById(R.id.iv_addAlarm);
        iv_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        return notification;
    }
}