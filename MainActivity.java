package com.example.chatroom;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Button bt;
    EditText et1,et2;
    TextView tv;
    String s1,s2,s3;
    ServerConnection sc;
    JSONArray ta;
    int msgno,msgno_org,i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button)findViewById(R.id.button);
        et1 = (EditText)findViewById(R.id.name);
        et2 = (EditText)findViewById(R.id.message);
        tv = (TextView)findViewById(R.id.output);
        sc = new ServerConnection();
        bt.setOnClickListener(b1cl);
        Timer timer01 =new Timer();
        timer01.schedule(task, 0,3000);
    }
    private TimerTask task = new TimerTask(){
        public void run() {
            Thread t2 = new Thread(r2);
            t2.start();
        }
    };
    Handler h1 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                s3 = "";
                msgno = ta.length();
                if (msgno > msgno_org) {
                    try {
                        for (i = 0; i <= ta.length(); i++) {
                            s3 = s3 + ta.getJSONObject(i).get("A") + ta.getJSONObject(i).get("F") + "\n";
                            tv.setText(s3);
                        }
                    } catch (JSONException e) {
                    }
                    ;
                }
            }
            super.handleMessage(msg);
        }
    };
    private Runnable r2 = new Runnable()
    {
        public void run()
        {
            ta = sc.query("imf00","imf00","A,F","id>0");
            Message message = new Message();
            message.what = 1;
            h1.sendMessage(message);
        }
    };
    private Runnable r1 = new Runnable()
    {
        public void run()
        {
            sc.insert("imf00", "imf00", "A,F" , "'"+s1+"','"+s2+"'");
        }
    };
    private View.OnClickListener b1cl =new View.OnClickListener(){
        public void onClick(View v) {
            s1 = et1.getText().toString();
            s2 = et2.getText().toString();
            et2.setText("");
            if(!s1.equals("")&&!s2.equals(""))
            {
                Thread t1 = new Thread(r1);
                t1.start();
            }
        }
    };
}