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
    void random_on_arc(float r){
        float kakudo = radians(random(0,360));
        x = cos(kakudo)*r;
        y = sin(kakudo)*r;
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

    armar arma;
    gun main;
    scope sc;

    npc[] enemy;
    gun[] gun_list;
    scope[] scope_list;
    armar[] armar_list;

    float total_damage;
    float total_suffered_damage;
    int kill_count;

    player(npc[] hoge, gun[] poyo, scope[] foo, armar[] bar){
        pos = new coordinate();
        arma = new armar(0, 0, 4);
        arma.is_show = false;
        main = new gun(AR);
        main.is_show = false;
        sc = new scope(2);
        sc.is_show = false;
        facing = 0;
        hitpoints = 100;
        entity_size = 50;
        enemy = hoge;
        gun_list = poyo;
        scope_list = foo;
        armar_list = bar;
        total_damage = 0;
        total_suffered_damage = 0;
    }

    boolean display(){
        if (hitpoints <= 0){
            fill(color(255,0,0));
            textSize(100*sc.magnification);
            text("YOU DIED", pos.x, pos.y);
            textSize(60*sc.magnification);
            fill(0);
            text("damage: "+int(total_damage)+", kill: "+kill_count, pos.x, pos.y+100*sc.magnification);
            text("suffered damage: "+int(total_suffered_damage), pos.x, pos.y+200*sc.magnification);
            return false;
        }
        shoot();
        translate(pos.x, pos.y);

            update_pos();
            rotate(facing);

                strokeWeight(3);
                stroke(255, 50);
                line(0, 9, main.range, 0);

                fill(0);
                rect(entity_size/2+30*(main.type+1), 5, -30*(main.type+1), 8);

                noStroke();
                fill(255);
                ellipse(0, 0, entity_size, entity_size);
                fill(0);
                ellipse(entity_size/2, -15, 10, 10);
                ellipse(entity_size/2, 15, 10, 10);

            rotate(-facing);

            scale(sc.magnification);

            showHP();
            showAMO();

        translate(-pos.x, -pos.y);
        gun_pickup();
        scope_pickup();
        armar_pickup();
        main.shoot_ct--;
        return true;
    }

    void showHP(){
        arma.showHP();
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
            translate(pos.x, pos.y);
            //display line
            rotate(facing+main.gap);
            strokeWeight(3);
            stroke(255, 212, 0);
            line(0,9, main.range, 0);
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
                if (main.range+enemy[i].entity_size > dist(pos.x, pos.y, enemy[i].pos.x, enemy[i].pos.y)){
                    if(abs((pos.y-end.y)*enemy[i].pos.x-(pos.x-end.x)*enemy[i].pos.y+pos.x*end.y-pos.y*end.x)/sqrt((pos.y-end.y)*(pos.y-end.y)+(pos.x-end.x)*(pos.x-end.x)) < enemy[i].entity_size){
                        enemy[i].hitpoints -= main.damage;
                        total_damage += main.damage;
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
                    pos.y-=3.0*main.mobility;
                    break;
                case 's':
                    pos.y+=3.0*main.mobility;
                    break;
                case 'a':
                    pos.x-=3.0*main.mobility;
                    break;
                case 'd':
                    pos.x+=3.0*main.mobility;
                    break;
            }
        }
        float facing_target = atan2(mouseY-height/2, mouseX-width/2);
        if(abs(facing_target-facing) > radians(180)){
            facing_target+=radians(360);
        }
        facing += (facing_target - facing)*main.weight;
    }

    void gun_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < gun_list.length; i++){
                    if (50>dist(pos.x, pos.y, gun_list[i].pos.x, gun_list[i].pos.y)){
                        if(kouho.distance > dist(pos.x, pos.y, gun_list[i].pos.x, gun_list[i].pos.y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, gun_list[i].pos.x, gun_list[i].pos.y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    gun swap = main;
                    main = gun_list[kouho.num];
                    main.is_show = false;
                    gun_list[kouho.num] = swap;
                    gun_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    gun_list[kouho.num].pos.x = pos.x + cos(kakudo)*100;
                    gun_list[kouho.num].pos.y = pos.y + sin(kakudo)*100;
                }
            }
        }
    }

    void scope_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < scope_list.length; i++){
                    if (50>dist(pos.x, pos.y, scope_list[i].pos.x, scope_list[i].pos.y)){
                        if(kouho.distance > dist(pos.x, pos.y, scope_list[i].pos.x, scope_list[i].pos.y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, scope_list[i].pos.x, scope_list[i].pos.y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    scope swap = sc;
                    sc = scope_list[kouho.num];
                    sc.is_show = false;
                    scope_list[kouho.num] = swap;
                    scope_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    scope_list[kouho.num].pos.x = pos.x + cos(kakudo)*100;
                    scope_list[kouho.num].pos.y = pos.y + sin(kakudo)*100;
                }
            }
        }
    }

    void armar_pickup(){
        if (keyPressed == true){
            if(key == 'e'){
                num_and_dist kouho = new num_and_dist(2147483647, 1000000);
                for (int i = 0; i < armar_list.length; i++){
                    if (50>dist(pos.x, pos.y, armar_list[i].pos.x, armar_list[i].pos.y)){
                        if(kouho.distance > dist(pos.x, pos.y, armar_list[i].pos.x, armar_list[i].pos.y)){
                            kouho.num = i;
                            kouho.distance = dist(pos.x, pos.y, armar_list[i].pos.x, armar_list[i].pos.y);
                        }
                    }
                }
                if (kouho.num == 2147483647){
                    return;
                }else{
                    armar swap = arma;
                    arma = armar_list[kouho.num];
                    arma.is_show = false;
                    armar_list[kouho.num] = swap;
                    armar_list[kouho.num].is_show = true;
                    float kakudo = random(0, radians(360));
                    armar_list[kouho.num].pos.x = pos.x + cos(kakudo)*100;
                    armar_list[kouho.num].pos.y = pos.y + sin(kakudo)*100;
                }
            }
        }
    }

    coordinate get_pos(){
        return pos;
    }

    void decrease_hitpoint(float points){
        total_suffered_damage += points;
        if (arma.hitpoints > 0){
            arma.hitpoints -= points;
            if (arma.hitpoints < 0){
                points = -arma.hitpoints;
                arma.hitpoints = 0;
                hitpoints -= points;
            }
        }else{
            hitpoints -= points;
        }
        if (hitpoints < 0){
            total_suffered_damage += hitpoints;
            hitpoints = 0;
        }
    }

    void increase_hitpoints(float points){
        hitpoints += points;
        if (hitpoints > 100){
            hitpoints = 100;
        }
    }

    void increase_armar_hitpoints(float points){
        arma.hitpoints += points;
        if (arma.hitpoints > 25*arma.armar_level){
            arma.hitpoints = 25*arma.armar_level;
        }
    }
}
