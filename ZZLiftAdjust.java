package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="ZZ Lift Adjust", group="ZZ")  // @Autonomous(...) is the other common choice
//@Disabled

public class ZZLiftAdjust extends LinearOpMode {

	/*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * Used to manually adjust positions of hang arm and debris lift
	 */


	DcMotor hangMtr;
	DcMotor debrisLift;

	Servo hangLock;

	/**
	 * Constructor
	 */
	public ZZLiftAdjust() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */

	public void runOpMode() throws InterruptedException {
		hangMtr = hardwareMap.dcMotor.get("hangMtr");
		debrisLift = hardwareMap.dcMotor.get("debrisLift");
		hangLock = hardwareMap.servo.get("hangLock");

		double hangTrigger;
		double liftTrigger;
		double timeHang = 0;
		double timeLift = 0;
		double speedHang = 0;
		double speedLift = 0;
		double hangDir = 0;
		double liftDir = 0;
		double scaleSpdHang = 0;
		double scaleSpdLift = 0;

		hangLock.setPosition(0);

		while (true) {
			hangTrigger = -gamepad1.left_stick_y;
			liftTrigger = -gamepad1.right_stick_y;

			//Handle timing - Hang
			if (Math.abs(hangTrigger) >= 0.5) {
				//If triggered, manage time, else reset
				if (Math.signum(hangTrigger) == hangDir) {
					//If still holding same direction, keep managing current timing and speed
					//Else, reset
					timeHang += 0.001;
					timeHang = timeHang <= 5 ? timeHang : 5;
					sleep(1);
					speedHang = Math.floor(1.2 * timeHang * timeHang + 20);
				} else {
					timeHang = 0;
					speedHang = 0;
					hangDir = Math.signum(hangTrigger);
				}
			} else {
				hangDir = 0;
			}

			//Handle timing - Lift
			if (Math.abs(liftTrigger) >= 0.5) {
				//If triggered, manage time, else reset
				if (Math.signum(liftTrigger) == liftDir) {
					//If still holding same direction, keep managing current timing and speed
					//Else, reset
					timeLift += 0.001;
					timeLift = timeLift <= 5 ? timeLift : 5;
					sleep(1);
					speedLift = Math.floor(1.2 * timeLift * timeLift + 20);
				} else {
					timeLift = 0;
					speedLift = 0;
					liftDir = Math.signum(liftTrigger);
				}
			} else {
				liftDir = 0;
			}

			//Power motor - Hang
			if(hangDir == 1) {
				scaleSpdHang = Range.clip(3 * speedHang, -100, 100) / 100;
				hangMtr.setPower(scaleSpdHang);
			} else if(hangDir  == -1) {
				scaleSpdHang = -Range.clip(Math.ceil(speedHang / 4), -100, 100) / 100;
				hangMtr.setPower(scaleSpdHang);
			} else {
				scaleSpdHang = 0;
				hangMtr.setPower(0);
			}

			//Power motor - Lift
			if(liftDir == 1) {
				scaleSpdLift = Range.clip(3 * speedLift, -100, 100) / 100;
				debrisLift.setPower(scaleSpdLift);
			} else if(liftDir  == -1) {
				scaleSpdLift = -Range.clip(Math.ceil(speedLift / 4), -100, 100) / 100;
				debrisLift.setPower(scaleSpdLift);
			} else {
				scaleSpdLift = 0;
				debrisLift.setPower(0);
			}

			telemetry.addData("Hang Encoder", hangMtr.getCurrentPosition());
			telemetry.addData("Hang Speed: Raw, Scaled", speedHang + ", " + scaleSpdHang);
			telemetry.addData("Hang Power", hangMtr.getPower());
			telemetry.addData("Lift Encoder", debrisLift.getCurrentPosition());
			telemetry.addData("Lift Speed: Raw, Scaled", speedLift + ", " + scaleSpdLift);
			telemetry.addData("Lift Power", debrisLift.getPower());
		}
	}
}
