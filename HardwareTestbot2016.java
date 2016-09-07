package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;
import static java.lang.Math.floor;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a TestBot2016.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left Front drive motor:  "left front motor"
 * Motor channel:  Left Rear drive motor:   "left rear motor"
 * Motor channel:  Right Front drive motor: "right front motor"
 * Motor channel:  Right Rear drive motor:  "right rear motor"
 * Servo channel:  Servo to latch:          "latch servo"
 * Servo channel:  CRServo:                 "continuous servo"
 */
public class HardwareTestbot2016
{
    /* Public OpMode members. */
    public static DcMotor  motorLeftFront = null;
    public static DcMotor  motorLeftRear = null;
    public static DcMotor  motorRightFront = null;
    public static DcMotor  motorRightRear = null;

    public static Servo    servoLatch = null;
    public static CRServo  servoContinous = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    public static final float MAX_SPEED = (float) 0.5;

    public static final float SERVO_LATCH_UP = (float) 1.0;
    public static final float SERVO_LATCH_DOWN = (float) 0.0;
    public static final float SERVO_LATCH_UP_SETPOINT = (float) 255;
    public static final float SERVO_LATCH_DOWN_SETPOINT = (float) 135;

    public static final float MOTOR_GEAR_TEETH = (float) 80;
    public static final float AXLE_GEAR_TEETH = (float) 40;
    public static final float WHEEL_DIAMETER = (float) 4.0;

    public static final float PULSES_PER_OUTPUT_NEVERREST20 = (float) 560; // NeverRest20
    public  static final float PULSES_PER_OUTPUT_NEVERREST40 = (float) 1120; // NeverRest40
    public static final float PULSES_PER_OUTPUT_NEVERREST60 = (float) 1680; // NeverRest60
    public static final float PULSES_PER_OUTPUT_TETRIX = (float) 1440; // Tetrix

    public static final float PULSES_PER_INCH = (float) ((PULSES_PER_OUTPUT_NEVERREST40
            * (MOTOR_GEAR_TEETH / AXLE_GEAR_TEETH)) / (PI * WHEEL_DIAMETER));

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareTestbot2016(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        motorLeftFront = hwMap.dcMotor.get("left front motor");
        motorLeftRear = hwMap.dcMotor.get("left rear motor");
        motorRightFront = hwMap.dcMotor.get("left right motor");
        motorRightRear = hwMap.dcMotor.get("right rear motor");
        motorLeftFront.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        motorLeftRear.setDirection(DcMotor.Direction.FORWARD);// Set to REVERSE if using AndyMark motors
        motorRightFront.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Set all motors to zero power
        motorLeftFront.setPower(0);
        motorLeftRear.setPower(0);
        motorRightFront.setPower(0);
        motorRightRear.setPower(0);

        // Set all motors to RUN_USING_ENCODER
        motorLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
        servoLatch = hwMap.servo.get("latch servo");
        servoLatch.setPosition(SERVO_LATCH_UP);

        // Define and initialize ALL installed CR servos.
        servoContinous = hwMap.crservo.get("continuous");
        servoContinous.setPower(0);

    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

