package com.example.calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.calendar.db.Note;
import com.example.calendar.db.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "lq";
    public TextView tv_title,tv_text;
    public String id;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button buttonTime, buttonCalender;
    private Calendar calendar;
    public String time,calender;
    private ToggleButton star_button;
    UserService userService;
    private  int[] imagelist = {R.drawable.ic_uncheck,R.drawable.ic_checked};
    public String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        tv_title = findViewById(R.id.text3);
        tv_text = findViewById(R.id.text4);
        star_button = findViewById(R.id.btStar2);
        tag = getIntent().getStringExtra("tag");
        if(tag.equals("1")){
            star_button.setChecked(true);}
        else
            star_button.setChecked(false);
        String title = getIntent().getStringExtra("title");
        String text = getIntent().getStringExtra("text");
        id = getIntent().getStringExtra("id");
        tv_title.setText(title);
        tv_text.setText(text);
        calendar = Calendar.getInstance();
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy年mm月dd日");
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calender = year + "年" + month + "月" + dayOfMonth+"日";
        time = formatter2.format(calendar.getTime());
        buttonCalender = findViewById(R.id.btCalender2);
        buttonTime = findViewById(R.id.btTime2);
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
        init();
        buttonTime.setOnClickListener((View.OnClickListener) this);
        buttonCalender.setOnClickListener((View.OnClickListener) this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void init(){

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btTime2:
                showTimeDialog();
                break;
            case R.id.btCalender2:
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
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calender = year + "年" + (month + 1) + "月" + dayOfMonth+"日";
                Log.e(TAG, "calender : " + calender);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void save(View v){
        String new_title = String.valueOf(tv_title.getText());
        String new_text = String.valueOf(tv_text.getText());
        String new_time = calender+" "+time;
        System.out.println(new_title+new_text+new_time+id);
        userService = new UserService(EditorActivity.this);
        Note note = new Note();
        note.setTag(tag);
        note.setTitle(new_title);
        note.setText(new_text);
        note.setTime(new_time);
        note.setId(Integer.parseInt(id));
        userService.update(note);
    }
}