package net.info420.fabien.dronetravailpratique.util;

import android.os.CountDownTimer;
import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import java.util.List;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.util.DJICommonCallbacks;

// TODO : Prod : Enlever les noms des Timers
// TODO : Prod : Enlever les logs

/**
 * Classe enfant de {@link CountDownTimer}.
 *
 * <p>Permet d'envoyer des données de mouvement au drone afin qu'il bouge pendant le
 * {@link CountDownTimer}</p>
 *
 * <p>A une liste des prochaines mouvements. Dans son {@link #onFinish()}, le {@link MouvementTimer}
 * appelle appèle {@link #start()} du prochain {@link MouvementTimer} sur la liste et lui transmet
 * la suite de la liste.</p>
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-03-22
 *
 * @see CountDownTimer#onTick(long)
 * @see CountDownTimer#onFinish()
 * @see #onTick(long)
 * @see #onFinish()
 *
 *  @see  <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/flightController_concepts.html"
 *        target="_blank">
 *        Source : Concepts de base du {@link dji.sdk.flightcontroller.DJIFlightController}</a>
 */
public class MouvementTimer extends CountDownTimer {
  private final static String TAG = MouvementTimer.class.getName();

  // Variables des données du mouvement
  private float pitch     = 0;
  private float roll      = 0;
  private float yaw       = 0;
  private float throttle  = 0;

  // Liste des prochains MouvementsTimers de la List
  private List<MouvementTimer> nextMouvementTimers = null;

  private String nom;

  /**
   * Création d'un {@link MouvementTimer}
   *
   * <ul>
   *   <li>Applique la durée et l'interval au {@link CountDownTimer}</li>
   *   <li>Mets les données du mouvement du drone</li>
   * </ul>
   *
   * @param nom       Nom du timer, pour le debug
   * @param duree     Durée du timer, en millisecondes
   * @param interval  Interval entre les appels de {@link #onTick(long)}
   * @param pitch     Pitch du mouvement du drone
   * @param roll      Roll du mouvement du drone
   * @param yaw       Yaw du mouvement du drone
   * @param throttle  Throttle du mouvement du drone
   */
  public MouvementTimer(String nom, long duree, long interval, float pitch, float roll, float yaw, float throttle) {
    super(duree, interval);

    this.pitch    = pitch;
    this.roll     = roll;
    this.yaw      = yaw;
    this.throttle = throttle;

    this.nom      = nom == null ? "sans nom" : nom;
    Log.d(TAG, String.format("MouvementTimer %s : create %s pitch %s roll %s yaw %s throttle : %s %s", this.nom, pitch, roll, yaw, throttle, duree, interval));
  }

  /**
   * Reçoit l'instance de la {@link List} des prochains {@link MouvementTimer}
   *
   * @param nextMouvementTimers {@link List} des prochains {@link MouvementTimer}
   */
  public void setNextMovementTimers(List<MouvementTimer> nextMouvementTimers) {
    this.nextMouvementTimers = nextMouvementTimers;
  }

  /**
   * Appeler à chaque interval du {@link MouvementTimer} jusqu'à la fin de sa durée
   *
   * <p>Fait faire au drone le mouvement sauvegarder dans les variables</p>
   *
   * <ul>
   *   <li>Vérifie si le {@link dji.sdk.flightcontroller.DJIFlightController} est utilisable</li>
   *   <li>Envoie les données au drone afin qu'il exécute le mouvement</li>
   * </ul>
   *
   * @param interval  L'interval avant le prochain {@link #onTick(long)} du {@link MouvementTimer}
   *
   * @see CountDownTimer#onTick(long)
   * @see dji.sdk.flightcontroller.DJIFlightController#sendVirtualStickFlightControlData(DJIVirtualStickFlightControlData, DJICommonCallbacks.DJICompletionCallback)
   */
  @Override
  public void onTick(long interval) {
    if (DroneApplication.isFlightControllerAvailable()) {
      DroneApplication.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          pitch, roll, yaw, throttle
        ), new DJICommonCallbacks.DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {
            Log.d(TAG, djiError == null ? String.format("MouvementTimer %s : Mouvement avec pitch %s roll %s yaw %s throttle %s", nom, pitch, roll, yaw, throttle) : djiError.getDescription());
          }
        }
      );
    } else {
      Log.e(TAG, "Le DJIFlightController n'est pas utilisable!");
    }
  }

  /**
   * Exécuté à la fin de la durée du {@link MouvementTimer}
   *
   * <p>Si il y a un prochain timer dans la liste, il lui envoie les {@link List} de
   * {@link MouvementTimer} et le démarre avec {@link #start()}. Sinon, il arrête le drone.</p>
   *
   * <ul>
   *   <li>Vérifie si la {@link List} de {@link MouvementTimer} existe et contient des
   *   {@link MouvementTimer}.
   *   <ul>
   *     <li><b>Si oui</b>, va chercher le prochain {@link MouvementTimer} dans la liste.</li>
   *     <li>Vérifie ensuite si la {@link List} de {@link MouvementTimer} contient d'autre
   *     {@link MouvementTimer} que le prochain. Si oui, ajoute la {@link List} de
   *     {@link MouvementTimer} au prochain {@link MouvementTimer}.</li>
   *     <li>Démarre le prochain {@link MouvementTimer} avec {@link #start()}.</li>
   *     <li><b>Si non</b>, vérifie si le {@link dji.sdk.flightcontroller.DJIFlightController} est
   *     utilisable. Si c'est le cas, Envoie les données au drone afin qu'il s'arrête.</li>
   *   </ul></li>
   * </ul>
   *
   * @see #setNextMovementTimers(List)
   * @see CountDownTimer#start()
   * @see dji.sdk.flightcontroller.DJIFlightController#sendVirtualStickFlightControlData(DJIVirtualStickFlightControlData, DJICommonCallbacks.DJICompletionCallback)
   *
   */
  @Override
  public void onFinish() {
    // Si la List de MouvementTimer n'est pas null (le timer actuel n'est pas le dernier d'une liste)
    if ((nextMouvementTimers != null) && (nextMouvementTimers.size() != 0)) {
      // Prochain timer
      MouvementTimer nextMouvementTimer = nextMouvementTimers.get(0);

      // S'il reste plus qu'un prochain timer (si la liste est size = 1, alors la liste doit être null)
      if (nextMouvementTimers.size() > 1) {
        // Si le prochain timer n'est pas le dernier, alors on lui envoie la List de MouvementTimer, moins lui-même
        nextMouvementTimer.setNextMovementTimers(nextMouvementTimers.subList(1, nextMouvementTimers.size()));
      }

      // On débute le timer
      nextMouvementTimer.start();
    } else {
      // Sinon, on arrête tout.
      if (DroneApplication.isFlightControllerAvailable()) {
        DroneApplication.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            0, 0, 0, 0
          ), new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              Log.d(TAG, djiError == null ? String.format("MouvementTimer %s : Mouvement à zéro", nom) : djiError.getDescription());
            }
          }
        );
      }else {
        Log.e(TAG, "Le DJIFlightController n'est pas utilisable!");
      }
    }
  }
}