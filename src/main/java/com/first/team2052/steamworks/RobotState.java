package com.first.team2052.steamworks;

import com.first.team2052.lib.interpolables.InterpolatingDouble;
import com.first.team2052.lib.interpolables.InterpolatingTreeMap;
import com.first.team2052.lib.vec.RigidTransform2d;
import com.first.team2052.lib.vec.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Map;
//Credit 254 - 2016
public class RobotState {
    private static RobotState instance = new RobotState();
    protected InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> field_to_vehicle_map;

    private RobotState() {
        reset(0, new RigidTransform2d());
    }

    public static RobotState getInstance() {
        return instance;
    }

    /**
     * Resets robot position
     * @param start_time the time to start (usually the current time)
     * @param initial_field_to_vehicle the initial position of the robot (usually just zeroed)
     */
    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle) {
        field_to_vehicle_map = new InterpolatingTreeMap<>(100);
        field_to_vehicle_map.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
    }

    /**
     * Generate Odometry (robot position) from sensors
     * @return an updated position of the robot
     */
    public synchronized RigidTransform2d generateOdometryFromSensors(double left_encoder_delta_distance,
                                                                     double right_encoder_delta_distance,
                                                                     Rotation2d current_gyro_angle) {
        RigidTransform2d last_measurement = getLatestFieldToVehicle().getValue();
        return Kinematics.integrateForwardKinematics(last_measurement, left_encoder_delta_distance,
                right_encoder_delta_distance, current_gyro_angle);
    }

    /**
     * @return the last entry of the robot position map (the current position)
     */
    public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
        return field_to_vehicle_map.lastEntry();
    }

    /**
     * Adds a robot position to a timed map, so we can access it later (in this case we only store 100 entries)
     */
    public synchronized void addVehicleObservation(double timestamp, RigidTransform2d observation) {
        field_to_vehicle_map.put(new InterpolatingDouble(timestamp), observation);
    }

    public void outputToSmartDashboard(){
        RigidTransform2d odometry = getLatestFieldToVehicle().getValue();
        SmartDashboard.putNumber("robot_pose_x", odometry.getTranslation().getX());
        SmartDashboard.putNumber("robot_pose_y", odometry.getTranslation().getY());
        SmartDashboard.putNumber("robot_pose_theta", odometry.getRotation().getDegrees());
    }
}
