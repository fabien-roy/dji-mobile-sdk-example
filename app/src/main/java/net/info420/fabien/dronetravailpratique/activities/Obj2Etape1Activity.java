package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

// TODO : Documenter Obj2Etape1Activity

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/gimbal/GimbalCapabilityView.java">
 *   Bouger le Gimbal</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-gimbal.html">
 *   Informations sur le gimbal</a>}
 */
public class Obj2Etape1Activity extends AppCompatActivity {
  public static final String TAG = Obj2Etape1Activity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneHelper().atterir();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj2_etape1);

    findViewById(R.id.btn_obj2_etape1_connecter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO : Vieux, ne sert plus
      }
    });

    findViewById(R.id.btn_obj2_etape1_pitch_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(-15, 0, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_pitch_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(15, 0, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_roll_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(0, -15, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_roll_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(0, 15, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_yaw_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(0, 0, -15);
      }
    });

    findViewById(R.id.btn_obj2_etape1_yaw_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DroneApplication.getGimbalHelper().bougerGimbal(0, 0, 15);
      }
    });
  }
}
