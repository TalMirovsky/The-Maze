import java.util.Arrays;

public class Node implements Comparable<Node> {
	int[] state = new int[2];
	Integer cost;
	Step step;

	public Node(int[] state, Integer cost, Step step) {
		super();
		this.state = state;
		this.cost = cost;
		this.step = step;
	}

	public int[] getState() {
		return state;
	}

	public void setState(int[] state) {
		this.state = state;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(state);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (!Arrays.equals(state, other.state))
			return false;
		return true;
	}

	public int compareTo(Node o) {
		return this.getCost()-o.getCost();
	}
	
	public Integer getDistance() {
		return (Math.abs(state[0]-Main.goal[0]) + Math.abs(state[1]-Main.goal[1]))/2;
	}
	
	public Integer getAstarCost() {
		return this.getDistance()+this.getCost();
	}

	@Override
	public String toString() {
		return "Node [state=" + Arrays.toString(state) + ", cost=" + cost + ", step=" + step + "]";
	}
	
	
	
	
    

}
