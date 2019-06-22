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
    PImage img;
    gun(int t){
        type = t;
        is_show = true;
        switch (type){
            case SMG:
                damage = 10;
                amo = 200;
                rate = 4;           //900rpm
                range = 400;
                dispersion = 0.2f;
                weight = 0.15f;
                img = loadImage("SMG.png");
                break;
            case AR:
                damage = 20;
                amo = 100;
                rate = 6;           //600rpm
                range = 700;
                dispersion = 0.05f;
                weight = 0.08f;
                img = loadImage("AR.png");
                break;
            case SR:
                damage = 90;
                amo = 20;
                rate = 120;         //30rpm
                range = 2000;
                dispersion = 0.01f;
                weight = 0.03f;
                img = loadImage("SR.png");
                break;
            case HG:
                damage = 15;
                amo = 100;
                rate = 20;          //180rpm
                range = 400;
                dispersion = 0.1f;
                weight = 0.4f;
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
    float x, y;
    float hitpoints;
    float entity_size;
    npc(float xx, float yy){
        x = xx;
        y = yy;
        hitpoints = 100;
        entity_size = 30;
    }

    npc(){
        x = 0;
        y = 0;
        hitpoints = 100;
        entity_size = 30;
    }

    public void display(){
        if (hitpoints > 0){
            noStroke();
            fill(255, 0, 0);
            ellipse(x, y, entity_size, entity_size);
            textSize(20);
            text(hitpoints, x, y);
        }
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
    // int moving_vec;     //0b(up)(down)(left)(right)

    private armar arma;
    private gun main;
    private scope sc;

    npc[] enemy;
    gun[] item_list;

    player(npc[] hoge, gun[] poyo){
        pos = new coordinate();
        arma = new armar(3);
        arma.is_show = false;
        main = new gun(AR);
        main.is_show = false;
        sc = new scope(2);
        sc.is_show = false;
        facing = 0;
        hitpoints = 100;
        hitpoints = 50;
        entity_size = 50;
        enemy = hoge;
        item_list = poyo;
    }

    public void display(){
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
        arma.showHP();
        showAMO();
        translate(-pos.x, -pos.y);
        pickup();
    }

    public void showHP(){
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
                if (main.range+enemy[i].entity_size > dist(pos.x, pos.y, enemy[i].x, enemy[i].y)){
                    if(abs((pos.y-end.y)*enemy[i].x-(pos.x-end.x)*enemy[i].y+pos.x*end.y-pos.y*end.x)/sqrt((pos.y-end.y)*(pos.y-end.y)+(pos.x-end.x)*(pos.x-end.x)) < enemy[i].entity_size){
                        enemy[i].hitpoints -= main.damage;
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
                    pos.y-=3.0f;
                    break;
                case 's':
                    pos.y+=3.0f;
                    break;
                case 'a':
                    pos.x-=3.0f;
                    break;
                case 'd':
                    pos.x+=3.0f;
                    break;
            }
        }
        float facing_target = atan2(mouseY-height/2, mouseX-width/2);
        if(abs(facing_target-facing) > radians(180)){
            facing_target+=radians(360);
        }
        facing += (facing_target - facing)*main.weight;
        main.shoot_ct--;
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
    }

    public coordinate get_pos(){
        return pos;
    }
}
class system{
    world hoge;
    system(){
        hoge = new world();
    }
    public void display(){
        hoge.display();
    }
}
class world{
    player pl;
    gun[] hoge;
    npc[] foo;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        hoge = new gun[100];
        for (int i = 0; i < hoge.length; i++) {
            hoge[i] = new gun(i%4);
            hoge[i].x = random(-world_width/2, world_width/2);
            hoge[i].y = random(-world_width/2, world_height/2);
        }
        foo = new npc[10];
        for (int i = 0; i < foo.length; i++){
            foo[i] = new npc(random(-1000,1000), random(-1000,1000));
        }
        pl = new player(foo, hoge);
    }
    public void display(){
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);
        scale(1/pl.sc.magnification);

        translate(-pl.get_pos().x, -pl.get_pos().y);
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
        for (int i = 0; i < hoge.length; i++) {
            hoge[i].display();
        }
        for (int i = 0; i < foo.length; i++) {
            foo[i].display();
        }
        pl.display();
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
