package theDinnerTable;

public class Philosopher implements Runnable {

	private enum State {
		// negative times to indicate that it doesn't matter, must incorporate in code
		Thinking(randomThinkingTime()), Hungry(-1), Eating(100);

		// random thinking time between 0 and 10 seconds
		private static int randomThinkingTime() {
			return (int) Math.random() * 10000;
		}

		final int thinkTime;

		private State(int thinkTime) {
			this.thinkTime = thinkTime;
		}
	}

	private final Thread thread;

	private State state;

	public Philosopher(int threadNum) {
		this.state = State.Thinking;
		// because he who thinks deeply is anxious
		this.thread = new Thread(this, "Anxiety-ridden Person[" + threadNum + "]");
	}

	public State getState() {
		return state;
	}

	@Override
	public void run() {

	}

	/**
	 * @desc literally idles for a random time
	 */
	public void think() {

	}

	/**
	 * @desc trys to acquire chopsticks; synchronize to check if there are two
	 *       chopsticks either center and left or center and right, then grab them
	 *       if they're available
	 */
	public void hungry() {

	}

	/**
	 * @desc eats rice
	 */
	public void eatRice() {

	}

	/**
	 * @desc puts chopsticks back on the table IN THE SAME SPOTS they were in
	 */
	public void returnSticks() {

	}

}
