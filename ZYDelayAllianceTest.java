package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.teamcode.drive.*;
import static org.firstinspires.ftc.teamcode.drive.autoDelay;
import static org.firstinspires.ftc.teamcode.drive.buttonChecking;

@Autonomous(name="ZY Delay Alliance Test", group="Z")  // @Autonomous(...) is the other common choice
//@Disabled

public class ZYDelayAllianceTest extends LinearOpMode {

	/*
	 * Robohackerz FTC 4605
	 * 2/6/2016
	 *
	 * A simple test program for an implementation of an autonomous delay
	 */

	String alliance = null;

	/**
	 * Constructor
	 */
	public ZYDelayAllianceTest() {
	}

	@Override
	public void runOpMode() throws InterruptedException {

		Thread delayAllianceSelect = new Thread(new DelayAllianceSelect());
		delayAllianceSelect.start();

		waitForStart();

		delayAllianceSelect.interrupt();

		sleep(autoDelay * 1000);
	}

	public class DelayAllianceSelect implements Runnable {
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
		if (trackEdge == edge) {
		}
		return trackEdge == edge;
	}
}
