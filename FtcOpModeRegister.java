/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {

  /**
   * The Op Mode Manager will call this method when it wants a list of all
   * available op modes. Add your op mode to the list to enable it.
   *
   * @param manager op mode manager
   */
  public void register(OpModeManager manager) {

    /*
     * First parameter name of Opmode*-
     * Second is Opmode class property
     *
     * If two or more op modes are registered with the same name, the app will display an error.
     *   __  __              __ ____     ____        __  __           _
     * |  \/  |            /_ |___ \   / __ \      |  \/  |         | |
     * | \  / | __ ___  __  | | __) | | |  | |_ __ | \  / | ___   __| | ___  ___
     * | |\/| |/ _` \ \/ /  | ||__ <  | |  | | '_ \| |\/| |/ _ \ / _` |/ _ \/ __|
     * | |  | | (_| |>  <   | |___) | | |__| | |_) | |  | | (_) | (_| |  __/\__ \
     * |_|  |_|\__,_/_/\_\  |_|____/   \____/| .__/|_|  |_|\___/ \__,_|\___||___/
     *                                       | |
     *                                       |_|
     */

    //manager.register("NullOp", NullOp.class);

    /*
     * Main OpModes
     * Teleop(s) first, then autonomii
     * EXPerimental OpModes are complete, but have not been tested. Use at your own risk.
     * WIP OpModes are not complete and shouldn't be used in competition
     * OLD OpModes are outdated and probably shouldn't be used in competition, either
     */

    manager.register("Teleop", Teleop.class);
    manager.register("Auto Straight Ramp", AutoStraightRamp.class);
    manager.register("EXP - Auto All Score", AutoAllScore.class);
    manager.register("Auto Ramp", AutoRamp.class);
    manager.register("EXP - Auto Beacon Tip", AutoBeaconTip.class);

    /*
     * Config OpModes
     */

    manager.register("Hang/Debris Lift Adjust", ZZLiftAdjust.class);
    //manager.register("Config Identify", ZZConfigCycle.class);

    /*
     * Demo OpModes
     */

    manager.register("Demo Stripped Teleop", SimplifiedDemoTeleop.class);
    manager.register("Simple Teleop", SimpleTeleop.class);
    manager.register("Mecanum Teleop", SimpleMecanumTeleop.class);

    /*
     * Testing OpModes
     */

    //manager.register("Delay/Alliance Select Test", ZYDelayAllianceTest.class);
    //manager.register("Camera Test", ZYCameraOp.class);

    /*
     * Built-in Junk
     */

    //manager.register("MatrixK9TeleOp", MatrixK9TeleOp.class);
    //manager.register("K9TeleOp", K9TeleOp.class);
    //manager.register("K9Line", K9Line.class);
    //manager.register ("PushBotAuto", PushBotAuto.class);
    //manager.register ("PushBotManual", PushBotManual.class);

    //manager.register("MR Gyro Test", MRGyroTest.class);

    //manager.register("AdafruitRGBExample", AdafruitRGBExample.class);
    //manager.register("ColorSensorDriver", ColorSensorDriver.class);

    //manager.register("IrSeekerOp", IrSeekerOp.class);
    //manager.register("CompassCalibration", CompassCalibration.class);
    //manager.register("I2cAddressChangeExample", LinearI2cAddressChange.class);


    //manager.register("NxtTeleOp", NxtTeleOp.class);

    //manager.register("LinearK9TeleOp", LinearK9TeleOp.class);
    //manager.register("LinearIrExample", LinearIrExample.class);


    //manager.register ("PushBotManual1", PushBotManual1.class);
    //manager.register ("PushBotAutoSensors", PushBotAutoSensors.class);
    //manager.register ("PushBotIrEvent", PushBotIrEvent.class);

    //manager.register ("PushBotManualSensors", PushBotManualSensors.class);
    //manager.register ("PushBotOdsDetectEvent", PushBotOdsDetectEvent.class);
    //manager.register ("PushBotOdsFollowEvent", PushBotOdsFollowEvent.class);
    //manager.register ("PushBotTouchEvent", PushBotTouchEvent.class);

    //manager.register("PushBotDriveTouch", PushBotDriveTouch.java);
    //manager.register("PushBotIrSeek", PushBotIrSeek.java);
    //manager.register("PushBotSquare", PushBotSquare.java);
  }
}
