package net.info420.fabien.dronetravailpratique.helpers;

import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.util.MouvementTimer;

import java.util.List;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightCoordinateSystem;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
import dji.common.flightcontroller.DJIVirtualStickVerticalControlMode;
import dji.common.flightcontroller.DJIVirtualStickYawControlMode;
import dji.common.util.DJICommonCallbacks;

/**
 * Classe permettant d'interragir avec le Drone et de lui donner des instructions pour bouger
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-22
 */
public class DroneHelper {
  private static final String TAG = DroneHelper.class.getName();

  // Variables statiques pour le calcul de mouvement par vecteurs (x, y)
  public static final int FACE_NORD               = 0;
  public static final int FACE_OUEST              = 1;
  public static final int FACE_SUD                = 2;
  public static final int FACE_EST                = 3;

  // Variables statiques pour la rotation (sens horaire ou antihoraire)
  public static final int ORIENTATION_HORAIRE     = 1;
  public static final int ORIENTATION_ANTIHORAIRE = -1;

  // Variables statiques pour la rotation (quarts de cercle à faire)
  public static final int CERCLE_QUART            = 90;
  public static final int CERCLE_DEMI             = 180;
  public static final int CERCLE_TROIS_QUARTS     = 270;
  public static final int CERCLE_COMPLET          = 360;

  // Variables statiques pour la rotation (côté du drone où débute la rotation)
  public static final int ROTATION_AVANT          = 0;
  public static final int ROTATION_DROITE         = 1;
  public static final int ROTATION_GAUCHE         = 2;
  public static final int ROTATION_ARRIERE        = 3;

  // Pour savoir si le MouvementTimer doit attérir après
  public static final int ATTERIR         = 0;
  public static final int NE_PAS_ATTERIR  = 1;

  private MouvementTimer mouvementTimer;

