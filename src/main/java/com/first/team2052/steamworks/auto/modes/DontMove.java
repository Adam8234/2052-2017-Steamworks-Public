package com.first.team2052.steamworks.auto.modes;

import com.first.team2052.steamworks.auto.AutoMode;
import com.first.team2052.steamworks.auto.AutoModeEndedException;
import com.first.team2052.steamworks.subsystems.drive.DriveSignal;
import com.first.team2052.steamworks.subsystems.drive.DriveTrain;

/**
 * Starts: At alliance station
 * Desc: Does exactly nothing
 * Ends: At alliance station
 */
public class DontMove extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        DriveTrain.getInstance().setOpenLoop(DriveSignal.NEUTRAL);
        DriveTrain.getInstance().setBrakeMode(false);
        DriveTrain.getInstance().setHighGear(true);
    }
}
