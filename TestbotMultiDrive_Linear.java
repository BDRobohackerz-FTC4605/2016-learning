/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.HardwareTestbot2016.motorLeftFront;
import static org.firstinspires.ftc.teamcode.HardwareTestbot2016.motorLeftRear;
import static org.firstinspires.ftc.teamcode.HardwareTestbot2016.motorRightFront;
import static org.firstinspires.ftc.teamcode.HardwareTestbot2016.motorRightRear;
import static org.firstinspires.ftc.teamcode.LibraryFunctionsFTC4605.arcade;
import static org.firstinspires.ftc.teamcode.LibraryFunctionsFTC4605.holonomic;
import static org.firstinspires.ftc.teamcode.LibraryFunctionsFTC4605.tank;

/**
 * This OpMode just executes a multiple drive systems fot the Testbot.
 */

//Make stupid change

@TeleOp(name="Testbot MultiDrive", group="testbot")  // @Autonomous(...) is the other common choice
//@Disabled
public class TestbotMultiDrive_Linear extends LinearOpMode {

    /* Declare OpMode members. */

    HardwareTestbot2016 robot = new HardwareTestbot2016();
    private ElapsedTime runtime =new ElapsedTime();

    enum DriveModeE{TANK, ARCADE, HOLONOMIC}
    DriveModeE DriveMode;

    boolean previousButtonServoLatchMode = false;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Testbot MultiDrive");
        telemetry.update();

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        DriveMode = DriveModeE.TANK;

        boolean buttonDriveModeArcade;
        boolean buttonDriveModeTank;
        boolean buttonDriveModeHolonomic;

        boolean Gp1DDown;
        boolean buttonServoLatchMode;

        float Gp1LeftTrigger;
        float Gp1RightTrigger;


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Ready");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();



            // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            buttonDriveModeArcade = gamepad1.a;
            buttonDriveModeTank = gamepad1.b;
            buttonDriveModeHolonomic = gamepad1.x;

            Gp1DDown = gamepad1.dpad_down;
            buttonServoLatchMode = gamepad1.dpad_up;

            Gp1LeftTrigger = gamepad1.left_trigger;
            Gp1RightTrigger = gamepad1.right_trigger;

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            if (gamepad1.y) {
                //    straight(distance);
            }

            if (buttonDriveModeArcade) {
                DriveMode = DriveModeE.ARCADE;
            }

            if (buttonDriveModeTank){
                DriveMode = DriveModeE.TANK;
            }

            if (buttonDriveModeHolonomic) {
                DriveMode = DriveModeE.HOLONOMIC;
            }

            switch (DriveMode) {
                case TANK:
                    tank(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear,
                            gamepad1.left_stick_y, gamepad1.right_stick_y);
                    break;

                case ARCADE:
                    arcade(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear,
                            gamepad1.left_stick_y, gamepad1.left_stick_x);
                    break;

                case HOLONOMIC:
                    holonomic(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear,
                            gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                    break;

                default:
                    break;
            }

                idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
}
