package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;

public class ActivityObj1Step1 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step1.class.getName();

  private Button mBtnStart;
  private Button mBtnStop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();
  }

  @Override
  protected void onDestroy(){
    stop();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step1);

    mBtnStart = (Button) findViewById(R.id.btn_obj1_step1_start);
    mBtnStop = (Button) findViewById(R.id.btn_obj1_step1_stop);

    mBtnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnStart : onClick()");
        start();
      }
    });

    mBtnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnStop : onClick()");
        stop();
      }
    });
  }

  private void start() {

  }

  private void stop() {

  }
}
