package com.first.team2052.steamworks;

public class Constants {
    public static final double kControlLoopPeriod = 1.0 / 100.0;
    public static final double kSlowControlLoopPeriod = 1.0 / 10.0;

    public static class CAN {
        //L/R in respect to the gearman being the "front"
        public static final int kDriveLeft1Id = 4;
        public static final int kDriveLeft2Id = 5;
        public static final int kDriveLeft3Id = 6;

        public static final int kDriveRight1Id = 1;
        public static final int kDriveRight2Id = 2;
        public static final int kDriveRight3Id = 3;


        public static final int kRightAgitatorMotorPort = 7;
        public static final int kLeftAgitatorMotorPort = 8;

        public static final int kShooterMotorPort = 9;
        public static final int kShooterMotorSlavePort = 10;
        public static final int kClimbId = 12;
        public static final int kIndexerId = 13;
        public static int kIntakeMotorPort = 11;
    }

    public static class Drive {
        public final static double kTrackScrubFactor = 0.65;
        public final static double kTrackEffectiveDiameter = (27.25 * 27.25 + 13 * 13) / 27.25;
        private static final int kDriveTicksPerRot = 256;
        private static final double kDriveThirdStageGearRatio = 54.0 / 30.0;
        //Solenoid Id's for shifting
        public static int kDriveOutSolenoidId = 1;
        public static int kDriveInSolenoidId = 0;
        // Default state of the drive train transmission when in teleopInit, autoInit, and robotInit
        public static boolean kDriveDefaultHighGear = false;
        // VEX 3 CIM ball shift with 3rd stage 3 rotations per gearbox output shaft rotation * ratio for gearbox
        // 5529.6 native units per rot
        public static int kDriveEncoderTicksPerRot = (int) (3 * kDriveTicksPerRot * kDriveThirdStageGearRatio);
        public static double kDriveWheelDiameterInches = 4.0;
        public static double kDriveVelocityKp = 0.2;
        public static double kDriveVelocityKi = 0.0;
        public static double kDriveVelocityKd = 2.0;
        public static double kDriveVelocityKf = 0.3;
        public static int kDriveVelocityIZone = 0;
        public static double kDriveVelocityRampRate = 0.0;
        public static int kDriveVelocityAllowableError = 0;
        public static double kPathFollowingLookahead = 24;
        public static double kPathFollowingMaxAccel = 50;
        public static double kPathFollowingMaxVel = 90;
        public static double kDriveHeadingVelocityKp = 5.0;
        public static double kDriveHeadingVelocityKi = 0.1;
        public static double kDriveHeadingVelocityKd = 60.0;
    }

    public static class GearMan {
        //Solenoid Id's
        public static int kGearManInSolenoidId = 2;//5 on phil 2 on hornet
        public static int kGearManOutSolenoidId = 3;//4 on phil 3 on hornet
        public static int kGearManPunchInSolenoidId = 4;//7 on phil 4 on hornet
        public static int kGearManPunchOutSolenoidId = 5;//6 on phil 5 on hornet

        public static double kGearManPunchWaitSeconds = 0.35;
    }

    public static class Pickup {
        //Intake speed in %
        public static final double kIntakeMotorSpeedIn = 0.8;
        public static final double kIntakeMotorSpeedOut = -0.5;
    }

    public static class Shooter {
        //Use cable to Mag encoder or use Magnetic Encoder converted to a Quad encoder
        //Talk to Adam or Nate about this
        public static final boolean kUseDoubleEncoderExtender = true;

        public static final double kShooterVelocityKp = 0.15;
        public static final double kShooterVelocityKi = 0.0;
        public static final double kShooterVelocityKd = 0.5;
        public static final double kShooterVelocityKf = 0.03568;
        public static final int kShooterVelocityIZone = 0;
        public static final int kShooterVelocityCloseLoopRampRate = 0;
        //RPM (MAX 4200rpm) Never set above 4200 because we might not get to that exact RPM
        //2900 not 3000 ;)
        public static final int kShooterKeyVelocity = 3100;
        //RPM amount away from the target that the agitators will begin spinning at
        public static final int kShooterVelocityWindow = 300;
        //Agitator speed in %
        public static double kTatorSpeed = 0.70;
    }

    public static class Climber {
        public static final double kClimberMotorSpeedUp = 1.0;
        public static final double kClimberMotorSlowSpeed = 0.7;
        public static final double kClimberAmpMax = 40;
    }
}
