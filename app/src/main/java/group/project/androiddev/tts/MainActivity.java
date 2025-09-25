/*
 * Copyright (c) 2025 Pham The Minh
 * All rights reserved.
 * Project: TTS implementation
 * File: MainActivity.java
 * Last Modified: 25/9/2025 8:55
 */

package group.project.androiddev.tts;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private EditText input;
    private Button speakBtn, stopBtn;

    private boolean nextSlow = false;   // toggle state
    private String lastText = "";       // remember last spoken text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // build simple layout
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        input = new EditText(this);
        input.setHint("Type something to speak…");
        input.setMinLines(2);
        input.setMaxLines(6);
        root.addView(input);

        speakBtn = new Button(this);
        speakBtn.setText("Speak!");
        root.addView(speakBtn);

        stopBtn = new Button(this);
        stopBtn.setText("Stop");
        root.addView(stopBtn);

        setContentView(root);

        // init TTS
        tts = new TextToSpeech(this, this);

        // speak logic
        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString().trim();

                if (text.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "No text found. Please enter text.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // reset toggle if text is new
                if (!text.equalsIgnoreCase(lastText)) {
                    nextSlow = false;       // reset to normal speed
                    lastText = text;        // update last text
                    // reset language (force reapply)
                    tts.setLanguage(Locale.US);
                }

                float rate = nextSlow ? 0.5f : 1.0f;
                tts.setSpeechRate(rate);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utt-id");

                // toggle for next time (only if same text)
                nextSlow = !nextSlow;
            }
        });

        // stop logic
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts.isSpeaking()) {
                    tts.stop();
                    Toast.makeText(MainActivity.this, "Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.US);
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this,
                        "TTS language missing/unsupported. Install English voice in settings.",
                        Toast.LENGTH_LONG).show();
            }
            speakBtn.setEnabled(true);
            stopBtn.setEnabled(true);
        } else {
            Toast.makeText(this, "TTS init failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
