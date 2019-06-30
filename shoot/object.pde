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

    void display(){
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
    boolean display(){
        if (hitpoints > 0){
            noStroke();
            color begin = color(255, 255, 0);
            color end = color(255, 0, 255);
            for(int i = 0; i < 10; i++){
                color c = lerpColor(begin, end, float(i)/10);
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
            text(int(hitpoints)+"/"+5000, 0, -225);
            return true;
        }else{
            return false;
        }
    }
    void decrease_hitpoint(float points){
        hitpoints -=points;
        if (hitpoints < 0){
            hitpoints = 0;
        }
    }
}
