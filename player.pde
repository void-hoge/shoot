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

    void display(){
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

    void update_pos(){
        if (keyPressed == true){
            switch (key){
                case 'w':
                    pos.y-=3.0;
                    break;
                case 's':
                    pos.y+=3.0;
                    break;
                case 'a':
                    pos.x-=3.0;
                    break;
                case 'd':
                    pos.x+=3.0;
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

    void pickup(){
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
                    if ((int(random(10000))&1) == 0){
                        item_list[kouho.num].x = pos.x +100 + random(-20,20);
                    }else{
                        item_list[kouho.num].x = pos.x -100 + random(-20,20);
                    }
                    if ((int(random(10000))&1) == 0){
                        item_list[kouho.num].y = pos.y +100 + random(-20,20);
                    }else{
                        item_list[kouho.num].y = pos.x -100 + random(-20,20);
                    }
                }
            }
        }
    }

    coordinate get_pos(){
        return pos;
    }
}
