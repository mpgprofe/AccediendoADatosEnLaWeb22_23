package com.example.accediendoadatosenlaweb22_23;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button buttonCSV, buttonXML, buttonJSON;
    ListView lista;

    static final String SERVIDOR = "http://192.168.3.18/nube/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCSV = findViewById(R.id.buttonCSV);
        buttonXML = findViewById(R.id.buttonXML);
        buttonJSON = findViewById(R.id.buttonJSON);
        lista = findViewById(R.id.listView);

        buttonCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescargarCSV descargarCSV = new DescargarCSV();
                descargarCSV.execute("listadoCSV.php");
            }
        });

    }


    private class DescargarCSV extends AsyncTask<String, Void, Void> {
        String todo = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();
            String[] lineas = todo.split("\n");

            for(String linea:lineas){
                String[] campos = linea.split(";");
                String dato="ID: "+campos[0];
                dato+=" PRODUCTO"+campos[1];
                dato+=" MODELO"+campos[2];
                dato+=" PRECIO"+campos[3];
                list.add(dato);
            }
            adapter= new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            lista.setAdapter(adapter);



        }

        @Override
        protected Void doInBackground(String... strings) {
            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + script);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String linea = "";
                    while ((linea = br.readLine()) != null) {
                        todo += linea+"\n";
                   }
                    br.close();
                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}