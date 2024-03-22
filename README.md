package com.example.meetingscheduler;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MeetingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    FloatingActionButton fab;
    List<Meeting> meetingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting);
        recyclerView = findViewById(R.id.recyclerViewId);
        fab = findViewById(R.id.fab);

        readData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookMeetingActivity.class);
                startActivity(intent);
            }
        });

    }


    private String formatTime(int hourOfDay, int minute) {
        String format;
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, format);
    }
    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
        String line = "";

        try{

            reader.readLine();

            while ((line = reader.readLine())!=null)
            {
                String[] tokens = line.split(",");

                Meeting meeting = new Meeting();
                meeting.setStartTime(tokens[0]);
                meeting.setEndTime(tokens[1]);
                meeting.setDate(tokens[2]);
                meeting.setOriginator(tokens[3]);
                meeting.setTitle(tokens[4]);
                meeting.setRoom(Integer.parseInt(tokens[5]));
                meetingList.add(meeting);

                Log.d("MEETING_ACTIVITY", "readData: " + meeting);

            }
        }catch (Exception e)
        {
            Log.d("MEETING_ACTIVITY", "readData: " + e);
        }

        showData();
    }

    private void showData() {

        adapter = new Adapter(this,meetingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}



<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/activity_main_screen"
    android:padding="20dp"
    tools:context=".BookMeetingActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Book Meeting Room"
        android:fontFamily="sans-serif-condensed"
        android:textSize="20sp"
        android:textStyle="bold"/>

<ScrollView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="30dp"
    >

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        >


    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:weightSum="2"
       >


<LinearLayout
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:orientation="vertical">
    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/filledTextField"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Title of the Meeting">

        <!--this is the actual edit text which takes the input-->
        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/titleMeetingETId"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </com.google.android.material.textfield.TextInputLayout>



          <CalendarView
              android:layout_marginTop="10dp"
              android:id="@+id/ms_calendarView"
              android:layout_width="wrap_content"
              android:layout_height="wrap_content"
              >
          </CalendarView>

    <TextView
        android:id="@+id/dateTvId"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Date"
        android:layout_marginTop="5dp"
        android:layout_marginLeft="10dp"
        android:textSize="15sp"/>
</LinearLayout>

<LinearLayout
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:gravity="center"

    android:orientation="vertical">

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/filledTextField2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Originator">

        <!--this is the actual edit text which takes the input-->
        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/originatorMeetingETId"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </com.google.android.material.textfield.TextInputLayout>

    <nl.joery.timerangepicker.TimeRangePicker
        android:id="@+id/picker"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_marginLeft="10dp"
        app:trp_endTime="10:00"
        app:trp_startTime="9:00"
        app:trp_thumbIconEnd="@drawable/clock"
        app:trp_thumbIconStart="@drawable/clock" />


<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:weightSum="2"
    android:orientation="horizontal">

    <TextView
        android:id="@+id/startTimeTvId"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="Start Time"
        android:layout_marginTop="5dp"
        android:layout_marginLeft="10dp"
        android:textSize="15sp"/>

    <TextView
        android:id="@+id/endTimeTvId"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="End Time"
        android:layout_marginTop="5dp"
        android:layout_marginLeft="10dp"
        android:textSize="15sp"/>
</LinearLayout>



</LinearLayout>


    </LinearLayout>


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:layout_marginTop="20dp"
            android:gravity="center"
            >

            <Button
                android:id="@+id/bookBtn"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Book"/>

            <Button
                android:id="@+id/cancelBtn"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="20dp"
                android:text="Cancel"/>
        </LinearLayout>

    </LinearLayout>

</ScrollView>


</RelativeLayout>

    implementation ("nl.joery.timerangepicker:timerangepicker:1.0.0")

