package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Simple Mecanum Teleop Backup", group="Demo")  // @Autonomous(...) is the other common choice
//@Disabled

public class SimpleMecanumTeleopBackup extends OpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * A simple teleop that includes a basic tank drive.
	 * Can be used with the demoBot or testBot
	 */


	DcMotor rf;
	DcMotor lf;
	DcMotor rb;
	DcMotor lb;

	float left;
	float right;

	/**
	 * Constructor
	 */
	public SimpleMecanumTeleopBackup() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	//Runs when OpMode loaded
	public void init() {
		//Get hardware map
		rf = hardwareMap.dcMotor.get("rf");
		lf = hardwareMap.dcMotor.get("lf");
		rb = hardwareMap.dcMotor.get("rb");
		lb = hardwareMap.dcMotor.get("lb");
		rf.setDirection(DcMotor.Direction.REVERSE);
		lf.setDirection(DcMotor.Direction.FORWARD);
		rb.setDirection(DcMotor.Direction.REVERSE);
		lb.setDirection(DcMotor.Direction.FORWARD);
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

		if (gamepad1.dpad_up) {
			//Forward
			rf.setPower(1);
			lf.setPower(1);
			rb.setPower(1);
			lb.setPower(1);
		} else if (gamepad1.dpad_down) {
			//Back
			rf.setPower(-1);
			lf.setPower(-1);
			rb.setPower(-1);
			lb.setPower(-1);
		} else if (gamepad1.dpad_left) {
			//Left
			rf.setPower(1);
			lf.setPower(-1);
			rb.setPower(-1);
			lb.setPower(1);
		} else if (gamepad1.dpad_right) {
			//Right
			rf.setPower(-1);
			lf.setPower(1);
			rb.setPower(1);
			lb.setPower(-1);
		} else {
			//Basic tank drive
			left = (float)scaleMotor(Range.clip(-gamepad1.left_stick_y, -1, 1));
			right = (float)scaleMotor(Range.clip(-gamepad1.right_stick_y, -1, 1));

			lf.setPower(left);
			lb.setPower(left);
			rf.setPower(right);
			rb.setPower(right);
		}
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
