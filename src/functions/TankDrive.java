package functions;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class TankDrive {

	/**
	 * Drives both wheels at the same time with individual speeds for a certain number of degrees
	 * @param leftSpeed The speed the left wheel should move in
	 * @param rightSpeed The speed the right wheel should move in
	 * @param degrees how many degrees the robot should move
	 */
	public static void degrees(int leftSpeed, int rightSpeed, int degrees) {
		// assigns received speeds to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(leftSpeed));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(rightSpeed));

		// makes robot drive the desired amount of degrees with correct direction
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (leftSpeed > 0)
			RobotStructure.getInstance().leftWheel.rotate(degrees);
		else
			RobotStructure.getInstance().leftWheel.rotate(-degrees);

		if (rightSpeed > 0)
			RobotStructure.getInstance().rightWheel.rotate(degrees);
		else
			RobotStructure.getInstance().rightWheel.rotate(-degrees);

		RobotStructure.getInstance().leftWheel.endSynchronization();

		// exits the function when both wheels stop moving
		while (RobotStructure.getInstance().leftWheel.isMoving()
				|| RobotStructure.getInstance().rightWheel.isMoving() && RunHandler.getCurrentRun().isActive())
			;

		RobotStructure.getInstance().leftWheel.stop(true);
		RobotStructure.getInstance().rightWheel.stop();
	}

	/**
	 * Drives both wheels at the same time with individual speeds for a certain amount of time
	 * @param leftSpeed The speed the left wheel should move in
	 * @param rightSpeed The speed the right wheel should move in
	 * @param duration How long the robot should move for (seconds)
	 */
	public static void secs(int leftSpeed, int rightSpeed, double duration) {
		seconds(leftSpeed, rightSpeed, duration, false);
	}
	
	/**
	 * Drives both wheels at the same time with individual speeds for a certain amount of time
	 * @param leftSpeed The speed the left wheel should move in
	 * @param rightSpeed The speed the right wheel should move in
	 * @param duration How long the robot should move for (seconds)
	 * @param coast Whether or not the robot should coast at the end(optional; default - false)
	 */
	public static void seconds(int leftSpeed, int rightSpeed, double duration, boolean coast) {

		// assigns received speeds to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(leftSpeed));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(rightSpeed));

		// sets direction based on whether the speed is positive or negative
		if (leftSpeed > 0) {
			RobotStructure.getInstance().leftWheel.forward();
		} else {
			RobotStructure.getInstance().leftWheel.backward();
		}
		if (rightSpeed > 0) {
			RobotStructure.getInstance().rightWheel.forward();
		} else {
			RobotStructure.getInstance().rightWheel.backward();
		}

		// waits the defined amount of time
		Wait.waitForSeconds(duration);

		// stops in a coast or brake
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (coast) {
			RobotStructure.getInstance().leftWheel.flt();
			RobotStructure.getInstance().rightWheel.flt();
		} else {
			RobotStructure.getInstance().leftWheel.stop();
			RobotStructure.getInstance().rightWheel.stop();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();

	}

	/**
	 * Drives until sees a certain light level
	 * @param speed The speed the robot should move in
	 * @param color The light level the robot should be looking for
	 * @param colorSensor The sensor that should look for the desired light level
	 */
	public static void untilColor(int speed, float color, EV3ColorSensor colorSensor) {
		untilColor(speed, color, colorSensor, false);
	}
	
	/**
	 * Drives until sees a certain light level
	 * @param speed The speed the robot should move in
	 * @param color The light level the robot should be looking for
	 * @param colorSensor The sensor that should look for the desired light level
	 * @param coast Whether or not the robot should coast at the end(optional; default - false)
	 */
	public static void untilColor(int speed, float color, EV3ColorSensor colorSensor, boolean coast) {
		double maxColor = color + 0.05;
		double minColor = color - 0.05;

		SampleProvider sample = colorSensor == RobotStructure.getInstance().rightColor
				? RobotStructure.getInstance().rightColorRedSampler
				: RobotStructure.getInstance().leftColorRedSampler;

		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(speed));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(speed));

		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (speed > 0) {
			RobotStructure.getInstance().leftWheel.forward();
			RobotStructure.getInstance().rightWheel.forward();
		} else {
			RobotStructure.getInstance().leftWheel.backward();
			RobotStructure.getInstance().rightWheel.backward();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();

		while (Color.getLight(sample) > maxColor
				|| Color.getLight(sample) < minColor && RunHandler.getCurrentRun().isActive())
			;

		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (coast) {
			RobotStructure.getInstance().leftWheel.flt();
			RobotStructure.getInstance().rightWheel.flt();
		} else {
			RobotStructure.getInstance().leftWheel.stop();
			RobotStructure.getInstance().rightWheel.stop();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();
	}

	/**
	 * Drives both wheels at the same time with individual speeds until reaches a certain Gyro angle.
	 This is not used for straightening, but for moving in a curve until getting to the desired angle
	 * @param leftSpeed The speed the left wheel should move in
	 * @param rightSpeed The speed the right wheel should move in
	 * @param targetGyroDegrees the Gyro angle the robot should stop on
	 */
	public static void untilGyroDegrees(int leftSpeed, int rightSpeed, int targetGyroDegrees) {

		// assigns received speeds to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(leftSpeed));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(rightSpeed));

		// makes robot drive the desired amount of degrees with correct direction
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (leftSpeed > 0)
			RobotStructure.getInstance().leftWheel.forward();
		else
			RobotStructure.getInstance().leftWheel.backward();

		if (rightSpeed > 0)
			RobotStructure.getInstance().rightWheel.forward();
		else
			RobotStructure.getInstance().rightWheel.backward();

		RobotStructure.getInstance().leftWheel.endSynchronization();

		// exits the function when both wheels stop moving
		if (leftSpeed < rightSpeed)
			while (Gyro.getAngle() > targetGyroDegrees && RunHandler.getCurrentRun().isActive())
				LCD.drawInt(Gyro.getAngle(), 0, 0);
		else
			while (Gyro.getAngle() < targetGyroDegrees && RunHandler.getCurrentRun().isActive())
				LCD.drawInt(Gyro.getAngle(), 0, 0);

		RobotStructure.getInstance().leftWheel.stop(true);
		RobotStructure.getInstance().rightWheel.stop();
	}

}
