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
    textAlign(CENTER, CENTER);
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
    scope(float mag){
        is_show = false;
        magnification = mag;
    }
    scope(float xx, float yy, float num){
        x = xx;
        y = yy;
        is_show = true;
        magnification = num;
    }
    public void display(){
        if (is_show){
            fill(color(0, 0, 255));
            textSize(30);
            text(PApplet.parseInt(magnification), x, y+10);
            strokeWeight(3);
            stroke(color(0, 0, 255));
            noFill();
            ellipse(x, y, 40, 40);
        }
    }
}

class armar extends item{
    int armar_level;
    float hitpoints;
    PImage img;
    armar(float xx, float yy, int level){
        x = xx;
        y = yy;
        armar_level = level;
        hitpoints = armar_level*25;
        is_show = true;
        switch(level){
            case 1:
                img = loadImage("ARMOR1.png");
                break;
            case 2:
                img = loadImage("ARMOR2.png");
                break;
            case 3:
                img = loadImage("ARMOR3.png");
                break;
            case 4:
                img = loadImage("ARMOR4.png");
                break;
        }
    }
    public float get_hitpoints(){
        return hitpoints;
    }
    public void display(){
        if (is_show){
            image(img, x, y, 100, 100);
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

final int HG = 0;
final int SMG = 1;
final int AR = 2;
final int SR = 3;

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
    core[] target_core;
    gun main;
    int type;

    npc(float xx, float yy){
        pos = new coordinate(xx, yy);
        main = new gun(HG);
        hitpoints = 100;
        entity_size = 50;
        facing = 0;
    }

    public void set_terget(player[] pl, core[] co){
        target = pl;
        target_core = co;
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
        if(target_core[0].hitpoints == 0){
            return;
        }
        if ((dist(target[0].pos.x, target[0].pos.y, pos.x, pos.y) <= main.range)&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            //display line
            translate(pos.x, pos.y);
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,9, main.range, 0);
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
        }else
        if ((dist(target_core[0].pos.x, target_core[0].pos.y, pos.x, pos.y) <= main.range)&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            //display line
            translate(pos.x, pos.y);
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,9, main.range, 0);
            rotate(-(facing+main.gap));
            translate(-pos.x, -pos.y);

            //culcurate hits
            coordinate end;
            end = new coordinate(cos(facing+main.gap),sin(facing+main.gap));
            end.x*=main.range;
            end.x+=pos.x;
            end.y*=main.range;
            end.y+=pos.y;
            if (main.range+target_core[0].entity_size > dist(pos.x, pos.y, target_core[0].pos.x, target_core[0].pos.y)){
                if(abs((pos.y-end.y)*target_core[0].pos.x-(pos.x-end.x)*target_core[0].pos.y+pos.x*end.y-pos.y*end.x)/sqrt((pos.y-end.y)*(pos.y-end.y)+(pos.x-end.x)*(pos.x-end.x)) < target_core[0].entity_size){
                    target_core[0].decrease_hitpoint(main.damage);
                }
            }
            main.shoot_ct = main.rate;
            main.set_gap();
            main.amo--;
        }
    }


    public void update_pos(){
        if ((dist(target[0].pos.x, target[0].pos.y, pos.x, pos.y) <= main.range)){
            //display line
            float facing_target= atan2(target[0].pos.y-pos.y, target[0].pos.x-pos.x);
            if(abs(facing_target-facing) > radians(180)){
                facing_target+=radians(360);
            }
            facing += (facing_target - facing)*(main.weight/5);
        }else
        if ((dist(target_core[0].pos.x, target_core[0].pos.y, pos.x, pos.y) <= main.range+target_core[0].entity_size)){
            //display line
            float facing_target= atan2(target_core[0].pos.y-pos.y, target_core[0].pos.x-pos.x);
            if(abs(facing_target-facing) > radians(180)){
                facing_target+=radians(360);
            }
            facing += (facing_target - facing)*(main.weight/5);
        }

        float direction = atan2(target_core[0].pos.y-pos.y, target_core[0].pos.x-pos.x);
        if(dist(target_core[0].pos.x, target_core[0].pos.y, pos.x, pos.y) > 500){
            pos.x += cos(direction);
            pos.y += sin(direction);
        }
    }

