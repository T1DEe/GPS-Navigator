package impl;

import java.util.ArrayList;

/**
 * Class describes the vertex of the graph
 */
class Vertex implements Comparable<Vertex> {

    final String name;
    ArrayList<Edge> adjacencies = new ArrayList<>();
    int minDistance = Integer.MAX_VALUE;
    Vertex previous;

    Vertex(String argName) { name = argName; }

    public String toString() { return name; }

    @Override
    public int compareTo(Vertex other) {
        return Integer.compare(minDistance, other.minDistance);
    }
}
