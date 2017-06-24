package com.first.team2052.steamworks.subsystems;

import com.ctre.CANTalon;
import com.first.team2052.steamworks.Constants;

/**
 * This class congregates the functionality of the intake apparatus; this is just the fuel intake.
 * Created by Ben Mildenberger on 1/21/2017.
 */
public class Pickup {
    private static Pickup instance = new Pickup();
    private CANTalon intakeMotor;

    private Pickup() {
        intakeMotor = new CANTalon(Constants.CAN.kIntakeMotorPort);
    }

    public static Pickup getInstance() {
        return instance;
    }

    /**
     * This function takes in a value from the joystick and if the appropriate button (for enabling the intake) is pressed,
     */
    public void setIntakeState(PickupState state) {
        intakeMotor.set(state.getSpeed());
    }

    public enum PickupState {
        IN(Constants.Pickup.kIntakeMotorSpeedIn),
        OUT(Constants.Pickup.kIntakeMotorSpeedOut),
        STOP(0.0);
        double speed;

        PickupState(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }
}