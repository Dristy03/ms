package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MeetingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    List<Meeting> meetingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meeting);
        recyclerView = findViewById(R.id.recyclerViewId);
        adapter = new Adapter(this);
        
        readData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        
    }

    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.Data);
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
    }
}