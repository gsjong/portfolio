package edu.skku.cs.pp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Game extends AppCompatActivity {

    TextView textview;
    TextView textview2;
    TextView textview3;
    TextView textview4;
    TextView textview5;
    TextView textview6;
    TextView textview7;
    TextView textview8;
    ImageView imageview;
    ImageView imageview2;
    ImageView imageview3;
    ImageView imageview4;
    ImageView imageview5;
    ImageView imageview6;
    ImageView imageview7;
    ImageView imageview8;
    AnimationDrawable hitani;
    AnimationDrawable attackani;
    Button button;
    Button button2;
    Button button3;
    Random random;
    private ListView listview;
    private Adapter adapter;
    private ArrayList<Log> items;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textview = findViewById(R.id.username);
        textview2 = findViewById(R.id.stage);
        textview3 = findViewById(R.id.die);
        textview4 = findViewById(R.id.hp);
        textview5 = findViewById(R.id.info);
        textview6 = findViewById(R.id.potion);
        textview7 = findViewById(R.id.enemy);
        textview8 = findViewById(R.id.enemyhp);
        imageview = findViewById(R.id.face);
        imageview2 = findViewById(R.id.pot);
        imageview3 = findViewById(R.id.special);
        imageview4 = findViewById(R.id.character);
        imageview5 = findViewById(R.id.monster);
        imageview6 = findViewById(R.id.hit);
        imageview7 = findViewById(R.id.attack);
        imageview8 = findViewById(R.id.battleview);
        button = findViewById(R.id.dice);
        button2 = findViewById(R.id.heal);
        button3 = findViewById(R.id.quit);
        random = new Random();
        listview = findViewById(R.id.stageview);
        items = new ArrayList<Log>();

        Intent intent = getIntent();
        model = new Model(intent.getStringExtra(MainActivity.USER_NAME));

        textview.setText(model.getusername());
        textview2.setText("STAGE\n     " + Integer.toString(model.getstage()));
        textview3.setText("MIN:" + model.getMIN() + "\nMAX:" + model.getMAX());
        textview4.setText("HP:" + model.getHP() + "/" + model.getMAXHP());
        textview6.setText("Potion:" + model.getpotion());
        textview7.setText("");
        textview8.setText("");
        imageview.setImageResource(R.drawable.face);
        imageview2.setImageResource(R.drawable.potion_2_red);
        imageview4.bringToFront();
        imageview5.bringToFront();
        imageview3.bringToFront();
        imageview4.setImageResource(R.drawable.swordsman_t);
        imageview6.bringToFront();
        imageview7.bringToFront();
        MapSpawner(model.getstage());

        random.setSeed(System.currentTimeMillis());

        OkHttpClient client = new OkHttpClient();
        PostModel data = new PostModel();
        data.setName(model.getusername());
        Gson gson = new Gson();
        HttpUrl.Builder urlbuilder = HttpUrl.parse("https://9fw84jm31g.execute-api.ap-northeast-2.amazonaws.com/dev/post").newBuilder();
        String url = urlbuilder.build().toString();

        final int[] state = {0};
        final int[] quit = {0};

        button.setOnClickListener(view -> {
            quit[0] = 0;
            if(state[0] == 0){ //여관
                model.nextstage();
                textview2.setText("STAGE\n     " + Integer.toString(model.getstage()));
                textview5.setText("Enemy!");
                model.setEnemy(model.getstage() * (model.getstage() / 20 + 1));
                model.setEnemyMAXHP(model.getstage() * (3 + model.getstage() / 10) * (model.getstage() / 10 + 1));
                model.setEnemyHP(model.getEnemyMAXHP());
                textview7.setText("Damage:" + model.getEnemy());
                textview8.setText("HP:" + model.getEnemyHP() + "/" + model.getEnemyMAXHP());
                imageview3.setImageResource(0);
                imageview4.setImageResource(R.drawable.swordsman_t);
                EnemySpawner(model.getstage());
                state[0] = 4;
            }
            else if(state[0] == 1){ //내 턴 시작
                int dice = model.getMIN() + random.nextInt(model.getMAX() - model.getMIN() + 1);
                if(dice == 0) {
                    textview5.setText("Miss!");
                }
                else {
                    textview5.setText(Integer.toString(dice));
                    imageview7.setImageResource(R.drawable.hit);
                    attackani = (AnimationDrawable) imageview7.getDrawable();
                    attackani.start();
                }
                model.setEnemyHP(model.getEnemyHP() - dice);
                textview8.setText("HP:" + model.getEnemyHP() + "/" + model.getEnemyMAXHP());
                state[0] = 2;
            }
            else if(state[0] == 2){ //내 턴 종료
                imageview7.setImageResource(0);
                if(model.getEnemyHP() <= 0){
                    textview5.setText("Defeat!");
                    model.setscore(model.getscore() + model.getstage() * (model.getstage() / 10 + 1));
                    state[0] = 5;
                }
                else{
                    textview5.setText("Enemy turn");
                    state[0] = 3;
                }
            }
            else if(state[0] == 3){ //상대 턴 시작
                int n = random.nextInt(model.getEnemy() + 1);
                if(n == 0){
                    textview5.setText("Dodge!");
                }
                else {
                    textview5.setText("You got " + n + " damage!");
                    imageview6.setImageResource(R.drawable.hit);
                    hitani = (AnimationDrawable) imageview6.getDrawable();
                    hitani.start();
                }
                model.setHP(model.getHP() - n);
                textview4.setText("HP:" + model.getHP() + "/" + model.getMAXHP());
                state[0] = 4;
            }
            else if(state[0] == 4){ //상대 턴 종료
                imageview6.setImageResource(0);
                if(model.getHP() <= 0){
                    textview5.setText("You died.");
                    state[0] = 6;
                }
                else{
                    textview5.setText("Your turn");
                    state[0] = 1;
                }
            }
            else if(state[0] == 5){ //승리
                if(model.getstage() >= 100){ //끝
                    model.setscore(model.getscore() + model.getpotion() * model.getstage());
                    textview5.setText("Your score:" + model.getscore());
                    state[0] = 8;
                }
                else{ //보상
                    int i = 0, j = 0, k = 0, l = 0;
                    if(random.nextInt(100) < 90){
                        i = random.nextInt(model.getstage() + 1);
                        model.setMAXHP(model.getMAXHP() + 2 * i);
                        model.setHP(model.getHP() + 2 * i);
                    }
                    if(random.nextInt(100) < 70){
                        j = random.nextInt(model.getstage() + 1);
                        model.setMAX(model.getMAX() + j);
                    }
                    if(random.nextInt(100) < 50){
                        k = random.nextInt(model.getstage() + 1);
                        model.setMIN(model.getMIN() + k);
                    }
                    if(random.nextInt(100) < 30){
                        l = 1;
                        model.setpotion(model.getpotion() + l);
                    }
                    textview5.setText("MAXHP+:" + 2 * i + "\nMIN+:" + k + "\nMAX+:" + j + "\nPotion+:" + l);
                    textview3.setText("MIN:" + model.getMIN() + "\nMAX:" + model.getMAX());
                    textview4.setText("HP:" + model.getHP() + "/" + model.getMAXHP());
                    textview6.setText("Potion:" + model.getpotion());
                    textview7.setText("");
                    textview8.setText("");
                    imageview5.setImageResource(0);

                    state[0] = 7;
                }
            }
            else if(state[0] == 6){ //패배
                model.setscore(model.getscore() + model.getpotion() * model.getstage());
                textview5.setText("Your score:" + model.getscore());
                state[0] = 8;
            }
            else if(state[0] == 7){ //무작위 선택
                int n;
                if(model.getstage() < 99) {
                    n = random.nextInt(100);
                }
                else{
                    n = 99;
                }
                model.nextstage();
                textview2.setText("STAGE\n     " + Integer.toString(model.getstage()));
                if(n < 10){
                    textview5.setText("You find a treasure box!!");
                    imageview3.setImageResource(R.drawable.tutorial_sample49_64x64);
                    imageview4.setImageResource(0);
                    items.add(new Log(model.getstage(), R.drawable.tutorial_sample49b_64x64));
                    adapter = new Adapter(items, getApplicationContext());
                    listview.setAdapter(adapter);
                    listview.setSelection(adapter.getCount() - 1);
                    state[0] = 5;
                }
                else if(n < 30){
                    textview5.setText("Rest...");
                    model.setHP(model.getMAXHP());
                    textview4.setText("HP:" + model.getHP() + "/" + model.getMAXHP());
                    imageview3.setImageResource(R.drawable.kampvuur);
                    imageview4.setImageResource(0);
                    items.add(new Log(model.getstage(), R.drawable.tent));
                    adapter = new Adapter(items, getApplicationContext());
                    listview.setAdapter(adapter);
                    listview.setSelection(adapter.getCount() - 1);
                    state[0] = 0;
                }
                else{
                    textview5.setText("Enemy!");
                    model.setEnemy(model.getstage() * (model.getstage() / 20 + 1));
                    model.setEnemyMAXHP(model.getstage() * (3 + model.getstage() / 10) * (model.getstage() / 10 + 1));
                    model.setEnemyHP(model.getEnemyMAXHP());
                    textview7.setText("Damage:" + model.getEnemy());
                    textview8.setText("HP:" + model.getEnemyHP() + "/" + model.getEnemyMAXHP());
                    imageview3.setImageResource(0);
                    imageview4.setImageResource(R.drawable.swordsman_t);
                    EnemySpawner(model.getstage());
                    state[0] = 4;
                }
            }
            else if(state[0] == 8) { //점수 포스트 및 종료
                data.setScore(model.getscore());
                String json = gson.toJson(data, PostModel.class);
                Request request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"), json)).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Game.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                });
            }
            else {
                Toast.makeText(this, "This is a BUG!", Toast.LENGTH_SHORT).show();
            }
            MapSpawner(model.getstage());
        });

        button2.setOnClickListener(view -> { //회복
            quit[0] = 0;
            if(model.getpotion() > 0 && model.getHP() > 0){
                if(state[0] == 1 || state[0] == 5 || state[0] == 7) {
                    Toast.makeText(this, "You use a potion!", Toast.LENGTH_SHORT).show();
                    model.setHP(model.getMAXHP());
                    model.setpotion(model.getpotion() - 1);
                    textview4.setText("HP:" + model.getHP() + "/" + model.getMAXHP());
                    textview6.setText("Potion:" + model.getpotion());
                }
                else{
                    Toast.makeText(this, "Not your turn!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(view -> { //퇴장
            if(quit[0] == 0) {
                Toast.makeText(this, "Really?", Toast.LENGTH_SHORT).show();
                quit[0] = 1;
            }
            else{
                finish();
            }
        });
    }

    public void EnemySpawner(int stage){
        if(stage < 10){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.__mushroom_idle_000);
                items.add(new Log(model.getstage(), R.drawable.__mushroom_idle_000));
            }
            else{
                imageview5.setImageResource(R.drawable.skeleton_animation_00);
                items.add(new Log(model.getstage(), R.drawable.skeleton_animation_00));
            }
        }
        else if(stage < 20){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.troll_1);
                items.add(new Log(model.getstage(), R.drawable.troll_1));
            }
            else{
                imageview5.setImageResource(R.drawable.archer);
                items.add(new Log(model.getstage(), R.drawable.archer));
            }
        }
        else if(stage < 30){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.troll_2);
                items.add(new Log(model.getstage(), R.drawable.troll_2));
            }
            else{
                imageview5.setImageResource(R.drawable.orc);
                items.add(new Log(model.getstage(), R.drawable.orc));
            }
        }
        else if(stage < 40){
            int n = random.nextInt(3);
            if(n == 0){
                imageview5.setImageResource(R.drawable.troll_3);
                items.add(new Log(model.getstage(), R.drawable.troll_3));
            }
            else if(n == 1){
                imageview5.setImageResource(R.drawable.orc_stone);
                items.add(new Log(model.getstage(), R.drawable.orc_stone));
            }
            else{
                imageview5.setImageResource(R.drawable.barbariant);
                items.add(new Log(model.getstage(), R.drawable.barbariant));
            }
        }
        else if(stage < 50){
            int n = random.nextInt(3);
            if(n == 0){
                imageview5.setImageResource(R.drawable.troll_4);
                items.add(new Log(model.getstage(), R.drawable.troll_4));
            }
            else if(n == 1){
                imageview5.setImageResource(R.drawable.orc_hig);
                items.add(new Log(model.getstage(), R.drawable.orc_hig));
            }
            else{
                imageview5.setImageResource(R.drawable.ranger_t);
                items.add(new Log(model.getstage(), R.drawable.ranger_t));
            }
        }
        else if(stage < 60){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.orc_zumbi);
                items.add(new Log(model.getstage(), R.drawable.orc_zumbi));
            }
            else{
                imageview5.setImageResource(R.drawable.archer_light);
                items.add(new Log(model.getstage(), R.drawable.archer_light));
            }
        }
        else if(stage < 70){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.orc_zumbi_2);
                items.add(new Log(model.getstage(), R.drawable.orc_zumbi_2));
            }
            else{
                imageview5.setImageResource(R.drawable.barbariandark);
                items.add(new Log(model.getstage(), R.drawable.barbariandark));
            }
        }
        else if(stage < 80){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.pisilohe12);
                items.add(new Log(model.getstage(), R.drawable.pisilohe12));
            }
            else{
                imageview5.setImageResource(R.drawable.archer_elf);
                items.add(new Log(model.getstage(), R.drawable.archer_elf));
            }
        }
        else if(stage < 90){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.pisilohe10);
                items.add(new Log(model.getstage(), R.drawable.pisilohe10));
            }
            else{
                imageview5.setImageResource(R.drawable.ranger_drow);
                items.add(new Log(model.getstage(), R.drawable.ranger_drow));
            }
        }
        else if(stage < 100){
            int n = random.nextInt(2);
            if(n == 0){
                imageview5.setImageResource(R.drawable.pisilohepunane3);
                items.add(new Log(model.getstage(), R.drawable.pisilohepunane3));
            }
            else{
                imageview5.setImageResource(R.drawable.archer_dark);
                items.add(new Log(model.getstage(), R.drawable.archer_dark));
            }
        }
        else{
            imageview5.setImageResource(R.drawable.barbarianlight);
            items.add(new Log(model.getstage(), R.drawable.barbarianlight));
        }
        adapter = new Adapter(items, getApplicationContext());
        listview.setAdapter(adapter);
        listview.setSelection(adapter.getCount() - 1);
    }

    public void MapSpawner(int stage){
        if(stage < 10){
            imageview8.setImageResource(R.drawable.backg1);
        }
        else if(stage < 20){
            imageview8.setImageResource(R.drawable.backg2);
        }
        else if(stage < 30){
            imageview8.setImageResource(R.drawable.backg3);
        }
        else if(stage < 40){
            imageview8.setImageResource(R.drawable.backg4);
        }
        else if(stage < 50){
            imageview8.setImageResource(R.drawable.backg5);
        }
        else if(stage < 60){
            imageview8.setImageResource(R.drawable.backg6);
        }
        else if(stage < 70){
            imageview8.setImageResource(R.drawable.backg7);
        }
        else if(stage < 80){
            imageview8.setImageResource(R.drawable.backg8);
        }
        else if(stage < 90){
            imageview8.setImageResource(R.drawable.backg9);
        }
        else if(stage < 100){
            imageview8.setImageResource(R.drawable.backg10);
        }
        else{
            imageview8.setImageResource(R.drawable.backg11);        }
    }
}