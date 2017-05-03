package net.info420.fabien.dronetravailpratique.util;

import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import java.util.List;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightCoordinateSystem;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
import dji.common.flightcontroller.DJIVirtualStickVerticalControlMode;
import dji.common.flightcontroller.DJIVirtualStickYawControlMode;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by fabien on 17-03-22.
 */

// TODO : Documenter DroneBougeur

public class DroneBougeur {
  private static final String TAG = DroneBougeur.class.getName();

  public static final int ORIENTATION_HORAIRE     = 1;
  public static final int ORIENTATION_ANTIHORAIRE = -1;

  public static final int CERCLE_QUART            = 90;
  public static final int CERCLE_DEMI             = 180;
  public static final int CERCLE_TROIS_QUARTS     = 270;
  public static final int CERCLE_COMPLET          = 360;

  public static final int ROTATION_AVANT          = 0;
  public static final int ROTATION_DROITE         = 1;
  public static final int ROTATION_GAUCHE         = 2;
  public static final int ROTATION_ARRIERE        = 3;

  public static final int FACE_NORD               = 0;
  public static final int FACE_EST                = 1;
  public static final int FACE_OUEST              = 2;
  public static final int FACE_SUD                = 3;

  private MovementTimer mMovementTimer;

  public void startMotors() {
    // Il est nécéssaire de démarrer les moteurs. Ceci permet de "tester" le mouvement.
    DroneApplication.getAircraftInstance().getFlightController().turnOnMotors(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de démarrage des moteurs : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void decoller() {
    // TAKE OFF
    DroneApplication.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de décollage : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void atterir() {
    if (null != mMovementTimer) {
      mMovementTimer.cancel();
      mMovementTimer = null;
    }

    // LANDING
    DroneApplication.getAircraftInstance().getFlightController().autoLanding(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur d'atterissage : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void move(MovementTimer movementTimer) {
    if (null != mMovementTimer) {
      Log.d(TAG, "mMovementTimer is not null : cancelling");
      mMovementTimer.cancel();
      mMovementTimer = null;
    }

    if (null == mMovementTimer) {
      mMovementTimer = movementTimer;
      mMovementTimer.start();
    }
  }

  public void moveList(List<MovementTimer> movementTimers) {
    if (null != mMovementTimer) {
      Log.d(TAG, "mMovementTimer is not null : cancelling");
      mMovementTimer.cancel();
      mMovementTimer = null;
    }

    if (null == mMovementTimer) {
      // Premier timer
      mMovementTimer = movementTimers.get(0);

      // On change le premier timer, afin qu'il ait la liste des prochain timers
      mMovementTimer.setNextMovementTimers(movementTimers.subList(1, (movementTimers.size() - 1)));

      mMovementTimer.start();
    }
  }

  public MovementTimer getMovementTimer(float[] pitchRollYawThrottle) {
    Log.d(TAG, String.format("Creating MovementTimer no name : pitch %s roll %s yaw %s throttle %s", pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3]));
    return new MovementTimer("no_name", 1000, 100, pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3]);
  }

  public MovementTimer getMovementTimer(String name, float x, float y, float elevationPerSecond, int facingDirection) {
    Log.d(TAG, String.format("Creating MovementTimer %s : x %s y %s elevationPerSecond %s facingDirection %s", name, x, y, elevationPerSecond, facingDirection));

    // Mouvement nul
    if ((x == 0) && (y == 0)) {
      // Élévation avec un x = 0 et y = 0
      if (elevationPerSecond != 0) {
       return new MovementTimer(name, (long) elevationPerSecond * 1000, 100, 0, 0, 0, elevationPerSecond);
      }

      return getAttenteMovementTimer(name);
    }

    // On change x et y si le drone ne fais pas face au Nord
    // Si vous voulez une interprêtation simple de ce qui se passe ici, en voici une.
    // Le switch-case plus haut "flip" le plan. On peut ici considéré que le drone fait face au Nord, que le plan a "flippé" afin que +x soit toujours au Nord et +y toujours à l'Est.
    // Ainsi, on peut toujours associé un x positif à un pitch positif et un y positif à un roll positif.
    // J'espère que c'est un peu clair.
    switch (facingDirection) {
      case FACE_EST:
        x = -x;
        break;
      case FACE_OUEST:
        y = -y;
        break;
      case FACE_SUD:
        x = -x;
        y = -y;
        break;
    }

    // Si un des mouvement est égal à zéro, c'est facile. On va à 1 ou -1 m/s (en fonction du signe de la coordonnée non-nulle).
    // Le temps, c'est simplement la valeur absolue de la coordonnée non-nulle
    if (x == 0) {
      return new MovementTimer(name, (long) Math.abs(y) * 1000, 100, 0, (y > 0) ? 1 : -1, 0, elevationPerSecond); // Le drone ne va qu'en y
    } else if (y == 0) {
      return new MovementTimer(name, (long) Math.abs(x) * 1000, 100, (x > 0) ? 1 : -1, 0, 0, elevationPerSecond); // Le drone ne va qu'en x
    } else {
      // Sinon, c'est soit un mouvement diagonale, soit

      // Aussi, il est nécéssaire de vérifier si le mouvement (x et/ou y) est supérieur à 0.
      // Par exemple, si on bouge de (-2, 3), alors il va falloir bouger pendant 3 secondes en (- (2/3), 1)
      // Pour faire cela, je dois calculer le "2/3" avec les valeurs absolues, puis donner un résultat négatif ou positif en fonction de x (-2).
      // Sincérement, la meilleur technique pour comprendre ceci c'est de prendre des coordonnées et de calculer avec le if-then-else ci-dessous
      if (Math.abs(x) > Math.abs(y)) {
        // Le vecteur en X est supérieur à celui en Y.
        return new  MovementTimer(name, (long) Math.abs(x) * 1000, 100, (y > 0) ? Math.abs(y/x) : - Math.abs(y/x), (x > 0) ? 1 : -1, 0, elevationPerSecond); // Le drone va plus en x qu'en y, donc on se sert de x
      } else if (Math.abs(y) > Math.abs(x)) {
        // Le vecteur en Y est supérieur à celui en X.
        return new  MovementTimer(name, (long) Math.abs(y) * 1000, 100, (y > 0) ? 1 : -1, (x > 0) ? Math.abs(x/y) : - Math.abs(x/y), 0, elevationPerSecond); // Le drone va plus en x qu'en y, donc on se sert de x
      } else {
        // Ils sont égaux
        return new  MovementTimer(name, (long) Math.abs(x) * 1000, 100, (x > 0) ? 1 : -1, (y > 0) ? 1 : -1,  0, elevationPerSecond); // Le drone va autant en x qu'en y, donc on se sert de x, puisque ça n'a aucune importance
      }
    }
  }

  public MovementTimer getAttenteMovementTimer(String name) {
    Log.d(TAG, String.format("Creating waiting MovementTimer %s", name));

    return new MovementTimer(name, 1000, 100, 0, 0, 0, 0);
  }

  // Par défaut, de devant
  public MovementTimer getCercleMovementTimer(String name, int radius, int angle, int orientation, int rotationSide) {
    Log.d(TAG, String.format("Creating Circular MovementTimer %s : radius %s angle %s orientation %s rotationSide %s", name, radius, angle, orientation, rotationSide));

    // Données par défaut
    float pitch = 0;
    float roll  = 0;

    if (radius == 0) {
      return null;
    }

    // On a quatre arguments. Le rayon (2m, 3m), l'angle (90°, 180°), l'orientation (sens des aiguilles d'une montre) et le côté (avant, droite, ...)
    // La seule différence avec le côté, c'est vers où le drone va se déplacer durant sa rotation.
    switch (rotationSide) {
      case ROTATION_AVANT:
        roll = 1;
        break;
      case ROTATION_ARRIERE:
        roll = -1;
        break;
      case ROTATION_DROITE:
        pitch = 1;
        break;
      case ROTATION_GAUCHE:
        pitch = -1;
        break;
    }

    return new MovementTimer(name,                                    // Nom du timer
      (radius * (angle / CERCLE_QUART) * 1000),                     // Temps : rayon * nombre de quart de tour en seconde (3m 90° -> 3s à 1m/s, 6m 90° -> 6s à 1m/s, 3m 180° -> 6s à 1m/s)
      100,                                                            // Fréquence
      pitch,                                                          // Pitch
      roll,                                                           // Roll
      ((orientation * angle) / (radius * (angle / CERCLE_QUART))),  // Yaw : (orientation (contre la montre, ...) * angle) divisé par (rayon * nombre de quart de tour en seconde) (3m 90° -> (90)/(3*1) = 30°/s, 6m à 90° -> (90)/(6*1) = 15°/s, 3m à 180° -> (180)/(3*2) = 30°/s)
      0);                                                             // Throttle
  }

  public void setupFlightController() {
    // Source : https://developer.dji.com/mobile-sdk/documentation/introduction/flightController_concepts.html
    // Mode de base du drone

    // Ceci permet de se servir de coordonées x, y, z qui n'utilisent pas le nord magnétique
    DroneApplication.getAircraftInstance().getFlightController().setHorizontalCoordinateSystem(DJIVirtualStickFlightCoordinateSystem.Body);

    // Mise en place du mode de vélocité pour le roll et le pitch (m/s)
    DroneApplication.getAircraftInstance().getFlightController().setRollPitchControlMode(DJIVirtualStickRollPitchControlMode.Velocity);

    // Mise en place du mode de vélocité pour le Throttle (m/s)
    DroneApplication.getAircraftInstance().getFlightController().setVerticalControlMode(DJIVirtualStickVerticalControlMode.Velocity);

    // Mise en place du mode de vélocité angulaire pour le Yaw (°/s)
    DroneApplication.getAircraftInstance().getFlightController().setYawControlMode(DJIVirtualStickYawControlMode.AngularVelocity);

    // Activation du mode de contrôle par Virtual Stick
    DroneApplication.getAircraftInstance().getFlightController().enableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur d'activation du mode de contrôle par Virtual Stick : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void disableVirtualStickMode() {
    // Désactivation du mode de controle par Virtual Stick
    DroneApplication.getAircraftInstance().getFlightController().disableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de désactivation du mode de contrôle par Virtual Stick : %s" + djiError.getDescription()));
          }
        }
      }
    );
  }
}