  /**
   * Démarre les moteurs du drone
   *
   * @see dji.sdk.flightcontroller.DJIFlightController#turnOnMotors(DJICommonCallbacks.DJICompletionCallback)
   */
  public void demarrerMoteurs() {
    DroneApplication.getAircraftInstance().getFlightController().turnOnMotors(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès du démarrage des moteurs" : djiError.getDescription());
        }
      }
    );
  }

  /**
   * Fait décoller le drone
   *
   * @see dji.sdk.flightcontroller.DJIFlightController#takeOff(DJICommonCallbacks.DJICompletionCallback)
   */
  public void decoller() {
    DroneApplication.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès du décollage" : djiError.getDescription());
        }
      }
    );
  }

  /**
   * Fais attérir le drone
   *
   * <p>Avant de faire attérir le drone, vérifie si le {@link MouvementTimer} existe. Si oui,
   * l'arrête.</p>
   *
   * @see dji.sdk.flightcontroller.DJIFlightController#autoLanding(DJICommonCallbacks.DJICompletionCallback)
   * @see MouvementTimer
   */
  public void atterir() {
    if (null != mouvementTimer) {
      mouvementTimer.cancel();
      mouvementTimer = null;
    }

    DroneApplication.getAircraftInstance().getFlightController().autoLanding(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès de l'atterissage" : djiError.getDescription());
        }
      }
    );
  }

  /**
   * Fais bouger le drone avec un {@link MouvementTimer}
   *
   * <p>Avant de faire bouger le drone, vérifie si le {@link MouvementTimer} existe. Si oui,
   * l'arrête.</p>
   *
   * @param mouvementTimer le {@link MouvementTimer}
   *
   * @see MouvementTimer
   */
  public void sendMovementTimer(MouvementTimer mouvementTimer) {
    if (null != this.mouvementTimer) {
      this.mouvementTimer.cancel();
      this.mouvementTimer = null;
    }

    // Vérification de sécurité
    if (null == this.mouvementTimer) {
      this.mouvementTimer = mouvementTimer;
      this.mouvementTimer.start();
    }
  }

  /**
   * Fais bouger le drone avec une {@link List} de {@link MouvementTimer}
   *
   * <p>Avant de faire bouger le drone, vérifie si le {@link MouvementTimer} existe. Si oui,
   * l'arrête.</p>
   *
   * <p>Petite particularité ici : il faut ajouter la liste des prochains {@link MouvementTimer}
   * au premier {@link MouvementTimer} avec {@link MouvementTimer#setNextMovementTimers(List)}</p>
   *
   * @param  mouvementTimers {@link List} de {@link MouvementTimer}
   *
   * @see MouvementTimer
   * @see MouvementTimer#setNextMovementTimers(List)
   */
  public void sendMovementTimerList(List<MouvementTimer> mouvementTimers) {
    if (null != mouvementTimer) {
      mouvementTimer.cancel();
      mouvementTimer = null;
    }

    // Vérification de sécurité
    if (null == mouvementTimer) {
      mouvementTimer = mouvementTimers.get(0); // Premier timer

      // On change le premier timer, afin qu'il ait la liste des prochain timers
      mouvementTimer.setNextMovementTimers(mouvementTimers.subList(1, (mouvementTimers.size() - 1)));

      mouvementTimer.start();
    }
  }

  /**
   * Crée et retourne un {@link MouvementTimer} avec un tableau représentation le pitch, roll, yaw
   * et throttle du mouvement.
   *
   * @param   nom                   Nom à donner au {@link MouvementTimer}
   * @param   pitchRollYawThrottle  Tableau de float des données pitch, roll, yaw et throttle du
   *                                mouvement
   * @param   atterissage           Mode d'atterissage du drone, suite au mouvement
   * @return  Un {@link MouvementTimer} avec les données envoyées
   *
   * @see MouvementTimer
   */
  public MouvementTimer getMovementTimer(String nom, Float[] pitchRollYawThrottle, Integer atterissage) {
    return new MouvementTimer(nom, 1000, 100, pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3], atterissage);
  }

  /**
   * Crée et retourne un {@link MouvementTimer} avec un tableau représentation le pitch, roll, yaw
   * et throttle du mouvement.
   *
   * @param   nom                   Nom à donner au {@link MouvementTimer}
   * @param   duree                 Durée en millisecondes à donner au {@link MouvementTimer}
   * @param   pitchRollYawThrottle  Tableau de float des données pitch, roll, yaw et throttle du
   *                                mouvement
   * @param   atterissage           Mode d'atterissage du drone, suite au mouvement
   * @return  Un {@link MouvementTimer} avec les données envoyées
   *
   * @see MouvementTimer
   */
  public MouvementTimer getMovementTimer(String nom, int duree, Float[] pitchRollYawThrottle, Integer atterissage) {
    return new MouvementTimer(nom, duree, 100, pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3], atterissage);
  }

  /**
   * Retourne un {@link MouvementTimer} nul (qui ne bouge pas)
   *
   * @param   nom                 Nom à donner au {@link MouvementTimer}
   * @param   atterissage         Mode d'atterissage du drone, suite au mouvement
   * @return  Un {@link MouvementTimer} nul
   */
  public MouvementTimer getAttenteMovementTimer(String nom, Integer atterissage) {
    return new MouvementTimer(nom, 1000, 100, 0, 0, 0, 0, atterissage);
  }

  /**
   * Crée et retourne un {@link MouvementTimer} avec des coordonées (x, y) et la direction à laquelle
   * le drone fait face
   *
   * <p>Le code est assez complexe. <b>Je recommande de se fier au commentaires présents dans le
   * JavaDoc et d'observer le code en même temps afin de mieux la comprendre.</b> Je vais essayer de
   * faire un petit topo : </p>
   *
   * <ul>
   *   <li>On vérifie si le mouvement est nul (x, y) == (0, 0). Si c'est le cas, on vérifie s'il
   *   y a une élévation par seconde. Si c'est le cas, on fait monter le drone, tout en gardant un
   *   vecteur de mouvement en (x, y) nul. Sinon, on envoie simplement un {@link MouvementTimer} nul
   *   servant à l'attente (voir {@link #getAttenteMovementTimer(String, Integer)}).</li>
   *
   *   <li>Ceci est l'étape cruciale. Afin que le système de mouvement du drone par coordonées
   *   (x, y) soit fonctionnel, il est nécéssaire de vérifier la direction à laquelle le drone
   *   fait face. C'est ici que la magie opère : plutôt que de modifier les valeurs de pitch et roll
   *   qui seront envoyées au drone, on rotate plutôt le plan.
   *
   *   Je m'explique : Si le drone fait face au Sud, alors il suffit de faire x = -x et y = -y. De
   *   cette manière, les calculs du mouvements en pitch et en roll restent les mêmes. On a
   *   simplement « rotater » le plan. Il en va de même lorsque le drone fait face à l'Est. On rend
   *   alors x = -y et y = x. Lorsqu'il fait face à l'Ouest, on rend x = y et y = -x.</li>
   *
   *   <li>Ensuite, on peut créer un {@link MouvementTimer} avec des données de pitch et de roll, en
   *   fonction des coordonées (x, y) résultantes de l'opération précédente.</li>
   *
   *   <li>D'abord, on vérifie si une des coordonées (x ou y) est nulle. Ceci représente un
   *   mouvement « droit », soit non-diagonal. Si c'est le cas, on fait un {@link MouvementTimer}
   *   avec comme données :
   *
   *   <ul>
   *     <li>Temps                : En secondes, le nombre de mètres de la coordonées non-nulles;
   *     </li>
   *     <li>Coordonée nulle      : 0;</li>
   *     <li>Coordonée non-nulle  : 1 ou -1, en fonction de si la coordonée non-nulle est plus
   *     grande que 0.</li>
   *   </ul>
   *
   *   Par exemple, disons qu'on a le vecteur de mouvement (4, 0) avec un drone qui fait face au
   *   Nord. Dans ce cas, on fait un {@link MouvementTimer} de 4 secondes avec un pitch de 1 m/s et
   *   un roll de 0 m/s.
   *
   *   Un autre exemple : le vecteur (0, -2). On fait un {@link MouvementTimer} de 2 secondes (valeur
   *   absolue de -2), avec un pitch de 0 et un roll de -1 (-2 est négatif).</li>
   *
   *   <li>C'est ici que ça se corse. D'abord on vérifie quelle coordonées (x ou y) a la plus grande
   *   valeur absolue. On fait ensuite un {@link MouvementTimer} avec comme données :
   *
   *   <ul>
   *     <li>Temps                                        : En secondes, le nombre de mètres de la
   *                                                        coordonées avec la plus grande valeur
   *                                                        absolue;</li>
   *     <li>Coordonée avec la plus petite valeur absolue : La valeur absolue de la division entre
   *                                                        la coordonées avec la plus petite valeur
   *                                                        absolue et celle qui a la plus grande.
   *                                                        Cette valeur est positive ou négative en
   *                                                        fonction de si la coordonée est plus
   *                                                        grande que 0;</li>
   *     <li>Coordonée avec la plus grande valeur absolue : 1 ou -1, en fonction de si la coordonée
   *                                                        est plus grande que 0.</li>
   *   </ul>
   *
   *   Par exemple, disons qu'on a le vecteur de mouvement (7, 4) avec un drone qui fait face au
   *   Nord. Dans ce cas, on fait un {@link MouvementTimer} de 7 secondes, avec un pitch de 1 m/s et
   *   un roll de 4/7 m/s.
   *
   *   Un autre exemple : le vecteur (3, -5). On fait un {@link MouvementTimer} de 5 secondes (valeur
   *   absolue de -5), avec un pitch de 3/5 m/s (3 est positif) et un roll de -1 (-5 est négatif).
   *   </li>
   *
   *   <li>Dans le cas où les deux coordonées sont non-nulles, on fait un {@link MouvementTimer}
   *   d'une durée en secondes du nombre de mètres d'une des coordonées. Le pitch et le roll sont
   *   de 1 ou -1 en fonction de la positivité de la coordonée appropriée.</li>
   * </ul>
   *
   * <p>Je tiens quand même à mettre l'accent sur une chose : <b>regarder le code en lisant la liste
   * ci-dessus de ce qui se passe dans la méthode aide fortement à la compréhension de ladite
   * méthode.</b></p>
   *
   * @param   nom                 Nom à donner au {@link MouvementTimer}
   * @param   x                   Coordonée x du vecteur de mouvement
   * @param   y                   Coordonée y du vecteur de mouvement
   * @param   elevationParSeconde Élévation par seconde du drone (throttle en m/s)
   * @param   directionFace       Direction à laquelle le drone fait face
   * @param   atterissage         Mode d'atterissage du drone, suite au mouvement
   * @return  Un {@link MouvementTimer} avec les données envoyées
   *
   * @see #getAttenteMovementTimer(String, Integer)
   * @see MouvementTimer
   */
  public MouvementTimer getMovementTimer(String nom, float x, float y, float elevationParSeconde, int directionFace, Integer atterissage) {
    float temp;

    // Mouvement nul
    if ((x == 0) && (y == 0)) {
      // Élévation avec un x = 0 et y = 0
      if (elevationParSeconde != 0) {
       return new MouvementTimer(nom, (long) elevationParSeconde * 1000, 100, 0, 0, 0, elevationParSeconde, atterissage);
      }

      return getAttenteMovementTimer(nom, atterissage);
    }

    // Rotation du plan lorsque le drone ne fait pas face au Nord
    switch (directionFace) {
      case FACE_EST:
        temp = x;
        x = -y;
        y = temp;
        break;
      case FACE_OUEST:
        temp = x;
        x = y;
        y = -temp;
        break;
      case FACE_SUD:
        x = -x;
        y = -y;
        break;
    }

    // Vérification si le mouvement est « droit », soit non-diagonal
    if (x == 0) {
      // Le drone ne va qu'en y
      return new MouvementTimer( nom,
                                (long) Math.abs(y) * 1000,
                                100,
                                0,
                                (y > 0) ? 1 : -1,
                                0,
                                elevationParSeconde,
                                atterissage);
    } else if (y == 0) {
      // Le drone ne va qu'en x
      return new MouvementTimer( nom,
                                (long) Math.abs(x) * 1000,
                                100,
                                (x > 0) ? 1 : -1,
                                0,
                                0,
                                elevationParSeconde,
                                atterissage);
    } else {
      // Sinon, c'est un mouvement diagonal

      if (Math.abs(x) > Math.abs(y)) {
        // Le vecteur en X est supérieur à celui en Y.
        // Le drone va plus en x qu'en y, donc on se sert de x pour le temps
        return new MouvementTimer(nom,
                                  (long) Math.abs(x) * 1000,
                                  100,
                                  (x > 0) ? 1 : -1,
                                  (y > 0) ? Math.abs(y/x) : - Math.abs(y/x),
                                  0,
                                  elevationParSeconde,
                                  atterissage);
      } else if (Math.abs(y) > Math.abs(x)) {
        // Le vecteur en Y est supérieur à celui en X.
        // Le drone va plus en y qu'en x, donc on se sert de y pour le temps
        return new MouvementTimer(nom,
                                  (long) Math.abs(y) * 1000,
                                  100,
                                  (x > 0) ? Math.abs(x/y) : - Math.abs(x/y),
                                  (y > 0) ? 1 : -1,
                                  0,
                                  elevationParSeconde,
                                  atterissage);
      } else {
        // Ils sont égaux.
        // Le drone va autant en x qu'en y, donc on se sert de x pour le temps, puisque ça n'a
        // aucune importance.
        return new MouvementTimer(nom,
                                  (long) Math.abs(x) * 1000,
                                  100,
                                  (x > 0) ? 1 : -1,
                                  (y > 0) ? 1 : -1,
                                  0,
                                  elevationParSeconde,
                                  atterissage);
      }
    }
  }

  /**
   * Renvoie un {@link MouvementTimer} circulaire, en fonction du rayon, des quarts de cercles, de
   * l'orientation et du sens de rotation.
   *
   * <p>Le code est assez complexe. <b>Je recommande de se fier au commentaires présents dans le
   * JavaDoc et d'observer le code en même temps afin de mieux la comprendre.</b> Je vais essayer de
   * faire un petit topo : </p>
   *
   * <ul>
   *   <li>Par défaut, on met un pitch et un roll de 0. Si aucun degré d'ajout à l'orientation n'a
   *   été spécifié, on lui donne une valeur de 9. Ceci sert à recalibrer l'angle du mouvement,
   *   puisque la précision du drone ne peut pas être parfaite.</li>
   *
   *   <li>On ajoute ensuite à l'angle de rotation les degrés de recalibration.</li>
   *
   *   <li>Si le rayon est égal à 0, on ne retourne rien. Présentement, cette fonction ne sert pas
   *   à rotater le drone sur lui-même. On pourrait éventuellement ajouter cette fonctionnalité,
   *   mais je n'en avais pas besoin pour le parcours.</li>
   *
   *   <li>On vérifie ensuite vers où le drone doit se déplacer durant la rotation, en fonction
   *   du côté où il va effectuer la rotation. Par exemple, si la rotation est par l'avant du drone,
   *   c'est avec un roll positif qu'on fera la rotation. Si la rotation est par le côté gauche du
   *   drone, c'est avec un pitch négatif.</li>
   *
   *   <li>On retourne ensuite le {@link MouvementTimer} avec les données suivantes :
   *   <ul>
   *     <li>Temps          : rayon * nombre de quart de tour en seconde
   *     (3m 90° = 3s à 1m/s,
   *     6m 90° = 6s à 1m/s,
   *     3m 180° = 6s à 1m/s)</li>
   *     <li>Pitch et roll  : En fonction du côté de la rotation, tel que mentionné ci-dessus;</li>
   *     <li>Yaw            : (orientation * angle) divisé par (rayon * nombre de quart de tour)
   *     (3m 90° : (90)/(3*1) = 30°/s,
   *     6m à 90° : (90)/(6*1) = 15°/s,
   *     3m à 180° : (180)/(3*2) = 30°/s
   *     3m à -180° : (-180)/(3*2) = -30°/s)</li>
   *   </ul>
   *
   *   Dans le calcul du yaw, l'orientation (horaire, antihoraire) est en fait un simple 1 ou -1.</li>
   * </ul>
   *
   * <p>Je tiens quand même à mettre l'accent sur une chose : <b>regarder le code en lisant la liste
   * ci-dessus de ce qui se passe dans la méthode aide fortement à la compréhension de ladite
   * méthode.</b></p>
   *
   * @param   nom                 Nom à donner au {@link MouvementTimer}
   * @param   rayon               Rayon de l'arc de cercle, en mètres
   * @param   angle               Angle de l'arc de cercle, en quarts de tours
   * @param   orientation         Orientation de l'arc de cercles, soit horaire ou antihoraire.
   *                              C'est en fait 1 ou -1.
   * @param   ajoutOrientation    Degrés à ajouté à chaque quarts de cercle, afin de calibrer
   *                              correctement le mouvement circulaire.
   * @param   coteRotation        Côté du drone par laquelle on effectue la rotation. Ceci change
   *                              par où le drone va bouger pendant qu'il rotate.
   * @param   atterissage         Mode d'atterissage du drone, suite au mouvement
   * @return  Un {@link MouvementTimer} circulaire
   */
  public MouvementTimer getCercleMovementTimer(String nom, float rayon, int angle, int orientation, Float ajoutOrientation, int coteRotation, Integer atterissage) {
    // Données par défaut
    float pitch = 0;
    float roll  = 0;
    if (ajoutOrientation == null) ajoutOrientation = 9F;

    // Ajustement de l'angle, puisque le drone ne tourne pas toujours assez
    angle += (int) ((angle / CERCLE_QUART) * ajoutOrientation);

    if (rayon == 0) return null;

    // On a quatre arguments. Le rayon (2m, 3m, ...), l'angle (90°, 180°, ...), l'orientation (sens
    // des aiguilles d'une montre, ...) et le côté (avant, droite, ...)
    // La seule différence avec le côté, c'est vers où le drone va se déplacer durant sa rotation.
    switch (coteRotation) {
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

    return new MouvementTimer( nom,
                              (long) (rayon * (angle / CERCLE_QUART) * 1000),
                              100,
                              pitch,
                              roll,
                              ((orientation * angle) / (rayon * (angle / CERCLE_QUART))),
                              0,
                              atterissage);
  }

  /**
   *  Mets les règlages de base du {@link dji.sdk.flightcontroller.DJIFlightController}
   *
   *  <ul>
   *    <li>Met le système de coordonées horizontal en mode « Body ». Ceci permet de se servir de
   *    coordonées x, y, z qui n'utilisent pas le nord magnétique</li>
   *    <li>Met le contrôle du roll et du pitch en mode « Velocity ». Ceci permet de se servir d'un
   *    roll et d'un pitch en mètres par secondes, plutôt qu'en angle.</li>
   *    <li>Met le contrôle du yaw en mode « AngularVelocity». Ceci permet de se servir d'un yaw
   *    en degrés par secondes.</li>
   *    <li>Met le contrôle du throttle en mode « Velocity ». Ceci permet de se servir d'un throttle
   *    en mètres par secondes, plutôt qu'en angle.</li>
   *    <li>Active le mode de contrôle par Virtual Stick, qui est imite le fonctionnement de la
   *    manette.</li>
   *  </ul>
   *
   *  @see dji.sdk.flightcontroller.DJIFlightController#setHorizontalCoordinateSystem(DJIVirtualStickFlightCoordinateSystem)
   *  @see dji.sdk.flightcontroller.DJIFlightController#setRollPitchControlMode(DJIVirtualStickRollPitchControlMode)
   *  @see dji.sdk.flightcontroller.DJIFlightController#setYawControlMode(DJIVirtualStickYawControlMode)
   *  @see dji.sdk.flightcontroller.DJIFlightController#setVerticalControlMode(DJIVirtualStickVerticalControlMode)
   *  @see dji.sdk.flightcontroller.DJIFlightController#enableVirtualStickControlMode(DJICommonCallbacks.DJICompletionCallback)
   *
   *  @see  <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/flightController_concepts.html"
   *        target="_blank">
   *        Source : Concepts de base du {@link dji.sdk.flightcontroller.DJIFlightController}</a>
   */
  public void setupFlightController() {
    DroneApplication.getAircraftInstance().getFlightController().setHorizontalCoordinateSystem(DJIVirtualStickFlightCoordinateSystem.Body);

    DroneApplication.getAircraftInstance().getFlightController().setRollPitchControlMode(DJIVirtualStickRollPitchControlMode.Velocity);

    DroneApplication.getAircraftInstance().getFlightController().setYawControlMode(DJIVirtualStickYawControlMode.AngularVelocity);

    DroneApplication.getAircraftInstance().getFlightController().setVerticalControlMode(DJIVirtualStickVerticalControlMode.Velocity);

    DroneApplication.getAircraftInstance().getFlightController().enableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès de l'activation du mode de contrôle Virtual Stick" : djiError.getDescription());
        }
      }
    );
  }

  /**
   * Désactive le mode de contrôle Virtual Stick du {@link dji.sdk.flightcontroller.DJIFlightController}
   *
   * @see dji.sdk.flightcontroller.DJIFlightController#disableVirtualStickControlMode(DJICommonCallbacks.DJICompletionCallback)
   */
  public void disableVirtualStickMode() {
    DroneApplication.getAircraftInstance().getFlightController().disableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès de la désactivation du mode de contrôle Virtual Stick" : djiError.getDescription());
        }
      }
    );
  }
}
