package net.info420.fabien.dronetravailpratique.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;

/**
 * Created by fabien on 17-02-15.
 */

// TODO : Documenter ObjectifsActivity

public class ObjectifsActivity extends Activity {

  public static final String TAG = ObjectifsActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  private void initUI(){
    setContentView(R.layout.activity_objectifs);

    findViewById(R.id.btn_obj1_etape1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape1Activity.class));
      }
    });

    findViewById(R.id.btn_obj1_etape2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape2Activity.class));
      }
    });

    findViewById(R.id.btn_obj1_etape3).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape3Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape1Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape2Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape3).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape3Activity.class));
      }
    });
  }
}