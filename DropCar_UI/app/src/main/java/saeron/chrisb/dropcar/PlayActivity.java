package saeron.chrisb.dropcar;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class PlayActivity extends AppCompatActivity {

    VideoView vv;
    Socket sockt = null;
    DataInputStream is;
    DataOutputStream os;
    ImageView ivPad;
    ImageButton ibJog;
    private float x,y;
    Button btCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        if(!LibsChecker.checkVitamioLibs(this)) return;

        vv = (VideoView)findViewById(R.id.vitamio_videoView);
        btCam = (Button)findViewById(R.id.turn);
       ibJog = (ImageButton)findViewById(R.id.jog);
        x = 280;
        y = 870;
        ibJog.setOnTouchListener(ibJogOnTouch);
        btCam.setOnClickListener(btCamOnCilck);
       // vv.setOnClickListener(vvOnClickListener);

        Video_Start();

        MyThread mMyThread = (MyThread) new MyThread();// 쓰레드 이름과 생성
        mMyThread.execute();

        //SendCommand((byte)0x88,(byte)0x00,(byte)0x01,(byte)0x00);
    }

    void delay(int n) {
        for (int i = 0; i < n; i++)
        {
            for(int j = 0; j < 1000; j++);
        }
    }
    int flag = 1;
    View.OnClickListener btCamOnCilck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


                    if(flag == 0) {
                        SendCommand((byte) 0x88, (byte) 0x00, (byte) 0x01, (byte) 0x00);
                        flag = 1;
                    }
                    else if(flag == 1)
                    {
                        SendCommand((byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x00);
                        flag = 0;
                    }

        }
    };

    private byte CheckSum(byte[] data, int offset, int len)
    {
        byte sum = 0;
        int t = offset + len;
        for(int i = offset; i < t; i++)
            sum += data[i];
        return sum;
    }
    void SendCommand(byte cmd1, byte cmd2, byte data1, byte data2)
    {
        byte[] msg = {(byte)0xFF, 0x01, cmd1, cmd2, data1, data2, 0x00};
        msg[6] = CheckSum(msg,1,5);

        try
        {
            os.write(msg,0,7);
            os.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    byte dir = 0x00;
    byte speed = 0x00;
    boolean jog_on = false;

    View.OnTouchListener ibJogOnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(x > 100 && x < 460) x = x + event.getX();
                if(y > 710 && y < 1030)   y = y + event.getY();
                if (x > 460) x = 459;
                if (y > 1030) y = 1029;
                if (x < 100) x = 101;
                if (y < 710) y = 711;
                if (x < 200) {
                    //writer.WriteLine ("@FML");
                    //writer.Flush ();
                    dir += 0x04;

                    //SendCommand((byte)0x00, (byte)0x04, (byte)0x00, (byte)0x00);
                }
                if (x > 360) {
                    //writer.WriteLine ("@FMR");
                    //writer.Flush ();
                    dir += 0x02;

                    //SendCommand((byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00);
                }

                if (y < 870)
                {
                    if (y > 780 && y < 870) //speed 1
                    {
                        //writer.WriteLine ("@RMS");
                        //writer.Flush ();
                        dir += 0x10;
                        speed = 0x01;
                        //SendCommand((byte)0x00, (byte)0x10, (byte)0x00, (byte)0x01);
                    }
                    if (y > 760 && y < 780) //speed 3
                    {
                        //writer.WriteLine ("@RMF1");
                        //writer.Flush ();
                        dir += 0x10;
                        speed = 0x03;
                        //SendCommand((byte)0x00, (byte)0x10, (byte)0x00, (byte)0x03);
                    }
                    if (y > 740 && y < 760) // speed 5
                    {
                        //writer.WriteLine ("@RMF2");
                        //writer.Flush ();
                        dir += 0x10;
                        speed = 0x05;
                        //SendCommand((byte)0x00, (byte)0x10, (byte)0x00, (byte)0x05);
                    }
                    if (y > 720 && y < 740) // speed 6
                    {
                        //writer.WriteLine ("@RMF3");
                        //writer.Flush ();
                        dir += 0x10;
                        speed = 0x06;
                        //SendCommand((byte)0x00, (byte)0x10, (byte)0x00, (byte)0x06);
                    }
                    if (y > 700 && y < 720) // speed7
                    {
                        // writer.WriteLine ("@RMF4");
                        //writer.Flush ();
                        dir += 0x10;
                        speed = 0x07;
                        //SendCommand((byte)0x00, (byte)0x10, (byte)0x00, (byte)0x07);
                    }
                }
                if (y > 870)
                {
                    if (y > 870 && y < 940) //speed1
                    {
                        //writer.WriteLine ("@RMS");
                        //writer.Flush ();
                        dir += 0x08;
                        speed = 0x01;
                        //SendCommand((byte)0x00, (byte)0x08, (byte)0x00, (byte)0x01);
                    }
                    if (y > 940 && y < 960) // speed 3
                    {
                        //writer.WriteLine ("@RMB1");
                        //writer.Flush ();
                        dir += 0x08;
                        speed = 0x03;
                        //SendCommand((byte)0x00, (byte)0x08, (byte)0x00, (byte)0x03);
                    }
                    if (y > 960 && y < 980) //speed 5
                    {
                        //writer.WriteLine ("@RMB2");
                        //writer.Flush ();
                        dir += 0x08;
                        speed = 0x05;
                        //SendCommand((byte)0x00, (byte)0x08, (byte)0x00, (byte)0x05);
                    }
                    if (y > 980 && y < 1000) // speed6
                    {
                        // writer.WriteLine ("@RMB3");
                        //writer.Flush ();
                        dir += 0x08;
                        speed = 0x06;
                        //SendCommand((byte)0x00, (byte)0x08, (byte)0x00, (byte)0x06);
                    }
                    if (y > 1000 && y < 1030) // speed7
                    {
                        // writer.WriteLine ("@RMB4");
                        //writer.Flush ();
                        dir += 0x08;
                        speed = 0x07;
                        //SendCommand((byte)0x00, (byte)0x08, (byte)0x00, (byte)0x07);
                    }
                }
                SendCommand((byte)0x00, (byte)dir, (byte)0x00, (byte)speed);
                dir = 0x00;
                ibJog.setX(x);
                ibJog.setY(y);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                ibJog.setX (280);
                ibJog.setY (870);
                x = 280;
                y = 870;
                SendCommand((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00);
                //stop
                //writer.WriteLine ("@RMS");
                //writer.Flush ();
                //writer.WriteLine ("@FMS");
                //writer.Flush ();
            }
            //tvText.setText(String.format("X:%02f,Y:%02f}",x,y));

            return true;
        }
    };

    private class MyThread extends AsyncTask<Void, Integer,Void>
    {
        protected  Void doInBackground(Void... p)
        {
            try {
                sockt = new Socket("192.168.0.14", 9000);

                is = new DataInputStream(sockt.getInputStream());
                os = new DataOutputStream(sockt.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

       // protected void onProgressUpdate(Integer[] values)
       // {
       //     switch(values[0])
        //}
    }
/*
    View.OnClickListener vvOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sockt = new Socket("192.168.0.90", 1081);

                        is = new DataInputStream(sockt.getInputStream());
                        os = new DataOutputStream(sockt.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };*/

    void Video_Start() {
        vv.setVideoLayout(2,0);
        //vv.setVideoPath("rtsp://192.168.0.90/axis-media/media.amp");
        //vv.setVideoPath("rtsp://192.168.0.123:554/mpeg4");
        vv.setVideoPath("rtsp://192.168.0.15:8554/test");
        vv.setBufferSize(1);
        vv.start();
        vv.seekTo(50);
        vv.setVisibility(View.GONE);
        vv.setVisibility(View.VISIBLE);
    }
}
