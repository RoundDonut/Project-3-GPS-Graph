import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import hashtable.*;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private HashTable<String, Node> nodeHashTable;

    public Graph(String inputFilePath) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nodeHashTable = new HashTable<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Process each line as needed
                String[] splitLine = line.split("\\s+");

                if (splitLine[0].equals("i")) {
                    Node node = new Node(splitLine[1], Double.parseDouble(splitLine[2]), Double.parseDouble(splitLine[3]));

                    nodes.add(node);
                    nodeHashTable.put(node.nodeID, node);
                }
                else {
                    Node startingNode = nodeHashTable.get(splitLine[2]);
                    Node endingNode = nodeHashTable.get(splitLine[3]);

                    Edge edge = new Edge(splitLine[1], startingNode, endingNode);

                    edges.add(edge);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printGraph() {
        System.out.println("Nodes: " + nodes);
        System.out.println("Edges: " + edges);

        System.out.print("Nodes & Edges: [");
        for (Edge edge : edges) {
            System.out.print(edge.getStartNode() + " -:(" + edge.getWeight() + "):- " + edge.getEndNode() + ", ");
        }
        System.out.print("]");
    }
}
