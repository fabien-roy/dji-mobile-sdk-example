package net.info420.fabien.dronetravailpratique.activities;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;
import net.info420.fabien.dronetravailpratique.helpers.GimbalHelper;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import dji.common.product.Model;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;

import static net.info420.fabien.dronetravailpratique.application.DroneApplication.getCameraInstance;

// TODO : Documenter Obj3Etape1Activity

/**
 * {@link android.app.Activity} pour aller chercher la vidéo du drone et faire un traitement
 * d'image
 *
 * <p>La méthode {@link #traiter()} effectue le traitement necéssaire au suivi de ligne. Elle est
 * appelée lors du {@link #onSurfaceTextureUpdated(SurfaceTexture)} lorsque le traitement est
 * en opération (activé avec un bouton).</p>
 *
 * @see <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/FetchMediaView.java"
 *      target="_blank">
 *      Source : Aller chercher le media de la caméra (vidéo/photo)</a>
 * @see <a href="https://developer.dji.com/mobile-sdk/documentation/android-tutorials/FPVDemo.html"
 *      target="_blank">
 *      Source : Tutoriel pour aller chercher les images de la caméra</a>
 * @see <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-camera.html"
 *      target="_blank">
 *      Source : Informations sur la caméra</a>
 */
public class Obj3Etape1Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
  public static final String TAG = Obj3Etape1Activity.class.getName();

  private TextureView tvVideo;
  private ImageView   ivImageTraitee;

  protected DJICamera.CameraReceivedVideoDataCallback receivedVideoDataCallBack = null;
  protected DJICodecManager codecManager;

  private boolean pretAuTraitement  = false;
  private boolean droneDecolle      = false;

  private int orientation = 1; // 1 = devant, -1 = derrière

  // Vérification du fonctionnement d'OpenCV
  static {
    if(!OpenCVLoader.initDebug()){
      Log.d(TAG, "OpenCV not loaded");
    } else {
      Log.d(TAG, "OpenCV loaded");
    }
  }

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appelle {@link #initUI()}</li>
   *   <li>Active le mode VirtualStick du drone</li>
   *   <li>Instancie le callback de la vidéo</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see DroneHelper#setupFlightController()
   * @see #initUI()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DroneApplication.getDroneHelper().setupFlightController();

    initUI();
    initCallback();
    initGimbal();
  }

  /**
   * Exécuté à la fermetture de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Fait attérir le drone</li>
   *   <li>Désactive le mode VirtualStick du drone</li>
   * </ul>
   *
   * @see #arreter()
   * @see DroneHelper#disableVirtualStickMode()
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();

    arreter();

    DroneApplication.getDroneHelper().disableVirtualStickMode();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Instancie les composants</li>
   *   <li>Met le Listener de texture au tvVideo</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * @see TextureView.SurfaceTextureListener
   * @see #onSurfaceTextureUpdated(SurfaceTexture)
   */
  private void initUI(){
    setContentView(R.layout.activity_obj3_etape1);

    tvVideo         = (TextureView) findViewById(R.id.tv_obj3_etape1_video);
    ivImageTraitee  = (ImageView) findViewById(R.id.iv_obj3_etape1_image_traitee);

    findViewById(R.id.btn_obj3_etape1_traiter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        initVideo();
        pretAuTraitement = true;
      }
    });

    findViewById(R.id.btn_obj3_etape1_arreter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        arreter();
      }
    });

    tvVideo.setSurfaceTextureListener(this);
  }

  /**
   * Instancie le {@link DJICamera.CameraReceivedVideoDataCallback}
   *
   * <ul>
   *   <li>Instancie receivedVideoDataCallBack</li>
   *   <li>Lorsque du data est reçu, vérifie si le {@link DJICodecManager} n'est pas null</li>
   *   <li>S'il ne l'est pas, décode la vidéo</li>
   * </ul>
   *
   * @see DJICamera.CameraReceivedVideoDataCallback
   * @see DJICamera.CameraReceivedVideoDataCallback#onResult(byte[], int)
   * @see DJICodecManager
   * @see DJICodecManager#sendDataToDecoder(byte[], int)
   */
  private void initCallback() {
    receivedVideoDataCallBack = new DJICamera.CameraReceivedVideoDataCallback() {
      @Override
      public void onResult(byte[] videoBuffer, int size) {
        if(codecManager != null){
          codecManager.sendDataToDecoder(videoBuffer, size);
        }
      }
    };
  }

  /**
   * Initialise la vidéo
   *
   * <ul>
   *   <li>Vérifie que le {@link DJIBaseProduct} est connecté</li>
   *   <li>Place le bon Listener sur la {@link TextureView}</li>
   *   <li>Place le bon {@link DJICamera.CameraReceivedVideoDataCallback} sur l'instance de la
   *   {@link DJICamera}</li>
   * </ul>
   *
   * @see DJIBaseProduct
   * @see DJICamera
   * @see DJICamera.CameraReceivedVideoDataCallback
   */
  private void initVideo() {
    DJIBaseProduct product = DroneApplication.getProductInstance();

    if (product == null || !product.isConnected()) {
      Log.e(TAG, "Le drone est déconnecté!");
    } else {
      if (null != tvVideo) tvVideo.setSurfaceTextureListener(this);

      if (!product.getModel().equals(Model.UnknownAircraft)) {
        setCameraCallback(receivedVideoDataCallBack);
      }
    }
  }

  /**
   * Place la {@link dji.sdk.gimbal.DJIGimbal} directement sous le drone
   *
   * @see GimbalHelper#setGroundGimbal()
   */
  private void initGimbal() {
    DroneApplication.getGimbalHelper().setGroundGimbal();
  }

  /**
   * Faire décoller le drone
   */
  private void decoller() {
    DroneApplication.getDroneHelper().decoller();

    // Suite au décollage, on attend 10 secondes.
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        droneDecolle = true;
      }
    }, 10000);
  }
  /**
   * Arrête toutes les opérations
   */
  private void arreter() {
    DroneApplication.getDroneHelper().atterir();

    pretAuTraitement = false;

    // On attend cinq seconde (le temps d'attérir) avant d'être prêt au traitement
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        droneDecolle = false;
      }
    }, 5000);
  }

  /**
   * Effectue le traitement necéssaire au suivi de ligne
   *
   * <ul>
   *   <li>Fait décoller le drone s'il n'est pas en vol</li>
   *   <li>Enlève l'image {@link Bitmap} déjà dans le {@link ImageView}</li>
   *   <li>Va chercher l'image du drone</li>
   *   <li>Avance jusqu'à qu'il trouve la bonne couleur</li>
   *   <li>Attéri lorsqu'il trouve la bonne couleur</li>
   * </ul>
   *
   * @see Mat
   * @see Utils#bitmapToMat(Bitmap, Mat)
   * @see Imgproc#cvtColor(Mat, Mat, int)
   * @see Core#inRange(Mat, Scalar, Scalar, Mat)
   * @see Imgproc#HoughLines(Mat, Mat, double, double, int)
   *
   * @see <a href="http://answers.opencv.org/question/82614/how-to-find-the-centre-of-multiple-objects-in-a-image/"
   *      target="_blank">
   *      Source : Trouver le centre de masse</a>
   * @see <a href="http://stackoverflow.com/questions/18330959/how-to-check-whether-a-number-is-a-nan-in-java-android"
   *      target="_blank">
   *      Source : Est-ce qu'un double est NaN en Java?</a>
   */
  private void traiter() {
    if (!droneDecolle) {
      decoller();
    } else {
      pretAuTraitement = false;

      // Matrice de l'image traitée
      Mat matImage = new Mat();

      // Conversion du Bitmap de la vidéo dans la matrice
      Utils.bitmapToMat(tvVideo.getBitmap(), matImage);

      // S'il faut réduire l'image, c'est ici.

      // On met l'image en HSV
      Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_RGB2HSV, 3);

      // On détecte une certaine couleur (vert)
      Core.inRange(matImage, new Scalar(50, 100, 30), new Scalar(85, 255, 255), matImage);

      // Recherche du centre de masse
      Moments momentz = Imgproc.moments(matImage);

      Point centreDeMasse = new Point(momentz.get_m10() / momentz.get_m00(),
                                      momentz.get_m01() / momentz.get_m00());
      // double centreDeMasseY = momentz.get_m01() / momentz.get_m00();

      String message = String.format("(%s, %s)", centreDeMasse.x, centreDeMasse.y);

      if (Double.isNaN(centreDeMasse.y)) {
        // Lorsque la couleur n'est pas trouvée, le drone avance
        // TODO : Avancer, tout simplement
        message = message + " : NaN";

        Log.d(TAG, message);
        ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

        DroneApplication.getDroneHelper().sendMovementTimer(
          DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers l'avant",
                                                              new float[] {0, 1, 0, 0},
                                                              null));

        afficherImage(matImage);

        // On attend une seconde (le temps de bouger) avant d'être prêt au traitement
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            pretAuTraitement = true;
          }
        }, 1000);
      } else {
        // Lorsque la couleur est trouvée, le drone avance un peu et attéri
        // TODO : Faire attérir le drone
        message = message + " : trouvée";

        Log.d(TAG, message);
        ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

        DroneApplication.getDroneHelper().sendMovementTimer(
          DroneApplication.getDroneHelper().getMovementTimer( "Traitement : trouvée! Vers l'avant + atteri",
                                                              new float[] {0, 1, 0, 0},
                                                              null));

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            arreter();
          }
        }, 1000);
      }
    }
  }

  /**
   * Affiche une matrice ({@link Mat} dans le {@link ImageView}
   *
   * @param matImage  {@link Mat} de l'image à afficher
   */
  private void afficherImage(Mat matImage) {
    // Affichage de l'image
    // Image bitmap pour l'affichage
    Bitmap bmpImageTraitee =  Bitmap.createBitmap(matImage.cols(),
                                                  matImage.rows(),
                                                  Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(matImage, bmpImageTraitee);

    ivImageTraitee.setImageBitmap(null);
    ivImageTraitee.setImageBitmap(bmpImageTraitee);
  }

  /**
   * Ajoute un {@link DJICamera.CameraReceivedVideoDataCallback} à la {@link DJICamera}
   */
  private void setCameraCallback(DJICamera.CameraReceivedVideoDataCallback cameraReceivedVideoDataCallback) {
    if (getCameraInstance() != null) {
      // Reset du callback
      getCameraInstance().setDJICameraReceivedVideoDataCallback(cameraReceivedVideoDataCallback);
    }
  }

  /**
   * S'assure que le {@link DJICodecManager} est initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   * @param width   La largeur du {@link SurfaceTexture}
   * @param height  La hauteur du {@link SurfaceTexture}
   *
   * @see SurfaceTexture
   * @see TextureView.SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)
   */
  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    if (codecManager == null) {
      codecManager = new DJICodecManager(this, surface, width, height);
    }
  }

  /**
   * Exécuté lorsque la grandeur du {@link SurfaceTexture} change
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   * @param width   La largeur du {@link SurfaceTexture}
   * @param height  La hauteur du {@link SurfaceTexture}
   *
   * @see SurfaceTexture
   * @see TextureView.SurfaceTextureListener#onSurfaceTextureSizeChanged(SurfaceTexture, int, int)
   */
  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }

  /**
   * S'assure que le {@link DJICodecManager} n'est plus initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   * @see TextureView.SurfaceTextureListener#onSurfaceTextureDestroyed(SurfaceTexture)
   * @see DJICodecManager#cleanSurface()
   */
  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    if (codecManager != null) {
      codecManager.cleanSurface();
      codecManager = null;
    }

    return false;
  }

  /**
   * Exécuté lorsque la {@link SurfaceTexture} change
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   * @see TextureView.SurfaceTextureListener#onSurfaceTextureUpdated(SurfaceTexture)
   * @see #traiter()
   */
  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    if (pretAuTraitement) traiter();
  }
}
