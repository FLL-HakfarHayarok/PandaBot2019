package robotUtils;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

/**
 * This is a class which maps out the hardware connected to the robot for use by
 * other classes. All port and hardware changes should be altered in this class
 * and any calls to motors or sensors should directly reference this class.
 * TODO: Replace existing variables with your own connections.
 * ***MAKE SURE TO CREATE SAMPLEPROVIDERS HERE AS WELL***
 * 
 * @author John & Wifi
 */
public class RobotStructure {

	private static RobotStructure instance;
	
	
	
	/**
	 * Method that returns the only instance of this class, or inits it.
	 * @return an instance of a robot structure
	 */
	public static RobotStructure getInstance() {
		if (instance == null)
			init();
		return instance;
	}
	
	private static void init() {
		instance = new RobotStructure();
	}
	
	//TODO: define your hardware below:
	
	public final EV3LargeRegulatedMotor leftWheel, rightWheel;
	public final EV3MediumRegulatedMotor leftArm, rightArm;
	public final EV3GyroSensor gyro;
	public final EV3ColorSensor rightColor, leftColor;
	public final SampleProvider gyroAngleSampler, rightColorRedSampler, leftColorRedSampler;
	
	/**
	 * Constructor for a new robot map. 
	 * TODO: Init your hardware here.
	 */
	public RobotStructure() {
		
		leftWheel = new EV3LargeRegulatedMotor(MotorPort.B);
		rightWheel = new EV3LargeRegulatedMotor(MotorPort.C);
		leftArm = new EV3MediumRegulatedMotor(MotorPort.D);
		rightArm = new EV3MediumRegulatedMotor(MotorPort.A);
		
		gyro = new EV3GyroSensor(SensorPort.S4);
		rightColor = new EV3ColorSensor(SensorPort.S3);
		leftColor = new EV3ColorSensor(SensorPort.S2);

		
		gyroAngleSampler = gyro.getAngleMode();
		rightColorRedSampler = rightColor.getRedMode();
		leftColorRedSampler = leftColor.getRedMode();
		
	}
	
	
	/**
	 * Stops all robot motors
	 * TODO: Input your own connections
	 */
	public void stopAllMotors() {
		leftWheel.stop(true);
		rightWheel.stop(true);
		leftArm.stop(true);
		rightArm.stop(true);
	}
	
	public void floatAllMotors() {
		leftWheel.flt(true);
		rightWheel.flt(true);
		leftArm.flt(true);
		rightArm.flt(true);
	}
}
