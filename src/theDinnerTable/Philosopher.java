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
	
	private final int pos;
	
	private Chopstick[] chopsticks;
	
	private volatile boolean timeToThink;

	public Philosopher(int threadNum) {
		this.state = State.Thinking;
		chopsticks = new Chopstick[2];
		// because he who thinks deeply is anxious
		this.thread = new Thread(this, "Anxiety-ridden Person[" + threadNum + "]");
		pos = threadNum;
	}

	public State getState() {
		return state;
	}

	/*
	 * @desc Has the Philosopher start thinking.
	 */
	public void startThinking() {
		timeToThink = true;
		thread.start();
	}

	/*
	 * @desc stops the Philosopher from thinking.
	 */
	public void stopThinking() {
		timeToThink = false;
	}


	/*
	 * @desc waits for the Philosophers to stop what they are doing.
	 */
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}
	
	/*
	 * @desc Gets the position of the Philosopher.
	 * @return pos
	 */
	public int getPos() {
		return pos;
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
	 * @desc acquires chopsticks from the table
	 */
	public synchronized void setChopsticks(Chopstick[] chopsticks) {
	  this.chopsticks = chopsticks;
	}
	
	public synchronized Chopstick[] getChopsticks() {
		  return chopsticks;
		}
	
	/**
	 * @desc puts chopsticks back on the table IN THE SAME SPOTS they were in
	 */
	public void returnSticks() {

	}

}
