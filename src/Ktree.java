import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

/**
 * pairs a k tree with its decomposition.
 * A Ktree is defined as follows: starting with a (k + 1)-vertex complete graph and then repeatedly adding vertices in such a way that each added vertex v has exactly k neighbors U such that, together, the k + 1 vertices formed by v and U form a clique
 * @author Matthew Askes
 */
public class Ktree {
    private Graph graph;
    private TreeDecomposition decomposition;
    private int nodeCounter = 0;
    static int treeCounter = 0;
    
    //starting with a (k + 1)-vertex complete graph and then repeatedly adding vertices in such a way that each added vertex v has exactly k neighbors U such that, together, the k + 1 vertices formed by v and U form a clique
    
    /**
     * create a new random Ktree of given size and width
     * @param size
     * @param treeWidth
     */
    public Ktree(int size, int treeWidth) {
        graph = new SingleGraph("kTree_"+ treeCounter++);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setStrict(false);
        graph.setAutoCreate(true);
    
        decomposition = new TreeDecomposition(graph);
        
        Set<Node> clique = new HashSet<>();
        for (int i = 0; i < treeWidth+1; i++) {
            clique.add(graph.addNode(String.valueOf(getNodeCount())));
        }
        makeClique(clique);
        decomposition.getTree().addNode(clique.toString());
        decomposition.getSetMap().put(clique.toString(),clique);
        
        //add rest of nodes
        for (int i = size-clique.size(); i > 0; i--) {
        
            //pick random node, A, in decomposition tree
            Graph DcomTree =  decomposition.getTree();
            Node node = DcomTree.getNode((int) (Math.random()*DcomTree.getNodeCount()));
            Set<Node> decomSet = decomposition.getSetMap().get(node.getId());
    
            //pick random neighbours in decomNode
            List<Node> list = new LinkedList<>(decomSet);
            Collections.shuffle(list);
            Set<Node> randomSet = new HashSet<>(list.subList(0, treeWidth));
            
            //connect v to A with treewidth edges
            Node newNode = graph.addNode(String.valueOf(getNodeCount()));
            Set<Node> newPartiton = new HashSet<>();
            newPartiton.add(newNode);
            for (Node s : randomSet) {
                graph.addEdge(newNode.getId()+s.getId(),newNode,s);
                newPartiton.add(s);
            }
            //create new node in decomposition
            DcomTree.addEdge(newPartiton.toString()+node.getId(),node.getId(), DcomTree.addNode(newPartiton.toString()).getId());
            decomposition.getSetMap().put(newPartiton.toString(),newPartiton);
        }
    }
    
    /**
     * turns the set of nodes into a clique
     * @param nodes
     */
    private void makeClique(Set<Node> nodes){
        //todo improve
        for (Node s : nodes) {
            for (Node t : nodes) {
                if (!t.equals(s)) {
                    graph.addEdge(s.getId()+ t.getId(), s, t);
                }
            }
        }
    }
    
    public Graph getGraph() {
        return graph;
    }
    
    public TreeDecomposition getDecomposition() {
        return decomposition;
    }
    
    public int getNodeCount() {
        return nodeCounter++;
    }
}
