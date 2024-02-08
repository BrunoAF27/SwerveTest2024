package frc.robot;
//: RobotMap is a class of global things that should and could be accesed from anywhere in the code

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.*;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Preferences;
public final class Constants {
    //General Constants
    public static final double stickDeadband = 0.1;
   //Swerve Constants
    public static final class Swerve {
        public static final int pigeon2ID = 13;
        //Dimensions:
        /*public static final double Original width = 26.6 Inches;
        public static final double Original length = 27 Inches;
        */
        //Divide by 2. Then divide by longest value
        public static final double width = 0.99;
        public static final double length = 1.0;
        //Some values:
        /* Motor Inverts */
        public static final boolean angleMotorInvert = true;
        public static final boolean driveMotorInvert = false;
        /* Angle Encoder Invert */
        public static final boolean canCoderInvert = false;

        /* Swerve Current Limiting */
        //The commented values are the fastest, but if you want to save energy, lower the values.
        public static final int angleContinuousCurrentLimit = 25/*25*/;
        public static final int anglePeakCurrentLimit = 30/*30*/;
        public static final double anglePeakCurrentDuration = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveContinuousCurrentLimit = 25/*35*/;
        public static final int drivePeakCurrentLimit = 30/*60*/;
        public static final double drivePeakCurrentDuration = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final String openLoopRampKey = "Open Loop Ramping";
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;
        /* Angle Motor PID Values */
        public static final String angleKPKey = "Angle KP";
        public static final double angleKP = 0.01;
        public static final String angleKIKey = "Angle KI";
        public static final double angleKI = 0.0;
        public static final String angleKDKey = "Angle KD";
        public static final double angleKD = 0.0;
        //kv what used to be kf
        public static final String angleKVKey = "Angle KV";
        public static final double angleKV = 0.0F;

        /* Drive Motor PID Values */
        public static final String driveKPKey = "Drive KP";
        public static final double driveKP = 0.05;
        public static final String driveKIKey = "Drive KI";
        public static final double driveKI = 0.0;
        public static final String driveKDKey = "Drive KD";
        public static final double driveKD = 0.0;
        public static final String driveKVKey = "Drive KV";
        public static final double driveKV = 0.0;
        //Max Velocities   
        public static final String maxDriveVelocityKey = "Max Drive Velocity";
        public static final double maxDriveVelocity = 1.0; 
        public static final String maxAngularVelocityKey = "Max Angular Velocity";
        public static final double maxAngularVelocity = 1.0;
        //Neutral deadband
        public static final String driveNeutralDeadbandKey = "Drive Neutral Deadband";
        public static final double  driveNeutralDeadband = 0.1;
        public static final String angleNeutralDeadbandKey = "Angle Neutral Deadband";
        public static final double  angleNeutralDeadband = 0.1;
        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Brake;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;
        /*Front Left Module - Module 1 */
        public static final class Mod1 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 5;
            public static final int angleMotorID = 6;
            public static final int canCoderID = 11;
            public static final String angleOffsetKey = "Mod 1 Angle Offset";
            public static final double angleOffset = 25.61;
        }

        /* Front Right Module - Module 2 */
        public static final class Mod2 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 7;
            public static final int angleMotorID = 8;
            public static final int canCoderID = 12;
            public static final String angleOffsetKey = "Mod 2 Angle Offset";
            public static final double angleOffset = 256.56;
        }
        
        /* Back Left Module - Module 3 */
        public static final class Mod3 { //TODO: This must be tuned to specific robot
            /*public static final int driveMotorID = 7;
            public static final int angleMotorID = 8;
            public static final int canCoderID = 12;*/
            public static final int driveMotorID = 4;
            public static final int angleMotorID = 3;
            public static final int canCoderID = 10;
            public static final String angleOffsetKey = "Mod 3 Angle Offset";
            public static final double angleOffset = 287.93;
        }

        /* Back Right Module - Module 4 */
        public static final class Mod4 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 2;
            public static final int angleMotorID = 1;
            public static final int canCoderID = 9;
            public static final String angleOffsetKey = "Mod 4 Angle Offset";
            public static final double angleOffset = 187.29;
        }
        //Components
        public static PigeonSubsystem pigeonSubsystem = new PigeonSubsystem(pigeon2ID);
        public static SwerveWheelSubsystem module1 = new SwerveWheelSubsystem(Constants.Swerve.Mod1.driveMotorID,Constants.Swerve.Mod1.angleMotorID, Constants.Swerve.Mod1.canCoderID);
        public static SwerveWheelSubsystem module2 = new SwerveWheelSubsystem(Constants.Swerve.Mod2.driveMotorID,Constants.Swerve.Mod2.angleMotorID, Constants.Swerve.Mod2.canCoderID);
        public static SwerveWheelSubsystem module3 = new SwerveWheelSubsystem(Constants.Swerve.Mod3.driveMotorID,Constants.Swerve.Mod3.angleMotorID, Constants.Swerve.Mod3.canCoderID);
        public static SwerveWheelSubsystem module4 = new SwerveWheelSubsystem(Constants.Swerve.Mod4.driveMotorID,Constants.Swerve.Mod4.angleMotorID, Constants.Swerve.Mod4.canCoderID);
    }
}
