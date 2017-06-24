package com.first.team2052.steamworks.auto.modes;

import com.first.team2052.lib.path.Path;
import com.first.team2052.lib.vec.Translation2d;
import com.first.team2052.steamworks.auto.AutoMode;
import com.first.team2052.steamworks.auto.AutoModeEndedException;
import com.first.team2052.steamworks.auto.actions.*;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Starts: Boiler
 * Desc: Places the gear on the right side of Airship
 * Ends: Airship
 */
public class RightSideGear extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        //Turning right has a small undershoot for driving forward - we have it 3 inches greater than the drive right path
        double fwd = 73.0;
        double peg = 64.5;

        double cosA = Math.cos(Math.PI / 3);
        double sinA = Math.sin(Math.PI / 3);

        List<Path.Waypoint> forwardPath = Lists.newArrayList();
        forwardPath.add(new Path.Waypoint(new Translation2d(0, 0), 40));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd - 20, 0), 40));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd, 10), 30));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd + .5 * peg * cosA, .5 * peg * sinA), 30));
        forwardPath.add(new Path.Waypoint(new Translation2d(fwd + peg * cosA, peg * sinA), 20));

        List<Path.Waypoint> backwardPath = Lists.newArrayList();
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd + peg * cosA, peg * sinA), 60));
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd + .5 * peg * cosA, .5 * peg * sinA), 40));
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd, 0), 30, "CloseGearMan"));
        backwardPath.add(new Path.Waypoint(new Translation2d(fwd - 20, 0), 30));

        List<Path.Waypoint> middleFieldPath = Lists.newArrayList();
        middleFieldPath.add(new Path.Waypoint(new Translation2d(fwd - 20, 0), 60));
        middleFieldPath.add(new Path.Waypoint(new Translation2d(fwd + 260, 0), 60));

        //Drive up to the peg and drop gear
        runAction(new SeriesAction(Arrays.asList(
                new TimeoutAction(new FollowPathAction(new Path(forwardPath), false), 7.0),
                new DropGearAction())));

        //Drive back and drive towards center of field
        runAction(new ParallelAction(Arrays.asList(
                new SeriesAction(Arrays.asList(new FollowPathAction(new Path(backwardPath), true), new FollowPathAction(new Path(middleFieldPath), false))),
                new SeriesAction(Arrays.asList(new WaitForPathMarkerAction("CloseGearMan"), new CloseGearAction()))
        )));
    }
}
