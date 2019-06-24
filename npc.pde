class npc{
    coordinate pos;
    float hitpoints;
    float entity_size;
    float facing;
    player[] target;
    gun main;

    npc(float xx, float yy){
        pos = new coordinate(xx, yy);
        pos.x = xx;
        pos.y = yy;
        main = new gun(HG);
        hitpoints = 100;
        entity_size = 50;
        facing = 0;
    }

    void set_terget(player[] pl){
        target = pl;
    }

    npc(){
        pos.x = 0;
        pos.y = 0;
        hitpoints = 100;
        entity_size = 30;
        facing = 0;
    }

    void shoot(){
        if(target[0].hitpoints == 0){
            return;
        }
        if ((dist(target[0].pos.x, target[0].pos.y, pos.x, pos.y) <= main.range)&&(main.shoot_ct <= 0)&&(main.amo > 0)){
            //display line
            facing= atan2(target[0].pos.y-pos.y, target[0].pos.x-pos.x);
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

    void display(){
        if (hitpoints > 0){
            noStroke();
            fill(255, 0, 0);
            ellipse(pos.x, pos.y, entity_size, entity_size);
            textSize(20);
            text(hitpoints, pos.x, pos.y);
            shoot();
        }
    }
}
