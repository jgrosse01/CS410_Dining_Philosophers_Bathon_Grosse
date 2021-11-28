package theDinnerTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc Class which manages {@code Philosopher} threads and provides placement
 *       and organization to {@code Philosopher} objects and {@code Chopstick}
 *       objects.
 * @author Jaden Bathon and Jake Grosse
 * 
 * @see Philosopher
 * @see Chopstick
 * @see List
 * @see ArrayList
 */
public class Table {
	// number of philosophers per table
	private static final int NUM_PHILS = 5;
	// irrelevant commented out line of code.
	// private static final int NUM_TABLES = 1;

	// ounces of rice at the table to start
	private static final int OUNCES_OF_RICE = 64;

	/**
	 * @desc Main method which will run the rest of the program
	 * @param args Strings which are passed into the program.
	 */
	public static void main(String[] args) {

		// inform the user that dinner has started
		System.out.println("Dinner has started.");

		// make the table
		Table t = new Table();

		// initialize the philosophers at the table
		for (Philosopher p : t.phils) {
			p.startThinking();
		}

		// wait until philosophers stop (when rice is gone)
		for (Philosopher p : t.phils) {
			p.waitToStopThinking();
		}

		// inform the user that dinner has ended
		System.out.println("Dinner has ended.");
	}

	// philosophers at the table
	private Philosopher[] phils;
	// chopsticks at the table
	private List<Chopstick> chopsticks;
	// rice bowl at the table, just an integer
	private int riceBowl;

	/**
	 * @desc Constructor to initialize table.
	 */
	Table() {
		// make philosophers a new array of length NUM_PHILS
		phils = new Philosopher[NUM_PHILS];
		// initialize chopstick list
		chopsticks = new ArrayList<Chopstick>();
		// make new phils and chopsticks to populate lists
		for (int i = 0; i < NUM_PHILS; i++) {
			phils[i] = new Philosopher(this, i);
			chopsticks.add(new Chopstick(i));
			riceBowl = OUNCES_OF_RICE;
		}
	}

	/**
	 * @desc Method to get a list of available {@code Chopstick} objects.
	 * @param pos The position of the {@code Philosopher} asking for chopsticks.
	 * @return A list of {@code Chopstick} objects that have been grabbed.
	 */
	public List<Chopstick> findChopsticks(int pos) {
		// gets range of positions of chopsticks which can be grabbed by a philosopher
		// at posistion pos.
		final int[] range = posRange(pos);
		// init chopstick list
		List<Chopstick> chops = new ArrayList<Chopstick>();
		// lock on chopsticks list
		synchronized (chopsticks) {
			// while there are less than 2 chopsticks
			while (chops.size() < 2) {
				// check the range of chopsticks available for a valid chopstick
				for (int i : range) {
					Chopstick c = chopsticks.get(i);
					// try to pick it up
					try {
						c.pickUp();
						chops.add(c);
						// catch thrown error by setting the chopstick down to prevent errors or
						// deadlocks
					} catch (IllegalStateException e) {
						// Prevents Deadlock from everyone holding one stick.
						for (Chopstick chop : chops) {
							chop.setDown();
						}
						// clear the chopstick array to start fresh next time
						chops.clear();

						/*
						 * Makes the Philosophers wait until a Chopstick is put back on the table before
						 * trying to find two again.
						 */

						try {
							chopsticks.wait();
						} catch (InterruptedException ignored) {
						}
					}
				}
			}
		}
		// return the valid list of chopsticks
		return chops;
	}

	/**
	 * @desc Method to put the chopsticks back on the table.
	 * @param chopstick A list of chopsticks held by the {@code Philosopher} object.
	 */
	public void placeChopsticks(List<Chopstick> chopstick) {
		synchronized (chopsticks) {
			chopsticks.notifyAll();
			for (Chopstick c : chopsticks) {
				c.setDown();
			}
		}
	}

	// gets range of positions of chopsticks available to a philosopher at position pos
	private int[] posRange(int pos) {
		if (pos == 0) {
			return new int[] { NUM_PHILS - 1, 0, 1 };
		}
		if (pos == NUM_PHILS - 1) {
			return new int[] { NUM_PHILS - 2, NUM_PHILS - 1, 0 };
		}
		return new int[] { pos - 1, pos, pos + 1 };
	}

	/**
	 * @desc Method to remove rice from the bowl so a {@code Philosopher} may eat it.
	 * @param request The amount of rice requested by a {@code Philosopher}.
	 * @return The amount of rice taken from the bowl (minimum of the request and what is left in the bowl).
	 */
	public synchronized int removeRice(int request) {
		request = Math.min(request, riceBowl);
		riceBowl -= request;
		// System.out.println("Rice Bowl: " + riceBowl);
		return request;
	}

	/**
	 * @desc A method to poll the rice bowl for how much rice it has in it.
	 * @return The amount of rice left in the rice bowl.
	 */
	public synchronized int getRiceBowl() {
		return riceBowl;
	}

}
