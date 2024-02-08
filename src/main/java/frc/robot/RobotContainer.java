// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*: Welcome to the Robot Container class. Here is where most of the magic happens, the robots commands 
and subsystems work together to move the robot.
*/
package frc.robot;
//: For every object you use you need to import its libraries
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.sql.Time;
/*: Some libraries aren't from WPILIB so you will need to manage your vendor libraries and import them
For Phoenix use the following link: 
https://maven.ctr-electronics.com/release/com/ctre/phoenixpro/PhoenixProAnd5-frc2023-latest.json*/
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.SwerveDriveCommand;
//: The following 2 imports are the folders that hold my commands and subsystems.
//import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class RobotContainer {
  /*⬇: First we need to declare the Subsystems. Subsystems are the parts of the robot divided in different
   sections. They are just the pieces, they hold the actions like move, return speed or anything you want.
   However the Commands are the ones that will define which actions the subsystems will do.
  */
  SwerveDriveSubsystem swerveDriveSubsystem = new SwerveDriveSubsystem(Constants.Swerve.module1, Constants.Swerve.module2, Constants.Swerve.module3, Constants.Swerve.module4);
  /*: Then we need to declare the Commands. The commands will recieve a subsystem and they will behave
  like commanders, sending orders to the subsystems to do the actions they want.
  */
  SwerveDriveCommand swerveDriveCommand = new SwerveDriveCommand(swerveDriveSubsystem);
  //: The swerveWheelCommand is in charge of commanding the swerveWheelSubsystem.

  // : Here we declared the player controller.
  public  XboxController playerController = new XboxController(0);
  public RobotContainer() {
    //: To initialize the RobotContainer we decided to set up Default Commands.
    /*: Default Commands are the ones that run over and over again, unless another command is 
    called that requires the same subsystem.*/
    Constants.Swerve.module1.setOffset(25.61);
    Constants.Swerve.module2.setOffset(256.56);
    Constants.Swerve.module3.setOffset(287.93);
   Constants.Swerve.module4.setOffset(187.29);
    swerveDriveSubsystem.setDefaultCommand(swerveDriveCommand);
  }
  /*: This funcition will run commands by checking if a button was pressed
  Didn't used it for this example, since it was just moving the robot.*/
  private void checkButtons() {
    if(playerController.getAButtonPressed()){
      Constants.Swerve.pigeonSubsystem.resetPigeon();
    }
  }

  //: Right now this isn't used and we will probably change it
  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  /*: We know teleopPeriodic is in the Robot class, but it isn't enough in order to control multiple
  tasks. That is why all the commands and subsytems interaction will be ran here.*/
  public void teleopPeriodic(){
     /*: In here we are updating the controller input. Now I understand it looks weird sending both
     values as negative, but since the driver station speaks Yoda and the controller joysticks values are
     inverted, this values need to be sent as negative.
     */
    swerveDriveCommand.drive(playerController.getLeftX(), -playerController.getLeftY(), playerController.getRightX());
    checkButtons();
     //: You can see how .setMovement works in the swerveWheelCommand.java file in the commands folder
     // : Once all commands are updated, everything will run.
     CommandScheduler.getInstance().run();
  }
}
