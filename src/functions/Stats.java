package functions;

public class Stats {
	/**
	 * Turns given value from degrees to centimeters
	 * @param degrees
	 The value in degrees.
	 * @return the value in centimeters
	 */
	public static double degreesToCm(int degrees) {
		return degreesToCm(degrees, 6.24);
	}

	/**
	 * Turns given value from degrees to centimeters
	 * @param degrees
	 The value in degrees.
	 * @param diameter
	 the diameter of the wheel in centimeters (optional, default: 6.24cm);
	 * @return the value in centimeters
	 */
	public static double degreesToCm(int degrees, double diameter) {
		return degrees * (diameter / 360);
	}
	
	/**
	 * Turns given value from centimeters to degrees
	 * @param cm
	 The value in centimeters.
	 * @return the value in degrees
	 */
	public static int cmToDegrees(double cm) {
		return cmToDegrees(cm, 6.24);
	}
	
	/**
	 * Turns given value from centimeters to degrees
	 * @param cm
	 The value in centimeters.
	 * @param diameter
	 the diameter of the wheel in centimeters (optional, default: 6.24cm);
	 * @return the value in degrees
	 */
	public static int cmToDegrees(double cm, double diameter) {
		return (int) (cm * 360 / (diameter * Math.PI));
	}
}
