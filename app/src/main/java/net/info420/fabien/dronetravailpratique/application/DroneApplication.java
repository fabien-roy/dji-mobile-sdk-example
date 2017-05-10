package net.info420.fabien.dronetravailpratique.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import net.info420.fabien.dronetravailpratique.helpers.CameraHelper;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;
import net.info420.fabien.dronetravailpratique.helpers.GimbalHelper;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.gimbal.DJIGimbal;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

// TODO : Construire une Activity pour montrer l'objectif 3

/**
 * {@link Application} contenant des méthodes et variables utilisées dans toute l'application
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-02-10
 *
 * @see DJIBaseProduct
 * @see DroneHelper
 *
 * @see <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/internal/controller/DJISampleApplication.java"
 *      target="_blank">
 *      Source : L'{@link Application} du code d'exemple de DJI</a>
 */
public class DroneApplication extends Application {
  private static final String TAG = DroneApplication.class.getName();

  public static final String FLAG_CONNECTION_CHANGE = "net_info420_fabien_dronetravailpratique_connection_change";

  public static final int MAX_HAUTEUR_VOL       = 20; // 20 mètres du sol (3 mètres = impossible)
  public static final int MAX_GO_HOME_ALTITUDE  = 20; // 20 mètres du sol (3 mètres = impossible)
  public static final int MAX_ANGLE             = 5;  // 5°
  public static final int MAX_VITESSE           = 5;  // 5 km/h

  private static DJIBaseProduct baseProduct;
  private static DJIGimbal      gimbal;
  private static DJICamera      camera;

  private Handler handler;

  public static DroneHelper   droneHelper;
  public static GimbalHelper  gimbalHelper;
  public static CameraHelper  cameraHelper;

  /**
   * Exécuté à la création de l'{@link Application}
   *
   * <ul>
   *   <li>Instancie le {@link Handler}</li>
   *   <li>Instancie le {@link DroneHelper}</li>
   *   <li>Identifie la clée de l'API du SDK</li>
   * </ul>
   *
   * @see DroneHelper
   * @see DJISDKManager#initSDKManager(Context, DJISDKManager.DJISDKManagerCallback)
   */
  @Override
  public void onCreate() {
    super.onCreate();

    handler = new Handler(Looper.getMainLooper());

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
    if (null == baseProduct) {
      baseProduct = DJISDKManager.getInstance().getDJIProduct();
    }
    return baseProduct;
  }

  /**
   * Méthode pour avoir l'instance du gimbal (Singleton)
   *
   * @return le Gimbal (DJIGimbal)
   *
   * @see DJIGimbal
   */
  public static synchronized DJIGimbal getGimbalInstance() {
    if (null == gimbal) {
      gimbal = getProductInstance().getGimbal();
    }
    return gimbal;
  }

  /**
   * Méthode pour avoir l'instance de la caméra (Singleton)
   *
   * @return la Caméra (DJICamera)
   *
   * @see dji.sdk.camera.DJICamera
   */
  public static synchronized DJICamera getCameraInstance() {
    if (null == camera) {
      camera = getProductInstance().getCamera();
    }
    return camera;
  }

  /**
   * Méthode pour avoir l'instance de droneHelper (Singleton)
   *
   * @return le droneHelper (DroneHelper)
   *
   * @see DroneHelper
   */
  public static synchronized DroneHelper getDroneHelper() {
    if (null == droneHelper) {
      droneHelper = new DroneHelper();
    }
    return droneHelper;
  }

  /**
   * Méthode pour avoir l'instance de gimbalHelper (Singleton)
   *
   * @return le gimbalHelper ({@link GimbalHelper})
   *
   * @see GimbalHelper
   */
  public static synchronized GimbalHelper getGimbalHelper() {
    if (null == gimbalHelper) {
      gimbalHelper = new GimbalHelper();
    }
    return gimbalHelper;
  }

  /**
   * Méthode pour avoir l'instance de cameraHelper (Singleton)
   *
   * @return le cameraHelper ({@link CameraHelper})
   *
   * @see CameraHelper
   */
  public static synchronized CameraHelper getCameraHelper() {
    if (null == cameraHelper) {
      cameraHelper = new CameraHelper();
    }
    return cameraHelper;
  }

  /**
   * Vérifie si le {@link DJIAircraft} est prêt connecté
   *
   * @return  boolean de la comparaison
   */
  public static boolean isAircraftConnected() {
    return getProductInstance() != null && getProductInstance() instanceof DJIAircraft;
  }

  /**
   * Vérifie si le {@link DJIAircraft} est connecté et renvoie son instance
   *
   * @return {@link DJIAircraft} (le drone)
   */
  public static synchronized DJIAircraft getAircraftInstance() {
    if (!isAircraftConnected()) return null;
    return (DJIAircraft) getProductInstance();
  }

  /**
   * Vérifie si le {@link DJIBaseProduct} est prêt à l'utilisation
   *
   * @return  boolean de la comparaison
   */
  public static boolean isProductModuleAvailable() {
    return (null != getProductInstance());
  }

