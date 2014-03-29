import java.util.ArrayList;
// CHECK INPUT WORKS.
// MANAGE ERRORS.

public class Controller {
	private static Board board;
	private static ArrayList<Cluster> clusters;
	public static void main(String[] args) {
		clusters = new ArrayList<Cluster>(0);
		board = new Board(args);
		board.printBoard();
		makeClusters();
		printClusters();
		for(Cluster clust: clusters){
			clust.testTripod(board);
		}
	}
		
	
	/* Takes the board state, determines individual groups of pieces */
	public static void makeClusters(){
		int i, j;
		Position node;
		int size = board.getArraySize();
		for(i=0;i<2*size-1;i++){
			for(j=Math.max(0, i-size+1);j< Math.min(size+i, 2*size-1);j++){
				node = board.getNodes()[i][j];
				if(node.getParentCluster() == null
						&& node.getColour()!='-' && node.getColour()!='O'){
					// Node is not in a cluster and is not empty
					fillCluster(node);
				}
			}
		}
	}
	
	/* Given a piece, finds all connected pieces of the same colour,
	 * creates a cluster object containing them
	 * 
	 * Method: Initially adds the node it is passed to the cluster.
	 * Populates the toAdd list with all the adjacent nodes.
	 * The first adjacent node is popped off, if it is the right colour,
	 * and not already in a cluster, it is added to the cluster, and all its
	 * adjacent nodes are added to the list.
	 * Repeat until list is empty.
	 */
	public static void fillCluster(Position node){
		Cluster newCluster = new Cluster(node.getColour());
		ArrayList<Position> toAdd = new ArrayList<Position>(0);
		Position tempNode;
		
		newCluster.addNode(node);
		node.setParentCluster(newCluster);
		toAdd.addAll(node.getAdjacents(board));
		
		while(!toAdd.isEmpty()){
			tempNode = toAdd.remove(0);
			if(tempNode.getParentCluster()==null &&
					tempNode.getColour()==node.getColour()){
				newCluster.addNode(tempNode);
				tempNode.setParentCluster(newCluster);
				toAdd.addAll(tempNode.getAdjacents(board));
			}
		}
		clusters.add(newCluster);
	}
	
	public static void printClusters(){
		for(Cluster cluster:clusters){
			System.out.print(cluster.getColour()+" cluster, positions:");
			for(Position node:cluster.getNodes()){
				System.out.print("("+node.getY()+","+node.getX()+")");
			}
			System.out.print("\n");
		}
	}
}
