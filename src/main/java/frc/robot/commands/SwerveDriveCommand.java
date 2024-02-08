package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.*;
import edu.wpi.first.hal.simulation.RoboRioDataJNI;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class SwerveDriveCommand extends Command{
  //: The only thing the DriveCommand needs is the power in which it will move the motors and the subsystem
  double magnitudes[] = new double[4];
  double angles[] = new double[4];
  double xComponents[] = new double[4];
  double yComponents[] = new double[4];
  private SwerveDriveSubsystem swerveDriveSubsystem;
  //: In the constructor we only need to store the subsystem in order for it to work.
  public SwerveDriveCommand(SwerveDriveSubsystem subsystem) {
    swerveDriveSubsystem = subsystem;
    addRequirements(swerveDriveSubsystem);
    magnitudes[0] = 0;
    magnitudes[1] = 0;
    magnitudes[2] = 0;
    magnitudes[3] = 0;
    angles[0] = 0;
    angles[1] = 0;
    angles[2] = 0;
    angles[3] = 0;
  }
  //: The drive command will do all the calculations for the swerve to work
  public void drive(double xAxis, double yAxis, double zAxis){
    if(Math.abs(xAxis) < Constants.stickDeadband){
      xAxis = 0.0;
    }
    if(Math.abs(yAxis) < Constants.stickDeadband){
      yAxis = 0.0;
    }
    if(Math.abs(zAxis) <  Constants.stickDeadband){
      zAxis = 0.0;
    }
    magnitudes[0] = 0;
    magnitudes[1] = 0;
    magnitudes[2] = 0;
    magnitudes[3] = 0;
    if(xAxis == 0 && yAxis == 0){
      //This scenario is just rotating the motor
      if(Math.abs(zAxis) > 0){
        SmartDashboard.putString("Controler State", "Rotate in place");
        angles[0] = 45;
        angles[1] = 315;
        angles[2] = 135;
        angles[3] = 225;
        magnitudes[0] = zAxis;
        magnitudes[1] = zAxis;
        magnitudes[2] = zAxis;
        magnitudes[3] = zAxis;
      }
      else{
        SmartDashboard.putString("Controler State", "STOP");
        magnitudes[0] = 0;
        magnitudes[1] = 0;
        magnitudes[2] = 0;
        magnitudes[3] = 0;
      }
    }
    else{
      if(zAxis ==  0){
        //This scenario is not rotation, but with translation
         SmartDashboard.putString("Controler State", "Translation");
        double generalAngle = getGeneralAngle(xAxis, yAxis);
        generalAngle -= Constants.Swerve.pigeonSubsystem.getZAxis();
        generalAngle = angleStabalizer(generalAngle);
        double generalMagnitude = getMagnitude(xAxis,yAxis);
        angles[0] = generalAngle;
        angles[1] = generalAngle;
        angles[2] = generalAngle;
        angles[3] = generalAngle;
        magnitudes[0] = generalMagnitude;
        magnitudes[1] = generalMagnitude;
        magnitudes[2] = generalMagnitude;
        magnitudes[3] = generalMagnitude;
      }
      else{
        //This scenario combines the rotation vector with the translation vector
        SmartDashboard.putString("Controler State", "Translation and Rotation");
        double robotAngle =  Constants.Swerve.pigeonSubsystem.getZAxis();
        xComponents[0] = (zAxis * Constants.Swerve.width)/2 + (xAxis/2);
        yComponents[0] = (zAxis * Constants.Swerve.length)/2 + (yAxis/2);
        xComponents[1] = (zAxis * Constants.Swerve.width)/2 + (xAxis/2);
        yComponents[1] = (zAxis * -Constants.Swerve.length)/2 + (yAxis/2);
        xComponents[2] = (zAxis * -Constants.Swerve.width)/2 + (xAxis/2);
        yComponents[2] = (zAxis * Constants.Swerve.length)/2 + (yAxis/2);
        xComponents[3] = (zAxis * -Constants.Swerve.width)/2 + (xAxis/2);
        yComponents[3] = (zAxis * -Constants.Swerve.length)/2 + (yAxis/2);
        angles[0] = angleStabalizer(getGeneralAngle(xComponents[0], yComponents[0]) - robotAngle);
        angles[1] = angleStabalizer(getGeneralAngle(xComponents[1], yComponents[1]) - robotAngle);
        angles[2] = angleStabalizer(getGeneralAngle(xComponents[2], yComponents[2]) - robotAngle);
        angles[3] = angleStabalizer(getGeneralAngle(xComponents[3], yComponents[3]) - robotAngle);
        magnitudes[0] = getMagnitude(xComponents[0], yComponents[0]);
        magnitudes[1] = getMagnitude(xComponents[1], yComponents[1]);
        magnitudes[2] = getMagnitude(xComponents[2], yComponents[2]);
        magnitudes[3] = getMagnitude(xComponents[3], yComponents[3]);
      }
    }
    
  }
  public double angleStabalizer(double angle){
    if (angle < 0) {
      angle += 360;
    }
    if (angle >= 360) {
      angle = angle % 360;
    }
    return angle;
  }
  public double getGeneralAngle(double x, double y){
    double z = Math.atan2(y,x)*180/Math.PI;
    angleStabalizer(z);
    return z;
  }
  public double getMagnitude(double x, double y){
    double magnitude = Math.sqrt((x * x) + (y * y));
    if(magnitude > 1){
      magnitude = 1.0;
    }
    return magnitude;
  }
  //Override functions are parent class modified functions that allow the command to execute itsel constantly.
  @Override
  public void execute() {
    swerveDriveSubsystem.setMovement(magnitudes[0], magnitudes[1], magnitudes[2], magnitudes[3]);
    swerveDriveSubsystem.setAngles(angles[0], angles[1], angles[2], angles[3]);
  }
  //By returning false, we are telling the program that is a default command.
  @Override
  public boolean isFinished() {
    return false;
  }
  
    
}
