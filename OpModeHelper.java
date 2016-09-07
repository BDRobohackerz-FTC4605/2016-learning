//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package org.firstinspires.ftc.teamcode;
//package com.qualcomm.ftcrobotcontroller.opmodes.globals;

import android.content.Context;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import java.util.concurrent.TimeUnit;

public abstract class OpModeHelper {

  /*
	 * Robohackerz FTC 4605
	 * 1/31/2016
	 *
	 * An modified version of the OpMode class to enable hardware interaction without
	 * conflict in the drive header
	 */

  public Gamepad gamepad1 = new Gamepad();
  public Gamepad gamepad2 = new Gamepad();
  public TelemetryMessage telemetry = new TelemetryMessage();
  //public HardwareMap hardwareMap = new HardwareMap();
  public double time = 0.0D;
  private long a = 0L;

  public OpModeHelper() {
    this.a = System.nanoTime();
  }

  public void initHardware() {}
  public void init_loop() {}
  public void start() {}
  public void loop() {}
  public void stop() {}

  public double getRuntime() {
    double var1 = (double)TimeUnit.SECONDS.toNanos(1L);
    return (double)(System.nanoTime() - this.a) / var1;
  }

  public void resetStartTime() {
    this.a = System.nanoTime();
  }
}
