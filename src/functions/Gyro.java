package functions;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class Gyro {

	public static float[] gyroValues = new float[10];

	/**
	 * calculates the current angle of the robot using the latest 10 samples from
	 * the gyro
	 * 
	 * @return current angle of the robot (int)
	 */
	public static int getAngle() {
		int avg = 0;
		for (int i = 0; i < gyroValues.length; i++) {
			robotUtils.RobotStructure.getInstance().gyroAngleSampler.fetchSample(gyroValues, i);
			avg += gyroValues[i];
		}
		avg /= gyroValues.length;
		return avg;
	}

	/**
	 * Makes the robot follow a certain Gyro angle for a certain distance
	 * 
	 * @param p0           The speed the robot should move in (in Centi-Volts, max
	 *                     800)
	 * @param wheelDegrees The amount of degrees the robot should move
	 * @param gyroDegrees  The Gyro angle the robot should follow
	 * @param kp           The error multiplier, how aggressive the angle correction
	 *                     should be (recommended higher kp with when in higher
	 *                     speeds)
	 * @param coast        Whether or not the robot should coast at the end
	 */
	public static void followerDegrees(int p0, int wheelDegrees, int gyroDegrees, double kp, boolean coast) {
		int error = 0;
		int wheelSpeedCalc;

		RobotStructure.getInstance().leftWheel.resetTachoCount();
		RobotStructure.getInstance().rightWheel.resetTachoCount();

		// assigns desired speed values to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(p0));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(p0));

		// makes robot drive the desired amount of degrees with correct direction
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (p0 > 0) {
			RobotStructure.getInstance().leftWheel.forward();
			RobotStructure.getInstance().rightWheel.forward();
		} else {
			RobotStructure.getInstance().leftWheel.backward();
			RobotStructure.getInstance().rightWheel.backward();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();

		int i = 0;

		// calculates and corrects the gyro error
		if (p0 > 0) {
			while ((RobotStructure.getInstance().leftWheel.getTachoCount() < wheelDegrees
					|| RobotStructure.getInstance().rightWheel.getTachoCount() < wheelDegrees)
					&& RunHandler.getCurrentRun().isActive()) {

				error = getAngle() - gyroDegrees;
				wheelSpeedCalc = (int) (error * kp + p0);

				RobotStructure.getInstance().rightWheel.setSpeed(wheelSpeedCalc);

				if (i % 500 == 0) {
					LCD.clear();
					LCD.drawInt(getAngle(), 0, 0);
					LCD.drawInt(wheelSpeedCalc, 0, 1);
				}

				i++;
			}
		} else {
			while ((RobotStructure.getInstance().leftWheel.getTachoCount() > -wheelDegrees
					|| RobotStructure.getInstance().rightWheel.getTachoCount() > -wheelDegrees)
					&& RunHandler.getCurrentRun().isActive()) {

				error = (getAngle() - gyroDegrees);
				wheelSpeedCalc = Math.abs((int) (p0 - (error * kp)));

				RobotStructure.getInstance().leftWheel.setSpeed(wheelSpeedCalc);

				if (i % 500 == 0) {
					LCD.clear();
					LCD.drawInt(getAngle(), 0, 0);
					LCD.drawInt(wheelSpeedCalc, 0, 1);
					LCD.drawInt(RobotStructure.getInstance().leftWheel.getTachoCount(), 0, 2);
					LCD.drawInt(RobotStructure.getInstance().rightWheel.getTachoCount(), 0, 3);
					LCD.drawInt(-wheelDegrees, 0, 4);
				}

				i++;
			}
		}

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
	 * Makes the robot follow a certain Gyro angle for a certain amount of time
	 * 
	 * @param p0          The speed the robot should move in (in Centi-Volts, max
	 *                    800)
	 * @param duration    The amount of time the robot should move(in seconds)
	 * @param gyroDegrees The Gyro angle the robot should follow
	 * @param kp          The error multiplier, how aggressive the angle correction
	 *                    should be (recommended higher kp with when in higher
	 *                    speeds)
	 * @param coast       Whether or not the robot should coast at the end
	 */
	public static void followerSecs(int p0, double duration, int gyroDegrees, double kp, boolean coast) {
		// switches drive duration value from seconds to millis
		duration *= 1000;
		int error = 0;
		long startTime = System.currentTimeMillis();
		long tempTime = System.currentTimeMillis();
		int wheelSpeedCalc;

		// assigns desired speed values to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(p0));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(p0));

		// makes robot drive the desired amount of degrees with correct direction
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (p0 > 0) {
			RobotStructure.getInstance().leftWheel.forward();
			RobotStructure.getInstance().rightWheel.forward();
		} else {
			RobotStructure.getInstance().leftWheel.backward();
			RobotStructure.getInstance().rightWheel.backward();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();

		while (System.currentTimeMillis() - startTime < duration && RunHandler.getCurrentRun().isActive()) {
			// for debugging
			if (System.currentTimeMillis() - tempTime > 500 && !Thread.currentThread().isInterrupted()) {
				tempTime = System.currentTimeMillis();
				LCD.clearDisplay();
				LCD.drawInt(getAngle(), 0, 0);
			}
			// calculates and corrects gyro error
			error = (getAngle() - gyroDegrees);
			wheelSpeedCalc = (int) (error * kp + p0);
			if (p0 > 0)
				RobotStructure.getInstance().rightWheel.setSpeed(wheelSpeedCalc);
			else
				RobotStructure.getInstance().leftWheel.setSpeed(wheelSpeedCalc);
		}

		// coasts if told to
		if (coast == true) {
			RobotStructure.getInstance().leftWheel.startSynchronization();
			RobotStructure.getInstance().leftWheel.stop(true);
			RobotStructure.getInstance().rightWheel.stop();
			RobotStructure.getInstance().leftWheel.endSynchronization();
		} else {
			RobotStructure.getInstance().leftWheel.startSynchronization();
			RobotStructure.getInstance().leftWheel.flt();
			RobotStructure.getInstance().rightWheel.flt();
			RobotStructure.getInstance().leftWheel.endSynchronization();
		}

	}

	/**
	 * Makes the robot follow a certain Gyro angle until it sees a certain shade
	 * between white and black
	 * 
	 * @param p0          The speed the robot should move in (in Centi-Volts, max
	 *                    800)
	 * @param color       the shade the robot should stop on (black:0.0, white:1.0)
	 * @param gyroDegrees The Gyro angle the robot should follow
	 * @param kp          The error multiplier, how aggressive the angle correction
	 *                    should be (recommended higher kp with when in higher
	 *                    speeds)
	 * @param coast       Whether or not the robot should coast at the end
	 * @param straighten  Whether or not the robot should straighten itself on the
	 *                    given color
	 */
	public static void followerUntilColor(int p0, float color, int gyroDegrees, double kp, boolean coast) {
		followerUntilColor(p0, color, null, gyroDegrees, kp, coast);
	}

	/**
	 * Makes the robot follow a certain Gyro angle until it sees a certain shade
	 * between white and black
	 * 
	 * @param p0          The speed the robot should move in (in Centi-Volts, max
	 *                    800)
	 * @param color       the shade the robot should stop on (black:0.0, white:1.0)
	 * @param colorSensor the color sensor you want to use (optional; Default - leftColorRedSampler)
	 * @param gyroDegrees The Gyro angle the robot should follow
	 * @param kp          The error multiplier, how aggressive the angle correction
	 *                    should be (recommended higher kp with when in higher
	 *                    speeds)
	 * @param coast       Whether or not the robot should coast at the end
	 * @param straighten  Whether or not the robot should straighten itself on the
	 *                    given color
	 */
	public static void followerUntilColor(int p0, float color, EV3ColorSensor colorSensor, int gyroDegrees, double kp,
			boolean coast) {
		int error = 0;
		long tempTime = System.currentTimeMillis();
		int wheelSpeedCalc;
		float maxColor = color + 0.05f;
		float minColor = color - 0.05f;
		SampleProvider sample;

		if (colorSensor == RobotStructure.getInstance().rightColor)
			sample = RobotStructure.getInstance().rightColorRedSampler;
		else
			sample = RobotStructure.getInstance().leftColorRedSampler;

		// assigns desired speed values to the wheels
		RobotStructure.getInstance().leftWheel.setSpeed(Math.abs(p0));
		RobotStructure.getInstance().rightWheel.setSpeed(Math.abs(p0));

		// makes robot drive the desired amount of degrees with correct direction
		RobotStructure.getInstance().leftWheel.startSynchronization();
		if (p0 > 0) {
			RobotStructure.getInstance().leftWheel.forward();
			RobotStructure.getInstance().rightWheel.forward();
		} else {
			RobotStructure.getInstance().leftWheel.backward();
			RobotStructure.getInstance().rightWheel.backward();
		}
		RobotStructure.getInstance().leftWheel.endSynchronization();

		if (sample != null) {
			while (Color.getLight(sample) > maxColor
					|| Color.getLight(sample) < minColor && RunHandler.getCurrentRun().isActive()) {
				// for debugging
				if (System.currentTimeMillis() - tempTime > 500 && !Thread.currentThread().isInterrupted()) {
					tempTime = System.currentTimeMillis();
					LCD.clearDisplay();
					LCD.drawInt(getAngle(), 0, 0);
				}
				// calculates and corrects gyro error
				error = (getAngle() - gyroDegrees);
				wheelSpeedCalc = (int) (error * kp + p0);
				if (p0 > 0)
					RobotStructure.getInstance().rightWheel.setSpeed(wheelSpeedCalc);
				else
					RobotStructure.getInstance().leftWheel.setSpeed(wheelSpeedCalc);
			}
		} else {
			while (Color.getLight(RobotStructure.getInstance().leftColorRedSampler) > maxColor
					|| Color.getLight(RobotStructure.getInstance().leftColorRedSampler) < minColor
					|| Color.getLight(RobotStructure.getInstance().rightColorRedSampler) > maxColor
					|| Color.getLight(RobotStructure.getInstance().rightColorRedSampler) < minColor
							&& RunHandler.getCurrentRun().isActive()) {
				// for debugging
				if (System.currentTimeMillis() - tempTime > 500 && !Thread.currentThread().isInterrupted()) {
					tempTime = System.currentTimeMillis();
					LCD.clearDisplay();
					LCD.drawInt(getAngle(), 0, 0);
				}
				// calculates and corrects gyro error
				error = (getAngle() - gyroDegrees);
				wheelSpeedCalc = (int) (error * kp + p0);
				if (p0 > 0)
					RobotStructure.getInstance().rightWheel.setSpeed(wheelSpeedCalc);
				else
					RobotStructure.getInstance().leftWheel.setSpeed(wheelSpeedCalc);
			}
		}

		// coasts if told to
		if (coast == true) {
			RobotStructure.getInstance().leftWheel.startSynchronization();
			RobotStructure.getInstance().leftWheel.stop(true);
			RobotStructure.getInstance().rightWheel.stop();
			RobotStructure.getInstance().leftWheel.endSynchronization();
		} else {
			RobotStructure.getInstance().leftWheel.startSynchronization();
			RobotStructure.getInstance().leftWheel.flt();
			RobotStructure.getInstance().rightWheel.flt();
			RobotStructure.getInstance().leftWheel.endSynchronization();
		}

	}

	/**
	 * Straightens the robot on a certain Gyro angle using backwards movement
	 * 
	 * @param speed       The speed the wheels should move in while straightening
	 * @param gyroDegrees The degrees the robot should straighten on
	 */
	public static void straightenBackwards(int speed, int gyroDegrees) {
		straightenBackwards(speed, gyroDegrees, 0);
	}

	/**
	 * Straightens the robot on a certain Gyro angle using backwards movement
	 * 
	 * @param speed       The speed the wheels should move in while straightening
	 * @param gyroDegrees The degrees the robot should straighten on
	 * @param maxSecs     The max amount of time the robot should try straightening.
	 *                    If the robot doesn't succeed in time, it will stop trying
	 *                    to prevent getting stuck (optional)
	 */
	public static void straightenBackwards(int speed, int gyroDegrees, int maxSecs) {
		long startTime = System.currentTimeMillis();
		RobotStructure.getInstance().leftWheel.setSpeed(speed);
		RobotStructure.getInstance().rightWheel.setSpeed(speed);
		if (maxSecs == 0) {
			while (getAngle() != gyroDegrees && RunHandler.getCurrentRun().isActive()) {
				if (getAngle() < 0) {
					RobotStructure.getInstance().rightWheel.backward();
					RobotStructure.getInstance().leftWheel.stop();
					while (getAngle() < 0 && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				} else if (getAngle() > 0) {
					RobotStructure.getInstance().leftWheel.backward();
					RobotStructure.getInstance().rightWheel.stop();
					while (getAngle() > 0 && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				}
			}
		} else {
			while (getAngle() != gyroDegrees && System.currentTimeMillis() - startTime < maxSecs * 1000
					&& RunHandler.getCurrentRun().isActive()) {
				if (getAngle() < 0) {
					RobotStructure.getInstance().rightWheel.backward();
					RobotStructure.getInstance().leftWheel.stop();
					while (getAngle() < 0 && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				} else if (getAngle() > 0) {
					RobotStructure.getInstance().leftWheel.backward();
					RobotStructure.getInstance().rightWheel.stop();
					while (getAngle() > 0 && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				}
			}
		}
		RobotStructure.getInstance().leftWheel.startSynchronization();
		RobotStructure.getInstance().leftWheel.stop();
		RobotStructure.getInstance().rightWheel.stop();
		RobotStructure.getInstance().leftWheel.endSynchronization();
	}

	/**
	 * Straightens the robot on a certain angle while staying in place(using both
	 * wheels)
	 * 
	 * @param speed       The speed the motors should rotate in
	 * @param gyroDegrees The Gyro degree the robot should straighten on
	 */
	public static void straightenBothWheels(int speed, int gyroDegrees) {
		straightenBothWheelsWithMaxSecs(speed, gyroDegrees, 0);
	}

	/**
	 * Straightens the robot on a certain angle while staying in place(using both
	 * wheels)
	 * 
	 * @param speed       The speed the motors should rotate in
	 * @param gyroDegrees The Gyro degree the robot should straighten on
	 * @param maxSecs     The max amount of time the robot should try straightening.
	 *                    If the robot doesn't succeed in time, it will stop trying
	 *                    to prevent getting stuck (optional)
	 */
	public static void straightenBothWheelsWithMaxSecs(int speed, int gyroDegrees, float maxSecs) {
		long startTime = System.currentTimeMillis();
		RobotStructure.getInstance().leftWheel.setSpeed(speed);
		RobotStructure.getInstance().rightWheel.setSpeed(speed);
		if (maxSecs == 0) {
			while (getAngle() != gyroDegrees && RunHandler.getCurrentRun().isActive()) {
				if (getAngle() < gyroDegrees) {
					RobotStructure.getInstance().rightWheel.backward();
					RobotStructure.getInstance().leftWheel.forward();
					while (getAngle() < gyroDegrees && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				} else if (getAngle() > gyroDegrees) {
					RobotStructure.getInstance().leftWheel.backward();
					RobotStructure.getInstance().rightWheel.forward();
					while (getAngle() > gyroDegrees && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				}
			}
		} else {
			while (getAngle() != gyroDegrees && System.currentTimeMillis() - startTime < maxSecs * 1000
					&& RunHandler.getCurrentRun().isActive()) {
				if (getAngle() < gyroDegrees) {
					RobotStructure.getInstance().rightWheel.backward();
					RobotStructure.getInstance().leftWheel.forward();
					while (getAngle() < gyroDegrees && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				} else if (getAngle() > gyroDegrees) {
					RobotStructure.getInstance().leftWheel.backward();
					RobotStructure.getInstance().rightWheel.forward();
					while (getAngle() > gyroDegrees && !Thread.currentThread().isInterrupted())
						LCD.drawInt(getAngle(), 0, 0);
				}
			}
		}
		RobotStructure.getInstance().leftWheel.startSynchronization();
		RobotStructure.getInstance().leftWheel.stop();
		RobotStructure.getInstance().rightWheel.stop();
		RobotStructure.getInstance().leftWheel.endSynchronization();
	}

	public static void straightenBothWheelsWithMaxTurns(int speed, int gyroDegrees, int maxTurns) {
		int numOfTurns = 0;
		RobotStructure.getInstance().leftWheel.setSpeed(speed);
		RobotStructure.getInstance().rightWheel.setSpeed(speed);
		while (numOfTurns < maxTurns && RunHandler.getCurrentRun().isActive()) {
			if (getAngle() < gyroDegrees) {
				RobotStructure.getInstance().rightWheel.backward();
				RobotStructure.getInstance().leftWheel.forward();
				while (getAngle() < gyroDegrees && !Thread.currentThread().isInterrupted())
					LCD.drawInt(getAngle(), 0, 0);
			} else if (getAngle() > gyroDegrees) {
				RobotStructure.getInstance().leftWheel.backward();
				RobotStructure.getInstance().rightWheel.forward();
				while (getAngle() > gyroDegrees && !Thread.currentThread().isInterrupted())
					LCD.drawInt(getAngle(), 0, 0);
			}
			numOfTurns++;
		}
		RobotStructure.getInstance().leftWheel.startSynchronization();
		RobotStructure.getInstance().leftWheel.stop();
		RobotStructure.getInstance().rightWheel.stop();
		RobotStructure.getInstance().leftWheel.endSynchronization();
	}

}
