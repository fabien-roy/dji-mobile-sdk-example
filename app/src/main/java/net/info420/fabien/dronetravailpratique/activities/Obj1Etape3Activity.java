package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;
import net.info420.fabien.dronetravailpratique.util.MouvementTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link android.app.Activity} qui fait le parcours prédéfinit
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-02-20
 *
 * @see DroneHelper
 * @see MouvementTimer
 */
public class Obj1Etape3Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape2Activity.class.getName();

  // Variables des mouvements du drone
  private float[] dataA = { 0,      3.8F  };        // OK
  private float[] dataB = { 3.5F,   0     };        // OK
  private float[] dataC = { 0,      -3.8F };        // OK
  private float[] dataD = { 5.95F,  0     };        // OK
  private float[] dataE = { 2,      9     };        // OK 180
  private float[] dataF = {-3.35F,  1.95F, 0.4F };  // OK
  private float[] dataG = { 2.5F,   13.5F };        // OK 180
  private float[] dataH = { 7.2F,   -1.6F };        // OK
  private float[] dataI = { 2,      9     };        // OK 180
  private float[] dataJ = {-2.7F,   0.7F  };        // 0-
  private float[] dataK = { 2,      9     };        // 270
  private float[] dataL = {-2.5F,   -4    };        // 00
  private float[] dataM = { 2,      9     };        // 360
  private float[] dataN = { 0,      -5    };        //

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appelle {@link #initUI()}</li>
   *   <li>Active le mode VirtualStick du drone</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see #initUI()
   * @see DroneHelper#setupFlightController()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DroneApplication.getDroneHelper().setupFlightController();

    initUI();
  }

  /**
   * Exécuté à la fermetture de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Fait attérir le drone</li>
   *   <li>Désactive le mode VirtualStick du drone</li>
   * </ul>
   *
   * @see DroneHelper#atterir()
   * @see DroneHelper#disableVirtualStickMode()
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();

    DroneApplication.getDroneHelper().atterir();

    DroneApplication.getDroneHelper().disableVirtualStickMode();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * @see #bouger(char)
   * @see DroneHelper#decoller()
   * @see DroneHelper#atterir()
   * @see MouvementTimer
   */
  private void initUI(){
    setContentView(R.layout.activity_obj1_etape3);


    findViewById(R.id.btn_obj1_etape3_decoller).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().decoller();
      }
    });
    findViewById(R.id.btn_obj1_etape3_arreter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().atterir();
      }
    });
    findViewById(R.id.btn_obj1_etape3_go).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        go();
      }
    });

    findViewById(R.id.btn_obj1_etape3_a).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('A');
      }
    });
    findViewById(R.id.btn_obj1_etape3_b).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('B');
      }
    });
    findViewById(R.id.btn_obj1_etape3_c).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('C');
      }
    });
    findViewById(R.id.btn_obj1_etape3_d).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('D');
      }
    });
    findViewById(R.id.btn_obj1_etape3_e).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('E');
      }
    });
    findViewById(R.id.btn_obj1_etape3_f).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('F');
      }
    });
    findViewById(R.id.btn_obj1_etape3_g).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('G');
      }
    });
    findViewById(R.id.btn_obj1_etape3_h).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('H');
      }
    });
    findViewById(R.id.btn_obj1_etape3_i).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('I');
      }
    });
    findViewById(R.id.btn_obj1_etape3_j).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('J');
      }
    });
    findViewById(R.id.btn_obj1_etape3_k).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('K');
      }
    });
    findViewById(R.id.btn_obj1_etape3_l).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('L');
      }
    });
    findViewById(R.id.btn_obj1_etape3_m).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('M');
      }
    });
    findViewById(R.id.btn_obj1_etape3_n).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bouger('N');
      }
    });
  }

  /**
   * Exécute le parcours
   *
   * <p>Toutes les coordonnées sont en pieds</p>
   *
   * <p>Coordonnées des poteaux</p>
   * <ul>
   *   <li>i    : (  6,    6 )</li>
   *   <li>ii   : ( 32,    6 )</li>
   *   <li>iii  : ( 19,   18 )</li>
   *   <li>iv   : (  6,   24 )</li>
   *   <li>v    : ( 32,   24 )</li>
   *   <li>vi   : ( 19, 37,6 )</li>
   * </ul>
   *
   * <p>Table des vecteurs de mouvement</p>
   * <ul>
   *   <li>A : (  0,    0 ) à (  0,   12 ) : (   0,    12 )</li>
   *   <li>B : (  0,   12 ) à ( 12,   12 ) : (  12,     0 )</li>
   *   <li>C : ( 12,   12 ) à ( 12,    0 ) : (   0,   -12 )</li>
   *   <li>D : ( 12,    0 ) à ( 32,    0 ) : (  20,     0 )</li>
   *   <li>E : ( 32,    0 ) à ( 32,   12 ) : (   0,    12 ), arc 180° antihoraire</li>
   *   <li>F : ( 32,   12 ) à (  6,   18 ) : ( -24,     6 )</li>
   *   <li>G : (  6,   18 ) à (  6,   30 ) : (   0,    12 ), arc 180° horaire</li>
   *   <li>H : (  6,   30 ) à ( 32,   18 ) : (  24,   -12 )</li>
   *   <li>I : ( 32,   18 ) à ( 32,   30 ) : (   0,    12 ), arc 180° antihoraire</li>
   *   <li>J : ( 32,   30 ) à ( 19, 31,6 ) : ( -13,   1,6 )</li>
   *   <li>K : ( 19, 31,6 ) à ( 25, 37,6 ) : (   6,     6 ), arc 270° horaire</li>
   *   <li>L : ( 25, 37,6 ) à ( 19,   12 ) : (  -6, -15,6 )</li>
   *   <li>N : ( 19,   12 ) à ( 19,   12 ) : (   0,     0 ), arc 360° antihoraire</li>
   *   <li>N : ( 19,   12 ) à ( 19,    0 ) : (   0,   -12 )</li>
   * </ul>
   *
   * <p>Puisqu'on travaille en mode Velocity, Body : </p>
   * <ul>
   *   <li>Pitch positif : vers la droite</li>
   *   <li>Pitch négatif : vers la gauche</li>
   *   <li>Roll  positif : vers l'avant</li>
   *   <li>Roll  négatif : vers l'arrière</li>
   * </ul>
   *
   * @see DroneHelper
   * @see DroneHelper#decoller()
   * @see DroneHelper#atterir()
   * @see DroneHelper#sendMovementTimerList(List)
   * @see MouvementTimer
   *
   * @see <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-flightController.html"
   *      target="_blank">
   *      Source : Roll Pitch Control Mode</a>
   */
  private void go() {
    // Décollage
    DroneApplication.getDroneHelper().decoller();

    // Suite au décollage, on attend 10 secondes.
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        List<MouvementTimer> mouvementTimers = new ArrayList<>();

        ajouterA(mouvementTimers);
        ajouterB(mouvementTimers);
        ajouterC(mouvementTimers);
        ajouterD(mouvementTimers);
        ajouterE(mouvementTimers);
        ajouterF(mouvementTimers);
        ajouterG(mouvementTimers);
        ajouterH(mouvementTimers);
        ajouterI(mouvementTimers);
        ajouterJ(mouvementTimers);
        ajouterK(mouvementTimers);
        ajouterL(mouvementTimers);
        ajouterM(mouvementTimers);
        ajouterN(mouvementTimers);

        // Exécution des mouvements
        DroneApplication.getDroneHelper().sendMovementTimerList(mouvementTimers);

        // L'atterrissage est géré par les timers
      }
    }, 10000);
  }

  /**
   * MOUVEMENT A
   *
   * <p>On va du point (0, 0) au point (0, 12), vecteur de mouvement : (0, 12)</p>
   * <p>On dépasse le poteau i vers le Nord</p>
   *
   * <p>Au départ, le drone pointe vers le <b>Nord</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterA(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("A1 : bouger", dataA[0], dataA[1], 0, DroneHelper.FACE_NORD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("A2 : attendre", null));
  }

  /**
   * MOUVEMENT B
   *
   * <p>On va du point (0, 12) au point (12, 12), vecteur de mouvement : (12, 0)</p>
   * <p>On dépasse le poteau i vers l'Est</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterB(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("B1 : bouger", dataB[0], dataB[1], 0, DroneHelper.FACE_NORD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("B2 : attendre", null));
  }

  /**
   * MOUVEMENT C
   *
   * <p>On va du point (12, 12) au point (12, 0), vecteur de mouvement : (0, -12)</p>
   * <p>On dépasse le poteau i vers le Sud</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterC(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("C1 : bouger", dataC[0], dataC[1], 0, DroneHelper.FACE_NORD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("C2 : attendre", null));
  }

  /**
   * MOUVEMENT D
   *
   * <p>On va du point (12, 0) au point (32, 0), vecteur de mouvement : (20, 0)</p>
   * <p>On va jusqu'au Sud du poteau ii</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterD(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("D1 : bouger", dataD[0], dataD[1], 0, DroneHelper.FACE_NORD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("D2 : attendre", null));
  }

  /**
   * MOUVEMENT E
   *
   * <p>On va du point (32, 0) au point (32, 12), vecteur de mouvement : Arc de 180° (0, 12),
   * concave vers l'Ouest, rayon de 6' (2m)</p>
   * <p>On fait un arc de 180° vers le Nord du poteau ii. L'arc est concave vers l'Ouest,
   * antihoraire.</p>
   *
   * <p>Après ce mouvement, le drone pointe vers le <b>Sud</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getCercleMovementTimer(String, float, int, int, Float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterE(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getCercleMovementTimer("E1 : 180°", dataE[0], DroneHelper.CERCLE_DEMI, DroneHelper.ORIENTATION_ANTIHORAIRE, dataE[1], DroneHelper.ROTATION_DROITE, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("E2 : attendre", null));
  }

  /**
   * MOUVEMENT F
   *
   * <p>On va du point (32, 12) au point (6, 18), vecteur de mouvement : (-24, 6)</p>
   * <p>On va vers le Sud du poteau iv, tout en passant <b>AU DESSUS</b> du poteau iii</p>
   *
   * <p>Le drone doit d'abord monter, puis descendre. Le mouvement est donc séparé en deux</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterF(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("F1 : bouger", dataF[0], dataF[1], dataF[2], DroneHelper.FACE_SUD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("F2 : bouger", dataF[0], dataF[1], - dataF[2], DroneHelper.FACE_SUD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("F3 : attendre", null));
  }

  /**
   * MOUVEMENT G
   *
   * <p>On va du point (6, 18) au point (6, 30), vecteur de mouvement : Arc de 180° (0, 12),
   * concave vers l'Est, rayon de 6' (2m)</p>
   * <p>On fait un arc de 180° vers le Nord du poteau iv. L'arc est concave vers l'Est, horaire.</p>
   *
   * <p>Après ce mouvement, le drone pointe vers le <b>Nord</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getCercleMovementTimer(String, float, int, int, Float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterG(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getCercleMovementTimer("G1 : 180°", dataG[0], DroneHelper.CERCLE_DEMI, DroneHelper.ORIENTATION_HORAIRE, dataG[1], DroneHelper.ROTATION_DROITE, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("G2 : attendre", null));
  }

  /**
   * MOUVEMENT H
   *
   * <p>On va du point (6, 30) au point (32, 18), vecteur de mouvement : (24, -12)</p>
   * <p>On va jusqu'au Sud du poteau v</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterH(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("H1 : bouger", dataH[0], dataH[1], 0, DroneHelper.FACE_NORD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("H2 : attendre", null));
  }

  /**
   * MOUVEMENT I
   *
   * <p>On va du point (32, 18) au point (32, 30), vecteur de mouvement : Arc de 180° (0, 12),
   * concave vers l'Ouest, rayon de 6' (2m)</p>
   * <p>On fait un arc de 180° vers le Nord du poteau v. L'arc est concave vers l'Ouest, antihoraire.</p>
   *
   * <p>Après ce mouvement, le drone pointe vers le <b>Sud</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getCercleMovementTimer(String, float, int, int, Float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterI(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getCercleMovementTimer("I1 : 180°", dataI[0], DroneHelper.CERCLE_DEMI, DroneHelper.ORIENTATION_ANTIHORAIRE, dataI[1], DroneHelper.ROTATION_DROITE, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("I2 : attendre", null));
  }

  /**
   * MOUVEMENT J
   *
   * <p>On va du point (32, 30) au point (19, 31.6), vecteur de mouvement : (-13, 1.6)</p>
   * <p>On va jusqu'au Sud du poteau vi</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterJ(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("J1 : bouger", dataJ[0], dataJ[1], 0, DroneHelper.FACE_SUD, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("J2 : attendre", null));
  }

  /**
   * MOUVEMENT K
   *
   * <p>On va du point (19, 31.6) au point (25, 37.6), vecteur de mouvement : Arc de 270° (6, 6),
   * concave vers l'Sud-Est, rayon de 6' (2m)</p>
   * <p>On fait un arc de 270° vers l'Est du poteau vi. L'arc est concave vers l'Sud-Est, horaire.</p>
   *
   * <p>Après ce mouvement, le drone pointe vers l'<b>Est</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getCercleMovementTimer(String, float, int, int, Float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterK(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getCercleMovementTimer("K1 : 270°", dataK[0], DroneHelper.CERCLE_TROIS_QUARTS, DroneHelper.ORIENTATION_HORAIRE, dataK[1], DroneHelper.ROTATION_DROITE, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("K2 : attendre", null));
  }

  /**
   * MOUVEMENT L
   *
   * <p>On va du point (25, 37.6) au point (19, 12), vecteur de mouvement : (-6, -15.6)</p>
   * <p>On va jusqu'à l'Ouest du poteau iii</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterL(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("L1 : bouger", dataL[0], dataL[1], 0, DroneHelper.FACE_EST, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("L2 : attendre", null));
  }

  /**
   * MOUVEMENT M
   *
   * <p>On va du point (19, 12) au point (19, 12), vecteur de mouvement : Arc de 360° (0, 0),
   * rayon de 6' (2m)</p>
   * <p>On fait un arc de 360° autour du poteau iii. L'arc est antihoraire.</p>
   *
   * <p>Après ce mouvement, le drone pointe vers l'<b>Est</b></p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getCercleMovementTimer(String, float, int, int, Float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterM(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getCercleMovementTimer("M1 : 360°", dataM[0], DroneHelper.CERCLE_COMPLET, DroneHelper.ORIENTATION_ANTIHORAIRE, dataM[1], DroneHelper.ROTATION_DROITE, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("M2 : attendre", null));
  }

  /**
   * MOUVEMENT N
   *
   * <p>On va du point (19, 12) au point (19, 0), vecteur de mouvement : (0, -12)</p>
   * <p>On va au Sud</p>
   *
   * @param mouvementTimers  {@link List} de {@link MouvementTimer} où ajouter les {@link MouvementTimer}
   *
   * @see DroneApplication#getDroneHelper()
   * @see DroneHelper#getMovementTimer(String, float, float, float, int, Integer)
   * @see DroneHelper#getAttenteMovementTimer(String, Integer)
   */
  private void ajouterN(List<MouvementTimer> mouvementTimers) {
    mouvementTimers.add(DroneApplication.getDroneHelper().getMovementTimer("N1 : bouger", dataN[0], dataN[1], 0, DroneHelper.FACE_EST, null));
    mouvementTimers.add(DroneApplication.getDroneHelper().getAttenteMovementTimer("N2 : attendre", DroneHelper.ATTERIR));
  }

  /**
   * Méthode pour n'effectuer qu'un seul mouvement. Sert au développement
   *
   * @param lettre La lettre du mouvement à faire
   *
   * @see #ajouterA(List)
   * @see DroneHelper
   * @see MouvementTimer
   */
  private void bouger(char lettre) {
    List<MouvementTimer> mouvementTimers = new ArrayList<>();

    switch(lettre) {
      case 'A':
        ajouterA(mouvementTimers);
        break;
      case 'B':
        ajouterB(mouvementTimers);
        break;
      case 'C':
        ajouterC(mouvementTimers);
        break;
      case 'D':
        ajouterD(mouvementTimers);
        break;
      case 'E':
        ajouterE(mouvementTimers);
        break;
      case 'F':
        ajouterF(mouvementTimers);
        break;
      case 'G':
        ajouterG(mouvementTimers);
        break;
      case 'H':
        ajouterH(mouvementTimers);
        break;
      case 'I':
        ajouterI(mouvementTimers);
        break;
      case 'J':
        ajouterJ(mouvementTimers);
        break;
      case 'K':
        ajouterK(mouvementTimers);
        break;
      case 'L':
        ajouterL(mouvementTimers);
        break;
      case 'M':
        ajouterM(mouvementTimers);
        break;
      case 'N':
        ajouterN(mouvementTimers);
        break;
      default:
        break;
    }

    DroneApplication.getDroneHelper().sendMovementTimerList(mouvementTimers);
  }
}