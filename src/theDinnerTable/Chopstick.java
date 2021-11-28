package theDinnerTable;

/**
 * @desc Class which defines {@code Chopstick}
 * 
 * @author Jaden Bathon and Jake Grosse
 */
public class Chopstick {
	// instance variables defining a chopstick
	private final int pos;
	private boolean inUse;

	/**
	 * @desc Constructor for a {@code Chopstick}
	 * @param pos defines where a {@code Chopstick} is to be placed on a theoretical table
	 *            of dining philosophers.
	 */
	Chopstick(int pos) {
		this.pos = pos;
		inUse = false;
	}

	@Override
	/**
	 * @desc Returns a String object to represents a {@code Chopstick} in the format
	 *       "chopstick " + <position of {@code Chopstick}>.
	 * 
	 * @return A String representing the instance of {@code Chopstick} which calls this
	 *         method.
	 */
	public String toString() {
		return "chopstick " + pos;
	}

	/**
	 * @desc A boolean check to see if the {@code Chopstick} is being held by a philosopher.
	 * 
	 * @return A Boolean value representing whether the {@code Chopstick} is in use.
	 */
	public boolean isInUse() {
		return inUse;
	}

	/**
	 * @desc A method which allows a {@code Philosopher} to pick up a {@code Chopstick}.
	 */
	public void pickUp() {
		inUse = true;
	}

	/**
	 * @desc A method which allows a {@code Philosopher} to pick up a {@code Chopstick}.
	 */
	public void setDown() {
		inUse = false;
	}
}
