package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Constants;
import frc.robot.Robot;
import com.ctre.phoenix.sensors.*;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.controls.DutyCycleOut;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/*: Welcome to the DriveSubsystem program. This program requires the existance of a Command in order for
it to know which actions to do. Remember extends? Well the SubsystemBase class allows us to make our own custom 
Subsystems. By the  way the implementation of commands changed this year. Hopefully they won't change it again, 
otherwise I'll advise you to see other programs
*/
public class SwerveWheelSubsystem extends SubsystemBase{
  /*: Here we are going to make an instance of the VictorSpx controller object in order to control
  the motors with Victor Controllers. To do this we equal a variable of type WPI_VictorSPX
  to its constructor new WPI_VictorSPX(int canId). Use Phoenix turner to check the IDs*/
  private final TalonFX driveMotor;
  private final TalonFX angleMotor;
  private CANcoder angleEncoder;
  private double angleDirectionMultiplier;
  private double driveDirectionMultiplier;
  private double distanceToTargetAngle;
  double driveSpeed;
  double angleSpeed;
  private DutyCycleOut drivePercentOutput = new DutyCycleOut(0);
  private DutyCycleOut anglePercentOutput = new DutyCycleOut(0);
  //Maybe you'll need PID: private double targetPosition;
  private double currentPosition;
  private double targetAngle;
  private double currentAngle;
  /*: This function is what is called a constructor. A function that can create instances, copies, of
   objects for them to be used in other programs. They have the exact same name as the class and as the file
  */
  public SwerveWheelSubsystem(int driveMotorID, int angleMotorID, int canCoderID){
    /*: There are two Neutral Modes Brake and Coast. Brake reacts faster and better for change and stops
    the robot inmediatley if necesary. Coast consumes less energy however it continues moving even if you tell
    it to stop.
    */
    driveMotor = new TalonFX(driveMotorID);
    angleMotor = new TalonFX(angleMotorID);
    driveMotor.getConfigurator().apply(new TalonFXConfiguration());
    angleMotor.getConfigurator().apply(new TalonFXConfiguration());
    driveMotor.setNeutralMode(Constants.Swerve.driveNeutralMode);
    angleMotor.setNeutralMode(Constants.Swerve.angleNeutralMode);
    driveMotor.setInverted(Constants.Swerve.driveMotorInvert);
    angleMotor.setInverted(Constants.Swerve.angleMotorInvert);
    angleEncoder = new CANcoder(canCoderID);
    angleEncoder.getConfigurator().apply(new CANcoderConfiguration());
    applyConfigurations();
  }

