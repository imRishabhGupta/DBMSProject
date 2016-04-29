package com.project.dbms.user.dbmsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int UNLOCK=1;
    private static final int READ_LOCK=2;
    private static final int WRITE_LOCK=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHandler db=new DatabaseHandler(this);

        Intent intent=getIntent();
        String username=intent.getExtras().get("data").toString();

        TextView textView=(TextView)findViewById(R.id.welcome);
        textView.setText("Welcome "+username+",\nChoose an operation");
        final TextView read_value=(TextView)findViewById(R.id.read_value);

        final EditText write_value=(EditText)findViewById(R.id.write_value);

        final Button read=(Button)findViewById(R.id.read);
        Button write=(Button)findViewById(R.id.write);
        final Button confirm=(Button)findViewById(R.id.write_confirm);


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getLock()==WRITE_LOCK){
                    Toast.makeText(getApplicationContext(),"The value is being written. Wait to read updated value",Toast.LENGTH_SHORT).show();
                    //        try {
                      //          Thread.sleep(5000);
                        //    }catch (Exception e){}
                            db.changeLock(READ_LOCK);
                            read_value.setVisibility(View.VISIBLE);
                            read_value.setText("Value is "+db.getValue());
                            db.changeLock(getRandomNumber());
                        }
                else {
                    db.changeLock(READ_LOCK);
                    read_value.setVisibility(View.VISIBLE);
                    read_value.setText("Value is " + db.getValue());
                    db.changeLock(getRandomNumber());
                }

            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getLock()!=UNLOCK){
                    Toast.makeText(getApplicationContext(),"The value is being read or written. Wait to write.",Toast.LENGTH_SHORT).show();

                            //try {
                              //  Thread.sleep(8000);
                            //}catch (Exception e){}
                            db.changeLock(WRITE_LOCK);
                            write_value.setVisibility(View.VISIBLE);
                            read_value.setVisibility(View.GONE);
                            read.setVisibility(View.GONE);
                            confirm.setVisibility(View.VISIBLE);
                }
                else {
                    db.changeLock(WRITE_LOCK);
                    write_value.setVisibility(View.VISIBLE);
                    read_value.setVisibility(View.GONE);
                    read.setVisibility(View.GONE);
                    confirm.setVisibility(View.VISIBLE);
                    db.changeLock(getRandomNumber());
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value= write_value.getText().toString();
                if(Integer.parseInt(value)<0){
                    Toast.makeText(getApplicationContext(),"Please enter some valid value",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(value)>Integer.parseInt(db.getValue())){
                    Toast.makeText(getApplicationContext(),"Insufficient balance. Try again.",Toast.LENGTH_LONG).show();
                }
                else{
                    db.changeValue(Integer.parseInt(value));
                    Toast.makeText(getApplicationContext(),"Value changed successfully.",Toast.LENGTH_LONG).show();
                    write_value.setVisibility(View.GONE);
                    read.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.GONE);
                    db.changeLock(getRandomNumber());
                }
            }
        });

    }
    int getRandomNumber(){
        Random r=new Random();
        return r.nextInt(3)+1;
    }
}
