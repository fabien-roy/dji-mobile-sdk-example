package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

/**
 * Created by fabien on 17-02-20.
 */

public class ActivityObj1Step2 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step2.class.getName();

  private Button mBtnStartMotors;
  private Button mBtnGoForward;
  private Button mBtnGoBack;
  private Button mBtnGoLeft;
  private Button mBtnGoRight;
  private Button mBtnTurnRight;
  private Button mBtnTurnLeft;

  // private CountDownTimer movementTimer;
  // private MovementTimerTask movementTimerTask;

  // Ceci a été fait avec un tableau sur le site de DJI : https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-flightController.html (Roll Pitch Control Mode)
  // Important à savoir, on se sert du mode Velocity et Body
  // Rappelez vous, c'est en m/s (et °/s pour le Yaw). Mon timer dure en fonction.
  //                              p    r    y    t

  private float[] goForward = {   0,   1,   0,   0};
  private float[] goBack    = {   0,  -1,   0,   0};
  private float[] goLeft    = {  -1,   0,   0,   0};
  private float[] goRight   = {   1,   0,   0,   0};
  private float[] turnLeft  = {   0,   0,  15,   0};
  private float[] turnRight = {   0,   0, -15,   0};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    ApplicationDrone.getDroneMover().enableVirtualStickMode();

    initUI();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    ApplicationDrone.getDroneMover().land();

    ApplicationDrone.getDroneMover().disableVirtualStickMode();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step2);

    mBtnStartMotors = (Button) findViewById(R.id.btn_obj1_step2_start_motors);
    mBtnGoForward   = (Button) findViewById(R.id.btn_obj1_step2_go_forward);
    mBtnGoBack      = (Button) findViewById(R.id.btn_obj1_step2_go_back);
    mBtnGoLeft      = (Button) findViewById(R.id.btn_obj1_step2_go_left);
    mBtnGoRight     = (Button) findViewById(R.id.btn_obj1_step2_go_right);
    mBtnTurnRight   = (Button) findViewById(R.id.btn_obj1_step2_turn_right);
    mBtnTurnLeft    = (Button) findViewById(R.id.btn_obj1_step2_turn_left);

    mBtnStartMotors.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().startMotors();
      }
    });

    mBtnGoForward.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(goForward);
      }
    });

    mBtnGoBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(goBack);
      }
    });

    mBtnGoLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(goLeft);
      }
    });

    mBtnGoRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(goRight);
      }
    });

    mBtnTurnLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(turnLeft);
      }
    });

    mBtnTurnRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().move(turnRight);
      }
    });
  }
}