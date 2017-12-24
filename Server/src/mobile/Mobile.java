/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author gdwyn
 */
public class Mobile extends Thread {
    private ServerSocket serverSocket;
    private String input = new String();
    
    public Mobile(int port) throws IOException {
        serverSocket = new ServerSocket(port);
   }
    public void run() {
       while(true) {
           try {
               System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
               Socket server = serverSocket.accept();
               
               DataInputStream in = new DataInputStream(server.getInputStream());
               input = in.readUTF();
               System.out.println(input);
               int jumlah = Integer.parseInt(input);
               ArrayList<String> tabrakan = new ArrayList();
               String output = "";
               if(jumlah > 0) {
                   int i = 0;
                   while(jumlah > i) {
                       input = in.readUTF();
                       System.out.println(input);
                       tabrakan.add(input);
                       i++;
                   }
               }
               Graph graph = new Graph(tabrakan);
               graph.colourVertices();
               int[] warna = graph.color;
               int max = 0;
               for(int i=0;i<warna.length;i++) {
                   output+=String.valueOf(warna[i]);
                   if(max < warna[i]) max = warna[i];
                   output+=",";
               }
               System.out.println("Max warna = "+max+" Total titik = "+warna.length);
               
               DataOutputStream out = new DataOutputStream(server.getOutputStream());
               out.writeUTF(output);
               server.close();
           }catch(IOException e) {
               break;
           }
       }
    }
    
    public static void main(String [] args) {
        int port = 4444;
        try {
            new Mobile(port).start();
        } catch(IOException e) {
            e.printStackTrace();
      }
   }
}
