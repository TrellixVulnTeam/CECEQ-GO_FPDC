package com.example.ceceqgo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;


public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //The thread of the Splashscreen is declared
        Thread splashscreen=new Thread(){
            @Override
            public void run() {
                try {
                    //The duration is set to 3 seconds
                    sleep(3000);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //The splashcreen is initiated
        splashscreen.start();
    }
}

