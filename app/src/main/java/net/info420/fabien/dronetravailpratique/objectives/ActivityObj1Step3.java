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
        move('A');
      }
    });

    mBtnMoveB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('B');
      }
    });

    mBtnMoveC.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('C');
      }
    });

    mBtnMoveD.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('D');
      }
    });

    mBtnMoveE.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('E');
      }
    });

    mBtnMoveF.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('F');
      }
    });

    mBtnMoveG.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('G');
      }
    });

    mBtnMoveH.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('H');
      }
    });

    mBtnMoveI.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('I');
      }
    });

    mBtnMoveJ.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('J');
      }
    });

    mBtnMoveK.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('K');
      }
    });

    mBtnMoveL.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('L');
      }
    });

    mBtnMoveM.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('M');
      }
    });

    mBtnMoveN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('N');
      }
    });
  }

  private int time(double meters) {
    return (int) (meters * 1000 * 0.875);
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

    addA(movementTimers);
    addB(movementTimers);
    addC(movementTimers);
    addD(movementTimers);
    addE(movementTimers);
    addF(movementTimers);
    addG(movementTimers);
    addH(movementTimers);
    addI(movementTimers);
    addJ(movementTimers);
    addK(movementTimers);
    addL(movementTimers);
    addM(movementTimers);
    addN(movementTimers);

    // Exécution des mouvements
    ApplicationDrone.getDroneMover().moveList(movementTimers);

    // Atterrissage
    // ApplicationDrone.getDroneMover().land();
  }

  private void addA(List<MovementTimer> movementTimers) {
    // MOUVEMENT A
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A1 : move", goForward[0], goForward[1], goForward[2], goForward[3], time(4), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("A2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addB(List<MovementTimer> movementTimers) {
    // MOUVEMENT B
    // On va du point (0, 12) au point (12,12)
    // Vecteur de mouvement : (12, 0)
    // On dépasse le poteau i vers l'Est

    // Le drone doit avancer de 12 pieds en à droite

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B1 : move", goRight[0], goRight[1], goRight[2], goRight[3], time(4), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("B2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addC(List<MovementTimer> movementTimers) {
    // MOUVEMENT C
    // On va du point (12, 12) au point (12, 0)
    // Vecteur de mouvement : (0, -12)
    // On dépasse le poteau i vers le Sud

    // Le drone doit avancer de 12 pieds en arrière

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C1 : move", goBack[0], goBack[1], goBack[2], goBack[3], time(4), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));

    // Préparation au prochain movement
    // On tourne le drone de 90° (horaire)

    // Tourner de 15°/s pendant 6s (90°)
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C3 : 90°", turnRight[0], turnRight[1], turnRight[2], turnRight[3], 6000, 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("C4 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addD(List<MovementTimer> movementTimers) {
    // MOUVEMENT D
    // On va du point (12, 0) au point (32, 0)
    // Vecteur de mouvement : (20, 0)
    // On va jusqu'au Sud du poteau ii

    // Le drone doit avancer de 32 pieds en avant

    // Avancer de 1 m/s pendant 11s (11m, environ 32')
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("D1 : move", goForward[0], goForward[1], goForward[2], goForward[3], time(11), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("D2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addE(List<MovementTimer> movementTimers) {
    // MOUVEMENT E
    // On va du point (32, 0) au point (32, 12)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau ii. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("E1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("E2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addF(List<MovementTimer> movementTimers) {
    // MOUVEMENT F
    // On va du point (32, 12) au point (6, 18)
    // Vecteur de mouvement : (-24, 6)
    // On va vers le Sud du poteau iv, tout en passant AU DESSUS du poteau iii

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("F1 : move", (6 / 24), 1, 0, 0, time(24), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("F2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addG(List<MovementTimer> movementTimers) {
    // MOUVEMENT G
    // On va du point (6, 18) au point (6, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Est, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau iv. L'arc est concave vers l'Est, horaire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("G1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("G2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addH(List<MovementTimer> movementTimers) {
    // MOUVEMENT H
    // On va du point (6, 30) au point (32, 18)
    // Vecteur de mouvement : (24, -12)
    // On va jusqu'au Sud du poteau v

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("H1 : move", (12 / 24), 1, 0, 0, time(24), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("H2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addI(List<MovementTimer> movementTimers) {
    // MOUVEMENT I
    // On va du point (32, 18) au point (32, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau v. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("I1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("I2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addJ(List<MovementTimer> movementTimers) {
    // MOUVEMENT J
    // On va du point (32, 30) au point (19, 31,6)
    // Vecteur de mouvement : (-13, 1,6)
    // On va jusqu'au Sud du poteau vi

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("J1 : move", (float) (1.6 / 13), 1, 0, 0, time(13), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("J2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addK(List<MovementTimer> movementTimers) {
    // MOUVEMENT K
    // On va du point (19, 31,6) au point (25, 37,6)
    // Vecteur de mouvement : Arc de 270° (6, 6), concave vers le Sud-Est, rayon de 6' (2m)
    // On fait un arc de 270° vers l'Est du poteau vi. L'arc est concave vers le Sud-Est, horaire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("K1 : 270°", 2, DroneMover.THREE_QUARTER_CIRCLE, DroneMover.CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("K2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addL(List<MovementTimer> movementTimers) {
    // MOUVEMENT L
    // On va du point (25, 37,6) au point (19, 12)
    // Vecteur de mouvement : (-6, -15,6)
    // On va jusqu'à l'Ouest du poteau iii

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("L1 : move", 1, (float) (6 / 15.6), 0, 0, time(15.6), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("L2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addM(List<MovementTimer> movementTimers) {
    // MOUVEMENT M
    // On va du point (19, 12) au point (19, 12)
    // Vecteur de mouvement : Arc de 360° (0, 0), rayon de 6' (2m)
    // On fait un arc de 360° autour du poteau iii. L'arc est antihoraire.

    movementTimers.add(ApplicationDrone.getDroneMover().getCircularMovementTimer("M1 : 360°", 2, DroneMover.FULL_CIRCLE, DroneMover.COUNTER_CLOCKWISE));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("M2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void addN(List<MovementTimer> movementTimers) {
    // MOUVEMENT N
    // On va du point (19, 12) au point (19, 0)
    // Vecteur de mouvement : (0, -12).
    // On va au Sud

    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("N1 : move", goForward[0], goForward[1], goForward[2], goForward[3], time(4), 100));

    // On attend 2 secondes
    movementTimers.add(ApplicationDrone.getDroneMover().getMovementTimer("N2 : wait", wait[0], wait[1], wait[2], wait[3], 2000, 100));
  }

  private void move(char letter) {
    List<MovementTimer> movementTimers = new ArrayList<MovementTimer>();

    switch(letter) {
      case 'A':
        addA(movementTimers);
        break;
      case 'B':
        addB(movementTimers);
        break;
      case 'C':
        addC(movementTimers);
        break;
      case 'D':
        addD(movementTimers);
        break;
      case 'E':
        addE(movementTimers);
        break;
      case 'F':
        addF(movementTimers);
        break;
      case 'G':
        addG(movementTimers);
        break;
      case 'H':
        addH(movementTimers);
        break;
      case 'I':
        addI(movementTimers);
        break;
      case 'J':
        addJ(movementTimers);
        break;
      case 'K':
        addK(movementTimers);
        break;
      case 'L':
        addL(movementTimers);
        break;
      case 'M':
        addM(movementTimers);
        break;
      case 'N':
        addN(movementTimers);
        break;
      default:
        break;
    }

    ApplicationDrone.getDroneMover().moveList(movementTimers);
  }
}