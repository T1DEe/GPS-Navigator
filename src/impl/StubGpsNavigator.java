package impl;

import api.*;

import java.io.FileInputStream;
import java.util.*;


public class StubGpsNavigator implements GpsNavigator {

    private String[] buffer;
    public String[] getBuffer() {
        return buffer;
    }

    //=========================================================================
    @Override
    public void readData(String filePath) {
        int tmp = -1;
        StringBuilder str = new StringBuilder();

        /* Reading the file */
        FileInputStream stream;

        try {
            stream = new FileInputStream(filePath);
            while ((tmp = stream.read()) != -1) {
                if ((char)tmp == '\n') {
                    str.append(" ");
                    continue;
                }
                str.append((char)tmp);
            }
            stream.close();
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            return;
        }

        /* Fixing file format  */
        buffer = str.toString().trim().replaceAll("\\s+", " ").split(" ");

        /* Checking correct data sequences */
        try {
            isCorrectSequence(buffer);
        } catch (Exception exc) {
            System.err.println("Invalid file content. " + exc.getMessage());
        }
    }

    //=========================================================================
    private boolean isCorrectSequence(String[] array) {
        boolean flag = false;

        for (int i = 0; i < array.length-1; i++) {
            if(flag)
                Integer.parseInt(array[i]);

            if ((i+1) % 2 == 0)
                flag = !flag;
        }
        return true;
    }

    //=========================================================================
    @Override
    public Path findPath(String pointA, String pointB) {

        /* Creating multiple vertices */
        Set<String> points = new LinkedHashSet<>();
        boolean flag = true;
        for (int i = 0; i < buffer.length-1; i++) {
            if(flag) {
                points.add(buffer[i]);
            }
            if ((i+1) % 2 == 0)
                flag = !flag;
        }

        /* Checking for the presence of the necessary vertices. */
        if (!points.contains(pointA)) {
            System.err.println("Point " + pointA + " doesn't contains in the map");
            return null;
        }
        if (!points.contains(pointB)) {
            System.err.println("Point " + pointB + " doesn't contains in the map");
            return null;
        }

        /* Creating a list of vertices and a list to determine the index of the required vertex */
        List<Vertex> vertices = new ArrayList<>();
        List<String> verticesIndex = new ArrayList<>();
        for (String item: points) {
            verticesIndex.add(item);
            vertices.add(new Vertex(item));
        }

        /* Creating connections between vertices */
        for (int i = 0; i < buffer.length-1; i += 4) {
            String from = buffer[i];
            String to = buffer[i+1];
            int count = Integer.parseInt(buffer[i+2]) * Integer.parseInt(buffer[i+3]);

            int indexFrom = verticesIndex.indexOf(from);
            int indexTo = verticesIndex.indexOf(to);
            vertices.get(indexFrom).adjacencies.add(new Edge(vertices.get(indexTo), count));
        }

        int indexPointA = verticesIndex.indexOf(pointA);
        int indexPointB = verticesIndex.indexOf(pointB);

        computePaths(vertices.get(indexPointA));

        List<String> path = null;
        try {
            path = getShortestPathTo(vertices.get(indexPointB));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return new Path(path, vertices.get(indexPointB).minDistance);
    }

    //=========================================================================
    private static void computePaths(Vertex pointA)
    {
        pointA.minDistance = 0;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>();
        vertexQueue.add(pointA);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge edge : u.adjacencies)
            {
                Vertex v = edge.target;
                int weight = edge.weight;
                int distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    //=========================================================================
    private static List<String> getShortestPathTo(Vertex pointB) throws Exception {
        if (pointB.minDistance == Integer.MAX_VALUE) {
            throw new Exception("No path to point \"" + pointB.name + "\".");
        }

        List<String> path = new ArrayList<>();
        for (Vertex vertex = pointB; vertex != null; vertex = vertex.previous)
            path.add(vertex.name);

        Collections.reverse(path);
        return path;
    }
}