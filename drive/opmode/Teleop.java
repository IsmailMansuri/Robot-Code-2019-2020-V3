//Bertone
package org.firstinspires.ftc.teamcode.drive.opmode;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.drive.mecanum.SampleMecanumDriveREVOptimized;
import org.firstinspires.ftc.teamcode.util.MathUtil;

/**
 * TeleOp class, contains separate functions that run the robot during driver operated period of the game
 * @author Aidan Ferry
 * @since 9/8/2019
 */

@TeleOp(group = "drive")
public class Teleop extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //construct drive class
    SampleMecanumDriveREVOptimized drive;

    @Override
    public void init() {
        drive = new SampleMecanumDriveREVOptimized(hardwareMap);
    }

    @Override
    public void loop() {
        driveUpdate();
        intakeUpdate();
    }
    public double[] calcVelocities(double lx, double ly, double rx, double ry) {
        double v1 = ly + rx + lx + ry;
        double v2 = ly - rx - lx + ry;
        double v3 = ly + rx - lx + ry;
        double v4 = ly - rx + lx + ry;
        double max = Math.abs(v1);
        if(Math.abs(v2) > max)
            max = Math.abs(v2);
        if(Math.abs(v3) > max)
            max = Math.abs(v3);
        if(Math.abs(v4) > max)
            max = Math.abs(v4);
        if(max > 1) {
            v1 /= max;
            v2 /= max;
            v3 /= max;
            v4 /= max;
        }
        double[] velocities = {v1, v2, v3, v4};
        return velocities;
    }

    @Override
    public void stop() {
    }
    public void intakeUpdate(){
        float left_trigger = gamepad1.left_trigger;//in
        float right_trigger = gamepad1.right_trigger;//out
        if(left_trigger>right_trigger){//suck in
            drive.setIntakePowers(-left_trigger);
        }else{
            drive.setIntakePowers(right_trigger);
        }
    }
    public void driveUpdate(){
        float lx = gamepad1.left_stick_x;
        float ly = gamepad1.left_stick_y;
        float rx = gamepad1.right_stick_x;
        float ry = gamepad1.right_stick_y;
        boolean gas = gamepad1.a;
        float mult = gas ? 1f : 0.4f;
        //float mult = MathUtil.map(gamepad1.right_trigger,0,1,0.4f,1);
        double[] velocities = calcVelocities(rx*mult, -ly*mult, lx*mult, -ry*mult);
        drive.setMotorPowers(velocities[0],velocities[2],velocities[3],velocities[1]);
    }
}
