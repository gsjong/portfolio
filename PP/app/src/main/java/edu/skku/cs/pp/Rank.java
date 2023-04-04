package edu.skku.cs.pp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Rank extends AppCompatActivity {

    TextView textview;
    TextView textview2;
    TextView textview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        textview = findViewById(R.id.ranking);
        textview2 = findViewById(R.id.rname);
        textview3 = findViewById(R.id.rscore);

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlbuilder = HttpUrl.parse("https://9fw84jm31g.execute-api.ap-northeast-2.amazonaws.com/dev/get").newBuilder();
        String url = urlbuilder.build().toString();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e){
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException{
                final String myResponse = response.body().string();
                String[] name = new String[10];
                String[] score = new String[10];

                String replace = myResponse.replaceAll("\"", "");
                replace = replace.replaceAll("\\[", "");
                replace = replace.replaceAll("\\]", "");
                String[] token = replace.split(",");

                for(int i = 0; i < 10; i++){
                    name[i] = token[2 * i];
                    score[i] = token[2 * i + 1];
                }

                String rname = name[0];
                String rscore = "\n" + score[0];
                for(int i = 1; i < 10; i++){
                    rname = rname + "\n" + name[i];
                    rscore = rscore + "\n" + score[i];
                }

                String finalrname = rname;
                String finalrscore = rscore;
                Rank.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText("1st\n2nd\n3rd\n4th\n5th\n6th\n7th\n8th\n9th\n10th");
                        textview2.setText(finalrname);
                        textview3.setText(finalrscore);
                    }
                });
            }
        });
    }
}