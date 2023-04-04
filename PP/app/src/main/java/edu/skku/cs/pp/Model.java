package edu.skku.cs.pp;

public class Model {
    private String username;
    private int stage;
    private int MIN;
    private int MAX;
    private int HP;
    private int MAXHP;
    private int potion;
    private int score;
    private int enemy;
    private int enemyHP;
    private int enemyMAXHP;

    public Model(String username){
        this.username = username;
        this.stage = 0;
        this.MIN = 0;
        this.MAX = 3;
        this.HP = 10;
        this.MAXHP = 10;
        this.potion = 3;
        this.score = 0;
        this.enemyHP = 3;
        this.enemyMAXHP = 3;
        this.enemy = 1;
    }

    public String getusername(){
        return this.username;
    }

    public void setusername(String name){
        this.username = name;
    }

    public int getstage(){
        return this.stage;
    }

    public void nextstage() {
        this.stage += 1;
    }

    public int getMIN(){
        return this.MIN;
    }

    public void setMIN(int min) {
        this.MIN = min;
        if(this.MIN > this.MAX){
            setMAX(this.MIN);
        }
    }

    public int getMAX(){
        return this.MAX;
    }

    public void setMAX(int max) {
        this.MAX = max;
    }

    public int getHP(){
        return this.HP;
    }

    public void setHP(int hp) {
        this.HP = hp;
    }

    public int getMAXHP(){
        return this.MAXHP;
    }

    public void setMAXHP(int hp) {
        this.MAXHP = hp;
    }

    public int getpotion(){
        return this.potion;
    }

    public void setpotion(int potion) {
        this.potion = potion;
    }

    public int getscore(){
        return this.score;
    }

    public void setscore(int score) {
        this.score = score;
    }

    public int getEnemyHP(){
        return this.enemyHP;
    }

    public void setEnemyHP(int enemyHP) {
        this.enemyHP = enemyHP;
    }

    public int getEnemyMAXHP(){
        return this.enemyMAXHP;
    }

    public void setEnemyMAXHP(int enemyMAXHP) {
        this.enemyMAXHP = enemyMAXHP;
    }

    public int getEnemy(){
        return this.enemy;
    }

    public void setEnemy(int enemy) {
        this.enemy = enemy;
    }
}
