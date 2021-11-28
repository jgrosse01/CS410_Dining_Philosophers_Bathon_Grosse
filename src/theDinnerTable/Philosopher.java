package theDinnerTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc A class which defines a {@code Philosopher}. It runs a thread which attempts to eat rice from a bowl off its assigned table by sharing {@code Chopstick} objects with other instances of {@code Philosopher}.
 * 
 * @author Jaden Bathon and Jake Grosse
 *
 * @see Runnable
 * @see Chopstick
 */
public class Philosopher implements Runnable {

	// private enumeration which defines the state of a Philosopher as Thinking for a random amount of time, Hungry until they eat, or eating for 100ms.
	private enum State {
		// negative times to indicate that it doesn't matter, must incorporate in code
		Thinking(randomThinkingTime()), Hungry(-1), Eating(100);

		// random thinking time between 0 and 5 seconds
		private static int randomThinkingTime() {
			return (int) (Math.random() * 5000);
		}

		// instance variable of state defining the thinking time for each philosopher object
		final int stateTime;

		// constructor 
		State(int stateTime) {
			this.stateTime = stateTime;
		}
	}

	private final Thread thread;

	private State state;
	
	private final int pos;
	
	private List<Chopstick> chopsticks;
	
	private int riceEaten;
	
	private Table t;

	public Philosopher(Table t, int threadNum) {
		this.state = State.Thinking;
		this.t = t;
		riceEaten = 0;
		chopsticks= new ArrayList<Chopstick>();
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
		thread.start();
	}


	/*
	 * @desc waits for the Philosophers to stop what they are doing.
	 */
	public void waitToStopThinking() {
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
		while(t.getRiceBowl() != 0) {
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
		System.out.println(thread.getName() + " is thinking.");
		doPhilosophy();
		state = State.Hungry;
	}

	/**
	 * @desc trys to acquire chopsticks; synchronize to check if there are two
	 *       chopsticks either center and left or center and right, then grab them
	 *       if they're available
	 */
	public void hungry() {
		System.out.println (thread.getName() + " is hungry.");
		setChopsticks(t.findChopsticks(pos));
		state = State.Eating;
		System.out.println(thread.getName() + " picked up " + chopsticks.get(0).toString() + " and " + chopsticks.get(1).toString());
	}

	/**
	 * @desc eats rice
	 */
	public void eatRice() {
		int riceRequested;
		int riceRecieved;
		riceRequested = Math.max((int) (Math.random() * 3), 1);
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
	public synchronized void setChopsticks(List<Chopstick> chopsticks) {
	  this.chopsticks = chopsticks;
	}
	
	public synchronized List<Chopstick> getChopsticks() {
		  return chopsticks;
		}
	
	/**
	 * @desc puts chopsticks back on the table IN THE SAME SPOTS they were in
	 */
	public synchronized void returnChopsticks() {
		t.placeChopsticks(chopsticks);
		System.out.println(thread.getName() + " placed " + 
				chopsticks.get(0).toString() + " and " + 
				chopsticks.get(1).toString() + " back on the table.");
		chopsticks = null;
	}
	
	private void doPhilosophy() {
		// Sleep for the amount of time necessary to do the state
		try {
			Thread.sleep(state.stateTime);
		} catch (InterruptedException e) {
			System.err.println("Philosoper [" + pos + "] thought to hard and hurt themselves in their own confusion.");
		}
	}

}
