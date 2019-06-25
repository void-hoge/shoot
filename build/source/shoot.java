import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class shoot extends PApplet {

system sys;

public void setup(){
    
    sys = new system();
    frameRate(60);
}

public void draw(){
    translate(width/2,height/2);
    textAlign(CENTER);
    sys.display();
}
abstract class item{
    float x, y;
    boolean is_show;
    public abstract void display();
}

// final int NONE = 0;
// final int SCOPE = 1;
// final int ARMAR = 2;
// final int GUN = 3;

class scope extends item{
    float magnification;
    scope(){
        is_show = true;
        magnification = 1;
    }
    scope(float num){
        is_show = true;
        magnification = num;
    }
    public void display(){
        if (is_show){

        }
    }
}

class amo extends item{
    int amo;
    amo(){
        is_show = false;
        amo = 100;
    }
    public void display(){}
}

class heal extends item{
    float hitpoints;
    heal(){
        is_show = false;
        hitpoints = 50;
    }
    public void display(){}
}

class charge extends item{
    float hitpoints;
    charge(){
        is_show = false;
        hitpoints = 50;
    }
    public void display(){}
}

class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
        is_show = true;
    }
    public float get_hitpoints(){
        return hitpoints;
    }
    public void display(){
        if (is_show){
            text(armar_level, x, y);
        }
    }
    public void showHP(){
        if (armar_level != 0){
            stroke(0);
            strokeWeight(1);
            noFill();
            rect(-width/2+250, height/2-150, -armar_level*25*2, 30);
            switch (armar_level){
                case 1:
                    //white
                    fill(255);
                    break;
                case 2:
                    //blue
                    fill(0xff87CEFA);
                    break;
                case 3:
                    //purple
                    fill(0xff9370DB);
                    break;
                case 4:
                    //yellow (gold)
                    fill(0xffFFFF00);
                    break;
            }
            noStroke();

            rect(-width/2+250, height/2-150, -hitpoints*2, 30);
        }
    }
}

final int SMG = 0;
final int AR = 1;
final int SR = 2;
final int HG = 3;

class gun extends item{
    int type;
    float damage;
    float range;
    int amo;
    int mag_size;       //未実装
    int rate;
    int shoot_ct;//cool time
    float dispersion;
    float gap;
    float weight;
    float mobility;
    PImage img;
    gun(int t){
        type = t;
        is_show = true;
        switch (type){
            case SMG:
                damage = 10;
                amo = 200;
                rate = 4;           //900rpm
                range = 700;
                dispersion = 0.2f;
                weight = 0.15f;
                mobility = 1.5f;
                img = loadImage("SMG.png");
                break;
            case AR:
                damage = 20;
                amo = 100;
                rate = 6;           //600rpm
                range = 1000;
                dispersion = 0.05f;
                weight = 0.08f;
                mobility = 1;
                img = loadImage("AR.png");
                break;
            case SR:
                damage = 90;
                amo = 20;
                rate = 120;         //30rpm
                range = 2000;
                dispersion = 0.01f;
                weight = 0.03f;
                mobility = 0.75f;
                img = loadImage("SR.png");
                break;
            case HG:
                damage = 15;
                amo = 100;
                rate = 20;          //180rpm
                range = 400;
                dispersion = 0.1f;
                weight = 0.4f;
                mobility = 2;
                img = loadImage("HG.png");
                break;
        }
        shoot_ct = 0;
        set_gap();
        x = 0;
        y = 0;
    }
    public void set_gap(){
        gap = random(-dispersion, dispersion);
    }
    public void display(){
        if (is_show){
            imageMode(CENTER);
            switch (type){
                case SMG:
                    image(img, x, y, 200, 100);
                    break;
                case AR:
                    image(img, x, y, 200, 100);
                    break;
                case SR:
                    image(img, x, y, 200, 100);
                    break;
                case HG:
                    image(img, x, y, 200, 100);
                    break;
            }
        }
    }
}
class npc{
    coordinate pos;
    float hitpoints;
    float entity_size;
    float facing;
    float damage_rate;
    int heal_possibility;
    player[] target;
    gun main;
    int type;

    npc(float xx, float yy){
        pos = new coordinate(xx, yy);
        main = new gun(HG);
        hitpoints = 100;
        entity_size = 50;
        facing = 0;
    }

