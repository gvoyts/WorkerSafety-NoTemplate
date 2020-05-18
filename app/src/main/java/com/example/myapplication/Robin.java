package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class Robin extends AppCompatActivity {

    public TextView message;
    public Connection con;
    public Button run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robin);

        configureBackButton();

    run = (Button) findViewById(R.id.button);
    run.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckLogin checkLogin = new CheckLogin();
            checkLogin.execute("");
        }
    });
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPreExecute() {
            //something
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(Robin.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess) {
                message = (TextView) findViewById(R.id.textView2);
                message.setText(name1);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                con = connectionClass();
                if (con == null) {
                    z = "internet access might be rough rn buddy";
                } else {
                    String query = "Select * from SalesLT.Address";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        name1 = rs.getString("City");
                        z = "query successful good job";
                        isSuccess = true;
                        con.close();
                    } else {
                        z = "invalid query :(";
                        isSuccess = false;
                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error oof", z);
            }
            return z;
        }
    }

    private void configureBackButton() {
        Button backButton = (Button) findViewById(R.id.backtomainrobin);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view ){
                finish();
            }
        });

    }

    @SuppressLint("NewApi")
    public Connection connectionClass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection conn = null;
        String ConnURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://teamtech-sqlserver.database.windows.net:1433;DatabaseName=TeamTechSqlDatabase;user=teamtech@teamtech-sqlserver;password=PASSWORDHERE;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

            conn = DriverManager.getConnection(ConnURL);



        }catch (SQLException se){
            Log.e("ERROR", se.getMessage());
        }catch (ClassNotFoundException e){
            Log.e("ERROR", e.getMessage());
        }catch(Exception e){
            Log.e("ERROR", e.getMessage());
        }
        //Log.d("conn",conn.toString());
        return conn;
    }

}
