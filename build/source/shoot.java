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
abstract public class item{
    float x, y;
    public abstract void display();
}

public class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
    }
    public float get_hitpoints(){
        return hitpoints;
    }
    public void display(){
        text(armar_level, x, y);
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
    gun(int t){
        type = t;
        switch (type){
            case SMG:
                damage = 10;
                amo = 200;
                rate = 4;           //900rpm
                range = 400;
                dispersion = 0.2f;
                weight = 0.1f;
                break;
            case AR:
                damage = 20;
                amo = 100;
                rate = 6;           //600rpm
                range = 700;
                dispersion = 0.05f;
                weight = 0.06f;
                break;
            case SR:
                damage = 90;
                amo = 20;
                rate = 120;         //30rpm
                range = 2000;
                dispersion = 0.01f;
                weight = 0.03f;
                break;
            case HG:
                damage = 15;
                amo = 100;
                rate = 20;          //180rpm
                range = 400;
                dispersion = 0.1f;
                weight = 0.4f;
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
        text(type, x, y);
        switch (type){
            case SMG:
                text("SMG", x, y);
                break;
            case AR:
                text("AR", x, y);
                break;
            case SR:
                text("SR", x, y);
                break;
            case HG:
                text("HG", x, y);
                break;
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

class player{
    coordinate pos;
    float facing;
    float hitpoints;
    int entity_size;    // radius

    armar arma;
    gun main;

    player(){
        pos = new coordinate();
        arma = new armar(4);
        main = new gun(SMG);
        facing = 0;
        hitpoints = 100;
        // hitpoints = 50;
        entity_size = 50;
    }

    public void display(){
        main.display();
        update_pos();
        shoot();

        noStroke();
        fill(255);
        translate(-pos.x, -pos.y);
        rotate(facing);
        ellipse(0, 0, entity_size, entity_size);
        fill(0);
        ellipse(entity_size/2, -15, 10, 10);
        ellipse(entity_size/2, 15, 10, 10);
        rotate(-facing);


        stroke(0);
        noFill();
        rect(50, -50, -100, 10);
        noStroke();
        fill(255);
        rect(50, -50, -hitpoints, 10);

        // stroke(0);
        // noFill();
        // rect(50, -60, -100, 10);
        // noStroke();
        // rect(50, -60, -(hitpoints-100),10);
        if (arma.armar_level != 0){
            stroke(0);
            noFill();
            rect(-width/2+250, height/2-150, -arma.armar_level*25*2, 30);
            switch (arma.armar_level){
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

            rect(-width/2+250, height/2-150, -arma.hitpoints*2, 30);
        }
    }

    public void shoot(){
        if (mousePressed&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            translate(0, 0);
            PVector mouse = new PVector(mouseX-width/2-pos.x, mouseY-height/2-pos.y);
            stroke(255, 212, 0);
            strokeWeight(4);
            line(-pos.x, -pos.y, cos(facing+main.gap)*main.range-pos.x, sin(facing+main.gap)*main.range-pos.y);
            main.shoot_ct = main.rate;
            main.set_gap();
            main.amo--;
        }
    }

    public void update_pos(){
        if (keyPressed == true){
            switch (key){
                case 'w':
                    pos.y+=3.0f;
                    break;
                case 's':
                    pos.y-=3.0f;
                    break;
                case 'a':
                    pos.x+=3.0f;
                    break;
                case 'd':
                    pos.x-=3.0f;
                    break;
            }
        }
        float facing_target = atan2(mouseY-height/2, mouseX-width/2);
        facing += (facing_target - facing)*main.weight;
        main.shoot_ct--;
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
    item[] hoge;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        hoge = new item[1000];
        for (int i = 0; i < hoge.length; i++) {
            hoge[i] = new gun(i%4);
            hoge[i].x = random(-world_width/2, world_width/2);
            hoge[i].y = random(-world_width/2, world_height/2);
        }
        pl = new player();
    }
    public void display(){
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);

        translate(pl.get_pos().x, pl.get_pos().y);
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
        pl.display();
        textSize(15);
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
