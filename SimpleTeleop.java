package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Simple Teleop", group="Demo")  // @Autonomous(...) is the other common choice
//@Disabled

public class SimpleTeleop extends OpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * A simple teleop that includes a basic tank drive.
	 * Can be used with the demoBot or testBot
	 */


	DcMotor motorRight;
	DcMotor motorLeft;

    float left;
    float right;

	/**
	 * Constructor
	 */
	public SimpleTeleop() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	//Runs when OpMode loaded
	public void init() {
		//Get hardware map
		motorRight = hardwareMap.dcMotor.get("rMtr");
		motorLeft = hardwareMap.dcMotor.get("lMtr");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
	}


	public void start() {
		//Runs when OpMode starts
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Driver 1
		 * Driving and goal lock
		 */

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right
		left = gamepad1.left_stick_y;
		right = gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleMotor(right);
		left =  (float)scaleMotor(left);

		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		telemetry.addData("Text", "*** Robot Data ***");
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%f", left));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%f", right));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	double scaleMotor(double joyValue)
	{
		double threshold = 0.1;		//Threshold to debounce joystick values
		int Max_Motor_Speed = 1;	//Max Motor Value
		float Max_Joy_Value = 1;	//Max Joystick Value

		if (joyValue > -threshold && joyValue < threshold)
		{
			return 0;
		}

		int direction = joyValue >= 0 ? 1 : -1; //Grab direction -> 1 or -1
		double ratio = (joyValue * joyValue) / (Max_Joy_Value * Max_Joy_Value); //Get ratio to help determine speed
		return ratio * Max_Motor_Speed * direction; //Scale motor value based on ratio
	}
}
