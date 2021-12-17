import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
//import main;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private static main.Graph pdfGraph;
    private static main.Graph connected7;
    private static main.Graph disconnected6;
    private static main.Graph disconnected15;

    private static double eps = 1e-6;  // p/ comparações de floats

    @BeforeAll
    static void setUp() {
        try {
            pdfGraph = new main.Graph("src/test/input/pdf.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo do pdf");
            // Como lidar com isso nos testes?
            // A rigor, acho que a gente deveria estar testando se cada um é null antes
            // de rodar mais testes, mas, como são sempre os mesmos 4 e já sabemos que
            // estão funcionando, vou evitar ficar enchendo de if (... != null)
        }

        try {
            connected7 = new main.Graph("src/test/input/teste1.txt", 0);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 1");
        }

        try {
            disconnected6 = new main.Graph("src/test/input/teste2.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 2");
        }

        try {  // vai falhar
            disconnected15 = new main.Graph("src/test/input/teste3.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 3");
        }
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    @DisplayName("Testa numero de nos")
    void getNNodes() {
        assertEquals(5, pdfGraph.getNNodes());
        assertEquals(7, connected7.getNNodes());
        assertEquals(6, disconnected6.getNNodes());
        assertEquals(16, disconnected15.getNNodes());
    }

    @Test
    @DisplayName("Testa numero de arestas")
    void getNEdges() {
        assertEquals(5, pdfGraph.getNEdges());
    }

    @Test
    @DisplayName("Testa impressao da lista de vizinhos")
    void getNeighbors() {
        ArrayList<Integer> neighbors = pdfGraph.getNeighbors(1);

        for (int i=1; i<=pdfGraph.getNNodes(); i++) {
            if (i == 2 || i == 5) {
                assertEquals(true, neighbors.contains(i));
            } else {
                assertEquals(false, neighbors.contains(i));
            }
        }
    }

    @Test
    @DisplayName("Testa grau")
    void getDegree() {
      assertEquals(4, pdfGraph.getDegree(5));
    }

    @Test
    @DisplayName("Testa resumo grau")
    void getDegreeOverview() {
        HashMap<String, Double> pdfData = pdfGraph.getDegreeOverview();

        assertEquals(4.0, pdfData.get("max"));
        assertEquals(1.0, pdfData.get("min"));
        assertEquals(true, Math.abs(pdfData.get("mean") - 2) < eps);
        assertEquals(true, Math.abs(pdfData.get("med") - 2) < eps);
        // 1, 1, 2, 2, 4

        // Só o pdfGraph está passando; dá uma conferida na sua representação
        // dos demais quando puder, Ray!
        HashMap<String, Double> c7Data = connected7.getDegreeOverview();

        assertEquals(3.0, c7Data.get("max"));
        assertEquals(1.0, c7Data.get("min"));
        assertEquals(true, Math.abs(c7Data.get("mean") - 12.0/7.0) < eps);
        assertEquals(true, Math.abs(c7Data.get("med") - 1.5) < eps);

        HashMap<String, Double> d6Data = disconnected6.getDegreeOverview();

        assertEquals(2.0, d6Data.get("max"));
        assertEquals(1.0, d6Data.get("min"));
        assertEquals(true, Math.abs(d6Data.get("mean") - 8.0/6.0) < eps);
        assertEquals(true, Math.abs(d6Data.get("med") - 1.5) < eps);

        HashMap<String, Double> d15Data = connected7.getDegreeOverview();

        assertEquals(4.0, d15Data.get("max"));
        assertEquals(1.0, d15Data.get("min"));
        assertEquals(true, Math.abs(d15Data.get("mean") - 24.0/15.0) < eps);
        assertEquals(true, Math.abs(d15Data.get("med") - 2.5) < eps);
    }

    @Test
    void calcDistance() {
        assertEquals(2, pdfGraph.calcDistance(5, 2));
        assertEquals(1, pdfGraph.calcDistance(5, 3));
    }

    @Test
    void calcDiameter() {
         assertEquals(2, pdfGraph.calcDiameter());
    }
//
//    @Test
//    void BFS() {
//    }
//
//    @Test
//    void DFS() {
//    }
//
//    @Test
//    void findConnectedComponents() {
//    }
}