    public void set_terget(player[] pl){
        target = pl;
    }

    npc(){
        pos = new coordinate(0, 0);
        hitpoints = 100;
        entity_size = 50;
        facing = 0;
    }

    npc(float xx, float yy, int difficulty, int gun_type){
        type = gun_type;
        main = new gun(gun_type);
        pos = new coordinate(xx,yy);
        hitpoints = 100;
        entity_size = 50;
        facing = 0;
        switch (difficulty){
            case BORING:
                damage_rate = 0.25f;
                heal_possibility = 100;
                break;
            case EASY:
                damage_rate = 0.5f;
                heal_possibility = 80;
                break;
            case NORMAL:
                damage_rate = 0.75f;
                heal_possibility = 60;
                break;
            case HARD:
                damage_rate = 1;
                heal_possibility = 40;
                break;
            case INSANE:
                damage_rate = 1;
                heal_possibility = 20;
                break;
        }
    }

    public void shoot(){
        if(target[0].hitpoints == 0){
            return;
        }
        if ((dist(target[0].pos.x, target[0].pos.y, pos.x, pos.y) <= main.range)&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            //display line
            float facing_target= atan2(target[0].pos.y-pos.y, target[0].pos.x-pos.x);
            if(abs(facing_target-facing) > radians(180)){
                facing_target+=radians(360);
            }
            facing += (facing_target - facing)*main.weight;

            translate(pos.x, pos.y);
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,0, main.range, 0);
            rotate(-(facing+main.gap));
            translate(-pos.x, -pos.y);

            //culcurate hits
            coordinate end;
            end = new coordinate(cos(facing+main.gap),sin(facing+main.gap));
            end.x*=main.range;
            end.x+=pos.x;
            end.y*=main.range;
            end.y+=pos.y;
            if (main.range+target[0].entity_size > dist(pos.x, pos.y, target[0].pos.x, target[0].pos.y)){
                if(abs((pos.y-end.y)*target[0].pos.x-(pos.x-end.x)*target[0].pos.y+pos.x*end.y-pos.y*end.x)/sqrt((pos.y-end.y)*(pos.y-end.y)+(pos.x-end.x)*(pos.x-end.x)) < target[0].entity_size){
                    target[0].decrease_hitpoint(main.damage);
                }
            }
            main.shoot_ct = main.rate;
            main.set_gap();
            main.amo--;
        }
        main.shoot_ct--;
    }

    public void display(){
        if (hitpoints > 0){
            noStroke();
            fill(255, 0, 0);
            ellipse(pos.x, pos.y, entity_size, entity_size);
            textSize(20);
            text(hitpoints, pos.x, pos.y);
            shoot();
        }else{
            target[0].kill_count++;
            switch (target[0].main.type){
                case SMG:
                    target[0].main.amo += 50;
                    break;
                case AR:
                    target[0].main.amo += 30;
                    break;
                case SR:
                    target[0].main.amo += 10;
                    break;
                case HG:
                    target[0].main.amo += 20;
                    break;
            }
            target[0].increase_hitpoints(20);
            if(PApplet.parseInt(random(100))<=heal_possibility){
                target[0].increase_armar_hitpoints(10);
            }
            respwan();
        }
    }

    public void respwan(){
        pos.x = random(-1000,1000);
        pos.y = random(-1000,1000);
        hitpoints = 100;
        main = new gun(type);
    }
}
class object{
    String shape;
    coordinate point1, point2, point3;
    float radius;
    float w,h;

    object(coordinate a_point, float a_radius){     //ellipse
        shape = "ellipse";
        point1 = a_point;
        radius = a_radius;
    }

    object(coordinate a_point, coordinate b_point, coordinate c_point){     //triangle
        shape = "triangle";
        point1 = a_point;
        point2 = b_point;
        point3 = c_point;
    }

    object(coordinate a_point, float a_w, float b_h){       //rectangle
        shape = "rect";
        point1 = a_point;
        w = a_w;
        h = b_h;
    }

    public void display(){
        switch (shape){
            case "ellipse":
                ellipse(point1.x, point1.y, radius, radius);
                break;
            case "triangle":
                triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
                break;
            case "rect":
                rect(point1.x, point1.y, w, h);
                break;
        }
    }
}
class coordinate{
    float x, y;
    coordinate(){
        x = 0;
        y = 0;
    }
    coordinate(float ax, float ay){
        x = ax;
        y = ay;
    }
}

