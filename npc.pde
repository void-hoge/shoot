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

    void set_terget(player[] pl, core[] co){
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
                damage_rate = 0.25;
                heal_possibility = 100;
                break;
            case EASY:
                damage_rate = 0.5;
                heal_possibility = 80;
                break;
            case NORMAL:
                damage_rate = 0.75;
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

    void shoot(){
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


    void update_pos(){
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

    void display(){
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
            text(int(hitpoints), pos.x, pos.y-40);

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
            if(int(random(100))<=heal_possibility){
                target[0].increase_armar_hitpoints(10);
            }
            respwan();
        }
    }

    void respwan(){
        pos.random_on_arc(1500);
        hitpoints = 100;
        main = new gun(type);
    }
}
