/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg05_clientetcpsimples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aulas
 */
public class Main extends Thread{
    private Tela telaJogo;
    private Socket socket;
    private BufferedReader bfr;
    private InputStreamReader inr;
    private OutputStream ou;
    ObjectOutputStream obj_out;
    private Writer ouw;
    private BufferedWriter bfw;
    int[][] matrizJogo;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main app = new Main();
        app.conectar();
        //app.escutar();
        app.start();
    }

    
    @Override
    public void run() {
        int lin, col;
        
        this.escutar();
        this.telaJogo = new Tela(8, 8, 60, matrizJogo, this);
        
        System.out.println("iniciar tela");
        while (socket.isConnected()) {
            this.escutar();
            telaJogo.desenhaTela(matrizJogo);
        }
    }
    
    public void conectar() throws IOException{
        socket = new Socket("127.0.0.1",10000);
        ou = socket.getOutputStream();
        obj_out = new ObjectOutputStream(ou);
        
    }
    
    public void enviarMensagem(int tecla) throws IOException{
        obj_out.writeObject(tecla);
        obj_out.flush();
    }
    
    public void escutar(){
        InputStream in = null;
        try {
            in = socket.getInputStream();
            String msg = "";
            ObjectInputStream obj = new ObjectInputStream(in);
            matrizJogo = (int[][]) obj.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
