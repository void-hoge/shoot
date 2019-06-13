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
}

public void draw(){
    translate(width/2,height/2);
    sys.display();
}
class item{
    float x, y;
}

class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
    }
    public float get_hitpoints(){
        return hitpoints;
    }
}

class gun extends item{
    float damage;
    float penetration;
    float range;
    int amo;
    float rate;   //cool time(frame)
    int shootcount;
    gun(){
        damage = 50;
        penetration = 0.5f;
        range = 500;
        amo = 100;
        rate = 20;
        shootcount = 0;
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
    gun gun2;

    player(){
        pos = new coordinate();
        arma = new armar(4);
        main = new gun();
        facing = 0;
        hitpoints = 100 + arma.hitpoints;
        entity_size = 50;
    }

    public void display(){
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
    }

    public void shoot(){
        if (mousePressed&&((main.shootcount % main.rate) == 0)){
            translate(0, 0);
            PVector mouse = new PVector(mouseX-width/2-pos.x, mouseY-height/2-pos.y);
            stroke(255, 212, 0);
            strokeWeight(4);
            line(-pos.x, -pos.y, cos(facing)*main.range-pos.x, sin(facing)*main.range-pos.y);
        }
        main.shootcount++;
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
        facing = atan2(mouseY-height/2, mouseX-width/2);
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
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
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
