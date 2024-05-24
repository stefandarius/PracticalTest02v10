package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02v10.model.PokemonInfo;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String name = bufferedReader.readLine();
            if (name == null || name.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                String url = Constants.WEB_SERVICE_ADDRESS +  name;
                URL urlAddress = new URL(url);
                URLConnection urlConnection = urlAddress.openConnection();
                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String pageSourceCode;
                StringBuilder stringBuilder = new StringBuilder();
                String currentLine;
                while ((currentLine = bufferedReader1.readLine()) != null) {
                    stringBuilder.append(currentLine);
                }
                bufferedReader1.close();
                pageSourceCode = stringBuilder.toString();

                JSONObject content = new JSONObject(pageSourceCode);

                List<String> types = new ArrayList<>();
                List<String> abilities = new ArrayList<>();

                JSONArray jsonTypes = content.getJSONArray("types");
                for (int i = 0; i < jsonTypes.length(); i++) {
                    JSONObject type = jsonTypes.getJSONObject(i);
                    JSONObject typeName = type.getJSONObject("type");
                    String typeNameString = typeName.getString("name");
                    types.add(typeNameString);
                }

                JSONArray jsonAbilities = content.getJSONArray("abilities");
                for (int i = 0; i < jsonAbilities.length(); i++) {
                    JSONObject ability = jsonAbilities.getJSONObject(i);
                    JSONObject abilityName = ability.getJSONObject("ability");
                    String abilityNameString = abilityName.getString("name");
                    abilities.add(abilityNameString);
                }

            PokemonInfo pokeInfo = new PokemonInfo(types, abilities);
               Log.d(Constants.TAG, pokeInfo.toString());

            printWriter.println(pokeInfo.getAbilities());
            printWriter.flush();
printWriter.println(pokeInfo.getTypes());
            printWriter.flush();
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

}
