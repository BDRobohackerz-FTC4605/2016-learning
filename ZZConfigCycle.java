package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="ZZ Config Cycle", group="ZZ")  // @Autonomous(...) is the other common choice
//@Disabled

public class ZZConfigCycle extends OpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * Cycles between available hardware using one hoystick in order to identify motors
	 * and servos without tracing wires
	 */

	public int buttonChecking = 0;
	public int active = 0;
	public int srvPos = 0;
	public int mtrPwr = 0;

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */


	DcMotor ma;
	DcMotor mb;
	DcMotor mc;
	DcMotor md;

	Servo sa;
	Servo sb;
	Servo sc;
	Servo sd;
	Servo se;
	Servo sf;

	/**
	 * Constructor
	 */
	public ZZConfigCycle() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	//Runs when OpMode loaded
	public void init() {
		//Get hardware map
		ma = hardwareMap.dcMotor.get("ma");
		mb = hardwareMap.dcMotor.get("mb");
		mc = hardwareMap.dcMotor.get("mc");
		md = hardwareMap.dcMotor.get("md");

		sa = hardwareMap.servo.get("sa");
		sb = hardwareMap.servo.get("sb");
		sc = hardwareMap.servo.get("sc");
		//sd = hardwareMap.servo.get("sd");
		//se = hardwareMap.servo.get("se");
		//sf = hardwareMap.servo.get("sf");
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
		 * Button press managing
		 */

		if (checkButton(gamepad1.dpad_up, 1, 0)) {
			active++;
			active = active > 10 ? 1 : active;
		}
		if (checkButton(gamepad1.dpad_down, 2, 0)) {
			active--;
			active = active < 1 ? 10 : active;
		}

		if (active >= 1 && active <= 4) {
			if (gamepad1.a) {
				mtrPwr = -1;
			} else if (gamepad1.y) {
				mtrPwr = 1;
			} else {
				mtrPwr = 0;
			}
		}

		ma.setPower(active == 1 ? mtrPwr : 0);
		mb.setPower(active == 2 ? mtrPwr: 0);
		mc.setPower(active == 3 ? mtrPwr : 0);
		md.setPower(active == 4 ? mtrPwr : 0);

		if (active >= 5 && active <= 10) {
			if (gamepad1.a) {
				srvPos = 1;
			} else if (gamepad1.y) {
				srvPos = 0;
			}

			if (active == 5) {
				sa.setPosition(srvPos);
			} else if (active == 6) {
				sb.setPosition(srvPos);
			} else if (active == 7) {
				sc.setPosition(srvPos);
			} /*else if (active == 8) {
				sd.setPosition(srvPos);
			} else if (active == 9) {
				se.setPosition(srvPos);
			} else if (active == 10) {
				sf.setPosition(srvPos);
			}*/
		}

		telemetry.addData("Active", "" + active);
		telemetry.addData("Type", (active >= 1 && active <= 4) ? "Motor" : "Servo");
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

	//Check for single button press, as opposed to holding down - Used mainly for toggles
	public boolean checkButton(boolean check, int btn, int trackEdge) {
		int edge = -1;

		//If we're not tracking a button, and we've pressed a button, log the press
		//Triggers on first pulse
		if (buttonChecking == 0 && check) {
			buttonChecking = btn;
			edge = 0;
		}

		//If button is being tracked, and we're no longer pressing it, stop tracking
		//Triggers on last pulse (when released)
		if (buttonChecking == btn && !check) {
			buttonChecking = 0;
			edge = 1;
		}

		//Return value based on leading or trailing edge
		//If edge has been triggere, and matches the edge we're watching, then return true, else false
		return trackEdge == edge;
	}
}
