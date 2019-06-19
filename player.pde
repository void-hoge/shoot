class coordinate{
    double x, y;
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
    // int moving_vec;     //0b(up)(down)(left)(right)

    armar arma;
    gun main;
    scope sc;

    player(){
        pos = new coordinate();
        arma = new armar(3);
        main = new gun(AR);
        sc = new scope(2);
        facing = 0;
        hitpoints = 100;
        hitpoints = 50;
        entity_size = 50;
        moving_vec = 0;
    }

    void display(){
        main.display();
        update_pos();
        shoot();
        translate(-pos.x, -pos.y);
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
    }

    void showHP(){
        strokeWeight(1);
        stroke(0);
        noFill();
        rect(-width/2+250, height/2-100, -100*2, 30);
        noStroke();
        fill(255);
        rect(-width/2+250, height/2-100, -hitpoints*2, 30);
    }

    void showAMO(){
        fill(0);
        textSize(40);
        text(main.amo, width/2-100, height/2-100);
    }

    void shoot(){
        if (mousePressed&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            translate(-pos.x, -pos.y);
            rotate(facing+main.gap);
            strokeWeight(4);
            stroke(255, 212, 0);
            line(0,0, main.range, 0);
            rotate(-(facing+main.gap));
            translate(pos.x, pos.y);
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
        if(abs(facing_target-facing) > radians(180)){
            facing_target+=radians(360);
        }
        facing += (facing_target - facing)*main.weight;
        main.shoot_ct--;
    }

    coordinate get_pos(){
        return pos;
    }
}
