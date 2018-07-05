package com.example.hp.tcp_client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Socket socket;

    private static final int SERVERPORT = 8030;
    private static final String SERVER_IP = "192.168.1.3";


    String x;
    String str;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);

        new Thread(new ClientThread()).start();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    public void onClick(View view) {
        try {
            EditText et = (EditText) findViewById(R.id.EditText01);
            String str = et.getText().toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(str);
            new LongOperation().execute("");
            et.setText("");

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("TAG", e.getMessage());
        }
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                InputStreamReader is = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(is);
                //String x;
                while ((x = (br.readLine())) != null) {
                    Toast.makeText(getApplicationContext(), x, Toast.LENGTH_LONG).show();

                    //print the input to the application screen
                    final TextView receivedMsg = (TextView) findViewById(R.id.textview);
                    receivedMsg.setText(br.readLine());
                }
            } catch (Exception e) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), x, Toast.LENGTH_LONG).show();
            textView.setText(x);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
