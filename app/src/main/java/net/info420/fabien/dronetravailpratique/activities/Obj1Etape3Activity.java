package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.util.DroneMover;
import net.info420.fabien.dronetravailpratique.util.MovementTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link android.app.Activity} qui fait le parcours prédéfinit
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-02-20
 *
 * @see DroneMover
 * @see MovementTimer
 */
public class Obj1Etape3Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape2Activity.class.getName();

  private float[] MOVE_A = { 0,     4};
  private float[] MOVE_B = { 3.5F,  0};
  private float[] MOVE_C = { 0,     -4};
  private float[] MOVE_D = { 6,     0};
  // Mouvement E : 180°
  private float[] MOVE_F = {-3,     2.5F,    0.3F};
  // Mouvement G : 180°
  private float[] MOVE_H = { 6,    -4};
  // Mouvement I : 180°
  private float[] MOVE_J = {-3,     3};
  // Mouvement K : 270°
  private float[] MOVE_L = {-3,    -6};
  // Mouvement M : 360°
  private float[] MOVE_N = { 0,    -4};

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
   * @see DroneMover#enableVirtualStickMode()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DroneApplication.getDroneBougeur().enableVirtualStickMode();

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
   * @see DroneMover#atterir()
   * @see DroneMover#disableVirtualStickMode()
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();

    DroneApplication.getDroneBougeur().atterir();

    DroneApplication.getDroneBougeur().disableVirtualStickMode();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * @see #move(char)
   * @see DroneMover#decoller()
   * @see DroneMover#atterir()
   * @see net.info420.fabien.dronetravailpratique.util.MovementTimer
   */
  private void initUI(){
    setContentView(R.layout.activity_obj1_etape3);


    findViewById(R.id.btn_obj1_etape3_decoller).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneBougeur().decoller();
      }
    });
    findViewById(R.id.btn_obj1_etape3_arreter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneBougeur().atterir();
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
        move('A');
      }
    });
    findViewById(R.id.btn_obj1_etape3_b).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('B');
      }
    });
    findViewById(R.id.btn_obj1_etape3_c).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('C');
      }
    });
    findViewById(R.id.btn_obj1_etape3_d).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('D');
      }
    });
    findViewById(R.id.btn_obj1_etape3_e).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('E');
      }
    });
    findViewById(R.id.btn_obj1_etape3_f).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('F');
      }
    });
    findViewById(R.id.btn_obj1_etape3_g).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('G');
      }
    });
    findViewById(R.id.btn_obj1_etape3_h).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('H');
      }
    });
    findViewById(R.id.btn_obj1_etape3_i).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('I');
      }
    });
    findViewById(R.id.btn_obj1_etape3_j).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('J');
      }
    });
    findViewById(R.id.btn_obj1_etape3_k).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('K');
      }
    });
    findViewById(R.id.btn_obj1_etape3_l).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('L');
      }
    });
    findViewById(R.id.btn_obj1_etape3_m).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('M');
      }
    });
    findViewById(R.id.btn_obj1_etape3_n).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        move('N');
      }
    });
  }

  /**
   * Exécute le parcours
   *
   * <p>Toutes les coordonnées sont en pieds</p>
   * <p>Nord : y positif. Est : x positif.</p>
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
   *   <li>M : ( 19,   12 ) à ( 19,   12 ) : (   0,     0 ), arc 360° antihoraire</li>
   *   <li>N : ( 19,   12 ) à ( 19,    0 ) : (   0,   -12 )</li>
   * </ul>
   */
  private void go() {
    // TODO : La doc est rendue là
    // Puisqu'on travaille en mode Velocity, body :
    // Pitch positif : vers la droite
    // Pitch négatif : vers la gauche
    // Roll  positif : vers l'avant
    // Roll  négatif : vers l'arrière

    // Décollage
    // DroneApplication.getDroneBougeur().decoller();

    List<MovementTimer> movementTimers = new ArrayList<>();

    // movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("Post-takeoff : wait"));

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

    // movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("Pre-atterir : wait"));

    // Exécution des mouvements
    DroneApplication.getDroneBougeur().moveList(movementTimers);

    // Atterrissage
    // DroneApplication.getDroneBougeur().atterir();
  }

  private void addA(List<MovementTimer> movementTimers) {
    // Test : OK
    // MOUVEMENT A
    // On va du point (0, 0) au point (0,12)
    // Vecteur de mouvement : (0, 12)
    // On dépasse le poteau i vers le Nord

    // Le drone doit avancer de 12 pieds en avant

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("A1 : move", goForward[0], goForward[1], goForward[2], goForward[3], traductMetersToTimeForDumbDrone(4), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("A1 : move", MOVE_A[0], MOVE_A[1], 0, DroneMover.FACING_NORTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("A2 : wait"));
  }

  private void addB(List<MovementTimer> movementTimers) {
    // Test : OK
    // MOUVEMENT B
    // On va du point (0, 12) au point (12,12)
    // Vecteur de mouvement : (12, 0)
    // On dépasse le poteau i vers l'Est

    // Le drone doit avancer de 12 pieds en à droite

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("B1 : move", goRight[0], goRight[1], goRight[2], goRight[3], traductMetersToTimeForDumbDrone(4), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("B1 : move", MOVE_B[0], MOVE_B[1], 0, DroneMover.FACING_NORTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("B2 : wait"));
  }

  private void addC(List<MovementTimer> movementTimers) {
    // Test : OK
    // MOUVEMENT C
    // On va du point (12, 12) au point (12, 0)
    // Vecteur de mouvement : (0, -12)
    // On dépasse le poteau i vers le Sud

    // Le drone doit avancer de 12 pieds en arrière

    // Avancer de 1 m/s pendant 4s (4m, environ 12')
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("C1 : move", goBack[0], goBack[1], goBack[2], goBack[3], traductMetersToTimeForDumbDrone(4), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("C1 : move", MOVE_C[0], MOVE_C[1], 0, DroneMover.FACING_NORTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("C2 : wait"));
  }

  private void addD(List<MovementTimer> movementTimers) {
    // Test : OK
    // MOUVEMENT D
    // On va du point (12, 0) au point (32, 0)
    // Vecteur de mouvement : (20, 0)
    // On va jusqu'au Sud du poteau ii

    // Le drone doit avancer de 32 pieds en avant

    // Avancer de 1 m/s pendant 11s (11m, environ 32')
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("D1 : move", goRight[0], goRight[1], goRight[2], goRight[3], traductMetersToTimeForDumbDrone(6), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("D1 : move", MOVE_D[0], MOVE_D[1], 0, DroneMover.FACING_NORTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("D2 : wait"));
  }

  private void addE(List<MovementTimer> movementTimers) {
    // Test : OK
    // MOUVEMENT E
    // On va du point (32, 0) au point (32, 12)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau ii. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(DroneApplication.getDroneBougeur().getCircularMovementTimer("E1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE, DroneMover.RIGHT_ROTATION));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("E2 : wait"));

    // Le drone pointe désormais vers le Sud
  }

  private void addF(List<MovementTimer> movementTimers) {
    // Test : Stall?
    // MOUVEMENT F
    // On va du point (32, 12) au point (6, 18)
    // Vecteur de mouvement : (-24, 6)
    // On va vers le Sud du poteau iv, tout en passant AU DESSUS du poteau iii

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    // On monte
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("F1 : move", 1, -(3 / 12), 0, (float) 0.1, traductMetersToTimeForDumbDrone(12), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("F1 : move", MOVE_F[0], MOVE_F[1], MOVE_F[2], DroneMover.FACING_SOUTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("F2 : wait"));

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    // On descend
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("F1 : move", 1, -(3 / 12), 0, (float) -0.1, traductMetersToTimeForDumbDrone(12), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("F3 : move", MOVE_F[0], MOVE_F[1], - MOVE_F[2], DroneMover.FACING_SOUTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("F4 : wait"));
  }

  private void addG(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT G
    // On va du point (6, 18) au point (6, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Est, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau iv. L'arc est concave vers l'Est, horaire.

    movementTimers.add(DroneApplication.getDroneBougeur().getCircularMovementTimer("G1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.CLOCKWISE, DroneMover.RIGHT_ROTATION));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("G2 : wait"));

    // Le drone pointe désormais vers le Nord
  }

  private void addH(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT H
    // On va du point (6, 30) au point (32, 18)
    // Vecteur de mouvement : (24, -12)
    // On va jusqu'au Sud du poteau v

    // Puisque le mouvement est en diagonale, je rentre moi-même les données
    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("H1 : move", 1, - (12 / 24), 0, 0, traductMetersToTimeForDumbDrone(24), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("H1 : move", MOVE_H[0], MOVE_H[1], 0, DroneMover.FACING_NORTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("H2 : wait"));
  }

  private void addI(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT I
    // On va du point (32, 18) au point (32, 30)
    // Vecteur de mouvement : Arc de 180° (0, 12), concave vers l'Ouest, rayon de 6' (2m)
    // On fait un arc de 180° vers le Nord du poteau v. L'arc est concave vers l'Ouest, antihoraire.

    movementTimers.add(DroneApplication.getDroneBougeur().getCircularMovementTimer("I1 : 180°", 2, DroneMover.HALF_CIRCLE, DroneMover.COUNTER_CLOCKWISE, DroneMover.RIGHT_ROTATION));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("I2 : wait"));

    // Le drone pointe désormais vers le Sud
  }

  private void addJ(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT J
    // On va du point (32, 30) au point (19, 31,6)
    // Vecteur de mouvement : (-13, 1,6)
    // On va jusqu'au Sud du poteau vi

    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("J1 : move", (float) (1.6 / 13), -1, 0, 0, traductMetersToTimeForDumbDrone(13), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("J1 : move", MOVE_J[0], MOVE_J[1], 0, DroneMover.FACING_SOUTH));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("J2 : wait"));
  }

  private void addK(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT K
    // On va du point (19, 31,6) au point (25, 37,6)
    // Vecteur de mouvement : Arc de 270° (6, 6), concave vers le Sud-Est, rayon de 6' (2m)
    // On fait un arc de 270° vers l'Est du poteau vi. L'arc est concave vers le Sud-Est, horaire.

    movementTimers.add(DroneApplication.getDroneBougeur().getCircularMovementTimer("K1 : 270°", 2, DroneMover.THREE_QUARTER_CIRCLE, DroneMover.CLOCKWISE, DroneMover.RIGHT_ROTATION));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("K2 : wait"));

    // Le drone pointe désormais vers l'Est
  }

  private void addL(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT L
    // On va du point (25, 37,6) au point (19, 12)
    // Vecteur de mouvement : (-6, -15,6)
    // On va jusqu'à l'Ouest du poteau iii

    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("L1 : move", 1, (float) - (6 / 15.6), 0, 0, traductMetersToTimeForDumbDrone(15.6), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("L1 : move", MOVE_L[0], MOVE_L[1], 0, DroneMover.FACING_EAST));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("L2 : wait"));
  }

  private void addM(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT M
    // On va du point (19, 12) au point (19, 12)
    // Vecteur de mouvement : Arc de 360° (0, 0), rayon de 6' (2m)
    // On fait un arc de 360° autour du poteau iii. L'arc est antihoraire.

    movementTimers.add(DroneApplication.getDroneBougeur().getCircularMovementTimer("M1 : 360°", 2, DroneMover.FULL_CIRCLE, DroneMover.COUNTER_CLOCKWISE, DroneMover.RIGHT_ROTATION));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("M2 : wait"));
  }

  private void addN(List<MovementTimer> movementTimers) {
    // Test : À faire
    // MOUVEMENT N
    // On va du point (19, 12) au point (19, 0)
    // Vecteur de mouvement : (0, -12).
    // On va au Sud

    // movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("N1 : move", goRight[0], goRight[1], goRight[2], goRight[3], traductMetersToTimeForDumbDrone(4), 100));
    movementTimers.add(DroneApplication.getDroneBougeur().getMovementTimer("N1 : move", MOVE_N[0], MOVE_N[1], 0, DroneMover.FACING_EAST));

    // On attend 2 secondes
    movementTimers.add(DroneApplication.getDroneBougeur().getWaitingMovementTimer("N2 : wait"));
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

    DroneApplication.getDroneBougeur().moveList(movementTimers);
  }
}