  /**
   * Vérifie si le {@link DJIBaseProduct} est un {@link DJIAircraft} (drone)
   *
   * @return  boolean de la comparaison
   */
  public static boolean isAircraft() {
    return getProductInstance() instanceof DJIAircraft;
  }

  /**
   * Vérifie si le {@link dji.sdk.flightcontroller.DJIFlightController} est prêt à l'utilisation
   *
   * @return  boolean de la comparaison
   */
  public static boolean isFlightControllerAvailable() {
    return isProductModuleAvailable() && isAircraft() &&
      (null != getAircraftInstance().getFlightController());
  }


  /**
   * Classe interne de callback pour la gestion du Mobile SDK et de l'authentification de la clé d'API
   *
   * @see DJISDKManager
   * @see dji.sdk.sdkmanager.DJISDKManager.DJISDKManagerCallback
   */
  private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {

    /**
     * Exécuté lorsqu'un résultat de l'enregistrement de la clé d'API est envoyé
     *
     * @param djiError Callback de DJI
     */
    @Override
    public void onGetRegisteredResult(DJIError djiError) {
      if(djiError == DJISDKError.REGISTRATION_SUCCESS) {
        Toast.makeText(getBaseContext(), "SDK enregistré avec succès!", Toast.LENGTH_LONG).show();

        // Réussite -> Début de la connection au produit
        DJISDKManager.getInstance().startConnectionToProduct();
      } else {
        Toast.makeText(getBaseContext(), "L'enregistrement du SDK a échoué...", Toast.LENGTH_LONG).show();
      }
    }

    /**
     * Exécuté lors d'un changement de produit
     *
     * <ul>
     *   <li>Remplace le produit utilisé par le nouveau</li>
     *   <li>Ajoute un Listener au produit</li>
     *   <li>Notifie le changement de statut du {@link DJIBaseComponent}</li>
     * </ul>
     *
     * @param vieuxProduct    {@link DJIBaseProduct} du vieux produit
     * @param nouveauProduct  {@link DJIBaseProduct} du nouveau produit
     *
     * @see dji.sdk.base.DJIBaseProduct.DJIBaseProductListener
     * @see #notifyStatusChange()
     */
    @Override
    public void onProductChanged(DJIBaseProduct vieuxProduct, DJIBaseProduct nouveauProduct) {
      baseProduct = nouveauProduct;

      if(baseProduct != null) {
        baseProduct.setDJIBaseProductListener(mDJIBaseProductListener);
      }

      notifyStatusChange();
    }

    /**
     * Listener de connexion du {@link DJIBaseComponent}
     *
     * <ul>
     *   <li>Notifie le changement de statut du {@link DJIBaseComponent}</li>
     * </ul>
     *
     * @see #notifyStatusChange()
     */
    private DJIBaseComponent.DJIComponentListener mDJIComponentListener = new DJIBaseComponent.DJIComponentListener() {

      @Override
      public void onComponentConnectivityChanged(boolean isConnected) {
        notifyStatusChange();
      }

    };

    /**
     * Ajuste les Callbacks et envoit un Broadcast de changement de connexion
     *
     * @see android.content.BroadcastReceiver
     * @see Handler
     * @see #updateRunnable
     */
    private void notifyStatusChange() {
      handler.removeCallbacks(updateRunnable);
      handler.postDelayed(updateRunnable, 500);
    }

    /**
     * {@link Runnable} pour envoyé un {@link android.content.BroadcastReceiver} de changement de connnexion
     */
    private Runnable updateRunnable = new Runnable() {

      @Override
      public void run() {
        Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
        sendBroadcast(intent);
      }
    };

    /**
     * Classe interne de Listener pour le {@link DJIBaseProduct} ({@link DJIBaseComponent})
     *
     * @see dji.sdk.base.DJIBaseProduct.DJIBaseProductListener
     */
    private DJIBaseProduct.DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProduct.DJIBaseProductListener() {

      /**
       * Exécuté lorsque le produit à écouté est changé
       *
       * <ul>
       *   <li>Remplace le {@link DJIBaseComponent} avec le nouveau</li>
       *   <li>Notifie le changement de statut du {@link DJIBaseComponent}</li>
       * </ul>
       *
       * @param key               {@link dji.sdk.base.DJIBaseProduct.DJIComponentKey} du {@link DJIBaseComponent}
       * @param vieuxComponent    {@link DJIBaseComponent} du vieux component
       * @param nouveauComponent  {@link DJIBaseComponent} du nouveau component
       */
      @Override
      public void onComponentChange(DJIBaseProduct.DJIComponentKey key, DJIBaseComponent vieuxComponent, DJIBaseComponent nouveauComponent) {
        if(nouveauComponent != null) {
          nouveauComponent.setDJIComponentListener(mDJIComponentListener);
        }

        notifyStatusChange();
      }

      /**
       * Exécuté lorsque la connectivité du {@link DJIBaseComponent} change
       *
       * <ul>
       *   <li>Notifie le changement de statut du {@link DJIBaseComponent}</li>
       * </ul>
       *
       * @param isConnected boolean vrai si le {@link DJIBaseComponent} est connecté
       */
      @Override
      public void onProductConnectivityChanged(boolean isConnected) {
        notifyStatusChange();
      }

    };
  };
}