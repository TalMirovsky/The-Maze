import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Main {
	
	static int x; //the number of rows in the maze
	static int y; //the number of cols in the maze
	static Character[][] maze; //the maze
	static int[] goal; //goal state
	static Node goalNode; 
	static Node initNode;
	static int[] init; //initial state
    static Parent visited[][] = new Parent[y*2+1][x*2+1]; //saves for each state the state he came from (parent)
	static ArrayList<Parent> pathSol = new ArrayList<Parent>(); //saves the path used to get to the goal
	static String path; 
	static int counter; //counts the number of visited states
	static int c = 1; //helps to calculate the cost
	static ArrayList<Node> nodesCosts=null;

	

	public static HashSet<Node> Successor(int[] state) {
		HashSet<Node> nextStates= new HashSet<>();
		//step down 
		if(state[0]<maze.length-1 && maze[state[0]+1][state[1]]==' ') {
			if(maze[state[0]+2][state[1]]==' ' || maze[state[0]+2][state[1]]=='X') {
				int[] toAdd=new int[2];
				toAdd[0]=state[0]+2;
				toAdd[1]=state[1];
				nextStates.add( new Node(toAdd, 1, Step.DOWN));
			}
		}
		//step up
		if(state[0]>1 && maze[state[0]-1][state[1]]==' ') {
			if(maze[state[0]-2][state[1]]==' ' || maze[state[0]-2][state[1]]=='X') {
				int[] toAdd=new int[2];
				toAdd[0]=state[0]-2;
				toAdd[1]=state[1];
				nextStates.add(new Node(toAdd, 1, Step.UP));
			}
		}
		//step left
		if(state[1]>1 && maze[state[0]][state[1]-1]==' ') {
			if(maze[state[0]][state[1]-2]==' ' || maze[state[0]][state[1]-2]=='X') {
				int[] toAdd=new int[2];
				toAdd[0]=state[0];
				toAdd[1]=state[1]-2;
				nextStates.add(new Node(toAdd, 1, Step.LEFT));
			}
		}
		//step right
		if(state[1]<maze[0].length-1 && maze[state[0]][state[1]+1]==' ') {
			if(maze[state[0]][state[1]+2]==' ' || maze[state[0]][state[1]+2]=='X') {
				int[] toAdd=new int[2];
				toAdd[0]=state[0];
				toAdd[1]=state[1]+2;
				nextStates.add(new Node(toAdd, 1, Step.RIGHT));
			}
		}
		return nextStates;
	}
	
	//this method checks if the state it got is the goal
	public static boolean isGoal(int[] state) {
		if(state[0]==goal[0] && state[1]==goal[1]) {
			return true;
		}
		else
			return false;
	}

	//prints the maze
	public static void printMaze(Character[][] arr) {
		for(int i=0;i<2*y+1;i++)
		{
			System.out.println();
			for(int j=0;j<2*x+1;j++)
			{
				System.out.print(arr[i][j]);
			}
		}
		System.out.println();
	}
		
	public static void main(String[] args) {
	 
		path = args[1];
		File file = new File(path);
		Scanner scanner = null;
		String str = "";
		//read the file
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				str += scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		int c = 0;
		String xStr;
		String yStr;
		while (str.charAt(c) != ' ')
			c++;
		xStr = str.substring(0, c);
		c++;
		int index = c;
		x = Integer.parseInt(xStr);
		while (str.charAt(c) != '\n')
			c++;
		yStr = str.substring(index, c);
		y = Integer.parseInt(yStr);
		maze = new Character[2 * y + 1][2 * x + 1];
		c++;
		for(int i=0;i<2*y+1;i++)
		{
			for(int j=0;j<2*x+1;j++)
			{
				if(str.charAt(c)=='\n')
					c++;
				if(str.charAt(c)=='X') { //saves the goal state
					goal=new int[2];
					goal[0]= i;
					goal[1]= j;
				}
				if(str.charAt(c)=='*') { //saves the initial state
					init=new int[2];
					init[0] = i;
					init[1] = j;
				}
				maze[i][j]=str.charAt(c++);
			}
		}
		
		printMaze(maze);
		initNode = new Node(init, 1,null);
		goalNode = new Node(goal, -1, null);
	
		//runs the chosen algorithm
		switch (args[0].toLowerCase()) {
		case "bfs":
			BFS(initNode);
			break;
		case "best":
			BestFirstSearch(initNode);
			break;
		case "a*":
			Astar(initNode);
			break;
		}

	}

/*if there is a tie between two states our algorithms prioritize by insertion order*/
	static void BFS(Node s)
    {
		//sets the matrix to the size of the maze 
        visited = new Parent[y*2+1][x*2+1];
        counter = 1;//counts the number of nodes visited
        // Create a queue for BFS
        PriorityQueue<Node> q= new PriorityQueue<Node>();
        // Mark the current node as visited and enqueue it
        visited[s.getState()[0]][s.getState()[1]]=new Parent(s.getState(), true, null);
        s.setCost(0);
        q.add(s);
 
        while (q.size() != 0 && !isGoal(s.getState()))
        {
            // Dequeue a node from queue
            s = q.poll();
    		c = s.getCost()+1;
 
            // Get all adjacent nodes of the dequeued node s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            HashSet<Node> nextStates = Successor(s.getState());
    		for(Node n : nextStates)
    		{
                if(visited[n.getState()[0]][n.getState()[1]]==null || !visited[n.getState()[0]][n.getState()[1]].isVisited())
                {
                    visited[n.getState()[0]][n.getState()[1]]=new Parent(s.getState(), true, s.getStep());
                    counter++;
                    n.setCost(c);
                    q.add(n);
                }
            }
        }
        goalNode.setStep(s.getStep());
        printPath("BFS"); //prints the algorithm output
    }
	
	static void BestFirstSearch(Node s)
    {
        visited = new Parent[y*2+1][x*2+1]; //sets the matrix to the size of the maze
        counter = 1;  //counts the number of nodes visited
        // Create a queue that is prioritized by heuristics 
        PriorityQueue<Node> q= new PriorityQueue<Node>(new Comparator<Node>() {
		    @Override
		    public int compare(Node n1,Node n2) {
		    	return n1.getDistance().compareTo(n2.getDistance());
		    }});
 
        // Mark the current node as visited and enqueue it
        visited[s.getState()[0]][s.getState()[1]]=new Parent(s.getState(), true, s.getStep());
        q.add(s);
        
 
        while (q.size() != 0 && !isGoal(s.getState()))
        {
            // Dequeue a node from queue
            s = q.poll();
            // Get all adjacent nodes of the dequeued node s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            HashSet<Node> nextStates=Successor(s.getState());
    		for(Node n : nextStates)
    		{
                if(visited[n.getState()[0]][n.getState()[1]]==null || !visited[n.getState()[0]][n.getState()[1]].isVisited())
                {
                    visited[n.getState()[0]][n.getState()[1]]=new Parent(s.getState(), true, s.getStep());
                    counter++;
                    q.add(n);

                }
            }
        }
        goalNode.setStep(s.getStep());
        printPath("Best-First-Search"); //prints the algorithm output
    }
	
	static void Astar(Node s)
    {
        visited = new Parent[y*2+1][x*2+1];
        counter = 1;//counts the number of nodes visited 
        nodesCosts=new ArrayList<Node>();  // Saves the nodes that are in the queue and their costs 
        // Create a queue that is prioritized by heuristics and costs
        PriorityQueue<Node> q= new PriorityQueue<Node>(new Comparator<Node>() {
		    @Override
		    public int compare(Node n1,Node n2) {
		    	return n1.getAstarCost().compareTo(n2.getAstarCost());
		    }});
 
        // Mark the current node as visited and enqueue it
        visited[s.getState()[0]][s.getState()[1]]=new Parent(s.getState(), true, null);
        s.setCost(0);
        q.add(s);
        nodesCosts.add(s);
 
        while (q.size() != 0 && !isGoal(s.getState()))
        {
            // Dequeue a node from queue
            s = q.poll();
            nodesCosts.remove(s);
    		c=s.getCost()+1;
 
            // Get all adjacent nodes of the dequeued node s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            HashSet<Node> nextStates=Successor(s.getState());
    		for(Node n : nextStates)
    		{
                if(visited[n.getState()[0]][n.getState()[1]]==null || !visited[n.getState()[0]][n.getState()[1]].isVisited())
                {
                    visited[n.getState()[0]][n.getState()[1]]=new Parent(s.getState(), true, s.getStep());
                    counter++;
                    n.setCost(c);
                    q.add(n);
                    nodesCosts.add(n);
                }
                else {   // Check if a lower cost can be achieved for this node
                	if(nodesCosts.contains(n)) {
                		int index= nodesCosts.indexOf(n);
                		Node currnet = nodesCosts.get(index);
                		if(currnet.getCost() > c) {
                			q.remove(currnet);
                			nodesCosts.remove(index);
                			n.setCost(c);
                			q.add(n);
                			nodesCosts.add(n);
                			visited[n.getState()[0]][n.getState()[1]]=new Parent(s.getState(), true, s.getStep());
                			
                		}
                	}
                }
            }
        }

        goalNode.setStep(s.getStep());
        printPath("A*"); //prints the algorithm output
    }
	
	static void printPath(String alg) {
			System.out.println("Alg Name : " + alg);
			System.out.println("Input: "+ path);
		 recursivePath(visited[goal[0]][goal[1]]);
	        Collections.reverse(pathSol); //reverse the path list so that the initial state will be first and the goal state will be last
	        System.out.print("Path : ");
			for(Parent p : pathSol) {
				if(pathSol.indexOf(p) == 0) {//the first state
					System.out.print( "(" + p.getState()[0] + "," + p.getState()[1] + ") " );
				}
				else { //the rest of the states
					System.out.print(" -> " + p.getStep().toString() + " -> (" + p.getState()[0] + "," + p.getState()[1] + ")");
				}
					
			}
			//the goal state
			System.out.print("-> " +goalNode.getStep().toString() + " -> (" + goalNode.getState()[0] + "," + goalNode.getState()[1] + ")\n");
			System.out.println("Cost : " + (pathSol.size()+1));
			System.out.println("Visit Count : " + counter);
	}
	
	//this method gets the goal state and adds to the path the parent recursively, until it gets to the initial state
	static Parent recursivePath(Parent p) {
		if(p.getState().equals(initNode.getState())) {
			pathSol.add(p);
			return null;
		}
		pathSol.add(p);
		p=visited[p.getState()[0]][p.getState()[1]]; //p is p's parent
		return recursivePath(p);
	}

}

