package com.first.team2052.steamworks.auto.modes;

import com.first.team2052.lib.path.Path;
import com.first.team2052.lib.vec.Rotation2d;
import com.first.team2052.lib.vec.Translation2d;
import com.first.team2052.steamworks.auto.AutoMode;
import com.first.team2052.steamworks.auto.AutoModeEndedException;
import com.first.team2052.steamworks.auto.AutoModeSelector;
import com.first.team2052.steamworks.auto.actions.FollowPathAction;
import com.first.team2052.steamworks.auto.actions.SeriesAction;
import com.first.team2052.steamworks.auto.actions.StartShootingAction;
import com.first.team2052.steamworks.auto.actions.WaitAction;
import com.first.team2052.steamworks.subsystems.drive.DriveTrain;
import com.first.team2052.steamworks.subsystems.shooter.Shooter;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Starts: Boiler
 * Desc: Turns and shoots
 * Ends: Back at the Boiler
 */
public class BoilerShoot extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        double distance_forward = 42;
        double turn = Math.toRadians(42.25);

        List<Path.Waypoint> forwardPath = Lists.newArrayList();
        forwardPath.add(new Path.Waypoint(new Translation2d(0, 0), 50));
        forwardPath.add(new Path.Waypoint(new Translation2d(-distance_forward + (15 * Math.cos(turn)), 0), 30));
        forwardPath.add(new Path.Waypoint(new Translation2d(-distance_forward + (15 * Math.cos(turn)), isBlue() ? -1 : 1 * 15 * Math.sin(turn)), 20));
        forwardPath.add(new Path.Waypoint(new Translation2d(-distance_forward + (39 * Math.cos(turn)), isBlue() ? -1 : 1 * 39 * Math.sin(turn)), 20));

        //Start running the shooter, but don't shoot
        Shooter.getInstance().setWantIdleRampUp(true);

        runAction(new SeriesAction(Arrays.asList(new FollowPathAction(new Path(forwardPath), true)/*, new StartShootingAction()*/, new WaitAction(4.0))));

        Shooter.getInstance().setWantIdleRampUp(false);
        Shooter.getInstance().setWantShoot(false);

        DriveTrain.getInstance().setVelocityHeadingSetpoint(20, Rotation2d.fromDegrees(-180));
    }
}
