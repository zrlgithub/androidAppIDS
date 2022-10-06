package com.example.ids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Zeek extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    Button btnGoBack,buttonGM,buttonLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeek);

        btnGoBack = (Button) findViewById(R.id.btnGoBack);
        buttonGM = (Button) findViewById(R.id.buttonGM);

        buttonLocation = (Button) findViewById(R.id.buttonLocation);
        btnGoBack.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });
        buttonGM.setOnClickListener(view -> {
            Intent i = new Intent(this, GoogleMaps.class);
            startActivity(i);
        });
        buttonLocation.setOnClickListener(view -> {
            setContentView(new DesenPieCharts(this));
        });



        /*


        double y,x;
        x = -5.0;

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        series = new LineGraphSeries<>();
        for(int i =0; i<100; i++) {
            x = x + 0.1;
            y = Math.sin(x);
            series.appendData(new DataPoint(x, y), true, 100);
        }
        graph.addSeries(series);
    }

 */

       // setContentView(new DesenPieCharts(this));

}
}