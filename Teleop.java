package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


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

@TeleOp(name="Teleop", group="Tele-op")  // @Autonomous(...) is the other common choice
//@Disabled
//    manager.register("Teleop", Teleop.class);

public class Teleop extends OpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * Our main teleop program
	 */

	String alliance = null;

	DeviceInterfaceModule cdim; //Core Device Interface Module

	DcMotor motorRight;
	DcMotor motorLeft;
	DcMotor motorHang;
	DcMotor debrisLift;
	DcMotor sweeper;

	Servo hangLock;
	Servo climbFlip;
	Servo zipLine;
	Servo zipLineAlt;
	Servo debrisRamp;
	Servo rampKick;

	//Drive modes
	boolean bayneDrive = true;
	boolean straightDrive = false;

	float left;
	float right;
	float hangSpd;
	float sweepSpd;

	double debrisLiftNet = 0;

	String driveMode = "bayneDrive";

	/*
	 * Constructor
	 */
	public Teleop() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	//Runs when OpMode loaded
	public void init() {
		//Get hardware map
		cdim = hardwareMap.deviceInterfaceModule.get("cdim");
		motorRight = hardwareMap.dcMotor.get("rMtr");
		motorLeft = hardwareMap.dcMotor.get("lMtr");
		motorRight.setDirection(DcMotor.Direction.FORWARD);
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
		motorHang = hardwareMap.dcMotor.get("hangMtr");
		debrisLift = hardwareMap.dcMotor.get("debrisLift");
		sweeper = hardwareMap.dcMotor.get("sweeper");

		hangLock = hardwareMap.servo.get("hangLock");
		climbFlip = hardwareMap.servo.get("climbFlip");
		zipLine = hardwareMap.servo.get("zipLineRight");
		zipLineAlt = hardwareMap.servo.get("zipLineLeft");
		debrisRamp = hardwareMap.servo.get("debrisRamp");
		rampKick = hardwareMap.servo.get("rampKick");

		zipLineUp = zipLineRightUp;
		zipLineUpAlt = zipLineLeftUp;
		zipLineHalf = zipLineRightHalf;
		zipLineDown = zipLineRightDown;
		debrisRampDown = debrisRampRed;

		climbFlip.setPosition(climbFlipBack);
		hangLock.setPosition(hangUnlocked);
		hardwareMap.servo.get("zipLineLeft").setPosition(zipLineUpAlt);
		hardwareMap.servo.get("zipLineRight").setPosition(zipLineUp);
		debrisRamp.setPosition(debrisRampMiddle);
		rampKick.setPosition(0.5); //rampKick is a continuous rotation servo. 0.5 position is 0 power

		zipBlock = 0;
		zipBlockAlt = 1;
	}

	//Loops during init
	public void init_loop() {
		if (checkButton(gamepad1.x, 1, 0)) {
			alliance = "Blue";
			cdim.setLED(0, true);
			cdim.setLED(1, false);
			zipLine = hardwareMap.servo.get("zipLineLeft");
			zipLineUp = zipLineLeftUp;
			zipLineUpAlt = zipLineRightUp;
			zipLineHalf = zipLineLeftHalf;
			zipLineDown = zipLineLeftDown;
			debrisRampDown = debrisRampBlue;
		} else if (checkButton(gamepad1.b, 3, 0)) {
			alliance = "Red";
			cdim.setLED(0, false);
			cdim.setLED(1, true);
			zipLine = hardwareMap.servo.get("zipLineRight");
			zipLineUp = zipLineRightUp;
			zipLineUpAlt = zipLineLeftUp;
			zipLineHalf = zipLineRightHalf;
			zipLineDown = zipLineRightDown;
			debrisRampDown = debrisRampRed;
		}

		if (alliance == null) {
			telemetry.addData("**PLEASE SELECT AN ALLIANCE**", "Driver 1 red/blue buttons to select");
		} else {
			telemetry.addData("Selected Alliance", alliance + " alliance");
		}
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
		 * Driving, zip line release, and ramp lock
		 */

		//Manage driving modes
		if (checkButton(gamepad1.b, 3, 0) && !straightDrive) {
			//Toggle bayneDrive with B
			bayneDrive = !bayneDrive;
		}
		if (checkButton(gamepad1.start, 10, 0)) {
			//Toggle straight driving with start
			straightDrive = !straightDrive;
		}

		//Joystick values range from -1 to 1, where -1 is left and up,
		//and 1 is right and down

		//Manage driving based on mode
		if (!straightDrive) {
			//If we're moving normally, tweak driving based on bayneDrive
			if (bayneDrive) {
				//Bayne's video game drive
				left = gamepad1.left_stick_y - gamepad1.right_stick_x;
				right = gamepad1.left_stick_y + gamepad1.right_stick_x;

				driveMode = "bayneDrive";
			} else {
				//Normal tank drive
				left = gamepad1.left_stick_y;
				right = gamepad1.right_stick_y;

				driveMode = "Tank drive";
			}
		} else {
			//Straight driving mode
			left = right = gamepad1.left_stick_y;

			driveMode = "Straight movement";
		}

		//Clip joystick values to prevent exceeding range
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		//Scale joystick values
		right = (float)scaleMotor(right);
		left =  (float)scaleMotor(left);

		//Write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);

		//Climber zip-lines
        if (gamepad1.dpad_up) {
			zipLine.setPosition(zipLineUp);
			//zipLineAlt.setPosition(zipLineUpAlt);
        } else if (gamepad1.dpad_left || gamepad1.dpad_right) {
            zipLine.setPosition(zipLineHalf);
			//zipLineAlt.setPosition(zipLineUpAlt);
        } else if (gamepad1.dpad_down) {
            zipLine.setPosition(zipLineDown);
			//zipLineAlt.setPosition(zipLineUpAlt);
        } /*else if (gamepad1.left_trigger > 0.5 || gamepad1.right_trigger > 0.5) {
			zipLine.setPosition(zipBlock);
			zipLineAlt.setPosition(zipBlockAlt);
		}*/

		/*
		 * Driver 2
		 * Climber flip arm, hang arm, hang lock, and debris ramp, lift, and sweeper
		 */

		//Hang motor
		hangSpd = gamepad2.left_stick_y;
		hangSpd = Range.clip(hangSpd, -1, 1);
		hangSpd = (float)scaleMotor(hangSpd);
		if (hangSpd != 0) {
			if (Math.abs(hangLock.getPosition() - hangUnlocked) < 0.0001) {
				motorHang.setPower(hangSpd);
			}
		} else {
			motorHang.setPower(0);
		}

		//Ramp booster
		if (gamepad1.right_bumper) {
			rampKick.setPosition(1);
		} else if (gamepad1.right_trigger > 0.5) {
			rampKick.setPosition(0);
		} else {
			rampKick.setPosition(0.5);
		}

		//Debris ramp
		if (checkButton(gamepad2.right_bumper, 15, 0)) {
			debrisRamp.setPosition(debrisRampMiddle);
		} else if (checkButton(gamepad2.right_trigger > 0.5, 17, 0)) {
			debrisRamp.setPosition(debrisRampDown);
		}

		//Debris lift - set targets
		if (checkButton(gamepad2.a, 12, 0)) {
			debrisLiftManual = false;
			debrisLiftTarget = debrisLiftDown;
		} else if (checkButton(gamepad2.y, 14, 0)) {
			debrisLiftManual = false;
			debrisLiftTarget = debrisLiftUp;
		} else if (gamepad2.dpad_down || gamepad2.dpad_up || gamepad2.x) {
			debrisLiftManual = true;
		}

		//Manual debris lift
		if (gamepad2.dpad_up) {
			debrisLift.setPower(0.3);
		} else if (gamepad2.dpad_down) {
			debrisLift.setPower(-0.075);
		} else {
			debrisLift.setPower(0);
			if (checkButton(gamepad2.x, 11, 0)){
				debrisLiftModifier = debrisLift.getCurrentPosition();
			};
		}

		//Debris lift - manage movement
		if (!debrisLiftManual) {
			debrisLiftNet = debrisLift.getCurrentPosition() - debrisLiftModifier;
			if ((debrisLiftTarget == debrisLiftUp && debrisLiftNet < debrisLiftTarget - 100) || (debrisLiftTarget == debrisLiftDown && debrisLiftNet < debrisLiftTarget)) {
				//If debris lift on way to upper position, or too low for lower, move there

				debrisLift.setPower((debrisLiftNet < debrisLiftTarget - 100 ? 1 : 0.4));
				sweeper.setPower(0.5);
			} else if ((debrisLiftTarget == debrisLiftDown && debrisLiftNet > debrisLiftTarget + 100) || (debrisLiftTarget == debrisLiftUp && debrisLiftNet > debrisLiftTarget)) {
				//If debris lift on way to lower position, or too high for upper, move down

				debrisLift.setPower((debrisLiftNet > debrisLiftTarget + 100 ? -0.3 : -0.15));
				sweeper.setPower(0);
			} else if (debrisLiftNet < debrisLiftDown || debrisLiftNet > debrisLiftUp) {
				//If debris lift out of encoder range, tweak it

				debrisLift.setPower(-0.4 * (debrisLiftTarget == debrisLiftUp ? 1 : 0.3) * Math.signum(debrisLift.getCurrentPosition()));
				sweeper.setPower(0);
			} else {
				//Else stop

				debrisLift.setPower(0);
				sweeper.setPower(0);
			}
		}

		//Climber flipping arm
		if (checkButton(gamepad2.b, 4, 0)) {
			if (Math.abs(climbFlip.getPosition() - climbFlipBack) < 0.0001) {
				climbFlip.setPosition(climbFlipTipped);
			} else if (Math.abs(climbFlip.getPosition() - climbFlipTipped) < 0.0001) {
				climbFlip.setPosition(climbFlipBack);
			}
		}

		//Debris sweeper
		if (gamepad2.right_stick_y < -0.5) {
			sweeper.setPower(-1.0);
		}/* else if (gamepad2.right_stick_y < -0.5 && debrisLift.getCurrentPosition() <= 200) {
			sweeper.setPower(-1.0);
		} */else {
			sweeper.setPower(0);
		}

		//Hang lock
		if (checkButton(gamepad2.start, 20, 0)) {
			if (Math.abs(hangLock.getPosition() - hangLocked) < 0.0001) {
				hangLock.setPosition(hangUnlocked);
			} else if (Math.abs(hangLock.getPosition() - hangUnlocked) < 0.0001) {
				hangLock.setPosition(hangLocked);
			}
		}

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		telemetry.addData("Drive mode", driveMode);
		telemetry.addData("Debris lift enc, mod", debrisLift.getCurrentPosition() + ", " + debrisLiftModifier);
		telemetry.addData("Debris lift net", debrisLift.getCurrentPosition() - debrisLiftModifier);
		telemetry.addData("Climb flip", climbFlip.getPosition() + " = " + (Math.abs(climbFlip.getPosition() - climbFlipTipped) < 0.0001 ? "Tipped" : "Back"));
		telemetry.addData("Hang Lock", hangLock.getPosition() + " = " + (Math.abs(hangLock.getPosition() - hangLocked) < 0.0001 ? "Locked" : "Unlocked"));
		telemetry.addData("Debris Ramp", debrisRamp.getPosition() + " = " + (Math.abs(debrisRamp.getPosition() - debrisRampDown) < 0.0001 ? "Down" : "Up"));
		telemetry.addData("Left/Right motors", String.format("%f", left) + "\n" + String.format("%f", right));
		telemetry.addData("Left/Right sticks", "\n" + String.format("%f", gamepad1.left_stick_x) + "\n" +
				String.format("%f", gamepad1.left_stick_y) + "\n" +
				String.format("%f", gamepad1.right_stick_x) + "\n" +
				String.format("%f", gamepad1.right_stick_y));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {
		//Kill CDIM lights, because apparently, it doesn't happen automatically
		cdim.setLED(0, false);
		cdim.setLED(1, false);
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
