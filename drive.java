package org.firstinspires.ftc.teamcode;

public class drive {

    /*
	 * Robohackerz FTC 4605
	 * 3/31/2016 file modified
	 * 3/31/2016 hardware config modified
	 *
	 * A global header included in all official OpModes that defines variables and default values.
	 */

    //Robot variables
    public static final double driveGearRatio = 0.666666666666; //Gear ratio of motor to wheels
    public static int autoDelay = 0;
    public static boolean[] buttonChecking = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    public static String alliance = null;
    public static int turnMult;

    //Antistall
    public static int stallCount = 0;
    public static boolean stalled = false;

    //General servo positions
    public static final double hangLocked = 0;
    public static final double hangUnlocked = 50f / 255;
    public static final double climbFlipBack = 1;
    public static final double climbFlipTipped = 0;
    public static final double debrisLiftDown = 0;
    public static final double debrisLiftUp = 1830;
    public static double debrisLiftModifier = 0;
    public static final double debrisRampMiddle = 140f / 255;
    public static final double debrisRampRed = 108f / 255;
    public static final double debrisRampBlue = 175f / 255;
    public static double debrisRampDown;
    public static double debrisRampTwitched;
    public static double debrisLiftTarget;
    public static boolean debrisLiftManual = false;

    //Zip line positions
    public static double zipLineUp;
    public static double zipLineUpAlt;
    public static double zipLineHalf;
    public static double zipLineDown;
    public static final double zipLineLeftUp = 0f;
    public static final double zipLineLeftHalf = 90f / 255;
    public static final double zipLineLeftDown = 120f / 255;
    public static final double zipLineRightUp = 1f;
    public static final double zipLineRightHalf = 120f / 255;
    public static final double zipLineRightDown = 70f / 255;
    public static double zipBlock = 0;
    public static double zipBlockAlt = 1;

    //Gyro vars
    public static boolean gyroReady = false;
    public static byte[] arr; //Legacy module returns hi and low bytes in the form of an array
    public static float curHeading = 0;
    public static long delTime = 0;
    public static float prevHeading = 0;
    public static float curRate = 0;
    public static long timeOff = System.currentTimeMillis();
    public static long prevTime = -timeOff;
    public static long curTime = -timeOff;

    /*
     * Core scaling
     */

    public static float scaleMotor(float joyValue) {
        final double threshold = 0.1;		//Threshold to debounce joystick values
        final int Max_Motor_Speed = 1;	//Max Motor Value
        final float Max_Joy_Value = 1;	//Max Joystick Value

        if (joyValue > -threshold && joyValue < threshold) {
            return 0;
        }

        int direction = joyValue >= 0 ? 1 : -1; //Grab direction -> 1 or -1
        /*
         * Ternary operator above equivalent to "condition ? if true : if false"
         *
         * int direction;
         * if (joyValue >= 0) {
         *    direction = 1;
         * } else {
         *    direction = -1;
         * }
         */

        float ratio = (joyValue * joyValue) / (Max_Joy_Value * Max_Joy_Value); //Get 0-1 ratio to help determine speed
        return ratio * Max_Motor_Speed * direction; //Scale motor value based on ratio
    }
}
