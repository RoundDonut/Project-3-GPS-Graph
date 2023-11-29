public class Edge {
    private String edgeID;
    private Node startNode; // Despite the naming convention, this is an undirected edge
    private Node endNode;
    private double weight; // in kilometers

    public Edge(String edgeID, Node startNode, Node endNode) {
        this.edgeID = edgeID;
        this.startNode = startNode;
        this.endNode = endNode;
        weight = calculateHaversineWeight(startNode, endNode);
    }

    /**
     * Calculates the distance between two points on earth using the Haversine formula.
     * Credit to ChatGPT for this code.
     *
     * @param startNode the starting node
     * @param endNode the ending node
     * @return the distance in kilometers between the two points
     */
    private double calculateHaversineWeight(Node startNode, Node endNode) {
            final int R = 6371; // Earth's radius in kilometers

            double startNodeLatitude = startNode.latitude;
            double startNodeLongitude = startNode.longitude;
            double endNodeLatitude = endNode.latitude;
            double endNodeLongitude = endNode.longitude;

            // Convert latitude and longitude from degrees to radians
            double dLat = Math.toRadians(endNodeLatitude - startNodeLatitude);
            double dLon = Math.toRadians(endNodeLongitude - startNodeLongitude);

            // Haversine formula
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(startNodeLatitude)) * Math.cos(Math.toRadians(endNodeLatitude)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // Calculate the distance
            return R * c;
    }

    @Override
    public String toString() {
        return edgeID;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public double getWeight() {
        return weight;
    }
}