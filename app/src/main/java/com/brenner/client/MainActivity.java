package com.brenner.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button buttonConnect;
    EditText portServer;
    EditText ipServer;
    EditText userName;
    TextView status;
    RunnableClient instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonConnect = (Button) findViewById(R.id.btConnect);
        portServer = (EditText) findViewById(R.id.etPortServer);
        ipServer = (EditText) findViewById(R.id.etIpServer);
        userName = (EditText) findViewById(R.id.etName);
        status = (TextView) findViewById(R.id.tvStatus);


        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tMsg = userName.getText().toString();
                int port = Integer.parseInt(portServer.getText().toString());
                String ip = ipServer.getText().toString();
                instance = new RunnableClient(ip, port, tMsg);
                new Thread(instance).start();

                try {
                    new Thread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Status que foi retornado ao tentar connectar no servidor
                String statusServer = instance.getStatus();

                //Aguarda a thread do envio ao servidor e verfica se ouve resposposta
                if(statusServer.contains("true")){
                    callClientActivity(ip, port);
                    buttonConnect.setEnabled(false);
                }else {
                    status.setText("Erro ao connectar no Servidor, tente novamente.");
                }


            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            instance.stopClient();
            buttonConnect.setEnabled(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    //Chama a tela de envio de mensagem
    public void callClientActivity(String ip, int port){
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("ip", ip);
        intent.putExtra("port", port + "");
        startActivity(intent);
    }



}
