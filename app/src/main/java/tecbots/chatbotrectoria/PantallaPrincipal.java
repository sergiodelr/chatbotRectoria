package tecbots.chatbotrectoria;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class PantallaPrincipal extends AppCompatActivity {

   private Button record, stop, play;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private TextView resultTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = (Button)findViewById(R.id.record);
        stop = (Button)findViewById(R.id.stop);
        play = (Button)findViewById(R.id.play);
        resultTEXT = (TextView) findViewById(R.id.TVresult);

        stop.setEnabled(false);
        play.setEnabled(false);
        record.setEnabled(true);
        resultTEXT.setVisibility(TextView.INVISIBLE);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    speechToText();
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();


                } catch (IllegalStateException ise){

                } catch (IOException ioe){

                }

                record.setEnabled(false);
                play.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTEXT.setVisibility(TextView.VISIBLE);
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio Recorded Successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try{
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                }catch(Exception e){

                }
            }
        });

    }
    public void speechToText(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 10);
        }catch(ActivityNotFoundException a){
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Language", Toast.LENGTH_LONG);
        }
    }
    public  void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code, result_code, i);
        switch (request_code){
            case 10:
                if (result_code == RESULT_OK && i != null){
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultTEXT.setText(result.get(0));
                }
            break;

        }
    }
}
