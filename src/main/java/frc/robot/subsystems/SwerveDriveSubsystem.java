package frc.robot.subsystems;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Preferences;
public class SwerveDriveSubsystem extends SubsystemBase{
    private SwerveWheelSubsystem driveLeftModuleF;
    private SwerveWheelSubsystem driveRightModuleF;
    private SwerveWheelSubsystem driveRightModuleB;
    private SwerveWheelSubsystem driveLeftModuleB;
    public SwerveDriveSubsystem(SwerveWheelSubsystem module1, SwerveWheelSubsystem module2, SwerveWheelSubsystem module3, SwerveWheelSubsystem module4){
        driveLeftModuleF = module1;
        driveRightModuleF = module2;
        driveLeftModuleB = module3;
        driveRightModuleB = module4;  
        Preferences.initDouble(Constants.Swerve.openLoopRampKey, Constants.Swerve.openLoopRamp);
        Preferences.initDouble(Constants.Swerve.openLoopRampKey, Constants.Swerve.openLoopRamp);
    }
    public void setAngles(double leftModuleFAngle, double rightModuleFAngle,double leftModuleBAngle, double rightModuleBAngle){
        driveLeftModuleF.setAnglePosition(leftModuleFAngle);
        driveRightModuleF.setAnglePosition(rightModuleFAngle);
        driveLeftModuleB.setAnglePosition(leftModuleBAngle);
        driveRightModuleB.setAnglePosition(rightModuleBAngle);
        SmartDashboard.putNumber("Left F Angle: ", driveLeftModuleF.getAngle());
        SmartDashboard.putNumber("Left B Module Angle: ", driveLeftModuleB.getAngle());
        SmartDashboard.putNumber("Right F Module Angle: ", driveRightModuleF.getAngle());
        SmartDashboard.putNumber("Right B Module Angle: ", driveRightModuleB.getAngle());
        SmartDashboard.putNumber("Target Left F: ", driveLeftModuleF.getTargetAngle());
        SmartDashboard.putNumber("Angle Speed Left F: ",  driveLeftModuleF.getAngleSpeed());
        SmartDashboard.putNumber("Target Left B: ", driveLeftModuleB.getTargetAngle());
        SmartDashboard.putNumber("Angle Speed 1",  driveLeftModuleF.getAngleSpeed());
    }
    public void setMovement(double leftModuleFSpeed, double rightModuleFSpeed, double rightModuleBSpeed, double leftModuleBSpeed){
        driveLeftModuleF.setDriveSpeed(leftModuleFSpeed);
        driveRightModuleF.setDriveSpeed(rightModuleFSpeed);
        driveRightModuleB.setDriveSpeed(rightModuleBSpeed);
        driveLeftModuleB.setDriveSpeed(leftModuleBSpeed);
    }
    //: Good news, may not need to switch the modules for now.
    public void setModules(SwerveWheelSubsystem module1, SwerveWheelSubsystem module2, SwerveWheelSubsystem module3, SwerveWheelSubsystem module4){
        driveLeftModuleF = module1;
        driveRightModuleF = module2;
        driveLeftModuleB = module3;
        driveRightModuleB = module4;
    }
    /*public void setRampingTime(double rampingTime){
        driveLeftModuleF.setRampingTime(rampingTime);
        driveRightModuleF.setRampingTime(rampingTime);
        driveRightModuleB.setRampingTime(rampingTime);
        driveLeftModuleB.setRampingTime(rampingTime);
    }
    */
}
