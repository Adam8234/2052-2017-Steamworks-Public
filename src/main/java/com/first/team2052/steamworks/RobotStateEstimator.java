package com.first.team2052.steamworks;

import com.first.team2052.lib.Loopable;
import com.first.team2052.lib.vec.RigidTransform2d;
import com.first.team2052.lib.vec.Rotation2d;
import com.first.team2052.steamworks.subsystems.drive.DriveTrain;
import edu.wpi.first.wpilibj.Timer;
//Credit 254 - 2016
public class RobotStateEstimator implements Loopable{
    private static RobotStateEstimator instance = new RobotStateEstimator();
    DriveTrain driveTrain = DriveTrain.getInstance();
    RobotState robotState = RobotState.getInstance();
    private double left_encoder_prev_distance = 0;
    private double right_encoder_prev_distance = 0;

    private RobotStateEstimator(){}

    public static RobotStateEstimator getInstance() {
        return instance;
    }

    @Override
    public void onStart() {
        left_encoder_prev_distance = driveTrain.getLeftDistanceInches();
        right_encoder_prev_distance = driveTrain.getRightDistanceInches();
    }

    @Override
    public void update() {
        double time = Timer.getFPGATimestamp();
        double left_distance = driveTrain.getLeftDistanceInches();
        double right_distance = driveTrain.getRightDistanceInches();
        Rotation2d gyroAngle = driveTrain.getGyroAngle();

        RigidTransform2d odometry = robotState.generateOdometryFromSensors(
                left_distance - left_encoder_prev_distance,
                right_distance - right_encoder_prev_distance,
                gyroAngle);

        robotState.addVehicleObservation(time, odometry);

        left_encoder_prev_distance = left_distance;
        right_encoder_prev_distance = right_distance;
    }

    @Override
    public void onStop() {

    }
}
