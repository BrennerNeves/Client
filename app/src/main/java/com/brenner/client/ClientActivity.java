package com.brenner.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;

/**
 * @author Brenner
 */
public class ClientActivity extends AppCompatActivity {

    Button buttonSend;
    Button buttonDisconnect;
    EditText message;
    TextView status;

    RunnableClient runnableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Intent intent = getIntent();
        final String ip = intent.getStringExtra("ip");
        final String portString =  intent.getStringExtra("port");

        buttonSend = (Button) findViewById(R.id.btSend);
        buttonDisconnect = (Button) findViewById(R.id.btDisconnect);
        message = (EditText) findViewById(R.id.etMessageToSend);
        status = (TextView) findViewById(R.id.tvStatus);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnableClient = new RunnableClient(ip, Integer.parseInt(portString), message.getText().toString());
                Thread thread = new Thread(runnableClient);
                thread.setPriority(Thread.MAX_PRIORITY);
                thread.setDaemon(true);
                thread.start();

                try {
                    new Thread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                String statusServer = runnableClient.getStatus();

                //Aguarda a thread do envio ao servidor e verfica se ouve resposposta
                if( statusServer.contains("true")){
                    status.setText("Mensage enviada com sucesso:" + message.getText().toString());
                }else{
                    status.setText("Erro ao enviar mensagem:" + message.getText().toString());
                }
                message.setText("");
            }



        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnableClient = new RunnableClient(ip, Integer.parseInt(portString), "true");
                new Thread(runnableClient).start();
                callClientActivity();
            }
        });
    }

    //Chama a tela Inicial
    public void callClientActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
