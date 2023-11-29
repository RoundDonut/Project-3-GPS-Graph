public class Node {
    String nodeID; // analagous to IntersectionID
    double latitude;
    double longitude;

    public Node(String nodeID, double latitude, double longitude) {
        this.nodeID = nodeID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return nodeID;
    }
}