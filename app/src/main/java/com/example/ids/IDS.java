package com.example.ids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IDS extends AppCompatActivity {

    private static final String TAG ="mytag" ;
    Button b_load;
    TextView tv_output;
    Button b_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ids);

        b_load = (Button) findViewById(R.id.buttonLoad);
        tv_output = (TextView) findViewById(R.id.tv_output);
        b_back = findViewById(R.id.buttonToBack);
        TableLayout tableStats = (TableLayout) findViewById(R.id.tableStats);

        @SuppressLint("SdCardPath") String path = "/sdcard/ids/";
        Log.d(TAG, "ids : "+ path);

            //req permisi
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1000);
        }

        b_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFilesSerch();
            }
        });


        b_back.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });


        ///XML PARSER

        XMLParser xmlParser=new XMLParser(){
            @Override
            protected void onPostExecute(List<ZeekXML> rates) {
                ListView listView = findViewById(R.id.lv);
                List<String> rateSchimb=new ArrayList<>();
                for(ZeekXML rate:rates){
                    rateSchimb.add(rate.toString());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,rateSchimb);
                listView.setAdapter(adapter);
            }
        };
        xmlParser.execute("https://www.xml-sitemaps.com/download/docs.zeek.org-2dea77a83/sitemap.xml?view=1");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permision granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No permision", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (requestCode == 10)
        {
            if(it!=null)
            {
                Uri uri = it.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":")+1);
                if(path.contains("emulated"))
                {
                    path=path.substring(path.indexOf("0")+1);
                }
                tv_output.setText(readFileData(path));
                Toast.makeText(this, readFileData(path), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void performFilesSerch()
    {
        Intent it = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.setType("text/*");
        startActivityForResult(it,10);
    }


    private void fillView(View view, String dataset1Value, String dataset2Value, String dataset3Value) {
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);

        textView1.setText(dataset1Value);
        textView2.setText(dataset2Value);
        textView3.setText(dataset3Value);
    }

    private String readFileData(String filePath) {
        List<String> values = new ArrayList<>();
        StringBuilder text = new StringBuilder();
        try {
            filePath = "storage/13F0-3210/"+ filePath;
            BufferedReader fileBufferedReader = new BufferedReader(new FileReader(new File(filePath)));
            Log.d(TAG, fileBufferedReader.toString());
            String value;
            while ((value = fileBufferedReader.readLine()) != null) {
                Log.d(TAG, value);
                text.append(value);
                text.append("\n");
            }
            fileBufferedReader.close();
            text.append("{\n" +
                    "  \"ts\": 1591367999.512593,\n" +
                    "  \"uid\": \"C5bLoe2Mvxqhawzqqd\",\n" +
                    "  \"id.orig_h\": \"192.168.4.76\",\n" +
                    "  \"id.orig_p\": 46378,\n" +
                    "  \"id.resp_h\": \"31.3.245.133\",\n" +
                    "  \"id.resp_p\": 80,\n" +
                    "  \"trans_depth\": 1,\n" +
                    "  \"method\": \"GET\",\n" +
                    "  \"host\": \"testmyids.com\",\n" +
                    "  \"uri\": \"/\",\n" +
                    "  \"version\": \"1.1\",\n" +
                    "  \"user_agent\": \"curl/7.47.0\",\n" +
                    "  \"request_body_len\": 0,\n" +
                    "  \"response_body_len\": 39,\n" +
                    "  \"status_code\": 200,\n" +
                    "  \"status_msg\": \"OK\",\n" +
                    "  \"tags\": [],\n" +
                    "  \"resp_fuids\": [\n" +
                    "    \"FEEsZS1w0Z0VJIb5x4\"\n" +
                    "  ],\n" +
                    "  \"resp_mime_types\": [\n" +
                    "    \"text/plain\"\n" +
                    "  ]\n" +
                    "}");
        } catch (IOException ignore) {

            Log.d(TAG, ignore.toString());

        }

        return text.toString();
    }
}

