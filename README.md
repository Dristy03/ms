
package com.example.meetingscheduler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import nl.joery.timerangepicker.TimeRangePicker;

public class BookMeetingActivity extends AppCompatActivity {

    TimeRangePicker picker;
    EditText titleET;
    EditText originatorET;
    CalendarView calendarView;
    TextView dateTv;

    TextView startTimeTv;

    TextView endTimeTv;


    Button bookBtn;

    Button cancelBtn;

    String date, startTime, endTime, title, originator;
    List<Meeting> meetingList = new ArrayList<>();
    ArrayList<String> existingData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_meeting);
        picker=findViewById(R.id.picker);
        titleET = findViewById(R.id.titleMeetingETId);
        originatorET = findViewById(R.id.originatorMeetingETId);
        calendarView = findViewById(R.id.ms_calendarView);
        dateTv = findViewById(R.id.dateTvId);
        startTimeTv = findViewById(R.id.startTimeTvId);
        endTimeTv = findViewById(R.id.endTimeTvId);
        bookBtn = findViewById(R.id.bookBtn);
        cancelBtn = findViewById(R.id.cancelBtn);


        calendarView.setMinDate((new Date().getTime()));
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        date = curFormater.format(new Date());
        startTime="9:00";
        endTime="10:00";
        dateTv.setText("Date: "+date);
        startTimeTv.setText("Start Time: 9:00");
        endTimeTv.setText("End TIme: 10:00");

        fetchData();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String  curDate = String.valueOf(dayOfMonth);
                String  Year = String.valueOf(year);
                String  Month = String.valueOf(month);
                date = curDate+"/"+Month+"/"+Year;
                dateTv.setText("Date: " + date);
            }
        });


        picker.setOnTimeChangeListener(new TimeRangePicker.OnTimeChangeListener() {
             @Override
             public void onStartTimeChange(@NonNull TimeRangePicker.Time time) {
                 Log.d("TimeRangePicker", "Start time: " + time);
                 startTime = time.toString();
                 startTimeTv.setText("Start Time: "+startTime);
             }

             @Override
             public void onEndTimeChange(@NonNull TimeRangePicker.Time time) {
                 Log.d("TimeRangePicker", "End time: " + time);
                 endTime = time.toString();
                 endTimeTv.setText("End Time: " + endTime);
             }

             @Override
             public void onDurationChange(@NonNull TimeRangePicker.TimeDuration timeDuration) {
                 Log.d("TimeRangePicker", "Duration: " + timeDuration);

             }
         });


        bookBtn.setOnClickListener(v -> {
            title = titleET.getText().toString().trim();
            originator = originatorET.getText().toString().trim();

            if(title.isEmpty())
            {
                titleET.setFocusable(true);
                titleET.setError("Please fillup");
            }
            else if(originator.isEmpty())
            {
                originatorET.setFocusable(true);
                originatorET.setError("Please fillup");
            }
            else
            {
                try {
                    compareTimeRange();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    void fetchData()
    {
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


    }
    private void compareTimeRange() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("H:mm", Locale.getDefault());
        Log.d("BOOOOOKKKKKKKK", "compareTimeRange: " + startTime);
        Date dateC = format.parse(startTime);
        long timeInMillis=dateC.getTime();
        Time new_start_time = new Time(timeInMillis);
        dateC = format.parse(endTime);
        timeInMillis=dateC.getTime();
        Time new_end_time = new Time(timeInMillis);


        Log.d("BOOOOOKKKKKKKK", "compareTimeRange: " + new_start_time);
        Log.d("BOOOOOKKKKKKKK", "compareTimeRange: " + new_end_time);


            for (int i = 0; i < meetingList.size(); i++) {

                Log.d("COMPAREEEEE", "compareTimeRange: HEREEEE" +  meetingList.get(i).getDate());
                Log.d("COMPAREEEEE", "compareTimeRange: HEREEEE" +  date);
                if(Objects.equals(meetingList.get(i).getDate(), date))
                {
                    Log.d("COMPAREEEEE", "compareTimeRange: HEREEEE" );
                    Date start_time =  format.parse(meetingList.get(i).getStartTime());
                    Date end_time = format.parse(meetingList.get(i).getEndTime());

                    if((new_start_time.equals(start_time) || new_start_time.after(start_time)) && (new_start_time.before(end_time)
                            || new_start_time.equals(end_time)) || (new_end_time.after(start_time) || new_end_time.equals(start_time))
                            && (new_end_time.before(end_time) || new_end_time.equals(end_time)))
                    {
                        Toast.makeText(getApplicationContext(),"Choose a vacant time range",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.d("BOOOOOOOOKKKKKKKK", "compareTimeRange: HEEEERREEEEEEEE");
                        updateDatabase();
                    }
                }




        }



    }

    private void updateDatabase() {
            String newMeetingData = startTime+","+endTime+","+date+","+originator+","+title+",602";
        String line = "3:00PM,5:00PM,27/03/2024,Ananna,Standup,403";

        try {
            File csvFile = new File(getFilesDir(), "data.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true)); // Append mode

            // Write data to CSV

            writer.write(line);
            writer.newLine();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.d("MEETING_ACTIVITY", "writeToCSV: "+ e);
        }


        startActivity(new Intent(getApplicationContext(),MeetingActivity.class));

    }
}
