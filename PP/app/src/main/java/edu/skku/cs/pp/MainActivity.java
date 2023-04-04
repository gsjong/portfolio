package edu.skku.cs.pp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edittext;
    Button button;
    Button button2;
    public static String USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = findViewById(R.id.enter);
        button = findViewById(R.id.start);
        button2 = findViewById(R.id.rank);

        button.setOnClickListener(view -> {
            int n = 0;
            String name = edittext.getText().toString();

            if(name.length() == 0) n = 1;
            for(int i = 0; i < name.length(); i++){
                if(String.valueOf(name.charAt(i)).matches("[^a-zA-Z0-9]")){
                    n = 1;
                    break;
                }
            }
            if(n == 1){
                Toast.makeText(this, "You can enter only alphabet or numbers.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, Game.class);
                intent.putExtra(USER_NAME, edittext.getText().toString());
                startActivity(intent);
            }
        });

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Rank.class);
            startActivity(intent);
        });
    }
}