class num_and_dist{
    int num;
    float distance;
    num_and_dist(){
        num = 0;
        distance = 0;
    }
    num_and_dist(int nnum, float ddistance){
        num = nnum;
        distance = ddistance;
    }
}

class player{
    coordinate pos;
    float facing;
    float hitpoints;
    int entity_size;    // radius

    armar arma;
    gun main;
    scope sc;

    npc[] enemy;
    gun[] item_list;

    float total_damage;
    float total_suffered_damage;
    int kill_count;

    player(npc[] hoge, gun[] poyo){
        pos = new coordinate();
        arma = new armar(4);
        arma.is_show = false;
        main = new gun(AR);
        main.is_show = false;
        sc = new scope(2);
        sc.is_show = false;
        facing = 0;
        hitpoints = 100;
        entity_size = 50;
        enemy = hoge;
        item_list = poyo;
        total_damage = 0;
        total_suffered_damage = 0;
    }

    public void display(){
        if (hitpoints == 0){
            textSize(100);
            text("YOU DIED", pos.x, pos.y);
            textSize(60);
            fill(0);
            text("damage: "+total_damage+" kill: "+kill_count, pos.x, pos.y+200);
            text("suffered damage: "+total_suffered_damage, pos.x, pos.y+400);
            return;
        }
        main.display();
        update_pos();
        shoot();
        translate(pos.x, pos.y);
        rotate(facing);
        strokeWeight(3);
        stroke(255, 50);
        line(0, 0, main.range, 0);
        noStroke();
        fill(255);
        ellipse(0, 0, entity_size, entity_size);
        fill(0);
        ellipse(entity_size/2, -15, 10, 10);
        ellipse(entity_size/2, 15, 10, 10);
        rotate(-facing);

        scale(sc.magnification);
        showHP();
        showAMO();
        translate(-pos.x, -pos.y);
        pickup();
    }

    public void showHP(){
        arma.showHP();
        strokeWeight(1);
        stroke(0);
        noFill();
        rect(-width/2+250, height/2-100, -100*2, 30);
        noStroke();
        fill(255);
        rect(-width/2+250, height/2-100, -hitpoints*2, 30);
    }

    public void showAMO(){
        fill(0);
        textSize(40);
        text(main.amo, width/2-100, height/2-100);
    }

