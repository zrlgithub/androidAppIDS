package com.example.ids;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bibliografie extends AppCompatActivity {

    private Button button;
    ArrayList<String> elm = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    CustomAdapter customAdapter;
    int poz;

    ArrayList<String> emptyListItems = new ArrayList<String>();
    ArrayAdapter<String> emptyAdapter;
    public static final String TAG ="MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibliografie);
        ListView listView = findViewById(R.id.listView);
        customAdapter = new CustomAdapter(this,elm); //////lab 6
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                elm);

        emptyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                emptyListItems);
        elm.add("https://www.fortinet.com/resources/cyberglossary/snort");
        elm.add("https://ro.wikipedia.org/wiki/Sistem_de_detectare_a_intruziunilor");
        elm.add("securitatea-retelelor.ro/sisteme-de-tip-ids-ips/");
        elm.add("https://www.rasfoiesc.com/educatie/informatica/Detectarea-intruziunilor57.php");
        customAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Bibliografie.this, elm.get(i).toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(elm.get(i).toString()));
                startActivity(intent);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l) {

                customAdapter.remove(i);


                if (elm.size() == 0) {
                    //  listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(null);
                    listView.setOnItemLongClickListener(null);
                }
                customAdapter.notifyDataSetChanged();
                return true;
            }
        });


        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openAct2();
            }
        });
    }
    private void openAct2() {
        Intent it = new Intent(this, HomeActivity.class);
        //startActivity(it);
        startActivityForResult(it,2);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG,"onStart()");


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG,"onPause()");
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActResult()");

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Bibliografie.this, elm.get(i).toString(), Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int i, long l) {

                customAdapter.remove(i);


                if (elm.size() == 0) {
                    //  listView.setAdapter(customAdapter);
                    listView.setOnItemClickListener(null);
                    listView.setOnItemLongClickListener(null);
                }
                customAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}