  public void setAnglePosition(double angle){
    /*: .set(ControlMode, speed) is a method for you to move the motors individually.
    I recommend using ControlMode.PercentOutput since the values are from -1 to 1 and you just give
    it a percentage of the maximum voltage that the motor can recieve.*/
    //: First we  get the position from the encoders
    currentAngle = angleEncoder.getAbsolutePosition().getValue();
    currentAngle *= 360;
    targetAngle = closestTargetAngle(angle);
    //270
    //angleSpeed = 0.17 * Math.log(distanceToTargetAngle + 1) * angleDirectionMultiplier;
    angleSpeed = (distanceToTargetAngle / 150 * angleDirectionMultiplier);
    if(angleSpeed > Constants.Swerve.maxAngularVelocity){
      angleSpeed = Constants.Swerve.maxAngularVelocity;
    }
    else if(angleSpeed <  -Constants.Swerve.maxAngularVelocity){
      angleSpeed = -Constants.Swerve.maxAngularVelocity;
    }
    //angleSpeed = angleSpeed * Math.abs(angleSpeed);
    //angleSpeed = angleSpeed * 0.5;
    angleMotor.setControl(anglePercentOutput.withOutput(angleSpeed));
    //Maybe you'll need PID: ptargetPosition = convertAngleToPosition(angle);
    //angleMotor.set(ControlMode.Position, targetPosition);
  }
  /*May need it for PID
  public double  convertAngleToPosition(double angle){
    double position = angle / (360 / (Constants.gearRatio * 2048.0));
    //double position = angle / (180 / (RobotMap.gearRatio * 2048.0));
    return position;
  }
  */
  public void setDriveSpeed(double speed){
    driveSpeed = speed * driveDirectionMultiplier;
    driveMotor.setControl(drivePercentOutput.withOutput(driveSpeed));
  }
  public double getAngleSpeed(){
    return angleSpeed;
  }
  public double  closestTargetAngle(double originalAngle){
      double clockwise = (originalAngle - currentAngle + 360) % 360;    
      // (targetAngle - currentAngle + 360 ) para encontrar un coterminal positivo en caso de que sea un ángulo negativo
      // %360 para asegurar que el ángulo siempre se encuentre entre 0-360 sin pasarnos
      //System.out.println("El ángulo clockwise es: " + clockwise + " grados");
      double counterClockwise = (currentAngle - originalAngle + 360) % 360;
      // Aquí también obtendremos un ángulo positivo porque el +360 hace que ambos sean coterminales positivos.
      //System.out.println("El ángulo counterclockwise es: " + counterclockwise + " grados");

      if (clockwise<counterClockwise)
      {
        distanceToTargetAngle = clockwise;

        targetAngle = currentAngle + clockwise;
        targetAngle = angleStabalizer(targetAngle);
        angleDirectionMultiplier = -1;
        if(targetAngle == originalAngle){
          driveDirectionMultiplier = 1;
        }
        else{
          driveDirectionMultiplier = -1;
        }
        return targetAngle; 
      }
      else{
        distanceToTargetAngle = counterClockwise;
        targetAngle = currentAngle - counterClockwise;
         targetAngle = angleStabalizer(targetAngle);
        angleDirectionMultiplier = 1;
        if(targetAngle == originalAngle){
          driveDirectionMultiplier = 1;
        }
        else{
          driveDirectionMultiplier = -1;
        }
        return targetAngle;
      }
  }
  public double getAngle(){
    double angle = angleEncoder.getAbsolutePosition().getValue();
    angle *= 360;
    angle = angleStabalizer(angle);
    return angle;
  }
   public double getTargetAngle(){
    return targetAngle;
  }
  public double angleStabalizer(double angle){
    if (angle < 0) {
      angle = -(Math.abs(angle) % 360);
      angle += 360;
    }
    if (angle >= 360) {
      angle = angle % 360;
    }
    return angle;
  }
  //CONFIGURATION FUNCTIONS:
  //Set the offset of the angle.
  public void setOffset(double angle){
    CANcoderConfiguration encoderConfigs = new CANcoderConfiguration();
    //Fetch *all* configs currently applied to the device
    angleEncoder.getConfigurator().refresh(encoderConfigs);
    angle /= 360;
    encoderConfigs.MagnetSensor.MagnetOffset = angle;
    angleEncoder.getConfigurator().apply(encoderConfigs);
  }
  //Set the configurations of the encoder and the motors.
  public void applyConfigurations(){
    TalonFXConfiguration driveMotorConfigs = new TalonFXConfiguration();
    TalonFXConfiguration angleMotorConfigs = new TalonFXConfiguration();
    CANcoderConfiguration encoderConfigs = new CANcoderConfiguration();
    //Invert Motors:
    //Set Up the drive PID Values
    driveMotorConfigs.Slot0.kP = Constants.Swerve.driveKP;
    driveMotorConfigs.Slot0.kI = Constants.Swerve.driveKI;
    driveMotorConfigs.Slot0.kD = Constants.Swerve.driveKD;
    driveMotorConfigs.Slot0.kV= Constants.Swerve.driveKV;
    //Set Up the angle PID Values
    angleMotorConfigs.Slot0.kP = Constants.Swerve.angleKP;
    angleMotorConfigs.Slot0.kI = Constants.Swerve.angleKI;
    angleMotorConfigs.Slot0.kD = Constants.Swerve.angleKD;
    angleMotorConfigs.Slot0.kV= Constants.Swerve.angleKV;
    //Stablish the drive Current Limits
    driveMotorConfigs.CurrentLimits.StatorCurrentLimit = Constants.Swerve.drivePeakCurrentLimit;
    driveMotorConfigs.CurrentLimits.SupplyCurrentLimit = Constants.Swerve.drivePeakCurrentLimit;
    driveMotorConfigs.CurrentLimits.SupplyCurrentThreshold = Constants.Swerve.drivePeakCurrentDuration;
    driveMotorConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    driveMotorConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    //Stablish the angle Current Limits
    angleMotorConfigs.CurrentLimits.StatorCurrentLimit = Constants.Swerve.anglePeakCurrentLimit;
    angleMotorConfigs.CurrentLimits.SupplyCurrentLimit = Constants.Swerve.anglePeakCurrentLimit;
    angleMotorConfigs.CurrentLimits.SupplyCurrentThreshold = Constants.Swerve.anglePeakCurrentDuration;
    angleMotorConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
    angleMotorConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    //Created drive ramping.
    driveMotorConfigs.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = Constants.Swerve.closedLoopRamp;
    driveMotorConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = Constants.Swerve.closedLoopRamp;
    driveMotorConfigs.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = Constants.Swerve.openLoopRamp;
    driveMotorConfigs.OpenLoopRamps.VoltageOpenLoopRampPeriod= Constants.Swerve.openLoopRamp;
    //Created angle ramping.
    angleMotorConfigs.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = Constants.Swerve.closedLoopRamp;
    angleMotorConfigs.ClosedLoopRamps.VoltageClosedLoopRampPeriod = Constants.Swerve.closedLoopRamp;
    angleMotorConfigs.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = Constants.Swerve.openLoopRamp;
    angleMotorConfigs.OpenLoopRamps.VoltageOpenLoopRampPeriod= Constants.Swerve.openLoopRamp;
    //Neutral deadband, values where output is 0
    driveMotorConfigs.MotorOutput.DutyCycleNeutralDeadband = Constants.Swerve.driveNeutralDeadband;
    //Angle deadband
    angleMotorConfigs.MotorOutput.DutyCycleNeutralDeadband = Constants.Swerve.angleNeutralDeadband;
    //Limit Drive Velocities
    driveMotorConfigs.MotorOutput.PeakForwardDutyCycle = Constants.Swerve.maxDriveVelocity;
    driveMotorConfigs.MotorOutput.PeakReverseDutyCycle = -Constants.Swerve.maxDriveVelocity;
    driveMotorConfigs.Voltage.PeakForwardVoltage = Constants.Swerve.maxDriveVelocity * 12;
    driveMotorConfigs.MotorOutput.PeakReverseDutyCycle = -Constants.Swerve.maxDriveVelocity * 12;
    //Limit Angle Velocities
    angleMotorConfigs.MotorOutput.PeakForwardDutyCycle = Constants.Swerve.maxAngularVelocity;
    angleMotorConfigs.MotorOutput.PeakReverseDutyCycle = -Constants.Swerve.maxAngularVelocity;
    angleMotorConfigs.Voltage.PeakForwardVoltage = Constants.Swerve.maxAngularVelocity * 12;
    angleMotorConfigs.MotorOutput.PeakReverseDutyCycle = -Constants.Swerve.maxAngularVelocity * 12;
    //Configure Encoder: Do it later
    encoderConfigs.MagnetSensor.AbsoluteSensorRange.valueOf(AbsoluteSensorRangeValue.Unsigned_0To1.value);
    angleEncoder.getConfigurator().apply(encoderConfigs);
    driveMotor.getConfigurator().apply(driveMotorConfigs);
    angleMotor.getConfigurator().apply(angleMotorConfigs);
  }
  /*public void setAngle(double angle){
    /*: .set(ControlMode, speed) is a method for you to move the motors individually.
    I recommend using ControlMode.PercentOutput since the values are from -1 to 1 and you just give
    it a percentage of the maximum voltage that the motor can recieve.
    //: First we  get the position from the encoders
    double currentAngle = angleEncoder.getAbsolutePosition();
    double speed = 0;
    SmartDashboard.putNumber("Angulo Deseado", angle);
    SmartDashboard.putNumber("Angulo Actual", currentAngle);
    if (currentAngle > angle + 5){
      speed = -1 * Math.abs(1 + (Math.abs(currentAngle/50) * -1));
    } 
    else if (currentAngle < angle - 5){
      speed = 1 * Math.abs(1 + (Math.abs(currentAngle/50) * -1));
    }
    angleMotor.set(ControlMode.PercentOutput, speed);
  }*/
}