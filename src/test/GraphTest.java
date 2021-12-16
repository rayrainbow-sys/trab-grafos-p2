import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
//import main;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private static main.Graph pdfGraph;

    @BeforeAll
    static void setUp() {
        try {
            pdfGraph = new main.Graph("src/test/input/pdf.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo do pdf");  // como lidar com isso nos testes?
        }
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    @DisplayName("Testa numero de nos")
    void getNNodes() {
        assertEquals(5, pdfGraph.getNNodes());
    }

    @Test
    @DisplayName("Testa impressao da lista de vizinhos")
    void getNeighbors() {
        ArrayList<Integer> neighbors = pdfGraph.getNeighbors(1);
        assertEquals(2, neighbors.get(0));
        assertEquals(5, neighbors.get(1));
    }

    @Test
    void getDegree() {
      assertEquals(4, pdfGraph.getDegree(5));
    
    }
//
     @Test
     void calcDistance() {
        assertEquals(2, pdfGraph.calcDistance(5, 2));
        assertEquals(1, pdfGraph.calcDistance(5, 3));
    }
//
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
