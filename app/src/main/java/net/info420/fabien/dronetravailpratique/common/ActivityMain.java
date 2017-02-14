package net.info420.fabien.dronetravailpratique.common;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

  public static final String TAG = ActivityMain.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    // On vérifie les permissions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
          Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
          Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
          Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
          Manifest.permission.READ_PHONE_STATE,
        }
        , 1);
    }

    setContentView(R.layout.activity_main);
  }

  @Override
  public void onClick(View view) {
    switch(view.getId()) {
      case 1:
        break;
    }
  }
}