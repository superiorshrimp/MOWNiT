import java.util.ArrayList;
import java.util.Random;

public class Person{
	private final ArrayList<Person> neighbours;
	private int currentState; //healthy/infected/dead
	private int nextState;
	private double immunity = Math.max(0.1, new Random().nextFloat()); //strength of immune system
	static private final double transmission = 0.15; //how easily virus spreads
	private final double decay = 0.01; //decay of immunity
	static private final int numStates = 3;
	private int day = -1;

	public Person() {
		this.currentState = 0;
		this.nextState = 0;
		this.neighbours = new ArrayList<Person>();
	}

	public void clicked() {
		currentState=(++currentState)%numStates;
		day = 0;
	}
	
	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState() {
		if(this.getState() == 0){
			double sum = 0.0;
			for(Person neighbour : this.neighbours){
				if(neighbour.getState() == 1){
					sum += 0.125;
				}
			}
			if(this.immunity < 10 * sum * transmission){
				this.nextState = 1;
				this.day = 0;
			}
			this.immunity = Math.max(0.1, this.immunity - decay + ((Math.random() * 0.01)));
		}
		else if(this.getState() == 1){
			this.day++;
			if(this.day >= 14 + (int) ((Math.random() * 4) - 2)){ //person can be infected for 12 to 16 days
				this.nextState = 0;
				this.immunity = Math.max(1.0, 25*decay); //after surviving infection the immunity is boosted
				this.day = -1;
			}
			else{
				this.immunity -= decay;
				if(this.immunity < 0){
					this.nextState = 2; //dead
				}
			}
		}
	}

	public void changeState() {
		currentState = nextState;
	}
	
	public void addNeighbour(Person neighbour) {
		neighbours.add(neighbour);
	}
}
