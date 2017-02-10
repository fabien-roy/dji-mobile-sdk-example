package net.info420.fabien.dronetravailpratique;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
  Button activity_main_btn_obj1;
  Button activity_main_btn_obj2;
  Button activity_main_btn_obj3;

  Intent intent_service_objective1_step1 = new Intent(this, IntentServiceObj1Step1.class);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    activity_main_btn_obj1 = (Button) findViewById(R.id.activity_main_btn_obj1);
    activity_main_btn_obj2 = (Button) findViewById(R.id.activity_main_btn_obj2);
    activity_main_btn_obj3 = (Button) findViewById(R.id.activity_main_btn_obj3);

    activity_main_btn_obj1.setOnClickListener(this);
    activity_main_btn_obj2.setOnClickListener(this);
    activity_main_btn_obj3.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch(view.getId()) {
      case R.id.activity_main_btn_obj1:
        startService(intent_service_objective1_step1);
        break;
      case R.id.activity_main_btn_obj2:
        break;
      case R.id.activity_main_btn_obj3:
        break;
    }
  }
}