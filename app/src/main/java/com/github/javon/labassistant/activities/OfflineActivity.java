package com.github.javon.labassistant.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.helpers.OfflineGrade;

import java.util.ArrayList;
import java.util.List;

public class OfflineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        List<OfflineGrade> offlineGrades = OfflineGrade.listAll(OfflineGrade.class);

        List<String> grades = new ArrayList<>();
        for(OfflineGrade offlineGrade: offlineGrades)
        {
            grades.add(offlineGrade.idNumber+"-"+offlineGrade.grade);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,grades);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
