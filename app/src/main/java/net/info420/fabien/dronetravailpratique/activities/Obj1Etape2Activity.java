package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

/**
 * Created by fabien on 17-02-20.
 */

public class Obj1Etape2Activity extends AppCompatActivity {

  public static final String TAG = Obj1Etape2Activity.class.getName();

  private Button mBtnStartMotors;
  private Button mBtnLand;
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

    DroneApplication.getDroneMover().enableVirtualStickMode();

    initUI();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    DroneApplication.getDroneMover().land();

    DroneApplication.getDroneMover().disableVirtualStickMode();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_etape2);

    mBtnStartMotors = (Button) findViewById(R.id.btn_obj1_step2_start_motors);
    mBtnLand        = (Button) findViewById(R.id.btn_obj1_step2_land);
    mBtnGoForward   = (Button) findViewById(R.id.btn_obj1_step2_go_forward);
    mBtnGoBack      = (Button) findViewById(R.id.btn_obj2_etape1_pitch_moins);
    mBtnGoLeft      = (Button) findViewById(R.id.btn_obj1_step2_go_left);
    mBtnGoRight     = (Button) findViewById(R.id.btn_obj1_step2_go_right);
    mBtnTurnRight   = (Button) findViewById(R.id.btn_obj1_step2_turn_right);
    mBtnTurnLeft    = (Button) findViewById(R.id.btn_obj2_etape1_tourner_gauche);

    mBtnStartMotors.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().startMotors();
      }
    });

    mBtnLand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().land();
      }
    });

    mBtnGoForward.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(goForward));
      }
    });

    mBtnGoBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(goBack));
      }
    });

    mBtnGoLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(goLeft));
      }
    });

    mBtnGoRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(goRight));
      }
    });

    mBtnTurnLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(turnLeft));
      }
    });

    mBtnTurnRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().move(DroneApplication.getDroneMover().getMovementTimer(turnRight));
      }
    });
  }
}