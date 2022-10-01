/*
* Krushkal's Algorithm to find minimum spanning tree for a cities-distances graph
* Uses Piority Queue to store edges (connections bw cities)
* Uses Array List to store cities
* Uses Union-Find Class to check the cycle (already connected cities)
*
* @author: Safal Tyagi
* */

import java.io.File;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Krushkal {

    public static ArrayList<Connection> kruskalMST(CityGraph cityGraph) {
        int nConnections = 0;
        int nCities = cityGraph.cities.size();
        DisjSets ds = new DisjSets(nCities);
        ArrayList<Connection> cityMST = new ArrayList<>(nCities - 1);
        Connection con;
        int cityA, cityB;

        // until n-1 connections needed to connect all n cities
        while (nConnections < nCities-1) {
            con = cityGraph.connections.poll();  // get minimum distance = (cityA, cityB)
            cityA = cityGraph.cities.indexOf(con.source);
            cityB = cityGraph.cities.indexOf(con.destination);

            int rootA = ds.find(cityA);
            int rootB = ds.find(cityB);

            if (rootA != rootB) {
                cityMST.add(con); // accept the edge
                ds.union(rootA, rootB); // connect them
                //ds.union(ds.find(cityA), ds.find(cityB)); // connect them
                nConnections++;
            }
        }
        return cityMST;
    }

    public static void printMST(ArrayList<Connection> cityMST) {
        int totalDistance = 0;
        for (Connection connection : cityMST) {
            System.out.println(connection.source + " - " + connection.destination + " : " + connection.distance);
            totalDistance+=connection.distance;
        }
        System.out.println("\nTotal distance: " + totalDistance);
    }

    public static void main(String[] args) throws Exception {

        // Create empty CityGraph, cities and connections added as read from cvs
        CityGraph cityGraph = new CityGraph();
        // Read csv file
        String csvFile = "C:/Users/tyagi/IdeaProjects/GraphMST/src/cities.csv";
        Scanner scanner = new Scanner(new File(csvFile));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            int nums = words.length;
            // source
            //System.out.println("From: " + words[0]);
            cityGraph.addCity(words[0]);
            // connected cities
            for (int i = 1; i < nums; i += 2) {
                //System.out.println("To: " + words[i] + " " + words[i+1] + " miles");
                // note: containsConnection check to be removed for DAG
                if(!cityGraph.containsConnection(words[i], words[0], Integer.parseInt(words[i + 1])))
                    cityGraph.addConnection(words[0], words[i], Integer.parseInt(words[i + 1]));
            }
        }
        scanner.close();

        // print city graph
        //cityGraph.printCityGraph();

        // call Krushkal's
        ArrayList<Connection> cityMST = kruskalMST(cityGraph);

        // print MST
        printMST(cityMST);
    }
}

/**
 * City graph class.
 *
 * @author Safal Tyagi
 */
class CityGraph {

    // Initialize graph: known size
    CityGraph() {
        this.cities = new ArrayList<>();
        this.connections = new PriorityQueue<>();
    }

    public void addCity(String cityName) {
        this.cities.add(cityName);
    }

    public void addConnection(String source, String destination, int distance) {
        Connection connection = new Connection(source, destination, distance);
        connections.add(connection);
    }

    public boolean containsConnection(String source, String destination, int distance) {
        boolean found = false;
        for (Connection connection : connections) {
            if (connection.source.compareTo(source) == 0 &&
                    connection.destination.compareTo(destination) == 0 &&
                    connection.distance == distance) {
                found = true;
                break;
            }
        }
        return  found;
    }

    public void printCityGraph() {
        connections.forEach(connection ->
                System.out.println("From " + connection.source +
                        " to " + connection.destination +
                        " " + connection.distance));
    }

    public ArrayList<String> cities;
    public PriorityQueue<Connection> connections;
}

/**
 * Connection class for Graph edge.
 *
 * @author Safal Tyagi
 */
class Connection implements Comparable<Connection> {
    public String source;
    public String destination;
    public int distance;

    Connection(String source, String destination, int distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public int compareTo(Connection other) {
        return (this.distance - other.distance);
    }
}

/**
 * Disjoint set class, using union by rank and path compression.
 * Elements in the set are numbered starting at 0.
 *
 * @author Mark Allen Weiss
 */
class DisjSets {
    // Constructor
    public DisjSets(int numElements) {
        s = new int[numElements];
        for (int i = 0; i < s.length; i++)
            s[i] = -1;
    }

    // Union
    public void union(int root1, int root2) {
        if (s[root2] < s[root1])  // root2 is deeper
            s[root1] = root2;        // Make root2 new root
        else {
            if (s[root1] == s[root2])
                s[root1]--;          // Update height if same
            s[root2] = root1;        // Make root1 new root
        }
    }

    // Find
    public int find(int x) {
        if (s[x] < 0)
            return x;
        else
            return s[x] = find(s[x]);
    }

    // indices array
    public int[] s;
}
