package org.panda.logicanalyzer.core.util;

/**
 * This class provides static methods for time unit conversion (e.g. from nanoseconds -> seconds)
 */
public class TimeConverter {

	/**
	 * This enumeration describes several time magnitutes in the area
	 * of seconds.
	 */
	public static enum TimeUnit {
		Nanoseconds("ns", 1.0f),
		Microseconds("us", 1000.0f),
		Milliseconds("ms", 1000000.0f),
		Seconds("s", 1000000000.0f);

		/**
		 * The shortname of this time unit
		 */
		private final String shortname;

		/**
		 * The divider in <code>nanoseconds / unit</code>
		 */
		private final float divider;

		TimeUnit(String shortname, float divider) {
			this.shortname = shortname;
			this.divider = divider;
		}

		/**
		 * Converts the given time in nanoseconds to this time unit.
		 *
		 * @param nanoseconds the time to convert
		 * @return the time in this unit
		 */
		public float convert(long nanoseconds) {
			return nanoseconds / divider;
		}

		/**
		 * @return the shortname of this time unit
		 */
		public String getShortname() {
			return shortname;
		}

	}

	private TimeConverter() { }

	/**
	 * Chooses an approrpiate time unit to format the time in a human
	 * understandable way.
	 *
	 * @param nanoseconds The time to choose the unit for
	 * @return the appropriate unit
	 */
	public static TimeUnit chooseAppropriateUnit(long nanoseconds) {
		int magnitude = (int) Math.log10(nanoseconds);
		TimeUnit result;

		if (magnitude < 3) {
			result = TimeUnit.Nanoseconds;
		} else if (magnitude < 6) {
			result = TimeUnit.Microseconds;
		} else if (magnitude < 9) {
			result = TimeUnit.Milliseconds;
		} else {
			result = TimeUnit.Seconds;
		}

		return result;
	}

	/**
	 * Converts a time in nanoseconds to user readable representation with
	 * its unit appended.
	 *
	 * @param ns The time in nanoseconds
	 * @return The user readable string representing the time
	 */
	public static String toString(long ns) {
		TimeUnit unit = chooseAppropriateUnit(ns);
		return String.format("%03.3f%s", unit.convert(ns), unit.getShortname());
	}

	/**
	 * Converts from nansoseconds to seconds
	 *
	 * @param ns The time in nansoseconds
	 * @return The time in seconds
	 */
	public static float toSeconds(float ns) {
		return ns / 1000000000.0f;
	}

}
