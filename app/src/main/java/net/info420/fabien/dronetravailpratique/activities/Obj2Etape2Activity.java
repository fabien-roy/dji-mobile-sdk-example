package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 */
public class Obj2Etape2Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape2Activity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneMover().land();
  }

  private void initUI(){
    setContentView(R.layout.obj2_etape2_activity);
  }
}
