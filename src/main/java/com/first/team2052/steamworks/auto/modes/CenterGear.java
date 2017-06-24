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
 * Starts: Center against alliance wall
 * Desc: Brings a gear to the airship
 * Ends: Airship
 */
public class CenterGear extends AutoMode {
    @Override
    protected void init() throws AutoModeEndedException {
        //Generate path waypoints
        List<Path.Waypoint> forwardPath = Lists.newArrayList();
        forwardPath.add(new Path.Waypoint(new Translation2d(0, 0), 50));
        forwardPath.add(new Path.Waypoint(new Translation2d(60, 0), 20));
        forwardPath.add(new Path.Waypoint(new Translation2d(72, 0), 20));

        List<Path.Waypoint> backwardPath = Lists.newArrayList();
        backwardPath.add(new Path.Waypoint(new Translation2d(72, 0), 50));
        backwardPath.add(new Path.Waypoint(new Translation2d(40, 0), 60,  "CloseGearMan"));
        backwardPath.add(new Path.Waypoint(new Translation2d(35, 0), 60));

        //Drive up to the peg and drop gear
        runAction(new SeriesAction(Arrays.asList(
                new TimeoutAction(new FollowPathAction(new Path(forwardPath), false), 8.0),
                new DropGearAction())));

        //Drive back, when we pass a waypoint close the gear manipulator
        runAction(new ParallelAction(Arrays.asList(
                new FollowPathAction(new Path(backwardPath), true),
                new SeriesAction(Arrays.asList(new WaitForPathMarkerAction("CloseGearMan"), new CloseGearAction())))));
    }
}
