package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
                bookRoom();
            }
        });
        
    }

    private void bookRoom() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.book_room_alert_dialog, null);

        EditText titleET;
        EditText originatorET;
        titleET = view.findViewById(R.id.titleMeetingETId);
        originatorET = view.findViewById(R.id.originatorMeetingETId);

        ImageButton datePicker;
        datePicker = view.findViewById(R.id.datePickerId);

        ImageButton startTimePicker;
        startTimePicker = view.findViewById(R.id.timePickerId);
        ImageButton endTimePicker;
        endTimePicker = view.findViewById(R.id.timePickerId2);

        TextView dateTv;
        dateTv = view.findViewById(R.id.dateTvId);
        TextView startTimeTv;
        startTimeTv = view.findViewById(R.id.startTimeTvId);
        TextView endTimeTv;
        endTimeTv = view.findViewById(R.id.endTimeTvId);

        Button bookBtn;
        bookBtn = view.findViewById(R.id.bookBtnId);
        Button cancelBtn;
        cancelBtn = view.findViewById(R.id.cancelBtnId);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setView(view)
        .create();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MeetingActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);

                        String dateText = DateFormat.format("dd/MM/yyyy", calendar1).toString();
                        String yearS = DateFormat.format("yyyy",calendar1).toString();
                        String monthS = DateFormat.format("MM",calendar1).toString();
                        String dayS = DateFormat.format("dd",calendar1).toString();
                        dateTv.setText(dateText);

                    }
                }, YEAR, MONTH, DATE);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis()+1000*60*60*24);  //+1000*60*60*24
                datePickerDialog.show();
            }
        });

        startTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Format the selected time
                                String formattedTime = formatTime(hourOfDay, minute);
                                startTimeTv.setText(formattedTime);
                            }
                        }, hour, minute, false);
                // Show time picker dialog
                timePickerDialog.show();
            }
        });

        endTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Format the selected time
                                String formattedTime = formatTime(hourOfDay, minute);
                                endTimeTv.setText(formattedTime);
                            }
                        }, hour, minute, false);
                // Show time picker dialog
                timePickerDialog.show();
            }
        });

        alertDialog.show();

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