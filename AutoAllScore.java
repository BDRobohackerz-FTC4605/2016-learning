package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.drive.*;
import static org.firstinspires.ftc.teamcode.drive.buttonChecking;
import static org.firstinspires.ftc.teamcode.drive.climbFlipBack;
import static org.firstinspires.ftc.teamcode.drive.climbFlipTipped;
import static org.firstinspires.ftc.teamcode.drive.debrisLiftDown;
import static org.firstinspires.ftc.teamcode.drive.debrisLiftManual;
import static org.firstinspires.ftc.teamcode.drive.debrisLiftModifier;
import static org.firstinspires.ftc.teamcode.drive.debrisLiftTarget;
import static org.firstinspires.ftc.teamcode.drive.debrisLiftUp;
import static org.firstinspires.ftc.teamcode.drive.debrisRampBlue;
import static org.firstinspires.ftc.teamcode.drive.debrisRampDown;
import static org.firstinspires.ftc.teamcode.drive.debrisRampMiddle;
import static org.firstinspires.ftc.teamcode.drive.debrisRampRed;
import static org.firstinspires.ftc.teamcode.drive.hangLocked;
import static org.firstinspires.ftc.teamcode.drive.hangUnlocked;
import static org.firstinspires.ftc.teamcode.drive.scaleMotor;
import static org.firstinspires.ftc.teamcode.drive.zipBlock;
import static org.firstinspires.ftc.teamcode.drive.zipBlockAlt;
import static org.firstinspires.ftc.teamcode.drive.zipLineDown;
import static org.firstinspires.ftc.teamcode.drive.zipLineHalf;
import static org.firstinspires.ftc.teamcode.drive.zipLineLeftDown;
import static org.firstinspires.ftc.teamcode.drive.zipLineLeftHalf;
import static org.firstinspires.ftc.teamcode.drive.zipLineLeftUp;
import static org.firstinspires.ftc.teamcode.drive.zipLineRightDown;
import static org.firstinspires.ftc.teamcode.drive.zipLineRightHalf;
import static org.firstinspires.ftc.teamcode.drive.zipLineRightUp;
import static org.firstinspires.ftc.teamcode.drive.zipLineUp;
import static org.firstinspires.ftc.teamcode.drive.zipLineUpAlt;

@Autonomous(name="EXP - Auto All Score", group="Autonomous")  // @Autonomous(...) is the other common choice
//@Disabled
public class AutoAllScore extends LinearOpMode {

	/*
	 * Robohackerz FTC 4605
	 * 2/14/2016
	 *
	 * An autonomous for tipping th climbers into the beacon and parking on the ramp
	 */

	LegacyModule lMod;

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
	public AutoAllScore() {
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
		lMod = hardwareMap.legacyModule.get("leg");
		rampKick = hardwareMap.servo.get("rampKick");

		climbFlip.setPosition(climbFlipBack);
		hangLock.setPosition(hangUnlocked);
		hardwareMap.servo.get("zipLineLeft").setPosition(0);
		hardwareMap.servo.get("zipLineRight").setPosition(1);
		debrisRamp.setPosition(debrisRampMiddle);
		rampKick.setPosition(0.5);

		Thread initSelect = new Thread(new InitSelect());
		initSelect.start();

		waitForStart();

		initSelect.interrupt();

		turnMult = alliance == "Blue" ? 1 : -1;

		sleep(autoDelay * 1000);

		curHeading = 0;

		Thread gyro = new Thread(new Gyro());
		gyro.start();

		sleep(100);

		gyroReady = true;

		//Move to pivot point
		inchMove(0.5, 6, 1120);
		sleep(500);
		turn(45 * turnMult);
		sleep(500);
		inchMove(0.5, 58.8675, 1120);
		sleep(500);
		turn(45 * turnMult);
		//sleep(500);
		//inchMove(0.5, 6, 1120);

		//Dump climbers into rescue beacon
		sleep(500);
		climbFlip.setPosition(climbFlipTipped);
		sleep(1000);
		climbFlip.setPosition(climbFlipBack);
		sleep(500);

		//Move to ramp
		//inchMove(-0.5, 6, 1120);
		//sleep(500);
		turn(-45 * turnMult, 1 * turnMult);
		sleep(500);
		inchMove(0.5, -17.465, 1120);
		sleep(500);
		turn(90 * turnMult);
		sleep(500);

		//Move onto ramp
		inchMove(0.5, 30, 1120);
	}

