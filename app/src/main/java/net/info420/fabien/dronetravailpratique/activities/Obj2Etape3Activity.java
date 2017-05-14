package net.info420.fabien.dronetravailpratique.activities;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
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

// TODO : Aller chercher la vidéo du drone dans Obj2Etape3Activity
// TODO : Faire un traitement OpenCV de la vidéo du drone
// TODO : Documenter Obj2Etape3Activity

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
public class Obj2Etape3Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
  public static final String TAG = Obj2Etape3Activity.class.getName();

  private TextureView tvVideo;
  private ImageView   ivImageTraitee;

  protected DJICamera.CameraReceivedVideoDataCallback receivedVideoDataCallBack = null;
  protected DJICodecManager codecManager;

  private boolean pretAuTraitement = false;

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
   *   <li>Instancie le callback d</li>
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
    initCallback();
    initGimbal();
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
   * @see android.view.TextureView.SurfaceTextureListener
   * @see #onSurfaceTextureUpdated(SurfaceTexture)
   */
  private void initUI(){
    setContentView(R.layout.activity_obj2_etape3);

    tvVideo         = (TextureView) findViewById(R.id.tv_obj2_etape3_video);
    ivImageTraitee  = (ImageView) findViewById(R.id.iv_obj2_etape3_image_traitee);

    findViewById(R.id.btn_obj2_etape3_traiter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        initVideo();
      }
    });

    findViewById(R.id.btn_obj2_etape3_arreter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        arreter();
      }
    });

    tvVideo.setSurfaceTextureListener(this);
  }

  /**
   * Instancie le {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback}
   *
   * <ul>
   *   <li>Instancie receivedVideoDataCallBack</li>
   *   <li>Lorsque du data est reçu, vérifie si le {@link DJICodecManager} n'est pas null</li>
   *   <li>S'il ne l'est pas, décode la vidéo</li>
   * </ul>
   *
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback#onResult(byte[], int)
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
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
   */
  private void initVideo() {
    pretAuTraitement = true;

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
   * Arrête toutes les opérations
   */
  private void arreter() {
    DroneApplication.getDroneHelper().atterir();

    pretAuTraitement = false;
  }

  /**
   * Effectue le traitement necéssaire au suivi de ligne
   *
   * <ul>
   *   <li>Enlève l'image {@link Bitmap} déjà dans le {@link ImageView}</li>
   *   <li>Va chercher l'image du drone</li>
   * </ul>
   *
   * @see Mat
   * @see Utils#bitmapToMat(Bitmap, Mat)
   * @see Imgproc#cvtColor(Mat, Mat, int)
   * @see Core#inRange(Mat, Scalar, Scalar, Mat)
   * @see Imgproc#HoughLines(Mat, Mat, double, double, int)
   *
   * @see <a href="http://opencvexamples.blogspot.com/2013/10/line-detection-by-hough-line-transform.html"
   *      target="_blank">
   *      Source : Détection de lignes</a>
   * @see <a href="http://stackoverflow.com/questions/43694436/why-different-result-of-houghlines-of-opencv-in-java-and-c"
   *      target="_blank">
   *      Source : Dessiner des lignes</a>
   * @see <a href="http://answers.opencv.org/question/82614/how-to-find-the-centre-of-multiple-objects-in-a-image/"
   *      target="_blank">
   *      Source : Trouver le centre de masse</a>
   */
  private void traiter() {
    // TODO : Envoyer les instructions au drone

    // Matrice de l'image traitée
    Mat matImage = new Mat();

    // Conversion du Bitmap de la vidéo dans la matrice
    Utils.bitmapToMat(tvVideo.getBitmap(), matImage);

    // Si il faut réduire l'image, c'est ici.

    // On met l'image en HSV
    Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_RGB2HSV, 3);

    // On détecte une certaine couleur (vert)
    Core.inRange(matImage, new Scalar(50, 100, 30), new Scalar(85, 255, 255), matImage);

    /*
    // Détection de lignes
    Mat matLignes = new Mat();

    // Plus le threshold (dernier argument) est bas, plus on est tolerant
    Imgproc.HoughLines(matImage, matLignes, 1, Math.PI / 180, 120);

    // TODO : Enlever l'affichage des lignes
    // Dessin de la ligne sur l'image affichée
    for (int i = 0; i < matLignes.cols(); i++) {
      double rho    = matLignes.get(0, i)[0];
      double theta  = matLignes.get(0, i)[1];

      double a = Math.cos(theta);
      double b = Math.sin(theta);

      Point pt1 = new Point();
      Point pt2 = new Point();

      pt1.x = Math.round(a * rho + 1000 * (-b));
      pt1.y = Math.round(b * rho + 1000 * (a));
      pt2.x = Math.round(a * rho - 1000 * (-b));
      pt2.y = Math.round(b * rho - 1000 * (a));

      Imgproc.line(matImage, pt1, pt2, new Scalar(255, 0, 0), 2, Core.LINE_AA, 0);
    }
    */

    // Recherche du centre de masse
    Moments momentz = Imgproc.moments(matImage);

    /*

    Log.d(TAG, String.format( "Centre de masse : (%s, %s) m00 : %s m01 : %s m10 : %s",
                              centreDeMasse.x,
                              centreDeMasse.y,
                              momentz.get_m00(),
                              momentz.get_m01(),
                              momentz.get_m10()));
    */

    Point centreDeMasse = new Point(momentz.get_m10() / momentz.get_m00(),
          momentz.get_m01() / momentz.get_m00());
    // double centreDeMasseY = momentz.get_m01() / momentz.get_m00();

    // C'est y qui nous interesse, puisqu'on veut un centre de masse horizontal.
    // Le centre de masse doit être entre -2.5 et 2.5.

    String message = String.format("(%s, %s)", centreDeMasse.x, centreDeMasse.y);

    if (centreDeMasse.y > 5) {
      // TODO : Amener le drone à gauche
      message = message + " : plus grand que 5";
    } else if (centreDeMasse.y < -5) {
      // TODO : Amener le drone à droite
      message = message + " : plus petit que -5";
    } else {
      // TODO : Avancer, tout simplement
      message = message + " : ok";
    }

    Log.d(TAG, message);
    ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

    afficherImage(matImage);
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
   * Ajoute un {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback} à la {@link DJICamera}
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
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)
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
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureSizeChanged(SurfaceTexture, int, int)
   */
  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }

  /**
   * S'assure que le {@link DJICodecManager} n'est plus initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureDestroyed(SurfaceTexture)
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
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureUpdated(SurfaceTexture)
   * @see #traiter()
   */
  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    if (pretAuTraitement) traiter();
  }
}
