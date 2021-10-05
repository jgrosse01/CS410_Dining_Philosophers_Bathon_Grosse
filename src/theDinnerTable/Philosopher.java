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

		State(int thinkTime) {
			this.thinkTime = thinkTime;
		}
	}

	private final Thread thread;

	private State state;
	
	private final int pos;
	
	private Chopstick[] chopsticks;
	
	private int riceEaten;
	
	private volatile boolean timeToThink;
	
	private Table t;

	public Philosopher(Table t, int threadNum) {
		this.state = State.Thinking;
		this.t = t;
		riceEaten = 0;
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
		while(timeToThink) {
			switch (state) {
			case Thinking:
				think();
				break;
			case Hungry:
				hungry();
				break;
			case Eating:
				eatRice();
				break;
			}
		}
		System.out.println(thread.getName() + " has eaten a total of " + riceEaten + " ounces of rice.");
	}

	/**
	 * @desc literally idles for a random time
	 */
	public void think() {
		doPhilosophy();
		state = State.Hungry;
		System.out.println(thread.getName() + " is thinking.");
	}

	/**
	 * @desc trys to acquire chopsticks; synchronize to check if there are two
	 *       chopsticks either center and left or center and right, then grab them
	 *       if they're available
	 */
	public void hungry() {
		setChopsticks(t.findChopsticks(this));
		state = State.Eating;
		System.out.println(thread.getName() + " picked up " + chopsticks[0].toString() + " and " + chopsticks[1].toString());
	}

	/**
	 * @desc eats rice
	 */
	public void eatRice() {
		int riceRequested;
		int riceRecieved;
		riceRequested = (int) Math.random() * 3;
		riceRecieved = t.removeRice(riceRequested);
		riceEaten += riceRecieved;
		System.out.println(thread.getName() + " ate " + riceRecieved + " ounces of rice.");
		doPhilosophy();
		returnChopsticks();
		state = State.Thinking;
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
	public synchronized void returnChopsticks() {
		t.placeChopsticks(chopsticks);
		System.out.println(thread.getName() + " placed " + 
				chopsticks[0].toString() + " and " + 
				chopsticks[1].toString() + " back on the table.");
		chopsticks = null;
	}
	
	private void doPhilosophy() {
		// Sleep for the amount of time necessary to do the state
		try {
			Thread.sleep(state.thinkTime);
		} catch (InterruptedException e) {
			System.err.println("Philosoper [" + pos + "] thought to hard and hurt themselves in their own confusion.");
		}
	}

}
