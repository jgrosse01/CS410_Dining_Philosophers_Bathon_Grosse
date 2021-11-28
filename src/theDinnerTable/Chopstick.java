package theDinnerTable;

public class Chopstick {
	private final int pos;
	private boolean inUse;
	
	Chopstick(int pos) {
		this.pos = pos;
		inUse = false;
	}
	
	@Override
	public String toString() {
		return "chopstick " + pos;
	}
	
	public boolean isInUse() {
		return inUse;
	}
	
	public synchronized void pickUp() {
      if (inUse) {
        throw new IllegalStateException("Attempt to steal a chopstick from another Philosopher");
      }
		inUse = true;
	}
	
	public synchronized void setDown() {
		inUse = false;
	}
}
