package com.brenner.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class RunnableClient implements Runnable{
    Socket socket = null;
    String dstAddress;
    int dstPort;
    String response = "";
    String msgToServer;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    private static  String instance;

    public RunnableClient(String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
    }

    public void stopClient(){
        try {
            new Socket(socket.getInetAddress(), socket.getLocalPort()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(){
        return response;
    }





    @Override
    public void run() {
        try {
            socket = new Socket(dstAddress, dstPort);
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            if(msgToServer != null){
                dataOutputStream.writeUTF(msgToServer);
            }
                response =  dataInputStream.readUTF();
                instance = response;
                System.out.println("Mensagem recebida do servidor " + response);



        } catch (IOException ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            response = "false";
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }





}