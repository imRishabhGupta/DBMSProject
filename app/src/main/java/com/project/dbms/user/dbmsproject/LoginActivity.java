package com.project.dbms.user.dbmsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText emailView;
    EditText passwordView;
    EditText usernameView;
    TextView alertView;
    DatabaseHandler db;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerButton=(Button)findViewById(R.id.register);
        emailView=(EditText)findViewById(R.id.email);
        passwordView=(EditText)findViewById(R.id.password);
        usernameView=(EditText)findViewById(R.id.username);
        alertView=(TextView)findViewById(R.id.alert);
        db=new DatabaseHandler(this);
        db.getAllUsers();

    }
    public void signIn(View v){

        usernameView.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        String email=emailView.getText().toString();
        String password=passwordView.getText().toString();
        User user=db.getUser(email);

        if(email.trim().length()==0||password.trim().length()==0){
            alertView.setVisibility(View.VISIBLE);
            alertView.setText("** Fields cannot be empty. **");
        }
        else if (user==null){
            alertView.setVisibility(View.VISIBLE);
            alertView.setText("** This email is not registered. **");
        }
        else if(email.equals(user.getEmail())&&!password.equals(user.getPassword())){
            alertView.setVisibility(View.VISIBLE);
            alertView.setText("** Incorrect password. Please try again! **");
        }
        else if(email.equals(user.getEmail())&&password.equals(user.getPassword())){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("data",user.getUsername());
            startActivity(intent);
        }

    }
    public void register(View v){

        usernameView.setVisibility(View.VISIBLE);
        Button register=(Button)findViewById(R.id.register);
        //Button signIn=(Button)findViewById(R.id.sign_in);
        //TextView text=(TextView)findViewById(R.id.text1);
        register.setVisibility(View.VISIBLE);
        //signIn.setVisibility(View.GONE);
        //text.setVisibility(View.GONE);

    }
    public void onRegister(View v){
        String email=emailView.getText().toString();
        System.out.println("see this "+email);
        String password=passwordView.getText().toString();
        String username=usernameView.getText().toString();
        User user=db.getUser(email);

        if(email.trim().length()==0||password.trim().length()==0||username.trim().length()==0){
            alertView.setVisibility(View.VISIBLE);
            alertView.setText("** All fields are required. **");
        }
        else if (user!=null){
            alertView.setVisibility(View.VISIBLE);
            alertView.setText("** Email is already registered. **");
        }
        else{
            user = new User(email, username, password);
            db.addUser(user);
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("data",user.getUsername());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
