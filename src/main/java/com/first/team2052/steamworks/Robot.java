package com.first.team2052.steamworks;

import com.first.team2052.lib.ControlLoop;
import com.first.team2052.lib.RevRoboticsPressureSensor;
import com.first.team2052.lib.vec.RigidTransform2d;
import com.first.team2052.lib.vec.Rotation2d;
import com.first.team2052.steamworks.auto.AutoModeRunner;
import com.first.team2052.steamworks.subsystems.Climber;
import com.first.team2052.steamworks.subsystems.GearMan;
import com.first.team2052.steamworks.subsystems.Pickup;
import com.first.team2052.steamworks.subsystems.drive.DriveSignal;
import com.first.team2052.steamworks.subsystems.drive.DriveTrain;
import com.first.team2052.steamworks.subsystems.light.LightFlasher;
import com.first.team2052.steamworks.subsystems.light.LightFlasherLoopable;
import com.first.team2052.steamworks.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
    private static DriveTrain driveTrain;
    private ControlLoop controlLoop;
    private ControlLoop logLooper;
    private ControlLoop slowerLooper;
    private Controls controls;
    private GearMan gearMan;
    private Pickup pickup;
    private Shooter shooter;
    private Climber climber;
    private RevRoboticsPressureSensor revRoboticsPressureSensor;
    private AutoModeRunner autoModeRunner;
    private RobotState robotState;
    private RobotStateEstimator stateEstimator;
    private PowerDistributionPanel pdp;
    private DriveHelper driveHelper;
    private LightFlasher lightFlasher;
    private boolean visionTurn = false;
    private Rotation2d visionTurnAngle;


    @Override
    public void robotInit() {
        System.out.println("Starting Robot Code - Hornet");
        driveHelper = new DriveHelper();

        //Subsystems
        driveTrain = DriveTrain.getInstance();
        controls = Controls.getInstance();
        gearMan = GearMan.getInstance();
        pickup = Pickup.getInstance();
        shooter = Shooter.getInstance();
        climber = Climber.getInstance();
        lightFlasher = LightFlasher.getInstance();

        pdp = new PowerDistributionPanel();

        //Control loops for auto and teleop
        controlLoop = new ControlLoop(Constants.kControlLoopPeriod);
        slowerLooper = new ControlLoop(Constants.kSlowControlLoopPeriod);

        robotState = RobotState.getInstance();
        stateEstimator = RobotStateEstimator.getInstance();

        controlLoop.addLoopable(driveTrain.getLoopable());
        controlLoop.addLoopable(stateEstimator);
        controlLoop.addLoopable(shooter);

        //Slower loops because why update them 100 times a second
        slowerLooper.addLoopable(gearMan);
        slowerLooper.addLoopable(LightFlasherLoopable.getInstance());

        //slowerLooper.addLoopable(VisionProcessor.getInstance());

        //Logging for auto
        logLooper = new ControlLoop(1.0);
        logLooper.addLoopable(PositionLoggerLoopable.getInstance());

        revRoboticsPressureSensor = new RevRoboticsPressureSensor(0);

        AutoModeSelector.putToSmartDashboard();
        autoModeRunner = new AutoModeRunner();
    }

    @Override
    public void autonomousInit() {
        zeroAllSensors();
        Timer.delay(.25);


        driveTrain.setHighGear(Constants.Drive.kDriveDefaultHighGear);
        driveTrain.setOpenLoop(DriveSignal.NEUTRAL);
        driveTrain.setBrakeMode(false);

        gearMan.setWantOpen(false);
        shooter.setWantIdleRampUp(false);
        shooter.setWantShoot(false);

        robotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());
        logLooper.start();
        controlLoop.start();
        slowerLooper.start();
        autoModeRunner.setAutoMode(AutoModeSelector.getAutoInstance());
        autoModeRunner.start();
    }

    @Override
    public void teleopInit() {
        robotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());

        zeroAllSensors();

        autoModeRunner.stop();

        shooter.setWantShoot(false);
        shooter.setWantIdleRampUp(false);

        controlLoop.start();
        slowerLooper.start();

        driveTrain.setOpenLoop(DriveSignal.NEUTRAL);
        driveTrain.setHighGear(Constants.Drive.kDriveDefaultHighGear);
        driveTrain.setBrakeMode(true);

        gearMan.setWantOpen(false);
        pickup.setIntakeState(Pickup.PickupState.STOP);

        driveTrain.resetEncoders();
    }

    @Override
    public void teleopPeriodic() {
        driveTrain.setHighGear(controls.getHighGear());

        driveTrain.setOpenLoop(driveHelper.drive(controls.getTank(), controls.getTurn(), controls.getQuickTurn()));
        visionTurn = false;

        gearMan.setWantOpen(controls.getGearManState());
        gearMan.setWantPunch(controls.getWantPunch());
        pickup.setIntakeState(controls.getIntakeState());

        shooter.setWantShoot(controls.getWantShoot());
        //shooter.setWantIdleRampUp(controls.getWantShooterIdle());
        shooter.setWantReverseAgitator(controls.getWantReverseAgitator());

        climber.setClimberState(controls.getClimberState(pdp.getCurrent(2)));

        SmartDashboard.putNumber("gyro", driveTrain.getGyroAngleDegrees());
        SmartDashboard.putNumber("gyroRate", driveTrain.getGyroRateDegrees());
        SmartDashboard.putNumber("psi", revRoboticsPressureSensor.getAirPressurePsi());
        SmartDashboard.putBoolean("gearman", gearMan.getSolenoidState());
        SmartDashboard.putNumber("climb_amp", pdp.getCurrent(2));
        SmartDashboard.putNumber("vel", driveTrain.getLeftVelocityInchesPerSec());
        robotState.outputToSmartDashboard();
    }

    @Override
    public void disabledInit() {
        controlLoop.stop();
        logLooper.stop();
        slowerLooper.stop();
        autoModeRunner.stop();
        zeroAllSensors();
    }

    @Override
    public void disabledPeriodic() {
        driveTrain.resetEncoders();
        robotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());
        System.gc();
    }

    public void zeroAllSensors() {
        driveTrain.resetEncoders();
        driveTrain.zeroGyro();
    }
}

