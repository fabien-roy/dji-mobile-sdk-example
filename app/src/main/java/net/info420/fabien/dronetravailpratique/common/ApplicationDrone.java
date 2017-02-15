package net.info420.fabien.dronetravailpratique.common;

import android.app.Application;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by fabien on 17-02-10.
 */

public class ApplicationDrone extends Application {

  private static final String TAG = ApplicationDrone.class.getName();

  private static DJIBaseProduct mProduct;

  @Override
  public void onCreate() {
    super.onCreate();

    // Instanciation used in the whole application
  }

  public static synchronized DJIBaseProduct getProductInstance() {
    if (null == mProduct) {
      mProduct = DJISDKManager.getInstance().getDJIProduct();
    }
    return mProduct;
  }
}