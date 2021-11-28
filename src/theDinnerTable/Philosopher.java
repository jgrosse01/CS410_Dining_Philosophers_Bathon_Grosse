package theDinnerTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc A class which defines a {@code Philosopher}. It runs a thread which
 *       attempts to eat rice from a bowl off its assigned table by sharing
 *       {@code Chopstick} objects with other instances of {@code Philosopher}.
 * 
 * @author Jaden Bathon and Jake Grosse
 *
 * @see Chopstick
 * @see Runnable
 * @see List
 * @see ArrayList
 */
public class Philosopher implements Runnable {

  // private enumeration which defines the state of a Philosopher as Thinking for
  // a random amount of time, Hungry until they eat, or eating for 100ms.
  private enum State {
    // negative times to indicate that it doesn't matter, must incorporate in code
    Thinking(randomThinkingTime()), Hungry(-1), Eating(100);

    // random thinking time between 0 and 5 seconds
    private static int randomThinkingTime() {
      return (int) (Math.random() * 5000);
    }

    // instance variable of state defining the thinking time for each philosopher
    // object
    final int stateTime;

    // constructor for the state given the time value
    State(int stateTime) {
      this.stateTime = stateTime;
    }
  }

  // instance variables of Philosopher

  // thread running the philosopher
  private final Thread thread;

  // state of philosopher
  private State state;

  // position at the table, they cannot move
  private final int pos;

  // list of chopsticks being held
  private List<Chopstick> chopsticks;

  // amount of rice each philosopher has eaten
  private int riceEaten;

  // the table the philosopher is sitting at
  private Table t;

  /**
   * @desc Constructor to initialize a {@code Philosopher}.
   * @param t         The {@code Table} which the {@code Philosopher} sits at.
   * @param threadNum The number of threads already initialized so this
   *                  {@code Philosopher} may be properly named and referred to.
   */
  public Philosopher(Table t, int threadNum) {
    // all philosophers start thinking when they are made
    this.state = State.Thinking;
    // the table the phil is sitting at
    this.t = t;
    // at the beginning they have eaten no rice
    riceEaten = 0;
    // empty arraylist of chopsticks
    chopsticks = new ArrayList<Chopstick>();
    // because he who thinks deeply is anxious
    this.thread = new Thread(this, "Anxiety-ridden Person[" + threadNum + "]");
    // spot at table is equal to the number of philosophers already created
    pos = threadNum;
  }

  /**
   * @desc Method to get the current state of the {@code Philosopher}
   * @return The current state of the {@code Philosopher}
   */
  public State getState() {
    return state;
  }

  /**
   * @desc Has the {@code Philosopher} start thinking.
   */
  public void startThinking() {
    thread.start();
  }

  /**
   * @desc waits for the {@code Philosopher} to stop what they are doing before
   *       killing the thread.
   */
  public void waitToStopThinking() {
    try {
      thread.join();
    } catch (InterruptedException e) {
      System.err.println(thread.getName() + " stop malfunction");
    }
  }

  /**
   * @desc Gets the position of the {@code Philosopher}.
   * 
   * @return The position of the {@code Philosopher} at the {@code Table}.
   */
  public int getPos() {
    return pos;
  }

  /**
   * @desc
   */
  @Override
  public void run() {
    // run loop
    while (t.getRiceBowl() != 0) {
      // constantly checking state and acting accordingly.
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

  // Literally idles for the random time that was defined for this Philosopher in
  // state.
  private void think() {
    System.out.println(thread.getName() + " is thinking.");
    doTask();
    state = State.Hungry;
  }

  /*
   * Tries to acquire Chopstick objects. Synchronized methods called by
   * findChopsticks(Philosopher) to check for adjacent Chopstick objects without
   * them being updated and then grabs two if they are available.
   */
  private void hungry() {
    System.out.println(thread.getName() + " is hungry.");
    setChopsticks(t.findChopsticks(pos));
    state = State.Eating;
    System.out.println(thread.getName() + " picked up " + chopsticks.get(0).toString() + " and "
        + chopsticks.get(1).toString());
  }

  // Tells the Philosopher to eat rice.

  private void eatRice() {
    // initialize rice vars
    int riceRequested;
    int riceRecieved;

    // rice requested is a random number between 1 and 3
    riceRequested = Math.max((int) (Math.random() * 3), 1);
    // rice received is the min between rice requested and rice in the bowl
    riceRecieved = t.removeRice(riceRequested);
    // add total to rice eaten
    riceEaten += riceRecieved;
    // print that the Philosopher ate riceReceieved amount of rice
    System.out.println(thread.getName() + " ate " + riceRecieved + " ounces of rice.");
    // thread takes time to do the thing
    doTask();
    // put down the chopsticks
    returnChopsticks();
    // set state back to thinking
    state = State.Thinking;
  }

  // Acquires Chopstick objects from the table.
  private synchronized void setChopsticks(List<Chopstick> chopsticks) {
    this.chopsticks = chopsticks;
  }

  /**
   * @desc Returns the list of {@code Chopstick} objects that the
   *       {@code Philosopher} currently has.
   * @return A list of currently held {@code Chopstick} objects.
   */
  public synchronized List<Chopstick> getChopsticks() {
    return chopsticks;
  }

  // Puts chopsticks objects back on the table IN THE SAME SPOTS they were in
  // before.

  private synchronized void returnChopsticks() {
    // places chopsticks back down on the table
    t.placeChopsticks(chopsticks);
    // print that this is done
    System.out.println(thread.getName() + " placed " + chopsticks.get(0).toString() + " and "
        + chopsticks.get(1).toString() + " back on the table.");
    // set the list of currently held chopsticks to null
    chopsticks = null;
  }

  // method to create delays equal to the amount of time each state takes to
  // process.
  private void doTask() {
    // Sleep for the amount of time necessary to do the state
    try {
      Thread.sleep(state.stateTime);
    } catch (InterruptedException e) {
      System.err.println("Philosoper [" + pos + "] thought to hard and hurt themselves in their own confusion.");
    }
  }

}
