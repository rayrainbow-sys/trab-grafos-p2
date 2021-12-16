import java.util.LinkedList;
import java.util.ArrayList;

public class Graph {
    private int nNodes;
    private ArrayList<ArrayList<Int>> adjMatrix;
    private ArrayList<LinkedList<Int>> adjList;

    public Graph(int nNodes, int reprChoice) {
        this.nNodes = nNodes;

        if (reprChoice == 0) {
            this.adjList = null;

            // Inicializa matriz de adjacência de zeros

            for (int i=0; i < nNodes; i++) {

                for (int j=0; j < nNodes; j++) {

                }
            }

        } else if (reprChoice == 1) {
            this.adjMatrix = null;

            // Inicializa lista de adjacências sem vizinhos

        } else {
            throw new IllegalArgumentException("0 para representacao por matriz, 1 para grafo");
        }
    }

}