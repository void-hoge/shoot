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
        hitpoints = 100 + arma.hitpoints;
        entity_size = 50;
    }

    void display(){
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
    }

    void shoot(){
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

    void update_pos(){
        if (keyPressed == true){
            switch (key){
                case 'w':
                    pos.y+=3.0;
                    break;
                case 's':
                    pos.y-=3.0;
                    break;
                case 'a':
                    pos.x+=3.0;
                    break;
                case 'd':
                    pos.x-=3.0;
                    break;
            }
        }
        facing = atan2(mouseY-height/2, mouseX-width/2);
        main.shoot_ct--;
    }

    coordinate get_pos(){
        return pos;
    }
}
