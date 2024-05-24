package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Utilities;

public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String name;
    private final TextView abilitiesTextView;
    private final TextView typesTextView;

    private Socket socket;

    public ClientThread(String address, int port, String name, TextView abilitiesTextView, TextView typesTextView) {
        this.address = address;
        this.port = port;
        this.name = name;
        this.abilitiesTextView = abilitiesTextView;
        this.typesTextView = typesTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(name);
            printWriter.flush();
            String pokenomInformation;
//            while ((pokenomInformation = bufferedReader.readLine()) != null) {
//                final String finalizedWeatherInformation = pokenomInformation;
//                abilitiesTextView.post(() -> abilitiesTextView.setText(finalizedWeatherInformation));
//            }
            pokenomInformation = bufferedReader.readLine();
            final String finalizedWeatherInformation = pokenomInformation;
            abilitiesTextView.post(() -> abilitiesTextView.setText(finalizedWeatherInformation));
            pokenomInformation = bufferedReader.readLine();
            final String finalizedWeatherInformation2 = pokenomInformation;
            typesTextView.post(() -> typesTextView.setText(finalizedWeatherInformation2));
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
