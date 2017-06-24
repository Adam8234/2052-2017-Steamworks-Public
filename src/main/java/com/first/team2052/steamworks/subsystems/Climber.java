package com.first.team2052.steamworks.subsystems;

import com.ctre.CANTalon;
import com.first.team2052.steamworks.Constants;

public class Climber {
    private CANTalon climbing_motor = new CANTalon(Constants.CAN.kClimbId);

    private static Climber instance = new Climber();
    private ClimberState currentState = ClimberState.STOP;

    private Climber() {
        //Allow only positive voltage so we NEVER backdrive and blow out our gearbox
        climbing_motor.configPeakOutputVoltage(+12.0f, 0.0f);
        //Set the ramp rate to really low so we don't go over our amp limit in code so we can climb in a continuous motion
        climbing_motor.setVoltageRampRate(15.0);
    }

    /**
     * Takes a enumerated ClimberState and sets the motor speed based off that state
     * @param state
     */
    public void setClimberState(ClimberState state) {
        currentState = state;
        climbing_motor.set(state.getSpeed());
    }

    /**
     * @return the last set climber state for the robot
     */
    public ClimberState getCurrentState() {
        return currentState;
    }

    /**
     * Enumerated Climb states store the percent that the motor should go in the given states
     */
    public enum ClimberState {
        UP(Constants.Climber.kClimberMotorSpeedUp),
        SLOW_UP(Constants.Climber.kClimberMotorSlowSpeed),
        STOP(0.0);

        double speed;

        ClimberState(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    public static Climber getInstance() {
        return instance;
    }
}
