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
                    fill(#87CEFA);
                    break;
                case 3:
                    //purple
                    fill(#9370DB);
                    break;
                case 4:
                    //yellow (gold)
                    fill(#FFFF00);
                    break;
            }
            noStroke();

            rect(-width/2+250, height/2-150, -arma.hitpoints*2, 30);
        }
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
        float facing_target = atan2(mouseY-height/2, mouseX-width/2);
        facing += (facing_target - facing)*main.weight;
        main.shoot_ct--;
    }

    coordinate get_pos(){
        return pos;
    }
}
