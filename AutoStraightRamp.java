package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.drive.*;
import static org.firstinspires.ftc.teamcode.drive.autoDelay;
import static org.firstinspires.ftc.teamcode.drive.buttonChecking;
import static org.firstinspires.ftc.teamcode.drive.climbFlipBack;
import static org.firstinspires.ftc.teamcode.drive.debrisRampMiddle;
import static org.firstinspires.ftc.teamcode.drive.driveGearRatio;
import static org.firstinspires.ftc.teamcode.drive.hangUnlocked;

@Autonomous(name="Auto Straight Ramp", group="Autonomous")  // @Autonomous(...) is the other common choice
//@Disabled

public class AutoStraightRamp extends LinearOpMode {

	/*
	 * Robohackerz FTC 4605
	 * 2/4/2016
	 *
	 * An autonomous for moving straight to partially park on the mountain
	 */

	DcMotor motorRight;
	DcMotor motorLeft;

	Servo hangLock;
	Servo climbFlip;
	Servo zipLine;
	Servo debrisRamp;
	Servo rampKick;

	/**
	 * Constructor
	 */
	public AutoStraightRamp() {
	}

	@Override
	public void runOpMode() throws InterruptedException {
		motorRight = hardwareMap.dcMotor.get("rMtr");
		motorLeft = hardwareMap.dcMotor.get("lMtr");
		motorRight.setDirection(DcMotor.Direction.REVERSE);
		motorLeft.setDirection(DcMotor.Direction.FORWARD);
		hangLock = hardwareMap.servo.get("hangLock");
		climbFlip = hardwareMap.servo.get("climbFlip");
		zipLine = hardwareMap.servo.get("zipLineRight");
		debrisRamp = hardwareMap.servo.get("debrisRamp");
		rampKick = hardwareMap.servo.get("rampKick");

		climbFlip.setPosition(climbFlipBack);
		hangLock.setPosition(hangUnlocked);
		hardwareMap.servo.get("zipLineLeft").setPosition(0);
		hardwareMap.servo.get("zipLineRight").setPosition(1);
		debrisRamp.setPosition(debrisRampMiddle);
		rampKick.setPosition(0.5);

		waitForStart();

		//Move to pivot point
		inchMove(0.5, 96, 1120);
	}

	public class DelaySelect implements Runnable {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				if (autoDelay < 25 && (checkButton(gamepad1.dpad_up, 1, 0) || checkButton(gamepad1.left_stick_y < -0.5, 2, 0) || checkButton(gamepad1.right_stick_y < -0.5, 3, 0))) {
					//If delay less than max, and trying to increase, increment by 5
					autoDelay = autoDelay + 5;
				} else if (autoDelay > 0 && (checkButton(gamepad1.dpad_down, 4, 0) || checkButton(gamepad1.left_stick_y > 0.5, 5, 0) || checkButton(gamepad1.right_stick_y > 0.5, 6, 0))) {
					//If delay less than max, and trying to increase, increment by 5
					autoDelay = autoDelay - 5;
				}
				telemetry.addData("Autonomous Delay", autoDelay + " seconds");
			}
		}
	}

	public void inchMove(double pwr, double rawInches, int tickRev) {
		double inches = rawInches / driveGearRatio;
		//Get current position
		int init = motorRight.getCurrentPosition();

		//Determine net change
		double dir = Math.signum(pwr * inches);
		double net = dir * inchToEnc(Math.abs(inches), tickRev);

		//Set motor power and target encoder position
		double targPwr = dir * Math.abs(pwr);
		double target = init + net; //Add net change to current position to get target

		move(targPwr, targPwr);

		//Move motors
		while ((dir == 1 && motorRight.getCurrentPosition() < target) || (dir == -1 && motorRight.getCurrentPosition() > target)) {
			telemetry.addData("init", init);
			telemetry.addData("net", net);
			telemetry.addData("target", target);
		}
		stopMove();
	}

	//Move to an encoder position
	public void moveEnc(double pwr, int encVal) {
		double target = Math.floor((encVal - motorRight.getCurrentPosition()) / driveGearRatio);
		while (motorRight.getCurrentPosition() < Math.abs(target)) {
			move(pwr, pwr);
		}
		stopMove();
	}

	public void stopMove() {
		motorLeft.setPower(0);
		motorRight.setPower(0);
	}

	//Basic moving
	public void move(double lPwr, double rPwr) {
		motorRight.setPower(rPwr);
		motorLeft.setPower(lPwr);
	}

	double encPerInch(double ticks) {
		//Return encoder ticks per inch based on a PPR
		return ticks / (4 * Math.PI);
	}

	double inchToEnc(double inch, double ticks) {
		//Return encoder ticks in X inches based on PPR
		return Math.floor(inch * ticks / (4 * Math.PI));
	}

	//Check for single button press, as opposed to holding down - Used mainly for toggles
	public boolean checkButton(boolean check, int btn, int trackEdge) {
		int edge = -1;

		//If we're not tracking a button, and we've pressed a button, log the press
		//Triggers on first pulse
		if (!buttonChecking[btn - 1] && check) {
			buttonChecking[btn - 1] = true;
			edge = 0;
		}

		//If button is being tracked, and we're no longer pressing it, stop tracking
		//Triggers on last pulse (when released)
		if (buttonChecking[btn - 1] && !check) {
			buttonChecking[btn - 1] = false;
			edge = 1;
		}

		//Return value based on leading or trailing edge
		//If edge has been triggered, and matches the edge we're watching, then return true, else false
		return trackEdge == edge;
	}
}
