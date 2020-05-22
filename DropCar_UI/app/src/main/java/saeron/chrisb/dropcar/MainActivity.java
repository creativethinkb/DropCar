package saeron.chrisb.dropcar;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText etIP, etPort;
    Button btConnect, btPlay;
    //ImageView ivUp, ivDown, ivLeft, ivRight;
    //Socket sock;

    private String html = "";

   // DataInputStream is;
   // DataOutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        etIP = (EditText)findViewById(R.id.IP);
        etPort = (EditText)findViewById(R.id.Port);
       // btConnect = (Button)findViewById(R.id.Connect);
        btPlay = (Button)findViewById(R.id.Play);
       // ivUp = (ImageView)findViewById(R.id.Up);
      //  ivDown = (ImageView)findViewById(R.id.Down);
      //  ivLeft = (ImageView)findViewById(R.id.Left);
     //   ivRight = (ImageView)findViewById(R.id.Right);

       // btConnect.setOnClickListener(btConnectOnClickListener);
        btPlay.setOnClickListener(btPlayOnClickListener);
     //   ivUp.setOnTouchListener(ivUpOnTouchListener);
     //   ivRight.setOnTouchListener(ivRightOnTouchListener);
     //   ivDown.setOnTouchListener(ivDownOnTouchListener);
     //   ivLeft.setOnTouchListener(ivLeftOnTouchListener);


    }

    OnClickListener btPlayOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(getApplicationContext(), PlayActivity.class));
           //ntent PlayActivity = new Intent(getApplicationContext(), PlayActivity.class);
           //startService(PlayActivity);
        }

    };
    /*
    OnTouchListener ivLeftOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    byte[] cmd = { (byte)0xFF, 0x01, 0x00, 0x02, 0x10, 0x00, 0x13};
                    try
                    {
                        os.write(cmd,0,7);
                        os.flush();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    byte[] cmdd = {(byte) 0xFF, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01};
                    try {
                        os.write(cmdd, 0, 7);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return true;
        }
    };

    OnTouchListener ivDownOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    byte[] cmd = { (byte)0xFF, 0x01, 0x00, 0x08, 0x00, 0x05, 0x0E};
                    try
                    {
                        os.write(cmd,0,7);
                        os.flush();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    byte[] cmdd = {(byte) 0xFF, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01};
                    try {
                        os.write(cmdd, 0, 7);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return true;
        }
    };

    View.OnTouchListener ivRightOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                byte[] cmd = { (byte)0xFF, 0x01, 0x00, 0x04, 0x10, 0x00, 0x15};
                try
                {
                    os.write(cmd,0,7);
                    os.flush();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                break;

                case MotionEvent.ACTION_UP:
                byte[] cmdd = {(byte) 0xFF, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01};
                try {
                    os.write(cmdd, 0, 7);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            return true;
        }
    };

    View.OnTouchListener ivUpOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    byte[] cmd = { (byte)0xFF, 0x01, 0x00, 0x10, 0x00, 0x05, 0x16};
                    try
                    {
                        os.write(cmd,0,7);
                        os.flush();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    byte[] cmdd = {(byte) 0xFF, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01};
                    try {
                        os.write(cmdd, 0, 7);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return true;
        }
    };
*/
    /*
    OnClickListener btConnectOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View arg0)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        sock = new Socket(etIP.getText().toString(),Integer.parseInt(etPort.getText().toString()));

                        is=new DataInputStream(sock.getInputStream());
                        os=new DataOutputStream(sock.getOutputStream());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                    while(true)
                    {
                        try
                        {
                            html = is.readUTF();

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    // tvResponse.setText(html);
                                }
                            });
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    };*/
}
