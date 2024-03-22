package com.home.onetothree_countrys_currency_convert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public Button enterConverter;
    public Button enterChanged;
    public Button enterAboutUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView() {
        enterConverter=findViewById(R.id.enterConverter);
        enterChanged=findViewById(R.id.enterChanged);
        enterAboutUse=findViewById(R.id.enterAboutUse);

        enterConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Converter_Page.class);
                startActivity(intent);
            }
        });

        enterChanged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        enterAboutUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}