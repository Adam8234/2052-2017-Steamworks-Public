package com.first.team2052.steamworks;

import com.first.team2052.lib.FlipFlopLatch;
import com.first.team2052.steamworks.subsystems.Climber;
import com.first.team2052.steamworks.subsystems.GearMan;
import com.first.team2052.steamworks.subsystems.Pickup;
import edu.wpi.first.wpilibj.Joystick;

/**
 * All the logic for all the control functions on the robot
 * This is the interface between the driver station and the real robot values
 */
public class Controls {
    public static Controls instance = new Controls();
    private Joystick joystick0 = new Joystick(0);
    private Joystick joystick1 = new Joystick(1);
    private Joystick secondaryStick = new Joystick(2);
    FlipFlopLatch gearManLatch = new FlipFlopLatch();
    FlipFlopLatch wantIdleLatch = new FlipFlopLatch();
    private boolean climberAmpLimitReached = false;
    private boolean wantOverride = false;

    private Controls() {
    }

    public double getTank() {
        double tank = -joystick0.getY();
        if (!joystick1.getTrigger()) {
            tank *= -1;
        }
        return tank;
    }

    public double getTurn() {
        return joystick1.getX();
    }

    public boolean getQuickTurn(){
        return joystick1.getRawButton(3);
    }

    public boolean getHighGear() {
        return joystick0.getRawButton(2);
    }

    public boolean getGearManState() {
        gearManLatch.update(secondaryStick.getRawButton(4));
        return gearManLatch.get();
    }

    public boolean getWantShooterIdle(){
        wantIdleLatch.update(secondaryStick.getRawButton(8));
        return wantIdleLatch.get();
    }

    public Pickup.PickupState getIntakeState() {
        if (secondaryStick.getRawButton(2)) {
            return Pickup.PickupState.IN;
        } else if (secondaryStick.getRawButton(3)) {
            return Pickup.PickupState.OUT;
        } else {
            return Pickup.PickupState.STOP;
        }
    }

    public Climber.ClimberState getClimberState(double current) {
        if (!climberAmpLimitReached) { //if the voltage is ok, run normally
            if (current < Constants.Climber.kClimberAmpMax) {
                if (secondaryStick.getRawButton(5)) {
                    return Climber.ClimberState.UP;
                } else if (secondaryStick.getRawButton(6)) {
                    return Climber.ClimberState.SLOW_UP;
                }
            } else {
                climberAmpLimitReached = true;
            }
        }

        if (climberAmpLimitReached) //the override works regardless if limit has been reached or not
        {
            if(!secondaryStick.getRawButton(5)){ //triggers when the climb button is released AFTER amp limit reached
                wantOverride = true;
            }
        }

        if(secondaryStick.getRawButton(5) && wantOverride){ //sets a repress of the climb button to always override
            return Climber.ClimberState.SLOW_UP;
        }
        return Climber.ClimberState.STOP;

    }

    public boolean isClimberAmpLimitReached() {
        return climberAmpLimitReached;
    }

    public static Controls getInstance() {
        return instance;
    }

    public boolean getWantShoot() {
        return joystick0.getTrigger();
    }

    public boolean getWantPunch() {
        return secondaryStick.getRawButton(7);
    }

    public boolean getWantReverseAgitator() {
        return secondaryStick.getRawButton(10);
    }
}
