package theDinnerTable;

public class Philosopher implements Runnable{
	
	private enum State {
		Thinking(randomThinkingTime()),
		// negative times to indicate that it doesn't matter, must incorporate in code
		Hungry(-1),
		Eating(-1);
		
		// random thinking time between 0 and 10 seconds
		private static int randomThinkingTime() {
			return (int)Math.random()*10000;
		}
		
		final int thinkTime;
		
		private State(int thinkTime) {
			this.thinkTime = thinkTime;
		}
	}
	
	private State state;

	public Philosopher() {
		state = State.Thinking;
	}
	
	public State getState() { 
		return state;
	}
	
	@Override
	public void run() {
		
	}
	
	public void think() {
		
	}
	
	public void hungry() {
		
	}
	
	public void eatRice() {
		
	}

}
