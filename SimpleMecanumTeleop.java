package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Simple Mecanum Teleop", group="Demo")  // @Autonomous(...) is the other common choice
//@Disabled

public class SimpleMecanumTeleop extends OpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * A simple teleop that includes a basic tank drive.
	 * Can be used with the demoBot or testBot
	 */


	DcMotor rightFront;
	DcMotor leftFront;
	DcMotor rightBack;
	DcMotor leftBack;

	double rightFrontVal;
	double leftFrontVal;
	double rightBackVal;
	double leftBackVal;

	double drive;
	double strafe;
	double turn;

	float left;
	float right;

	/**
	 * Constructor
	 */
	public SimpleMecanumTeleop() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	//Runs when OpMode loaded
	public void init() {
		//Get hardware map
		rightFront = hardwareMap.dcMotor.get("rf");
		leftFront = hardwareMap.dcMotor.get("lf");
		rightBack = hardwareMap.dcMotor.get("rb");
		leftBack = hardwareMap.dcMotor.get("lb");
		rightFront.setDirection(DcMotor.Direction.REVERSE);
		leftFront.setDirection(DcMotor.Direction.FORWARD);
		rightBack.setDirection(DcMotor.Direction.REVERSE);
		leftBack.setDirection(DcMotor.Direction.FORWARD);
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

		//Joystick values
		drive = scaleMotor(-gamepad1.left_stick_y);
		strafe = scaleMotor(gamepad1.left_stick_x);
		turn = scaleMotor(-gamepad1.right_stick_x);

		leftFrontVal = drive + strafe + turn;
		leftBackVal = drive - strafe + turn;
		rightFrontVal = drive - strafe - turn;
		rightBackVal = drive + strafe - turn;

		//Check for max value, and scale appropriately
		double scale = 0;
		double[] rawList = {leftFrontVal, leftBackVal, rightFrontVal, rightBackVal};
		for (int i = 0, l = rawList.length; i < l; i++) {
			if (rawList[i] > scale) {
				scale = rawList[i];
			}
		}

		//If largest value greater than 1, keep scale, else set to 1 to not scale values
		scale = scale > 1 ? scale : 1;

		leftFront.setPower(Range.clip(leftFrontVal / scale, -1, 1));
		leftBack.setPower(Range.clip(leftBackVal / scale, -1, 1));
		rightFront.setPower(Range.clip(rightFrontVal / scale, -1, 1));
		rightBack.setPower(Range.clip(rightBackVal / scale, -1, 1));

		//Telemetry
		telemetry.addData("Chosen Scale", scale);
		telemetry.addData("Drive", drive);
		telemetry.addData("Strafe", strafe);
		telemetry.addData("Turn", turn);
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
