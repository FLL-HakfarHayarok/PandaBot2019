package functions;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import robotUtils.RunHandler;

public class MediumMotors {

	/**
	 * Rotates Medium Motor for a certain amount of DEGREES.
	 * 
	 * @param motor   The Motor to rotate.
	 * @param speed   The speed that the Motor should rotate in.
	 * @param degrees The distance in DEGREES that the Motor should rotate.
	 */
	public static void rotateDegrees(EV3MediumRegulatedMotor motor, int speed, int degrees) {
		rotateDegrees(motor, speed, degrees, false);
	}

	/**
	 * Rotates Medium Motor for a certain amount of DEGREES.
	 * 
	 * @param motor           The Motor to rotate.
	 * @param speed           The speed that the Motor should rotate in.
	 * @param degrees         The distance in DEGREES that the Motor should rotate.
	 * @param immediateReturn Whether or not the code should continue playing
	 *                        without waiting for the Motor to finish
	 *                        rotating(optional; Default - false).
	 */
	public static void rotateDegrees(EV3MediumRegulatedMotor motor, int speed, int degrees, boolean immediateReturn) {
		motor.setSpeed(Math.abs(speed));
		if (speed > 0)
			motor.rotate(degrees, immediateReturn);
		else
			motor.rotate(-degrees, immediateReturn);
	}

	/**
	 * Rotates Medium Motor for a certain amount of SECONDS.
	 * 
	 * @param motor    The Motor to rotate.
	 * @param speed    The speed that the Motor should rotate in.
	 * @param duration How long the Motor should rotate for(in SECONDS).
	 */
	public static void rotateSecs(EV3MediumRegulatedMotor motor, int speed, float duration) {
		rotateSecs(motor, speed, duration, false);
	}

	/**
	 * Rotates Medium Motor for a certain amount of SECONDS.
	 * 
	 * @param motor    The Motor to rotate.
	 * @param speed    The speed that the Motor should rotate in.
	 * @param duration How long the Motor should rotate for(in SECONDS).
	 * @param coast    Whether or not the robot should coast at the end(optional;
	 *                 default - false)
	 */
	public static void rotateSecs(EV3MediumRegulatedMotor motor, int speed, float duration, boolean coast) {
		// changes the varible duration from secs to millisecs
		duration *= 1000;
		long startTime = System.currentTimeMillis();

		// sets the motor speed
		motor.setSpeed(Math.abs(speed));

		// rotates the motor forward or backwards
		if (speed > 0)
			motor.forward();
		else
			motor.backward();

		// waits until time expectation is met
		while (System.currentTimeMillis() - startTime < duration && RunHandler.getCurrentRun().isActive())
			;

		// coasts or brakes the motor
		if (coast)
			motor.flt();
		else
			motor.stop();
	}

}
