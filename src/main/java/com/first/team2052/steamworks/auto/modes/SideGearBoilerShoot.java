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
 * Starts: Boiler
 * Desc: Places the gear on the right side of Airship then shoots into the Boiler
 * Ends: Boiler
 */
public class SideGearBoilerShoot extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        double fwd = 68.0;
        double peg = 64.5;

        if (isRed()) {
            fwd = 73.0;  
        }

        double turn = Math.toRadians(42.25);
        double distance_backward = 42;

        double cosA = Math.cos(Math.PI / 3);
        double sinA = Math.sin(Math.PI / 3);

        List<Path.Waypoint> forwardPath = Lists.newArrayList();
        forwardPath.add(new Path.Waypoint(new Translation2d(0, 0), 40));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd - 20, 0), 40));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd, -10), 40));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd + .5 * peg * cosA, -.5 * peg * sinA), 30));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd + peg * cosA, -peg * sinA), 20));

        List<Path.Waypoint> backwardPath = Lists.newArrayList();
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd + peg * cosA, -peg * sinA), 70));
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd + .5 * peg * cosA, -.5 * peg * sinA), 40));
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd, 0), 40, "CloseGearMan"));
        backwardPath.add(new Path.Waypoint(new Translation2d(distance_backward, 0), 60));
        backwardPath.add(new Path.Waypoint(new Translation2d(distance_backward - (30 * Math.cos(turn)), 30 * Math.sin(turn)), 60));

        if (isRed()) {
            forwardPath = Util.inverseY(forwardPath);
            backwardPath = Util.inverseY(backwardPath);
        }

        //Drive up to the peg and drop gear
        runAction(new SeriesAction(Arrays.asList(
                new TimeoutAction(new FollowPathAction(new Path(forwardPath), false), 7.0),
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
