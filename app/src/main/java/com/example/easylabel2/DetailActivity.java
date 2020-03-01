package com.example.easylabel2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    TextView heading;
    ArrayList<String> finalIngredientsList;
    ArrayList<String> compareList;
    TextView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        initializeArrayList();
        listView = findViewById(R.id.listView);
        listView.setTextColor(Color.WHITE);
        heading = findViewById(R.id.Heading);
        heading.setTextColor(Color.WHITE);
        finalIngredientsList = (ArrayList<String>) getIntent().getSerializableExtra("finalIngredientsList");
        putStuffinListView();


//        heading = findViewById(R.id.Heading);


    }

    public void putStuffinListView(){
        String result = "";
        for(int i = 0; i<compareList.size(); i++){
            result += compareList.get(i) + "\n\n";
        }
        listView.setText(result);
    }

    public void initializeArrayList(){
        compareList = new ArrayList<String>();
        this.compareList.add("Glycerine");
        this.compareList.add("Cetearyl Alcohol");
        this.compareList.add("Butyrosepermum");
        this.compareList.add("Parkii Butter ");
        this.compareList.add("Ethylhexyl Palmitate");
        this.compareList.add("Myristyl Myristate");
        this.compareList.add("Glyceryl Stearate");
        this.compareList.add("PEG-100 Stearate");
        this.compareList.add("Bertholletia Excelsa");
        this.compareList.add("Coco-Caprylate");
        this.compareList.add("Dimethicone");
        this.compareList.add("Panthenol");
        this.compareList.add("Parfum");




    }
}
