package impl;

/**
 * Class describes the edge between two vertices
 */
class Edge {
    final Vertex target;
    final int weight;

    Edge(Vertex argTarget, int argWeight) {
        target = argTarget; weight = argWeight;
    }
}