	public class Gyro implements Runnable {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					curHeading = 0;
					prevHeading = 0;
					lMod.enableAnalogReadMode(4);

					while (!gyroReady) {
						curHeading = 0;
						delTime = 99999;
						curTime = 0;
					}

					while (true) {
						arr = lMod.readAnalogRaw(4);
						curRate = arr[0] - 75f;

						if (Math.abs(curRate) > 0) {
							prevHeading = curHeading;
							curHeading = prevHeading + curRate * delTime * 0.001f;    //to update the current position
						}
						prevTime = curTime;
						sleep(10);                                                                                        //Wait a bit, and then manipulate the time
						curTime = System.currentTimeMillis() - timeOff;                                                                        //And repeat the cycle
						delTime = curTime - prevTime;
						telemetry.addData("Rate", curRate);
						telemetry.addData("Heading", curHeading);
						telemetry.addData("prevHeading", prevHeading);
						telemetry.addData("Time", delTime * 0.001);
					}
				} catch (Exception e) {
					e.getLocalizedMessage();
				}
			}
		}
	}

	public class InitSelect implements Runnable {
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

				if (checkButton(gamepad1.b, 7, 0)) {
					alliance = "Red";
				}
				if (checkButton(gamepad1.x, 8, 0)) {
					alliance = "Blue";
				}

				if (alliance != null) {
					telemetry.addData("Alliance selected", alliance);
				} else {
					telemetry.addData("***** WARNING *****", "Please select an alliance!");
				}
				telemetry.addData("Autonomous Delay", autoDelay + " seconds");
			}
		}
	}

	public class Antistall implements Runnable {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					int firstStallPosL;
					int firstStallPosR;
					int secondStallPosL;
					int secondStallPosR;
					double lPwr;
					double rPwr;
					stallCount = 0;
					stalled = false;
					while (true) {
						lPwr =  Math.abs(motorLeft.getPower());
						rPwr = Math.abs(motorRight.getPower());
						if (rPwr > 0.1 || lPwr > 0.1) {
							//Grab encoder positions
							firstStallPosR = motorRight.getCurrentPosition();
							firstStallPosL = motorLeft.getCurrentPosition();
							sleep(15);
							secondStallPosR = motorRight.getCurrentPosition();
							secondStallPosL = motorLeft.getCurrentPosition();

							//For reference, an encoder value of 133.something is an inch
							//If we're stalled now, increment the stall counter by 1, else reset
							stallCount = ((rPwr > 0.1 && Math.abs(secondStallPosR - firstStallPosR) < 25) || (lPwr > 0.1 && Math.abs(secondStallPosL - firstStallPosL) < 25)) ? stallCount + 1 : 0;

							if (stallCount >= 200) {
								stalled = true;
								stopMove();
								sleep(2000);
								stalled = false;
								stallCount = 0;
								motorRight.setPower(rPwr);
								motorLeft.setPower(lPwr);
							}
						} else {
							stallCount = 0;
						}

						telemetry.addData("Stall Count", stallCount);
						telemetry.addData("Stalled", stalled);
					}
				} catch (Exception e) {
					e.getLocalizedMessage();
				}
			}
		}
	}

	public void turn(float turnDeg) throws InterruptedException {
		//If no pivot side specified, assume additive turn
		int fixedSide = Math.signum(turnDeg) == 1 ? 1 : -1;
		turn(turnDeg, fixedSide, true);
	}
	public void turn(float turnDeg, int fixedSide) throws InterruptedException {
		turn(turnDeg, fixedSide, true);
	}
	public void turn(float turnDeg, int rawFix, boolean stopTheThing) throws InterruptedException {
		int fixedSide = (int)Math.signum(rawFix);
		float init = curHeading;
		float target = init + turnDeg;

		double power = 1 * Math.signum(turnDeg * fixedSide);

		if (fixedSide == -1) {
			move(0, power);
		} else {
			move(power, 0);
		}

		while ((Math.signum(turnDeg) == 1 && curHeading < target) || (Math.signum(turnDeg) == -1 && curHeading > target)) {
			//While we're on our way to our target, keep moving
			telemetry.addData("Moving", "True");
			telemetry.addData("Init", init);
			telemetry.addData("Target", target);
			telemetry.addData("Current", curHeading);
		}

		if (stopTheThing) {
			stopMove();
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
		if (!stalled) {
			motorRight.setPower(rPwr);
			motorLeft.setPower(lPwr);
		}
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
