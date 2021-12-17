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
    @Test
    void BFS() {
        ArrayList<Integer> expectedPdfGraph = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
        ArrayList<Integer> expectedTest1AnyOrigin = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7));
        ArrayList<Integer> expectedTest2Origin1 = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        ArrayList<Integer> expectedTest2Origin5 = new ArrayList<Integer>(Arrays.asList(5,6));
        ArrayList<Integer> expectedTest3Origin1 = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6));
        ArrayList<Integer> expectedTest3Origin7 = new ArrayList<Integer>(Arrays.asList(7,8,9,10));
        ArrayList<Integer> expectedTest3Origin11 = new ArrayList<Integer>(Arrays.asList(11,12,13,14,15));

        assertEquals(expectedPdfGraph, pdfGraph.BFS(1));
        assertEquals(expectedTest1AnyOrigin, connected7.BFS(1));
        assertEquals(expectedTest2Origin1, disconnected6.BFS(1));
        assertEquals(expectedTest2Origin5, disconnected6.BFS(5));
        assertEquals(expectedTest3Origin1, disconnected15.BFS(1));
        assertEquals(expectedTest3Origin7, disconnected15.BFS(7));
        assertEquals(expectedTest3Origin11, disconnected15.BFS(11));

    }
//
//    @Test
//    void DFS() {
//    }
//
//    @Test
//    void findConnectedComponents() {
//    }
}
