package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;
import net.info420.fabien.dronetravailpratique.util.DroneMover;
import net.info420.fabien.dronetravailpratique.util.MovementTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabien on 17-02-20.
 */

public class ActivityObj1Step3 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step2.class.getName();

  private Button mBtnTakeoff;
  private Button mBtnLand;
  private Button mBtnGo;

  private Button mBtnMoveA;
  private Button mBtnMoveB;
  private Button mBtnMoveC;
  private Button mBtnMoveD;
  private Button mBtnMoveE;
  private Button mBtnMoveF;
  private Button mBtnMoveG;
  private Button mBtnMoveH;
  private Button mBtnMoveI;
  private Button mBtnMoveJ;
  private Button mBtnMoveK;
  private Button mBtnMoveL;
  private Button mBtnMoveM;
  private Button mBtnMoveN;

  // private CountDownTimer movementTimer;
  // private MovementTimerTask movementTimerTask;

  // Ceci a été fait avec un tableau sur le site de DJI : https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-flightController.html (Roll Pitch Control Mode)
  // Important à savoir, on se sert du mode Velocity et Body
  // Rappelez vous, c'est en m/s (et °/s pour le Yaw). Mon timer dure en fonction.
  //                              p    r    y    t

  private float[] goForward = {   0,   1,   0,   0};
  private float[] goBack    = {   0,  -1,   0,   0};
  private float[] goLeft    = {  -1,   0,   0,   0};
  private float[] goRight   = {   1,   0,   0,   0};
  private float[] turnLeft  = {   0,   0, -15,   0};
  private float[] turnRight = {   0,   0,  15,   0};
  private float[] wait      = {   0,   0,   0,   0};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    ApplicationDrone.getDroneMover().enableVirtualStickMode();

    initUI();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    ApplicationDrone.getDroneMover().land();

    ApplicationDrone.getDroneMover().disableVirtualStickMode();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step3);

    mBtnTakeoff     = (Button) findViewById(R.id.btn_obj1_step3_takeoff);
    mBtnLand        = (Button) findViewById(R.id.btn_obj1_step3_stop);
    mBtnGo          = (Button) findViewById(R.id.btn_obj1_step3_run);

    mBtnMoveA = (Button) findViewById(R.id.btn_obj1_step3_a);
    mBtnMoveB = (Button) findViewById(R.id.btn_obj1_step3_b);
    mBtnMoveC = (Button) findViewById(R.id.btn_obj1_step3_c);
    mBtnMoveD = (Button) findViewById(R.id.btn_obj1_step3_d);
    mBtnMoveE = (Button) findViewById(R.id.btn_obj1_step3_e);
    mBtnMoveF = (Button) findViewById(R.id.btn_obj1_step3_f);
    mBtnMoveG = (Button) findViewById(R.id.btn_obj1_step3_g);
    mBtnMoveH = (Button) findViewById(R.id.btn_obj1_step3_h);
    mBtnMoveI = (Button) findViewById(R.id.btn_obj1_step3_i);
    mBtnMoveJ = (Button) findViewById(R.id.btn_obj1_step3_j);
    mBtnMoveK = (Button) findViewById(R.id.btn_obj1_step3_k);
    mBtnMoveL = (Button) findViewById(R.id.btn_obj1_step3_l);
    mBtnMoveM = (Button) findViewById(R.id.btn_obj1_step3_m);
    mBtnMoveN = (Button) findViewById(R.id.btn_obj1_step3_n);

    mBtnTakeoff.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().takeOff();
      }
    });

    mBtnLand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ApplicationDrone.getDroneMover().land();
      }
    });

    mBtnGo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        go();
      }
    });

    mBtnMoveA.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveA();
      }
    });

    mBtnMoveB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveB();
      }
    });

    mBtnMoveC.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveC();
      }
    });

    mBtnMoveD.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveD();
      }
    });

    mBtnMoveE.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveE();
      }
    });

    mBtnMoveF.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveF();
      }
    });

    mBtnMoveG.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveG();
      }
    });

    mBtnMoveH.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveH();
      }
    });

    mBtnMoveI.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveI();
      }
    });

    mBtnMoveJ.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveJ();
      }
    });

    mBtnMoveK.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveK();
      }
    });

    mBtnMoveL.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveL();
      }
    });

    mBtnMoveM.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveM();
      }
    });

    mBtnMoveN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        moveN();
      }
    });
  }

  private void go() {
    // Toutes les coordonnées sont en pieds
    // Nord : y positif. Est : x positif.

    // Coordonnées des poteaux
    // i    : (  6,    6 )
    // ii   : ( 32,    6 )
    // iii  : ( 19,   18 )
    // iv   : (  6,   24 )
    // v    : ( 32,   24 )
    // vi   : ( 19, 37,6 )

    // Tables des vecteurs de mouvement
    // A : (  0,    0 ) à (  0,   12 ) : (   0,    12 )
    // B : (  0,   12 ) à ( 12,   12 ) : (  12,     0 )
    // C : ( 12,   12 ) à ( 12,    0 ) : (   0,   -12 )
    // D : ( 12,    0 ) à ( 32,    0 ) : (  20,     0 )
    // E : ( 32,    0 ) à ( 32,   12 ) : (   0,    12 ), arc 180° antihoraire
    // F : ( 32,   12 ) à (  6,   18 ) : ( -24,     6 )
    // G : (  6,   18 ) à (  6,   30 ) : (   0,    12 ), arc 180° horaire
    // H : (  6,   30 ) à ( 32,   18 ) : (  24,   -12 )
    // I : ( 32,   18 ) à ( 32,   30 ) : (   0,    12 ), arc 180° antihoraire
    // J : ( 32,   30 ) à ( 19, 31,6 ) : ( -13,   1,6 )
    // K : ( 19, 31,6 ) à ( 25, 37,6 ) : (   6,     6 ), arc 270° horaire
    // L : ( 25, 37,6 ) à ( 19,   12 ) : (  -6, -15,6 )
    // M : ( 19,   12 ) à ( 19,   12 ) : (   0,     0 ), arc 360° antihoraire
    // N : ( 19,   12 ) à ( 19,    0 ) : (   0,   -12 )

    // Puisqu'on travaille en mode Velocity, body :
    // Pitch positif : vers la droite
    // Pitch négatif : vers la gauche
    // Roll  positif : vers l'avant
    // Roll  négatif : vers l'arrière

    // Décollage
    // ApplicationDrone.getDroneMover().takeOff();

    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    // MOUVEMENT A
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A1 : move", goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A3 : turn", turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A4 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // MOUVEMENT B
    // On va du point (0, 12) au point (12,12)
    // Vecteur de mouvement : (12, 0)
    // On dépasse le poteau i vers l'Est

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B1 : move", goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    // movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B3 : turn", turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));
    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("E1 : 90°", 0, DroneMover.QUARTER_CIRCLE, DroneMover.CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B4 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // MOUVEMENT C
    // On va du point (12, 12) au point (12, 0)
    // Vecteur de mouvement : (0, -12)
    // On dépasse le poteau i vers le Sud

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    // movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C1 : move", goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));
    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("E1 : 90°", 0, DroneMover.QUARTER_CIRCLE, DroneMover.CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    // movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C3 : move", turnLeft[0], turnLeft[1], turnLeft[2], turnLeft[3], 6000, 100));
    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("E1 : 90°", 0, DroneMover.QUARTER_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C4 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // MOUVEMENT D
    // On va du point (12, 0) au point (32, 0)
    // Vecteur de mouvement : (20, 0)
    // On va jusqu'au Sud du poteau ii

    // Le drone doit avancer de 32 pieds en avant

    // Avancer de 1 m/s pendant 11s (11m, environ 32')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("D1 : move", goForward[0], goForward[1], goForward[2], goForward[3], 11000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("D2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // MOUVEMENT E
    // On va du point (32, 0) au point (32, 12)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau ii. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("E1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // MOUVEMENT F
    // On va du point (32, 12) au point (6, 18)
    // Vecteur de mouvement : (-24, 6)
    // On va vers le Sud du poteau iv, tout en passant AU DESSUS du poteau iii

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("F1 : move", (6 / 24), 1, 0, 0, 24000, 100));

    // MOUVEMENT G
    // On va du point (6, 18) au point (6, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Est, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau iv. L'arc est concave vers l'Est, horaire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("G1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.CLOCKWISE));

    // MOUVEMENT H
    // On va du point (6, 30) au point (32, 18)
    // Vecteur de mouvement : (24, -12)
    // On va jusqu'au Sud du poteau v

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("F1 : move", (12 / 24), 1, 0, 0, 24000, 100));

    // MOUVEMENT I
    // On va du point (32, 18) au point (32, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau v. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("I1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // MOUVEMENT J
    // On va du point (32, 30) au point (19, 31,6)
    // Vecteur de mouvement : (-13, 1,6)
    // On va jusqu'au Sud du poteau vi

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("F1 : move", (float) (1.6 / 13), 1, 0, 0, 13000, 100));

    // MOUVEMENT K
    // On va du point (19, 31,6) au point (25, 37,6)
    // Vecteur de mouvement : Arc de 270° (6, 6), concave vers le Sud-Est, rayon de 6' (2m)
    // On fait un arc de 270° vers l'Est du poteau vi. L'arc est concave vers le Sud-Est, horaire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("K1 : 270°", 2, DroneMover.THREE_QUARTER_CIRCLE, DroneMover.CLOCKWISE));

    // MOUVEMENT L
    // On va du point (25, 37,6) au point (19, 12)
    // Vecteur de mouvement : (-6, -15,6)
    // On va jusqu'à l'Ouest du poteau iii

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("L1 : move", 1, (float) (6 / 15.6), 0, 0, 15600, 100));

    // MOUVEMENT M
    // On va du point (19, 12) au point (19, 12)
    // Vecteur de mouvement : Arc de 360° (0, 0), rayon de 6' (2m)
    // On fait un arc de 360° autour du poteau iii. L'arc est antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("M1 : 360°", 2, DroneMover.FULL_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // MOUVEMENT N
    // On va du point (19, 12) au point (19, 0)
    // Vecteur de mouvement : (0, -12).
    // On va au Sud

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("N1 : move", goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // Exécution des mouvements
    ApplicationDrone.getDroneMover().moveList(movementTimers);

    // Atterrissage
    // ApplicationDrone.getDroneMover().land();
  }

  private void moveA() {
    Log.d(TAG, "Début : Mouvement A");

    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    // MOUVEMENT A
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));

    ApplicationDrone.getDroneMover().moveList(movementTimers);

    Log.d(TAG, "Fin : Mouvement A");
  }

  private void moveB() {
    Log.d(TAG, "Début : Mouvement B");

    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    // MOUVEMENT D
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));

    ApplicationDrone.getDroneMover().moveList(movementTimers);

    Log.d(TAG, "Fin : Mouvement B");
  }

  private void moveC() {
    Log.d(TAG, "Début : Mouvement C");

    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    // MOUVEMENT C
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(goForward[0], goForward[1], goForward[2], goForward[3], 4000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));

    ApplicationDrone.getDroneMover().moveList(movementTimers);

    Log.d(TAG, "Fin : Mouvement C");
  }

  private void moveD() {
    Log.d(TAG, "Début : Mouvement D");

    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    // MOUVEMENT D
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(goForward[0], goForward[1], goForward[2], goForward[3], 11000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer(wait[0], wait[1], wait[2], wait[3], 2000, 100));

    ApplicationDrone.getDroneMover().moveList(movementTimers);

    Log.d(TAG, "Fin : Mouvement D");
  }

  private void moveE() {
    // Mouvement E
    // On va du point (32, 0) au point (32, 12)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest
    // On fait un arc de 180° vers le Nord du poteau ii. L'arc est concave vers l'Ouest, antihoraire.

    // TODO : Mouvement E
  }

  private void moveF() {
    // Mouvement F
    // On va du point (32, 12) au point (6, 18)
    // Vecteur de mouvement : (-24, 6)
    // On va vers le Sud du poteau iv, tout en passant à côté du poteau iii
    // TODO : Vérifier qu'il ne faut pas voler plus haut pour ne pas accrocher le poteau iii

    // TODO : Mouvement F
  }

  private void moveG() {
    // Mouvement G
    // On va du point (6, 18) au point (6, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Est
    // On fait un arc de 180° vers le Nord du poteau iv. L'arc est concave vers l'Est, horaire.

    // TODO : Mouvement G
  }

  private void moveH() {
    // Mouvement H
    // On va du point (6, 30) au point (32, 18)
    // Vecteur de mouvement : (24, -12)
    // On va jusqu'au Sud du poteau v

    // TODO : Mouvement H
  }

  private void moveI() {
    // Mouvement I
    // On va du point (32, 18) au point (32, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest
    // On fait un arc de 180° vers le Nord du poteau v. L'arc est concave vers l'Ouest, antihoraire.

    // TODO : Mouvement I
  }

  private void moveJ() {
    // Mouvement J
    // On va du point (32, 30) au point (19, 31,6)
    // Vecteur de mouvement : (-13, 1,6)
    // On va jusqu'au Sud du poteau vi

    // TODO : Mouvement J
  }

  private void moveK() {
    // Mouvement K
    // On va du point (19, 31,6) au point (25, 37,6)
    // Vecteur de mouvement : Arc de 270° (6, 6), concave vers le Sud-Est.
    // On fait un arc de 270° vers l'Est du poteau vi. L'arc est concave vers le Sud-Est, horaire.

    // TODO : Mouvement K
  }

  private void moveL() {
    // Mouvement L
    // On va du point (25, 37,6) au point (19, 12)
    // Vecteur de mouvement : (-6, -15,6)
    // On va jusqu'à l'Ouest du poteau iii

    // TODO : Mouvement L
  }

  private void moveM() {
    // Mouvement M
    // On va du point (19, 12) au point (19, 12)
    // Vecteur de mouvement : Arc de 360° (0, 0).
    // On fait un arc de 360° autour du poteau iii. L'arc est antihoraire.

    // TODO : Mouvement M
  }

  private void moveN() {
    // Mouvement N
    // On va du point (19, 12) au point (19, 0)
    // Vecteur de mouvement : (0, -12).
    // On va au Sud

    // TODO : Mouvement N
  }
}