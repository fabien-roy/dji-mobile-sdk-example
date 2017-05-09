package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * @see <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/gimbal/GimbalCapabilityView.java"
 *      target="_blank">
 *      Source : Bouger le Gimbal</a>
 * @see <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-gimbal.html"
 *      target="_blank">
 *      Source : Informations sur le gimbal</a>
 */
public class Obj2Etape1Activity extends AppCompatActivity {
  public static final String TAG = Obj2Etape1Activity.class.getName();

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appelle {@link #initUI()}</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see #initUI()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  /**
   * Exécuté à la fermetture de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Fait attérir le drone</li>
   * </ul>
   *
   * @see DroneHelper#atterir()
   */
  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneHelper().atterir();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * <p>Les listeners des boutons pour bouger le Gimbal se servent de
   * {@link net.info420.fabien.dronetravailpratique.helpers.GimbalHelper#bougerGimbal(int, int, int)},
   * avec les paramètres appropriés.</p>
   *
   * @see net.info420.fabien.dronetravailpratique.helpers.GimbalHelper
   * @see net.info420.fabien.dronetravailpratique.helpers.GimbalHelper#bougerGimbal(int, int, int)
   */
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
