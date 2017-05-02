package net.info420.fabien.dronetravailpratique.application;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import net.info420.fabien.dronetravailpratique.util.DroneMover;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.gimbal.DJIGimbal;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by fabien on 17-02-10.
 */

public class DroneApplication extends Application {

  private static final String TAG = DroneApplication.class.getName();

  public static final String FLAG_CONNECTION_CHANGE = "net_info420_fabien_dronetravailpratique_connection_change";

  public static final int MAX_FLIGHT_HEIGHT     = 20; // 20 mètres du sol (3 mètres = impossible)
  public static final int MAX_GO_HOME_ALTITURE  = 20; // 20 mètres du sol (3 mètres = impossible)
  public static final int MAX_ANGLE             = 5;  // 5°
  public static final int MAX_SPEED             = 5;  // 5 km/h

  private static DJIBaseProduct mProduct;
  private static DJIGimbal      mGimbal;
  private static DJICamera      mCamera;

  private Handler mHandler;

  public static DroneMover droneMover;

  @Override
  public void onCreate() {
    super.onCreate();

    mHandler = new Handler(Looper.getMainLooper());

    droneMover = new DroneMover();

    // Identification de la clé d'API du SDK
    Log.d(TAG, "onCreate(), initSDKManager...");
    DJISDKManager.getInstance().initSDKManager(this, mDJISDKManagerCallback);
  }

  /**
   * Méthode pour avoir l'instance du produit (Singleton)
   *
   * @return le mProduit (DJIBaseProduct)
   *
   * @see DJIBaseProduct
   */
  public static synchronized DJIBaseProduct getProductInstance() {
    if (null == mProduct) {
      mProduct = DJISDKManager.getInstance().getDJIProduct();
    }
    return mProduct;
  }

  /**
   * Méthode pour avoir l'instance du gimbal (Singleton)
   *
   * @return le Gimbal (DJIGimbal)
   *
   * @see DJIGimbal
   */
  public static synchronized DJIGimbal getGimbalInstance() {
    if (null == mGimbal) {
      mGimbal = getProductInstance().getGimbal();
    }
    return mGimbal;
  }

  /**
   * Méthode pour avoir l'instance de la caméra (Singleton)
   *
   * @return la Caméra (DJICamera)
   *
   * @see dji.sdk.camera.DJICamera
   */
  public static synchronized DJICamera getCameraInstance() {
    if (null == mCamera) {
      mCamera = getProductInstance().getCamera();
    }
    return mCamera;
  }

  /**
   * Méthode pour avoir l'instance de droneMover (Singleton)
   *
   * @return le droneMover (DroneMover)
   *
   * @see DroneMover
   */
  public static synchronized DroneMover getDroneBougeur() {
    if (null == droneMover) {
      droneMover = new DroneMover();
    }
    return droneMover;
  }

  public static boolean isAircraftConnected() {
    return getProductInstance() != null && getProductInstance() instanceof DJIAircraft;
  }

  public static synchronized DJIAircraft getAircraftInstance() {
    if (!isAircraftConnected()) return null;
    return (DJIAircraft) getProductInstance();
  }

  public static boolean isProductModuleAvailable() {
    return (null != getProductInstance());
  }

  public static boolean isAircraft() {
    return getProductInstance() instanceof DJIAircraft;
  }

  public static boolean isFlightControllerAvailable() {
    return isProductModuleAvailable() && isAircraft() &&
      (null != getAircraftInstance().getFlightController());
  }


  // Gestion du Mobile SDK, de l'authentification de la clé d'API
  private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {

    @Override
    public void onGetRegisteredResult(DJIError error) {
      if(error == DJISDKError.REGISTRATION_SUCCESS) {
        Toast.makeText(getBaseContext(), "SDK enregistré avec succès!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "SDK enregistré avec succès!");

        // Réussite -> Début de la connection au produit
        DJISDKManager.getInstance().startConnectionToProduct();
      } else {
        Toast.makeText(getBaseContext(), "L'enregistrement du SDK a échoué...", Toast.LENGTH_LONG).show();
        Log.d(TAG, "L'enregistrement du SDK a échoué...");
      }
      Log.v(TAG, error.getDescription());
    }

    @Override
    public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {
      Log.d(TAG, String.format("onProductChanged oldProduct : %s, newProduct : %s", oldProduct, newProduct));
      mProduct = newProduct;
      if(mProduct != null) {
        mProduct.setDJIBaseProductListener(mDJIBaseProductListener);
      }

      notifyStatusChange();
    }

    private DJIBaseProduct.DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProduct.DJIBaseProductListener() {

      @Override
      public void onComponentChange(DJIBaseProduct.DJIComponentKey key, DJIBaseComponent oldComponent, DJIBaseComponent newComponent) {

        if(newComponent != null) {
          newComponent.setDJIComponentListener(mDJIComponentListener);
        }
        Log.d(TAG, String.format("onComponentChange key : %s, oldComponent : %s, newComponent : %s", key, oldComponent, newComponent));

        notifyStatusChange();
      }

      @Override
      public void onProductConnectivityChanged(boolean isConnected) {

        Log.d(TAG, String.format("onProductConnectivityChanged : %s", isConnected));

        notifyStatusChange();
      }

    };

    private DJIBaseComponent.DJIComponentListener mDJIComponentListener = new DJIBaseComponent.DJIComponentListener() {

      @Override
      public void onComponentConnectivityChanged(boolean isConnected) {
        Log.d(TAG, String.format("onComponentConnectivityChanged : %s", isConnected));
        notifyStatusChange();
      }

    };

    private void notifyStatusChange() {
      mHandler.removeCallbacks(updateRunnable);
      mHandler.postDelayed(updateRunnable, 500);
    }

    private Runnable updateRunnable = new Runnable() {

      @Override
      public void run() {
        Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
        sendBroadcast(intent);
      }
    };
  };
}