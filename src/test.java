import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * @author Matthew Askes
 */
public class test {
    
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
    
    
    public test() {
        Graph graph = new SingleGraph("Tutorial 1");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("CE", "C", "E");
        graph.addEdge("EF", "E", "F");
        graph.addEdge("DH", "D", "H");
        graph.addEdge("FH", "F", "H");
        graph.addEdge("FG", "F", "G");
        graph.addEdge("HI", "H", "I");
        graph.addEdge("IJ", "I", "J");
        graph.addEdge("IK", "I", "K");
        
        Node A = graph.getNode("A");
        Node B = graph.getNode("B");
        Node C = graph.getNode("C");
        Node D = graph.getNode("D");
        Node E = graph.getNode("E");
        Node F = graph.getNode("F");
        Node G = graph.getNode("G");
        Node H = graph.getNode("H");
        Node I = graph.getNode("I");
        Node J = graph.getNode("J");
        Node K = graph.getNode("K");
        graph.addAttribute("ui.stylesheet", styleSheet);
        
        new ColouringGame(graph.getEdgeSet(),4);
        
//        Viewer viewer = graph.display();
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
        
        //add labels to nodes
//        for (Node node : graph) {
//            node.addAttribute("ui.label", node.getId());
//        }
//
//        List<Set<Node>> pathDecomposition = new ArrayList<>();
//        pathDecomposition.add(new HashSet<>(Arrays.asList(A, B, C)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(C, D, E)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(D, E, F)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(D, F, G)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(D, F, H)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(H, I)));
//        pathDecomposition.add(new HashSet<>(Arrays.asList(I, J, K)));
//
//        List<Node> linearOrder = LOinBoundedPWGenerator.calculateOrder(pathDecomposition);
//
//        System.out.println(linearOrder.toString());
        
    }
    
    public static void main(String[] args) {
        
        new test();
        
    }
}
