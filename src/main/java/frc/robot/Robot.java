// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//: Package is the whole folder that controls the robot All your code will be written in the src folder.
package frc.robot;
//: | libraries are for controlling the Timed Robot. They appear by default by using the template.
import edu.wpi.first.wpilibj.TimedRobot;
/*: The difference between a Timed robot and a Command Based Programing Robot is that the command based 
robot can run multiple codes at the same time (That involve different Subsystems "parts of the robot") and
the Timed Robot is just one code running everything which gives it a stroke and is not highly recommended.
*/
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer;

//-: In the Robot program we are going to do a few things. Basically make sure that the RobotContainer is running.
/*extends is a keyword for creating a program of a different name but constructed based on the same class.
  For example if I have an Animals class, Duck will extend Animals.
*/
//⬇: All the code will be written inside the class
public class Robot extends TimedRobot {
 
  //⬇: The autonomous command exists. All commands declared here are in robotContainer
  private Command autonomousCommand;

  //⬇: here the robotContainer code is declared.
  private RobotContainer robotContainer;
  /*⬇: Override is rewrite the actions that a function do. Remember the extends TimedRobot? Well the Robot
  class inherited the robotInit function and many more, but we need to modify them in order for the robot to do
  our job. robotInit runs actions when the robot is turned on.
  */
  @Override
  public void robotInit() {
    /*⬇: RobotContainer is a class personaly made that has the job to run all commands with their respective subsystem.
    You can see robotContainer as a file of code that will be ran by timed robot.
    robotContainer is private, because we only want one instance of it and only accesible by the Robot program.
    */
    robotContainer = new RobotContainer();
    /*- : To make one instance of an object you write: 
    private/public ObjectName variableWithObjectName = new ObjectName(parameters);
    For a good practice write variables in camelCase. However Objects don't follow camel case, since they are
    codes written by someone else or yourself they always go with UpperCase in each word.
    Difference in good practices: ObjectName, variableName
    */
  }
  /*⬇: Override is written again here and many more times. By the way, void methods are the ones that only
  do actions. They don't return a value. You will understand this concept better in other files*/
  @Override
  public void robotPeriodic() {
   //↑: robotPeriodic() is the function that runs actions while the robot is on
  }

  @Override
  public void disabledInit() {
    //↑: disabledInit() is the function that runs actions when the robot enters the disabled state
  }

  @Override
  public void disabledPeriodic() {
     //↑: disabledPeriodic() is the function that runs actions while the robot is in the disabled state
  }

  @Override
  public void disabledExit() {
    /*↑: disabledExit() is the function that runs actions when the robot is enabled, no longer disabled.
    It is easier to understand with the Driver Station*/

  }

  @Override
  public void autonomousInit() {
    /*↑: autonomousInit() is the function that runs actions when the robot starts the autonomous mode. 
    Right now it is empty since it doesn't exist. 
    ⬇For the autonomous consider making a sendable chooser which is a menu for autonomous but don't 
    need to worry about it right now. Check it after reviewing the other test.
    */
    autonomousCommand = robotContainer.getAutonomousCommand();
    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    //↑: autonomousPeriodic() is the function that runs actions while the robot is in the autonomous mode. 
  }

  @Override
  public void autonomousExit() {
     //↑: autonomousExit() is the function that runs actions while the robot gets out of the autonomous mode
  }

  @Override
  public void teleopInit() {
      /*↑: teleopInit() is the function that runs actions when the robot starts teleoparated mode
      teleoparated is when we can move the robot*/
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    /*↑: teleopPeriodic() is the function that runs actions while the robot is in teleoparated mode.
    ⬇The robotContainer class will be in charge of running the commands. This is why we call its
    teleopPeriodic method. I know it sounds weird and dumb, but trust me it works perfectly.*/
   robotContainer.teleopPeriodic();
  }

  @Override
  public void teleopExit() {
    //↑: teleopExit() is the function that runs actions when the robot stops its teleoparated mode.
  }
  //⬇: We never use test mode so don't worry about it
  @Override
  public void testInit() {
    //⬇: This line of code just stops all commands from running. It is useful if it saturates while using robot.
    CommandScheduler.getInstance().cancelAll();
    
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
