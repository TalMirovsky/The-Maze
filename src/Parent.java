import java.util.Arrays;

public class Parent {
	
	int[] state = new int[2];
	boolean visited;
	Step step;
	
	public Parent(int[] state, boolean visited, Step step) {
		super();
		this.state = state;
		this.visited = visited;
		this.step = step;
		
	}

	
	
	public Step getStep() {
		return step;
	}



	public void setStep(Step step) {
		this.step = step;
	}



	public int[] getState() {
		return state;
	}

	public void setState(int[] state) {
		this.state = state;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	@Override
	public String toString() {
		return "Parent [state=" +state[0] + ","+ state[1] + ", visited=" + visited + "step=" + step +"]";
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
		if (!(obj instanceof Parent))
			return false;
		Parent other = (Parent) obj;
		if (!(state[0]==other.getState()[0] && state[1]==other.getState()[1]))
			return false;
		return true;
	}
	
	
	
	

}
