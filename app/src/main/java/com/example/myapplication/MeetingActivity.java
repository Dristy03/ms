package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    FloatingActionButton fab;

    private static final String TAG = "WriteFile";
    private static final String CSV_FILE_NAME = "data.csv";
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/data.csv";
    List<Meeting> meetingList = new ArrayList<>();
    List<Meeting> upcomingMeetings = new ArrayList<>();
    String line = "3:00PM,5:00PM,27/03/2024,Ananna,Standup,403";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting);
        recyclerView = findViewById(R.id.recyclerViewId);
        fab = findViewById(R.id.fab);


        


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showData();
                readData();
              //writeToCSV();
            //  startActivity(new Intent(getApplicationContext(),DemoActivity.class));
            }
        });
    }

    private void readData()  {
        try{
        File csvFile = new File(getFilesDir(), "data.csv");
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String line = "";

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


    private void writeToCSV() {
        try {
            File csvFile = new File(getFilesDir(), "data.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true)); // Append mode

            // Write data to CSV

            writer.write(line);
            writer.newLine();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.d(TAG, "writeToCSV: "+ e);
        }

        readData();
    }

    private void showData() {

        Date currentDate = new Date();

        for (Meeting meeting : meetingList) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = sdf.parse(meeting.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (date.after(currentDate)) {
                upcomingMeetings.add(meeting);
            }
        }

        adapter = new Adapter(this,upcomingMeetings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}