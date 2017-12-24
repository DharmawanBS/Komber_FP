/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author gdwyn
 */
public class Graph {
    public List<Vertex> vertices;
    public int[] color;

    private static class Vertex{
        private int node;
        private int[] neighbors;

        public Vertex(int node, int[] neighbors){
            this.node = node;
            this.neighbors = neighbors;
        }
    }

    class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex a, Vertex b) {
            if(a.neighbors == null) return -1;
            else if(b.neighbors == null) return 1;
            else if(a.neighbors.length < b.neighbors.length) {
                return 1;
            }
            else if(a.neighbors.length == b.neighbors.length)return 0;
            else return -1;
        }
    }

    public Graph(ArrayList<String> lines) {
        vertices = new ArrayList();
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                int node = Integer.parseInt(lines.get(i).split(":")[0]);
                String[] adj;
                if(lines.get(i).split(":").length == 2) {
                    adj = lines.get(i).split(":")[1].split("-");
                    int[] neighbors = new int[adj.length];
                    for (int j = 1; j < adj.length; j++) {
                        neighbors[j] = Integer.parseInt(adj[j]);
                    }
                    vertices.add(new Vertex(node, neighbors));
                }
                else vertices.add(new Vertex(node, null));
            }
        }
    }

    private int cari(int[] dilarang) {
        boolean ketemu = false;
        int i = 0;
        for (i = 0; i < vertices.size(); i++) {
            if(dilarang[i] == 0 && color[vertices.get(cari1(i)).node] == 0) {
                ketemu = true;
                break;
            }
        }
        if(ketemu) return i;
        else return -1;
    }

    private int cari1(int key) {
        for (int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i).node == key) return i;
        }
        return -1;
    }

    public void colourVertices(){
        Collections.sort(vertices, new VertexComparator()); // arrange vertices in order of descending valence
        color = new int[vertices.size()];
        int warna = 1;
        for (int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i).neighbors == null) {
                color[vertices.get(i).node] = warna;
                continue;
            }
            if(color[vertices.get(i).node] != 0) continue;
            color[vertices.get(i).node] = warna;
            int[] dilarang = new int[vertices.size()];
            dilarang[vertices.get(i).node] = 1;
            for(int j = 1; j < vertices.get(i).neighbors.length; j++) {
                dilarang[vertices.get(i).neighbors[j]] = 1;
            }
            while(true) {
                int temp = cari(dilarang);
                if(temp == -1) break;
                color[temp] = warna;
                dilarang[temp] = 1;
                for(int j = 0; j < vertices.get(cari1(temp)).neighbors.length; j++) {
                    dilarang[vertices.get(cari1(temp)).neighbors[j]] = 1;
                }
            }
            warna++;
        }
    }
}