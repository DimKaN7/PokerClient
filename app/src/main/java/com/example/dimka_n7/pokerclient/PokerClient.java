package com.example.dimka_n7.pokerclient;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by DimKa_N7 on 07.08.17.
 */

import com.example.dimka_n7.pokerclient.R.*;

public class PokerClient {

    private final String TAG = "DimKa_N7";
    private final String IP  = "192.168.0.224";
    private final int PORT = 11110;

    private String sMessage; // сообщение от сервера
    private boolean isRunning = false;
    private PrintWriter pw = null;
    private BufferedReader br = null;
    private OnMessageReceived listener = null;

    public Socket pClient;
    public InetAddress addr;
    public String name;

    public PokerClient(OnMessageReceived listener) {

        this.listener = listener;

    }

    public void sendMessage(String message) {

        if (pw != null && !pw.checkError()) {
            Log.e(TAG, "Sending message: " + message);
            pw.println(message);
            pw.flush();
        }

    }

    public String getName() {

        return this.name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public void setStop() {

        isRunning = false;

    }

    public void run() {

        isRunning = true;

        try {
            addr = InetAddress.getByName(IP);
            pClient = new Socket(addr, PORT);
            try {
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(pClient.getOutputStream())));
                br = new BufferedReader(new InputStreamReader(pClient.getInputStream()));
                sendMessage("NAME:" + name);
                Log.e(TAG, "Output/input streams created");
                while (isRunning) {
                    sMessage = br.readLine();
                    if (sMessage != null && !sMessage.equals("")) {
                        Log.e(TAG, "Message from the server: " + sMessage);
                        listener.messageReceived(sMessage);
                    }
                    sMessage = null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in creating output/input streams or in receiving messages from the server: " + e.toString());
            }
            finally {
                pClient.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Client running error: " + e.toString());
        }

    }

    public interface OnMessageReceived {

        public void messageReceived(String message);

    }

}
