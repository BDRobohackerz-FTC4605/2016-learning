package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.robotcore.util.Range.clip;
import static com.qualcomm.robotcore.util.Range.scale;
import static java.lang.Math.abs;
import static org.firstinspires.ftc.teamcode.HardwareTestbot2016.MAX_SPEED;

/**
 * Created by Rob on 8/13/2016.
 */
public class LibraryFunctionsFTC4605 {
/*
* Constants
*/
    public static final float PULSES_PER_OUTPUT_NEVERREST20 = (float) 560; // NeverRest20
    public static final float PULSES_PER_OUTPUT_NEVERREST40 = (float) 1120; // NeverRest40
    public static final float PULSES_PER_OUTPUT_NEVERREST60 = (float) 1680; // NeverRest60
    public static final float PULSES_PER_OUTPUT_TETRIX = (float) 1440; // Tetrix

/*
* methods
*/
    static double scaleInput(double dVal)  {

/*
* This method scales the joystick input so for low joystick values, the
* scaled value is less than linear.  This is to make it easier to drive
* the robot more precisely at slower speeds.
*/

        double[] scaleArray = new double[]{ 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
                0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

    public static void tank(DcMotor motorLeftFront, DcMotor motorLeftRear,
                     DcMotor motorRightFront, DcMotor motorRightRear,
                     double LeftStick, double RightStick){

        motorLeftFront.setPower(clip(scaleInput(LeftStick), -MAX_SPEED, +MAX_SPEED));
        motorLeftRear.setPower(clip(scaleInput(LeftStick), -MAX_SPEED, +MAX_SPEED));
        motorRightFront.setPower(clip(scaleInput(RightStick), -MAX_SPEED, MAX_SPEED));
        motorRightRear.setPower(clip(scaleInput(RightStick), -MAX_SPEED, MAX_SPEED));
    }

    public static void arcade(DcMotor motorLeftFront,  DcMotor motorLeftRear,
                              DcMotor motorRightFront, DcMotor motorRightRear, 
                              double Speed, double Turn){
        holonomic(motorLeftFront,  motorLeftRear, motorRightFront, motorRightRear, Speed, Turn, 0.0);
    }

    public static void holonomic(DcMotor motorLeftFront,  DcMotor motorLeftRear,
                                 DcMotor motorRightFront, DcMotor motorRightRear, 
                                 double Speed, double Turn, double Strafe){

        double Magnitude = abs(Speed) + abs(Turn) + abs(Strafe);
        Magnitude = (Magnitude > 1) ? Magnitude : 1;

        motorLeftFront.setPower(scale((scaleInput(Speed) + scaleInput(Turn) + scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        motorLeftRear.setPower(scale((scaleInput(Speed) + scaleInput(Turn) - scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        motorRightFront.setPower(scale((scaleInput(Speed) - scaleInput(Turn) + scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
        motorRightRear.setPower(scale((scaleInput(Speed) - scaleInput(Turn) - scaleInput(Strafe)),
                -Magnitude, +Magnitude, -MAX_SPEED, +MAX_SPEED));
    }


}
