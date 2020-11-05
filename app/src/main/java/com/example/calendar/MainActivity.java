package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.calendar.db.Note;
import com.example.calendar.db.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "lq";
    private Button buttonTime, buttonCalender,buttonSubmit;
    public TextView textView,textView2;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private ArrayList<Note> noteList;
    private SimpleAdapter listItemAdapter;
    private String tag = "0";
//    private String[] testData = {"one","two","three"};
    public String time,calender;
    private ToggleButton star_button;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calender = year + "年" + month + "月" + dayOfMonth+"日";
//        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        time = hourOfDay + ":" + minute;
        time = formatter2.format(calendar.getTime());
        buttonTime = findViewById(R.id.btTime);
        buttonCalender = findViewById(R.id.btCalender);
        buttonSubmit = findViewById(R.id.btSubmit);
        star_button = findViewById(R.id.btStar);
        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.text2);
        buttonTime.setOnClickListener((View.OnClickListener) this);
        buttonCalender.setOnClickListener((View.OnClickListener) this);
        buttonSubmit.setOnClickListener((View.OnClickListener) this);
        ActionBar actionBar = getSupportActionBar();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initView();
    }
    class StarInfo
    {
        String time;
        String title;
        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public void set(String key,String value){
            if(key.equals("time"))
                time = value;
            if(key.equals("title"))
                title = value;
        }
    }
    private void initView(){
        noteList = new ArrayList<Note>();
        userService = new UserService(MainActivity.this);
        star_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){//开
                    tag = "1";
                }
                else{//关
                    tag = "0";
                }
            }
        });
        for(Note note : userService.listStar()){
            Note temp = new Note();
            temp.setTitle(note.getTitle());
            temp.setTime(note.getTime());
//            System.out.println(note.getTime());
            noteList.add(temp);
        }
        Collections.sort(noteList, new Comparator<Note>() {
            public int compare(Note t1, Note t2) {
                // TODO Auto-generated method stub
                //定义比较大小
                if(t1.getTime().compareTo(t2.getTime())>0)
                {
                    return 1;
                }
                else if(t1.getTime().compareTo(t2.getTime())==0)
                {
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
        });
        ArrayList<HashMap<String,String>> noteList2 = parseMap(noteList);
        listItemAdapter = new SimpleAdapter(this, noteList2,
                R.layout.list_item,
                new String[] { "title", "time" },
                new int[] { R.id.tv_title, R.id.tv_time }
        );
        ListView listview=(ListView)findViewById(R.id.list);
        listview.setAdapter(listItemAdapter);
    }
    public ArrayList<HashMap<String, String>> parseMap(ArrayList<Note> list) {
        ArrayList<HashMap<String,String>> list2 = new ArrayList<>();
        for(Note note : list) {
            HashMap map = new HashMap<String,String>();
            map.put("title",note.getTitle());
            map.put("time",note.getTime());
            list2.add(map);
            }
        return list2;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSubmit:
                submit();
                break;
            case R.id.btTime:
                showTimeDialog();
                break;
            case R.id.btCalender:
                showCalenderDialog();
                break;
        }
    }

    private void showTimeDialog() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = hourOfDay + ":" + minute;//人工校准
                Log.e(TAG, "time : " + time);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showCalenderDialog() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calender = year + "年" + (month + 1) + "月" + dayOfMonth+"日";
                Log.e(TAG, "calender : " + calender);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void open(View v){
        startActivity(new Intent(this, MainActivity2.class));
    }
    protected void onResume() {
        super.onResume();
        initView();
    }
    public void submit(){
        Note note = new Note();
//        UserService userService = new UserService(MainActivity.this);
        note.setTag(tag);
        note.setTitle(String.valueOf(textView.getText()));
        note.setText(String.valueOf(textView2.getText()));
        String date = calender+" "+time;
        note.setTime(date);
        userService.add(note);
        Toast toast=Toast.makeText(getApplicationContext(),"Save successfully", Toast.LENGTH_SHORT);
        toast.show();
        onResume();
    }
}
