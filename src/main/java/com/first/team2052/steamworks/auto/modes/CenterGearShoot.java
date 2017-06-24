package com.first.team2052.steamworks.auto.modes;

import com.first.team2052.lib.path.Path;
import com.first.team2052.lib.vec.Translation2d;
import com.first.team2052.steamworks.Util;
import com.first.team2052.steamworks.auto.AutoMode;
import com.first.team2052.steamworks.auto.AutoModeEndedException;
import com.first.team2052.steamworks.auto.actions.*;
import com.first.team2052.steamworks.subsystems.drive.DriveSignal;
import com.first.team2052.steamworks.subsystems.drive.DriveTrain;
import com.first.team2052.steamworks.subsystems.shooter.Shooter;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Starts: Center against alliance wall
 * Desc: Brings a gear to the airship then shoots
 * Ends: Boiler
 */
public class CenterGearShoot extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        //Generate path waypoints
        List<Path.Waypoint> forwardPath = Lists.newArrayList();
        forwardPath.add(new Path.Waypoint(new Translation2d(0, 0), 50));
        forwardPath.add(new Path.Waypoint(new Translation2d(60, 0), 20));
        forwardPath.add(new Path.Waypoint(new Translation2d(73, 0), 20));

        List<Path.Waypoint> backwardPath = Lists.newArrayList();
        backwardPath.add(new Path.Waypoint(new Translation2d(73, 0), 40));
        backwardPath.add(new Path.Waypoint(new Translation2d(45, 0), 40));
        backwardPath.add(new Path.Waypoint(new Translation2d(41, 90.5), 40, "CloseGearMan"));
        backwardPath.add(new Path.Waypoint(new Translation2d(16.5, 121.5), 40));

        if (isRed()) {
            backwardPath = Util.inverseY(backwardPath);
        }

        //Drive up to the peg and drop gear
        runAction(new SeriesAction(Arrays.asList(
                new FollowPathAction(new Path(forwardPath), false),
                new DropGearAction())));

        //Start running the shooter, but don't shoot
        Shooter.getInstance().setWantIdleRampUp(true);

        //Drive back to the boiler
        runAction(new ParallelAction(Arrays.asList(
                new SeriesAction(Arrays.asList(new FollowPathAction(new Path(backwardPath), true))),
                new SeriesAction(Arrays.asList(new WaitForPathMarkerAction("CloseGearMan"), new CloseGearAction()))
        )));

        DriveTrain.getInstance().setOpenLoop(new DriveSignal(-.50, -.50));
        runAction(new WaitAction(0.50));
        DriveTrain.getInstance().setOpenLoop(DriveSignal.NEUTRAL);

        runAction(new SeriesAction(Arrays.asList(new StartShootingAction(), new WaitAction(4))));

        //Start shooting
        Shooter.getInstance().setWantIdleRampUp(false);
        Shooter.getInstance().setWantShoot(false);
    }
}