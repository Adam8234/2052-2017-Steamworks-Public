package com.first.team2052.steamworks.subsystems.shooter;

import com.ctre.CANTalon;
import com.first.team2052.steamworks.Constants;

class Flywheel {
    private final CANTalon master_motor;
    private static Flywheel instance = new Flywheel();

    private Flywheel() {
        master_motor = new CANTalon(Constants.CAN.kShooterMotorPort);
        CANTalon slave_motor = new CANTalon(Constants.CAN.kShooterMotorSlavePort);

        //Configure for follower mode for the slave talon to match the master talons voltage without having to set the two individually
        slave_motor.changeControlMode(CANTalon.TalonControlMode.Follower);
        slave_motor.set(master_motor.getDeviceID());

        //Only allow positive voltage, this stops the flywheel from oscillating when you want to be at 0rpm.
        // Otherwise the flywheel would run in reverse and forward over and over and keep gettting faster
        master_motor.configPeakOutputVoltage(12.0f, 0f);

        //Disable brake mode, the flywheel will coast
        master_motor.enableBrakeMode(false);
        slave_motor.enableBrakeMode(false);

        //Config for our encoder options on the flywheel.
        //The cable we used is macgyvered to make the distance that the cable has to make. We only have 6 inch block connectors, so we extended it with 2 encoder breakout boards.
        if (Constants.Shooter.kUseDoubleEncoderExtender) {
            master_motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            master_motor.configEncoderCodesPerRev(1024);
        } else {
            master_motor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        }

        //Set the voltage ramp-rate to be really large so the flywheel spins up really fast
        master_motor.setVoltageRampRate(36.0);
        slave_motor.setVoltageRampRate(36.0);

        //Configure Talon SRX PID + Feed-forward constants under profile index 0
        master_motor.setPID(Constants.Shooter.kShooterVelocityKp,
                Constants.Shooter.kShooterVelocityKi,
                Constants.Shooter.kShooterVelocityKd,
                Constants.Shooter.kShooterVelocityKf,
                Constants.Shooter.kShooterVelocityIZone,
                Constants.Shooter.kShooterVelocityCloseLoopRampRate,
                0);
        //Set the current profile gains to use to index 0
        master_motor.setProfile(0);

        //Change control mode to Speed (RPMs) so any .set method call allows the Closed-Loop control profile from above to reach its setpoint via .set(rpm)
        master_motor.changeControlMode(CANTalon.TalonControlMode.Speed);
        master_motor.set(0.0);
    }

    /**
     * @param speed in RPM you want the RPM to go
     */
    public void setRpm(double speed) {
        master_motor.set(speed);
    }

    /**
     * @return if the flywheel is within it's target window based off the constants
     */
    public boolean isOnTarget() {
        return Math.abs(getRpm() - getSetpoint()) < Constants.Shooter.kShooterVelocityWindow;
    }

    /**
     * @return the current RPM
     */
    public double getRpm() {
        return master_motor.getSpeed();
    }

    /**
     * @return the current goal for RPM
     */
    public synchronized double getSetpoint() {
        return master_motor.getSetpoint();
    }

    public static Flywheel getInstance() {
        return instance;
    }
}