    public void display(){
        if (hitpoints > 0){
            update_pos();
            shoot();
            translate(pos.x, pos.y);
            rotate(facing);
            noStroke();
            fill(0);
            rect(entity_size/2+30*(main.type+1), 5, -30*(main.type+1), 8);
            rotate(-facing);
            translate(-pos.x, -pos.y);

            noStroke();
            fill(255, 0, 0);
            ellipse(pos.x, pos.y, entity_size, entity_size);
            textSize(20);
            text(PApplet.parseInt(hitpoints), pos.x, pos.y-40);

            main.shoot_ct--;
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

class core{
    coordinate pos;
    float entity_size = 400;
    float hitpoints;
    core(){
        hitpoints = 5000;
        pos = new coordinate(0, 0);
    }
    core(float hp){
        hitpoints = hp;
        pos = new coordinate(0, 0);
    }
    public boolean display(){
        if (hitpoints > 0){
            noStroke();
            int begin = color(255, 255, 0);
            int end = color(255, 0, 255);
            for(int i = 0; i < 10; i++){
                int c = lerpColor(begin, end, PApplet.parseFloat(i)/10);
                fill(c);
                ellipse(0,0,(10-i)*40,(10-i)*40);
            }

            stroke(0);
            strokeWeight(1);
            noFill();
            rect(-250, -250, 500, 30);
            noStroke();
            fill(255);
            rect(-250, -250, hitpoints/10, 30);
            fill(0);
            text(PApplet.parseInt(hitpoints)+"/"+5000, 0, -225);
            return true;
        }else{
            return false;
        }
    }
    public void decrease_hitpoint(float points){
        hitpoints -=points;
        if (hitpoints < 0){
            hitpoints = 0;
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
    gun[] gun_list;
    scope[] scope_list;
    armar[] armar_list;

    float total_damage;
    float total_suffered_damage;
    int kill_count;

    player(npc[] hoge, gun[] poyo, scope[] foo, armar[] bar){
        pos = new coordinate();
        arma = new armar(0,0,4);
        arma.is_show = false;
        main = new gun(AR);
        main.is_show = false;
        sc = new scope(2);
        sc.is_show = false;
        facing = 0;
        hitpoints = 100;
        entity_size = 50;
        enemy = hoge;
        gun_list = poyo;
        scope_list = foo;
        armar_list = bar;
        total_damage = 0;
        total_suffered_damage = 0;
    }

    public void display(){
        if (hitpoints <= 0){
            fill(color(255,0,0));
            textSize(100*sc.magnification);
            text("YOU DIED", pos.x, pos.y);
            textSize(60*sc.magnification);
            fill(0);
            text("damage: "+PApplet.parseInt(total_damage)+" kill: "+kill_count, pos.x, pos.y+100*sc.magnification);
            text("suffered damage: "+PApplet.parseInt(total_suffered_damage), pos.x, pos.y+200*sc.magnification);
            return;
        }
        shoot();
        translate(pos.x, pos.y);

            update_pos();
            rotate(facing);

                strokeWeight(3);
                stroke(255, 50);
                line(0, 9, main.range, 0);

                fill(0);
                rect(entity_size/2+30*(main.type+1), 5, -30*(main.type+1), 8);

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
        gun_pickup();
        scope_pickup();
        armar_pickup();
        main.shoot_ct--;
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
            translate(pos.x, pos.y);
            //display line
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,9, main.range, 0);
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

    public void gun_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < gun_list.length; i++){
                    if (50>dist(pos.x, pos.y, gun_list[i].x, gun_list[i].y)){
                        if(kouho.distance > dist(pos.x, pos.y, gun_list[i].x, gun_list[i].y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, gun_list[i].x, gun_list[i].y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    gun swap = main;
                    main = gun_list[kouho.num];
                    main.is_show = false;
                    gun_list[kouho.num] = swap;
                    gun_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    gun_list[kouho.num].x = pos.x + cos(kakudo)*100;
                    gun_list[kouho.num].y = pos.y + sin(kakudo)*100;
                }
            }
        }
    }

    public void scope_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < scope_list.length; i++){
                    if (50>dist(pos.x, pos.y, scope_list[i].x, scope_list[i].y)){
                        if(kouho.distance > dist(pos.x, pos.y, scope_list[i].x, scope_list[i].y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, scope_list[i].x, scope_list[i].y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    scope swap = sc;
                    sc = scope_list[kouho.num];
                    sc.is_show = false;
                    scope_list[kouho.num] = swap;
                    scope_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    scope_list[kouho.num].x = pos.x + cos(kakudo)*100;
                    scope_list[kouho.num].y = pos.y + sin(kakudo)*100;
                }
            }
        }
    }

    public void armar_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < armar_list.length; i++){
                    if (50>dist(pos.x, pos.y, armar_list[i].x, armar_list[i].y)){
                        if(kouho.distance > dist(pos.x, pos.y, armar_list[i].x, armar_list[i].y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, armar_list[i].x, armar_list[i].y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    armar swap = arma;
                    arma = armar_list[kouho.num];
                    arma.is_show = false;
                    armar_list[kouho.num] = swap;
                    armar_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    armar_list[kouho.num].x = pos.x + cos(kakudo)*100;
                    armar_list[kouho.num].y = pos.y + sin(kakudo)*100;
                }
            }
        }
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
    boolean ismenu;
    int difficulty;
    system(){
        ismenu = true;
        difficulty = 0;
        hoge = new world(EASY);
    }
    public void display(){
        if (!ismenu){
            hoge.display();
        }else{
            noStroke();
            int begin = color(0, 255, 255);
            int end = color(255, 0, 255);
            for (int i = 0; i < width; i+=5){
                int c = lerpColor(begin, end, PApplet.parseFloat(i)/width);
                fill(c);
                rect(i, 0, 5, height);
            }

            if(start_button()){
                ismenu = false;
            }
            // difficulty = difficulty_button(difficulty);
        }
    }
}

final String[] difficulty_name = {"BORING", "EASY", "NORMAL", "HARD", "INSANE"};

public boolean start_button(){
    float x = (width/2)-300;
    float y = 25;
    float w = 600;
    float h = 100;
    rectMode(CORNER);
    textSize(150);
    textAlign(CENTER, CENTER);
    noStroke();
    if ((mouseX > x)&&(mouseX < x+w)&&(mouseY > y)&&(mouseY < y+h)){
        if (mousePressed){
            fill(color(255, 0, 255));
            text("SHOOT", width/2, 50);
            return true;
        }
        fill(color(255, 0, 255));
        text("SHOOT", width/2, 50);
        return false;
    }else{
        fill(color(0, 255, 255));
        text("SHOOT", width/2, 50);
        return false;
    }
}

public int difficulty_button(){
    return 1;
}
final int TEST = -1;
final int BORING = 0;
final int EASY = 1;
final int NORMAL = 2;
final int HARD = 3;
final int INSANE = 4;

class world{
    player[] player_list;
    gun[] gun_list;
    npc[] npc_list;
    scope[] scope_list;
    armar[] armar_list;
    core[] core_list;
    boolean isgameover;
    int score;
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
        player pl = new player(npc_list, gun_list, scope_list, armar_list);
        player_list[0] = pl;
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list, core_list);
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
        scope_list = new scope[4];
        for (int i = 0; i < scope_list.length; i++){
            scope_list[i] = new scope(random(-300, 300), random(-300, 300), PApplet.parseFloat(i+1));
        }
        armar_list = new armar[4];
        for (int i = 0; i < armar_list.length; i++){
            armar_list[i] = new armar(random(-300, 300), random(-300, 300), (i%4)+1);
        }
        switch (difficulty){
            case TEST:
                npc_list = new npc[1];
                npc_list[0] = new npc(random(-1000,1000), random(-1000, 1000), EASY, HG);
                break;
            case BORING:
                npc_list = new npc[2];
                for (int i = 0; i < npc_list.length; i++){
                    npc_list[i] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                }
                break;
            case EASY:
                npc_list = new npc[3];
                npc_list[0] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                npc_list[1] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                npc_list[2] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
                break;
            case NORMAL:
                npc_list = new npc[5];
                npc_list[0] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                npc_list[1] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                npc_list[2] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
                npc_list[3] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
                npc_list[4] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, AR);
                break;
            case HARD:
                npc_list = new npc[5];
                npc_list[0] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, HG);
                npc_list[1] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
                npc_list[2] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SMG);
                npc_list[3] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, AR);
                npc_list[4] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SR);
                break;
            case INSANE:
                npc_list = new npc[5];
                npc_list[0] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, AR);
                npc_list[1] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, AR);
                npc_list[2] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, AR);
                npc_list[3] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SR);
                npc_list[4] = new npc(random(-1000,1000), random(-1000, 1000), difficulty, SR);
                break;
        }

        core_list = new core[1];
        core_list[0] = new core();
        // player pl = new player(npc_list, gun_list, scope_list, armar_list);
        // player_list[0] = pl;
        player_list = new player[1];
        player_list[0] = new player(npc_list, gun_list, scope_list, armar_list);
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list, core_list);
        }
    }
    public void display(){
        translate(width/2, height/2);
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
        for (int i = 0; i < scope_list.length; i++){
            scope_list[i].display();
        }
        for (int i = 0; i < armar_list.length; i++){
            armar_list[i].display();
        }
        if(!core_list[0].display()){
            isgameover = true;
            textSize(100*player_list[0].sc.magnification);
            fill(255, 0, 0);
            text("Game Over", 0, 0);

            textSize(60*player_list[0].sc.magnification);
            fill(0);
            text("Score: "+score, 0, 100*player_list[0].sc.magnification);
        }
        if(isgameover == false){
            score++;
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
