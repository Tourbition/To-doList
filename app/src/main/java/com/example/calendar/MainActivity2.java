package com.example.calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.calendar.db.Note;
import com.example.calendar.db.UserService;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    private static final String TAG = "lq";
    private ArrayList<HashMap<String, Object>> noteList;
    UserService userService;
    private SimpleAdapter listItemAdapter;
    public TextView tv_id;
    public String id2;
    GridView gridview;
    private  int[] imagelist = {R.drawable.ic_uncheck,R.drawable.ic_checked};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        gridview = (GridView) findViewById(R.id.grid);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }
    private void initView(){
        noteList = new ArrayList<>();
        userService = new UserService(MainActivity2.this);
        for(Note note : userService.listAll()){
            HashMap<String, Object>map = new HashMap<>();
            map.put("id",String.valueOf(note.getId()));
            map.put("image",imagelist[Integer.parseInt(note.getTag())]);
            map.put("tag",note.getTag());
            map.put("title", note.getTitle());
            map.put("text",note.getText());
            map.put("time", note.getTime());
//            System.out.println(note.getTime());
            noteList.add(map);
        }
        listItemAdapter = new SimpleAdapter(this, noteList,
                R.layout.list_item2,
                new String[] { "image","tag","title","text", "time","id" },
                new int[] { R.id.tv_star,R.id.tv_tag,R.id.tv_title2, R.id.tv_text2,R.id.tv_time2,R.id.tv_id }
        );

        gridview.setAdapter(listItemAdapter);
        gridview.setOnItemClickListener(this);
        gridview.setOnItemLongClickListener(this);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView title = (TextView) view.findViewById(R.id.tv_title2);
        TextView text = (TextView) view.findViewById(R.id.tv_text2);
        TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
        TextView tv_tag = view.findViewById(R.id.tv_tag);
        String title2 = String.valueOf(title.getText());
        String text2 = String.valueOf(text.getText());
        String tag2 = String.valueOf(tv_tag.getText());
        id2 = String.valueOf(tv_id.getText());
        System.out.println(id2);
        Intent editor = new Intent(this,EditorActivity.class);
        editor.putExtra("id",id2);
        editor.putExtra("tag",tag2);
        editor.putExtra("title",title2);
        editor.putExtra("text",text2);
        startActivity(editor);
    }
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("Are you sure to delete this note?").setPositiveButton("Yes",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                noteList.remove(position);
                userService.delete(Integer.parseInt(id2));
                listItemAdapter.notifyDataSetChanged();
                onResume();
            }
        }).setNegativeButton("No",null);
        builder.create().show();
        Log.i(TAG, "onItemLongClick: size=" + noteList.size());

        return true;
    }
}