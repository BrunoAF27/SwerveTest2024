package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
//This is the library and object of the NavX
import com.ctre.phoenix6.hardware.Pigeon2;
//This are the libraries for the NavX port
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/*Welcome to the NavxSubsystem Program.
The NavxSubsystem is used to gather data from the Navx is a more efficient and readable way. Also for it to
be accesed from anywhere in the program. Axis could change depending of the navX and roborio positioning.
*/
public class PigeonSubsystem extends SubsystemBase{
  //The AHRS object allow us to get data from the NavX. 
  private final Pigeon2 pigeon2; 
  //⬇: zAxis is the rotation in of right and left of the roboRIO
  private  double zAxis = 0;
  //⬇: xAxis is the rotation of up and down of the roborio
  private  double xAxis = 0;
  //⬇: yAxis is the rotation of the spinning right and left of the roboRIO.
  private  double yAxis = 0;
  /*⬇: navXAngle is a value used in the odometry. It is like another version of the zAxis, but it can go
  further than 360 degrees.
  */
  private  double pigeonXAngle = 0;
  public PigeonSubsystem(int pigeonID){
    pigeon2 = new Pigeon2(pigeonID);
    pigeon2.getConfigurator().apply(new Pigeon2Configuration());
    pigeon2.setYaw(0);
  }
  //⬇: The getData() method prints the data of the navX in the console.
  public void getData(){
    //⬇: .getYaw() returns the angle it is in the rotation of left and right of the roborio
    zAxis = pigeon2.getYaw().getValue();
    //⬇: .getPitch() returns the angle it is in the rotation of up and down of the roborio
    xAxis = pigeon2.getPitch().getValue();
     //⬇: .getRoll() returns the angle it is in the rotation of spinning left and right
    yAxis = pigeon2.getRoll().getValue();
    System.out.println("Z axis Angle: " + zAxis);
    System.out.println("x axis Angle: " +  xAxis);
    System.out.println("y axis Angle: " + yAxis);
  }
  /*⬇: The getAxis methods are designed to return their respective axis, so other methods could read the
  navx data.*/
  public double getXAxis(){
    xAxis = pigeon2.getPitch().getValue();
    return xAxis;
  }
  public double getYAxis(){
    yAxis = pigeon2.getRoll().getValue();
    return yAxis;
  }
  public double getZAxis(){
    pigeon2.getRotation2d();
    zAxis = pigeon2.getYaw().getValueAsDouble();
    zAxis = angleStabalizer(zAxis);
    //pigeon2.setYaw(zAxis);
    SmartDashboard.putNumber("Pigeon Z Axis: ", zAxis);
    return zAxis;
  }
  public double angleStabalizer(double angle){
    if(angle < -360){
      angle = -(Math.abs(angle) %360);
    }
    if (angle < 0) {
      angle += 360;
    }
    if (angle >= 360) {
      angle = angle % 360;
    }
    return angle;
  }
  public void resetPigeon(){
    pigeon2.setYaw(0);
  }
  /*⬇: The getnavX returns the navX, it is used in some parts of the odometry.*/
  public Pigeon2 getPigeon2(){
    return pigeon2;
  }
}