    public void shoot(){
        if (mousePressed&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            //display line
            translate(pos.x, pos.y);
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,0, main.range, 0);
            rotate(-(facing+main.gap));
            translate(-pos.x, -pos.y);

            //culcurate hits
            coordinate end;
            end = new coordinate(cos(facing+main.gap),sin(facing+main.gap));//unit vector
            end.x*=main.range;
            end.x+=pos.x;
            end.y*=main.range;
            end.y+=pos.y;
            for (int i = 0; i < enemy.length; i++){
                if (main.range+enemy[i].entity_size > dist(pos.x, pos.y, enemy[i].pos.x, enemy[i].pos.y)){
                    if(abs((pos.y-end.y)*enemy[i].pos.x-(pos.x-end.x)*enemy[i].pos.y+pos.x*end.y-pos.y*end.x)/sqrt((pos.y-end.y)*(pos.y-end.y)+(pos.x-end.x)*(pos.x-end.x)) < enemy[i].entity_size){
                        enemy[i].hitpoints -= main.damage;
                        total_damage += main.damage;
                    }
                }
            }
            main.shoot_ct = main.rate;
            main.set_gap();
            main.amo--;
        }
    }

    public void update_pos(){
        if (keyPressed == true){
            switch (key){
                case 'w':
                    pos.y-=3.0f*main.mobility;
                    break;
                case 's':
                    pos.y+=3.0f*main.mobility;
                    break;
                case 'a':
                    pos.x-=3.0f*main.mobility;
                    break;
                case 'd':
                    pos.x+=3.0f*main.mobility;
                    break;
            }
        }
        float facing_target = atan2(mouseY-height/2, mouseX-width/2);
        if(abs(facing_target-facing) > radians(180)){
            facing_target+=radians(360);
        }
        facing += (facing_target - facing)*main.weight;
    }

    public void pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < item_list.length; i++){
                    if (50>dist(pos.x, pos.y, item_list[i].x, item_list[i].y)){
                        if(kouho.distance > dist(pos.x, pos.y, item_list[i].x, item_list[i].y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, item_list[i].x, item_list[i].y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    gun sw = main;
                    main = item_list[kouho.num];
                    main.is_show = false;
                    item_list[kouho.num] = sw;
                    item_list[kouho.num].is_show = true;
                    if ((PApplet.parseInt(random(10000))&1) == 0){
                        item_list[kouho.num].x = pos.x +100 + random(-20,20);
                    }else{
                        item_list[kouho.num].x = pos.x -100 + random(-20,20);
                    }
                    if ((PApplet.parseInt(random(10000))&1) == 0){
                        item_list[kouho.num].y = pos.y +100 + random(-20,20);
                    }else{
                        item_list[kouho.num].y = pos.x -100 + random(-20,20);
                    }
                }
            }
        }
        main.shoot_ct--;
    }

    public coordinate get_pos(){
        return pos;
    }

    public void decrease_hitpoint(float points){
        total_suffered_damage += points;
        if (arma.hitpoints > 0){
            arma.hitpoints -= points;
            if (arma.hitpoints < 0){
                points = -arma.hitpoints;
                arma.hitpoints = 0;
                hitpoints -= points;
            }
        }else{
            hitpoints -= points;
        }
        if (hitpoints < 0){
            total_suffered_damage += hitpoints;
            hitpoints = 0;
        }
    }

    public void increase_hitpoints(float points){
        hitpoints += points;
        if (hitpoints > 100){
            hitpoints = 100;
        }
    }

    public void increase_armar_hitpoints(float points){
        arma.hitpoints += points;
        if (arma.hitpoints > 25*arma.armar_level){
            arma.hitpoints = 25*arma.armar_level;
        }
    }
}
class system{
    world hoge;
    system(){
        hoge = new world(HARD);
    }
    public void display(){
        hoge.display();
    }
}
final int BORING = 0;
final int EASY = 1;
final int NORMAL = 2;
final int HARD = 3;
final int INSANE = 4;

class world{
    player[] player_list;
    gun[] gun_list;
    npc[] npc_list;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        gun_list = new gun[10];
        for (int i = 0; i < gun_list.length; i++) {
            gun_list[i] = new gun(i%4);
            gun_list[i].x = random(-world_width/2, world_width/2);
            gun_list[i].y = random(-world_width/2, world_height/2);
        }
        npc_list = new npc[5];
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i] = new npc(random(-1000,1000), random(-1000,1000));
        }
        player_list = new player[1];
        player pl = new player(npc_list, gun_list);
        player_list[0] = pl;
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list);
        }
    }
    world(int difficulty){
        world_width = 10000;
        world_height = 10000;
        gun_list = new gun[10];
        for (int i = 0; i < gun_list.length; i++){
            gun_list[i] = new gun(i%4);
            gun_list[i].x = random(-world_width/2, world_height/2);
            gun_list[i].y = random(-world_width/2, world_height/2);
        }
        switch (difficulty){
            case BORING:
                npc_list = new npc[2];
                break;
            case EASY:
                npc_list = new npc[3];
                break;
            case NORMAL:
                npc_list = new npc[5];
                break;
            case HARD:
                npc_list = new npc[5];
                break;
            case INSANE:
                npc_list = new npc[5];
                break;
        }
        player_list = new player[1];
        player pl = new player(npc_list, gun_list);
        player_list[0] = pl;
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
            npc_list[i].set_terget(player_list);
        }
    }
    public void display(){
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);
        scale(1/player_list[0].sc.magnification);

        translate(-player_list[0].get_pos().x, -player_list[0].get_pos().y);
        line(world_width/2, 0, -world_width/2, 0);
        for (float i = 0; i < world_width/2; i+=100){
            line(i, 25, i, -25);
            line(-i, 25, -i, -25);
        }
        line(0, world_height/2, 0, -world_height/2);
        for (float i = 0; i < world_height/2; i+=100){
            line(25, i, -25, i);
            line(25, -i, -25, -i);
        }
        for (int i = 0; i < gun_list.length; i++) {
            gun_list[i].display();
        }
        for (int i = 0; i < npc_list.length; i++) {
            npc_list[i].display();
        }
        player_list[0].display();
    }
}
  public void settings() {  size(1440, 810); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "shoot" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
