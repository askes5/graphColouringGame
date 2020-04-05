import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

/**
 * @author Matthew Askes
 */
public class main {
    
    protected String styleSheet =
            "node {" +
                    "fill-color: black;" +
                    "size: 15px; " +
                    "text-background-mode: rounded-box; " +
                    "text-background-color: red;" +
                    "}" +
                    "node.marked {" +
                    "	fill-color: red;" +
                    "}";
    
    
    public main() {
        Graph pwGraph = new SingleGraph("pathwidth");
        pwGraph.setAttribute("ui.quality");
        pwGraph.setAttribute("ui.antialias");
        
        pwGraph.setStrict(false);
        pwGraph.setAutoCreate(true);
        pwGraph.addEdge("AB", "A", "B");
        pwGraph.addEdge("BC", "B", "C");
        pwGraph.addEdge("CA", "C", "A");
        pwGraph.addEdge("CD", "C", "D");
        pwGraph.addEdge("CE", "C", "E");
        pwGraph.addEdge("EF", "E", "F");
        pwGraph.addEdge("DH", "D", "H");
        pwGraph.addEdge("FH", "F", "H");
        pwGraph.addEdge("FG", "F", "G");
        pwGraph.addEdge("HI", "H", "I");
        pwGraph.addEdge("IJ", "I", "J");
        pwGraph.addEdge("IK", "I", "K");
        
        Node A = pwGraph.getNode("A");
        Node B = pwGraph.getNode("B");
        Node C = pwGraph.getNode("C");
        Node D = pwGraph.getNode("D");
        Node E = pwGraph.getNode("E");
        Node F = pwGraph.getNode("F");
        Node G = pwGraph.getNode("G");
        Node H = pwGraph.getNode("H");
        Node I = pwGraph.getNode("I");
        Node J = pwGraph.getNode("J");
        Node K = pwGraph.getNode("K");
        pwGraph.addAttribute("ui.stylesheet", styleSheet);
    
        List<Set<Node>> pathDecomposition = new ArrayList<>();
        pathDecomposition.add(new HashSet<>(Arrays.asList(A, B, C)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(C, D, E)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(D, E, F)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(D, F, G)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(D, F, H)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(H, I)));
        pathDecomposition.add(new HashSet<>(Arrays.asList(I, J, K)));
        List<Node> linearOrder = LOinPWGenerator.calculateListOrder(pathDecomposition);
    
//        System.out.println(linearOrder.toString());
//        new ColouringGame(pwGraph.getEdgeSet(), new ActivationStrategy(LOinPWGenerator.calculateComparator(pathDecomposition)), 3);
    
        Graph twGraph = new SingleGraph("pathwidth");
        twGraph.setAttribute("ui.quality");
        twGraph.setAttribute("ui.antialias");
        twGraph.setStrict(false);
        twGraph.setAutoCreate(true);
        twGraph.addEdge("AB", "A", "B");
        twGraph.addEdge("AC", "A", "C");
        twGraph.addEdge("BC", "B", "C");
        twGraph.addEdge("BF", "B", "F");
        twGraph.addEdge("BG", "B", "G");
        twGraph.addEdge("BE", "B", "E");
        twGraph.addEdge("CD", "C", "D");
        twGraph.addEdge("CE", "C", "E");
        twGraph.addEdge("DE", "D", "E");
        twGraph.addEdge("EG", "E", "G");
        twGraph.addEdge("EH", "E", "H");
        twGraph.addEdge("FG", "F", "G");
        twGraph.addEdge("GH", "G", "H");
        twGraph.addAttribute("ui.stylesheet", styleSheet);
    
         A = twGraph.getNode("A");
         B = twGraph.getNode("B");
         C = twGraph.getNode("C");
         D = twGraph.getNode("D");
         E = twGraph.getNode("E");
         F = twGraph.getNode("F");
         G = twGraph.getNode("G");
         H = twGraph.getNode("H");
    
        //make the tree decomposition
        Graph tree = new SingleGraph("pathwidth");
        tree.setAttribute("ui.quality");
        tree.setAttribute("ui.antialias");
        tree.setStrict(false);
        tree.setAutoCreate(true);
    
        tree.addEdge("AB", "A", "B");
        tree.addEdge("BC", "C", "B");
        tree.addEdge("AB", "A", "B");
        tree.addEdge("DB", "D", "B");
        tree.addEdge("DE", "D", "E");
        tree.addEdge("DF", "D", "F");
        
        Map<String,Set<Node>> setMap = new HashMap<>();
    
        setMap.put("A", new HashSet<>(Arrays.asList(A, B, C)));
        setMap.put("B", new HashSet<>(Arrays.asList(E, B, C)));
        setMap.put("C", new HashSet<>(Arrays.asList(C, D, C)));
        setMap.put("D", new HashSet<>(Arrays.asList(E, B, G)));
        setMap.put("E", new HashSet<>(Arrays.asList(B, F, G)));
        setMap.put("F", new HashSet<>(Arrays.asList(E, G, H)));
        
        new ColouringGame(twGraph.getEdgeSet(), new ActivationStrategy(LOinTWGenerator.calculateComparator(setMap,tree)), 4);
        
//        Viewer viewer = tree.display();
//        viewer.getDefaultView().addMouseListener(new graphMouseListener(graph, viewer.getDefaultView()));


//        try {
//            Thread.sleep(1000);
//
//            graph.addEdge( "AD", "A", "D" );
//
//            Thread.sleep(1000);
//            graph.addEdge( "CD", "C", "D" );
//
//            Thread.sleep(1000);
//            graph.getNode("C").addAttribute("ui.class", "marked");
//
//            Thread.sleep(500);
//            graph.addEdge( "DC", "C", "D" );
//
//            for (Edge edge : graph.getEachEdge()) {
//                System.out.println(edge.toString());
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //add labels to nodes
//        for (Node node : graph) {
//            node.addAttribute("ui.label", node.getId());
//        }
//
//
//
    
        
    }
    
    public static void main(String[] args) {
        
        new main();
        
    }
}
