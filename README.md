# DJI Mobile SDK and OpenCV example : Drone AI #

**Main branch is dev**

**This program has a lot of inperfections and French. This is because it is a college project. I will translate any file in the repo, all you have to do is ask!**

I mainly leave this here so anyone using DJI's Mobile SDK will have a much less hard time than me. Note that DJI's Mobile SDK version used is 3.5.1. Also, the drone used was a DJI Matrice 100.

Single-man college project where I had to use a drone to do three specific goals : 

    A) Make the drone follow a precise course (featuring loops) -> Done
    B) Make the drone follow a line in the school's gymnasium   -> Mostly done (see #7 and #8)
    C) Make the drone find a ball in the school's gymnasium     -> Cancelled due to time restrictions

## How does it work? ##

Basically, you just install the app on your Android phone, link your device to DJI's flying controller, start both the drone and the controller and that's it. Everything else is pretty straight forward. There are currently 7 main activities : 

    Objective 1, step 1 : Takeoff and landing
    Objective 1, step 2 : Basic drone movements (pitch, roll, yaw)
    Objective 1, step 3 : Course using hardcoded movements (school project requirements)
    Objective 2, step 1 : Access and movement of gimbal
    Objective 2, step 2 : Camera video access
    Objective 2, step 3 : Line detection and tracing using OpenCV
    Objective 3, step 1 : Landing when a specific color is detected
    
Other than that, main files located in src/main/java/net/info420/fabien/dronetravailpratique for drone movement and general usage are the following : 

    application/DroneApplication.java   : Basic communication for drone; Should have been a helper class
    util/MouvementTimer.java            : Extends CountDownTimer; This is where the fun is at : class to send pitch/roll/yaw/throttle data to drone during a certain time;
    helpers/DroneHelper.java            : Class to build and interact with MouvementTimers; MouvementTimers can be sent as an array to helpers/DroneHelper.java. In this case, they will be executed one after the other; Most methods here should have been static methods located in util/MouvementTimer.java
    helpers/GimbalHelper.java           : Class for basic gimbal movements and setup
    helpers/CameraHelper.java           : Class for camera access and setup

I also recommend checking up those two activities, since they involve lots of previously mentionned classes usages :  

    activities/Obj1Etape3Activity.java  : Activity for course with hardcoded movements
    activities/Obj2Etape3Activity.java  : Activity for line detection and tracing using OpenCV

## Time flyes. What is really important in there? ##

Alright, the juicy stuff is mostly in helpers/DroneHelper.java and util/MouvementTimer.java. There is a hell lot of JavaDoc explaining all of it but it is **currently all doc'd in French** (see this README's first paragraph).

Really important methods are : 

    helpers/DroneHelper#sendMouvementTimer(MouvementTimer)                                    : Sends a single MouvementTimer
    helpers/DroneHelper#sendMouvementTimerList(List<MouvementTimer>)                          : Sends a list of MouvementTimer that will be executed one after the other
    helpers/DroneHelper#getMouvementTimer(String, Float[], Integer)                           : Returns a MouvementTimer built with sent pitch/roll/yaw/throttle data
    helpers/DroneHelper#getMouvementTimer(String, float, float, float, int, Integer)          : Returns a MouvementTimer built with (x, y) coordinates and elevation per second (yes, I am fucking serious)
    helpers/DroneHelper#getCercleMouvementTimer(String, float, int, int, Float, int, Integer) : Return a MouvementTimer for circular movement with sent radius, angle in circle quarters, orientation and rotation side
    util/MouvementTimer#onTick(long)                                                          : From CountDownTimer; Sends pitch/roll/yaw/throttle movement data to DJIFlightController
    util/MouvementTimer#onFinish()                                                            : From CountDownTimer; Starts next MouvementTimer, if there is one in the list
    
## Problems ##

Please be indulgent. This was a school project and I missed time to make perfect code.

    #1 Everything needs to be translated to English
    #2 MouvementTimer should extend an abstract class defining any timer that works with the drone (e.g. takeoff and landing). This way, anything concerning a timed task and the drone could extend the abstract class and be used in the same list as MouvementTimers.
    #3 Something should be used to manipulate a list of MouvementTimer other than what currently does the magic
    #4 Methods for gettings MouvementTimers should be static methods within util/MouvementTimer
    #5 Methods concerning drone in application/DroneApplication should be within a helper class
    #6 Course data is hardcoded
    #7 Line tracing does not support line turning
    #8 Line tracing only works with a single line color (green)
