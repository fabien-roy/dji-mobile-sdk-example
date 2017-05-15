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
import android.widget.ToggleButton;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;
import net.info420.fabien.dronetravailpratique.helpers.GimbalHelper;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dji.common.product.Model;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;

import static net.info420.fabien.dronetravailpratique.application.DroneApplication.getCameraInstance;

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

  // Seuil necéssaire afin d'ajuster le suivi de la ligne
  private static final int SEUIL_LIGNE = 5;

  // Views
  private TextureView tvVideo;
  private ImageView   ivImageTraitee;

  protected DJICamera.CameraReceivedVideoDataCallback receivedVideoDataCallBack = null;
  protected DJICodecManager codecManager;

  private boolean pretAuTraitement  = false; // Vrai si le traitement est prêt et qu'aucun
                                             // traitement n'est en cours
  private boolean droneDecolle      = false; // Vrai si le drone est décollé
  private boolean enRetour          = false; // Vrai si le drone a perdu la ligne (car il l'a
                                             // dépassé) et doit revenir sur ses pas
  private boolean enRotation        = false; // Vrai si le drone voit encore le coin de la ligne et
                                             // doit s'en éloigner

  private Float orientation = 1F; // 1 = devant, -1 = derrière

  private int face = DroneHelper.FACE_NORD; // Face du suivi de ligne

  // Vérification du fonctionnement d'OpenCV
  static {
    if(!OpenCVLoader.initDebug()){
      Log.d(TAG, "OpenCV non loadé");
    } else {
      Log.d(TAG, "OpenCV loadé correctement");
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
        pretAuTraitement = true;
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
   *   <li>Calcul le centre de masse de l'image</li>
   *   <li>Fait bouger le drone en fonction du mode <b>(voir plus bas)</b></li>
   *   <li>Affiche le centre de masse dans un {@link TextView}</li>
   * </ul>
   *
   * <p>Mode A : Suivi de la ligne sans tourner</p>
   * <ul>
   *   <li>Si le centre de masse est NaN : on vérifie si le drone est en mode "enRetour" :
   *   <ul>
   *     <li>Si le drone est "enRetour", il ne fait qu'avancer avec l'orientation en cours.</li>
   *     <li>Sinon, il changer l'orientation en cours (devant/derrière) et se met en mode
   *     "enRetour".</li>
   *   </ul></li>
   *   <li>Si le centre de masse est plus grand que le seuil, le drone ajuste son mouvement.</li>
   *   <li>Si le centre de masse est plus petit que le seuil, le drone ajuste son mouvement.</li>
   *   <li>Sinon, le drone avance en fonction de l'orientation choisie.</li>
   * </ul>
   *
   * <p><i>N.B. : Le mode "enRetour" est désactivé lorsque le centre de masse n'est pas NaN</i></p>
   *
   * <p>Mode B : Suivi de la ligne en tournant</p>
   * <ul>
   *   <li>Ajuste les mouvements (avancer, ajustement sous le seuil, ajustement au-dessus du seuil)
   *   en fonction de la face du suivi de ligne</li>
   *   <li>Si le nombre de coins détecté est supérieur à 1 :
   *   <ul>
   *     <li>Si le drone est "enRotation", il ne fait qu'avancer avec l'orientation en cours.</li>
   *     <li>Sinon, il changer l'orientation en cours (Nord, Ouest, Sud, Est) et se met en mode
   *     "enRotation". Cela va modifier ses mouvements d'avance et d'ajustement avec l'orientation
   *     (la face).</li>
   *   </ul></li>
   *   <li>Sinon :
   *   <ul>
   *     <li>Si le centre de masse est NaN, le drone se pose.</li>
   *     <li>Si le centre de masse est plus grand que le seuil, le drone ajuste son mouvement.</li>
   *     <li>Si le centre de masse est plus petit que le seuil, le drone ajuste son mouvement.</li>
   *     <li>Sinon, le drone avance en fonction de l'orientation choisie.</li>
   *   </ul></li>
   * </ul>
   *
   * @see Mat
   * @see Utils#bitmapToMat(Bitmap, Mat)
   * @see Imgproc#cvtColor(Mat, Mat, int)
   * @see Core#inRange(Mat, Scalar, Scalar, Mat)
   * @see Imgproc#HoughLines(Mat, Mat, double, double, int)
   * @see Imgproc#goodFeaturesToTrack(Mat, MatOfPoint, int, double, double)
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

      // On met l'image en HSV
      Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_RGB2HSV, 3);

      // On détecte une certaine couleur (vert)
      Core.inRange(matImage, new Scalar(50, 100, 30), new Scalar(85, 255, 255), matImage);

      // La détection de ligne a été enlevée complètement, puisqu'on a besoin que de la couleur
      // Je laisse quand même ça ici.
      /*
      // Détection de lignes
      Mat matLignes = new Mat();

      // Plus le threshold (dernier argument) est bas, plus on est tolerant
      Imgproc.HoughLines(matImage, matLignes, 1, Math.PI / 180, 120);

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

      Point centreDeMasse = new Point(momentz.get_m10() / momentz.get_m00(),
                                      momentz.get_m01() / momentz.get_m00());

      String message = String.format("(%s, %s)", centreDeMasse.x, centreDeMasse.y);

      if (!((ToggleButton) findViewById(R.id.btn_obj2_etape3_mode)).isChecked()) {
        // Mode de suivi A : au bout de la ligne, on retourne

        enRotation = false; // Sécurité

        if (Double.isNaN(centreDeMasse.y)) {
          message = message + " : NaN";

          // TODO : On pourrait aussi plutôt utiliser la détection de coins

          // Je laisse ceci ici : arrêt du drone lorsque la ligne est perdue.
          /*
          Log.d(TAG, message);
          ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

          arreter();

          // On retourne. On ne veut pas que pretAuTraitement soit true;
          return;
          */

          // La variable enRetour est false dès que la ligne est retrouvée
          if (enRetour) {
            DroneApplication.getDroneHelper().sendMovementTimer(
              DroneApplication.getDroneHelper().getMovementTimer( "Traitement : retour vers la ligne",
                                                                  new Float[] {0F, orientation, 0F, 0F},
                                                                  null));
          } else {
            orientation = -orientation;
            enRetour = true;
          }
        } else if (centreDeMasse.y > SEUIL_LIGNE) {
          message = message + String.format(" : plus grand que %s", SEUIL_LIGNE);

          DroneApplication.getDroneHelper().sendMovementTimer(
            DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers la gauche + avant",
                                                                new Float[] {-1F, orientation, 0F, 0F},
                                                                null));

          enRetour = false;
        } else if (centreDeMasse.y < -SEUIL_LIGNE) {
          message = message + String.format(" : plus petit que -%s", SEUIL_LIGNE);

          DroneApplication.getDroneHelper().sendMovementTimer(
            DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers la droite + avant",
                                                                new Float[] {1F, orientation, 0F, 0F},
                                                                null));
          enRetour = false;
        } else {
          message = message + " : ok";

          DroneApplication.getDroneHelper().sendMovementTimer(
            DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers l'avant",
                                                                new Float[] {0F, orientation, 0F, 0F},
                                                                null));
          enRetour = false;
        }
      } else {
        // Mode de suivi B : Lorsque le drone détecte un coin, il tourne à droite

        enRetour = false; // Sécurité

        // Mouvements par défaut, soit quand le suivi est face au Nord
        Double      centreDeMasseCoord  = centreDeMasse.y;
        List<Float> mouvementAvant      = new ArrayList<>(Arrays.asList(1F,  0F, 0F, 0F));
        List<Float> mouvementSeuilMin   = new ArrayList<>(Arrays.asList(1F,  1F, 0F, 0F));
        List<Float> mouvementSeuilMax   = new ArrayList<>(Arrays.asList(1F, -1F, 0F, 0F));

        // En fonction de la face du suivi de ligne, les mouvements d'ajustements changent
        switch(face) {
          case DroneHelper.FACE_OUEST:
            centreDeMasseCoord  = centreDeMasse.x;
            mouvementAvant      = new ArrayList<>(Arrays.asList( 0F,  1F, 0F, 0F));
            mouvementSeuilMin   = new ArrayList<>(Arrays.asList( 1F,  1F, 0F, 0F));
            mouvementSeuilMax   = new ArrayList<>(Arrays.asList(-1F,  1F, 0F, 0F));
            break;
          case DroneHelper.FACE_SUD:
            centreDeMasseCoord  = centreDeMasse.y;
            mouvementAvant      = new ArrayList<>(Arrays.asList(-1F,  0F, 0F, 0F));
            mouvementSeuilMin   = new ArrayList<>(Arrays.asList(-1F,  1F, 0F, 0F));
            mouvementSeuilMax   = new ArrayList<>(Arrays.asList(-1F, -1F, 0F, 0F));
            break;
          case DroneHelper.FACE_EST:
            centreDeMasseCoord  = centreDeMasse.x;
            mouvementAvant      = new ArrayList<>(Arrays.asList( 0F, -1F, 0F, 0F));
            mouvementSeuilMin   = new ArrayList<>(Arrays.asList( 1F, -1F, 0F, 0F));
            mouvementSeuilMax   = new ArrayList<>(Arrays.asList(-1F, -1F, 0F, 0F));
            break;
        }

        // On doit d'abord vérifié si il y a un coin
        MatOfPoint coins = new MatOfPoint();

        // Détection de contours
        // Je pourrais pogner davantage de coins, mais je préfère limiter à 150. Normalement, on
        // devrait juste mettre le nombre de coins qu'on veut, approximativement.
        // Arguments : Source, Destination (coins seulement, c'est un MatOfPoints), Nombre de coins,
        // Niveau de qualité, Distance minimale
        Imgproc.goodFeaturesToTrack(matImage, coins, 2, 0.01, 10);

        if (coins.toList().size() > 0) {
          if (enRotation) {
            // Le drone avance jusqu'à ce qu'il ne voit plus de coin
            DroneApplication.getDroneHelper().sendMovementTimer(
              DroneApplication.getDroneHelper().getMovementTimer( "Traitement : en rotation",
                                                                  mouvementAvant.toArray(new Float[mouvementAvant.size()]),
                                                                  null));
          } else {
            // Rotation du mouvement
            face = face < DroneHelper.FACE_EST ? face++ : DroneHelper.FACE_NORD;

            enRotation = true;
          }
        } else {
          enRotation = false;

          if (Double.isNaN(centreDeMasseCoord)) {
            message = message + " : NaN";

            Log.d(TAG, message);
            ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

            arreter();

            // On retourne. On ne veut pas que pretAuTraitement soit true;
            return;
          } else if (centreDeMasseCoord > SEUIL_LIGNE) {
            message = message + String.format(" : plus grand que %s", SEUIL_LIGNE);

            DroneApplication.getDroneHelper().sendMovementTimer(
              DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers la gauche + avant",
                                                                  mouvementSeuilMax.toArray(new Float[mouvementSeuilMax.size()]),
                                                                  null));

          } else if (centreDeMasseCoord < -SEUIL_LIGNE) {
            message = message + String.format(" : plus petit que -%s", SEUIL_LIGNE);

            DroneApplication.getDroneHelper().sendMovementTimer(
              DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers la droite + avant",
                                                                  mouvementSeuilMin.toArray(new Float[mouvementSeuilMin.size()]),
                                                                  null));
          } else {
            message = message + " : ok";

            DroneApplication.getDroneHelper().sendMovementTimer(
              DroneApplication.getDroneHelper().getMovementTimer( "Traitement : vers l'avant",
                                                                  mouvementAvant.toArray(new Float[mouvementAvant.size()]),
                                                                  null));
          }
        }
      }

      Log.d(TAG, message);
      ((TextView) findViewById(R.id.tv_obj2_etape3_coords)).setText(message);

      afficherImage(matImage);

      // On attend une seconde (le temps de bouger) avant d'être prêt au traitement
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          pretAuTraitement = true;
        }
      }, 1000);
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
