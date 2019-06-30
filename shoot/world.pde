final int TEST = -1;
final int BORING = 0;
final int EASY = 1;
final int NORMAL = 2;
final int HARD = 3;
final int INSANE = 4;

class world{
    player[] player_list;
    gun[] gun_list;
    npc[] npc_list;
    scope[] scope_list;
    armar[] armar_list;
    core[] core_list;
    boolean isgameover;
    boolean[] back_to_menu;
    int score;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        gun_list = new gun[10];
        for (int i = 0; i < gun_list.length; i++) {
            gun_list[i] = new gun(i%4);
            gun_list[i].pos.random_on_arc(1000);
        }
        npc_list = new npc[5];
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i] = new npc(0,0);
            npc_list[i].pos.random_on_arc(3000);
        }
        player_list = new player[1];
        player pl = new player(npc_list, gun_list, scope_list, armar_list);
        player_list[0] = pl;
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list, core_list);
        }
    }
    world(int difficulty, boolean[] back_to){
        back_to_menu = back_to;
        world_width = 10000;
        world_height = 10000;
        gun_list = new gun[10];
        for (int i = 0; i < gun_list.length; i++){
            gun_list[i] = new gun(i%4);
            gun_list[i].pos.random_on_arc(1000);
        }
        scope_list = new scope[4];
        for (int i = 0; i < scope_list.length; i++){
            scope_list[i] = new scope(0, 0, float(i+1));
            scope_list[i].pos.random_on_arc(800);
        }
        armar_list = new armar[4];
        for (int i = 0; i < armar_list.length; i++){
            armar_list[i] = new armar(0, 0, (i%4)+1);
            armar_list[i].pos.random_on_arc(800);
        }
        switch (difficulty){
            case TEST:
                npc_list = new npc[1];
                npc_list[0] = new npc(0, 0, EASY, HG);
                npc_list[0].pos.random_on_arc(3000);
                break;
            case BORING:
                npc_list = new npc[2];
                for (int i = 0; i < npc_list.length; i++){
                    npc_list[i] = new npc(0,0, difficulty, HG);
                    npc_list[i].pos.random_on_arc(3000);
                }
                break;
            case EASY:
                npc_list = new npc[3];
                npc_list[0] = new npc(0,0, difficulty, HG);
                npc_list[1] = new npc(0,0, difficulty, HG);
                npc_list[2] = new npc(0,0, difficulty, SMG);
                npc_list[0].pos.random_on_arc(3000);
                npc_list[1].pos.random_on_arc(3000);
                npc_list[2].pos.random_on_arc(3000);
                break;
            case NORMAL:
                npc_list = new npc[5];
                npc_list[0] = new npc(0,0, difficulty, HG);
                npc_list[1] = new npc(0,0, difficulty, HG);
                npc_list[2] = new npc(0,0, difficulty, SMG);
                npc_list[3] = new npc(0,0, difficulty, SMG);
                npc_list[4] = new npc(0,0, difficulty, AR);
                npc_list[0].pos.random_on_arc(3000);
                npc_list[1].pos.random_on_arc(3000);
                npc_list[2].pos.random_on_arc(3000);
                npc_list[3].pos.random_on_arc(3000);
                npc_list[4].pos.random_on_arc(3000);
                break;
            case HARD:
                npc_list = new npc[5];
                npc_list[0] = new npc(0,0, difficulty, HG);
                npc_list[1] = new npc(0,0, difficulty, SMG);
                npc_list[2] = new npc(0,0, difficulty, SMG);
                npc_list[3] = new npc(0,0, difficulty, AR);
                npc_list[4] = new npc(0,0, difficulty, SR);
                npc_list[0].pos.random_on_arc(3000);
                npc_list[1].pos.random_on_arc(3000);
                npc_list[2].pos.random_on_arc(3000);
                npc_list[3].pos.random_on_arc(3000);
                npc_list[4].pos.random_on_arc(3000);
                break;
            case INSANE:
                npc_list = new npc[5];
                npc_list[0] = new npc(0,0, difficulty, AR);
                npc_list[1] = new npc(0,0, difficulty, AR);
                npc_list[2] = new npc(0,0, difficulty, AR);
                npc_list[3] = new npc(0,0, difficulty, SR);
                npc_list[4] = new npc(0,0, difficulty, SR);
                npc_list[0].pos.random_on_arc(3000);
                npc_list[1].pos.random_on_arc(3000);
                npc_list[2].pos.random_on_arc(3000);
                npc_list[3].pos.random_on_arc(3000);
                npc_list[4].pos.random_on_arc(3000);
                break;
        }

        core_list = new core[1];
        core_list[0] = new core();
        player_list = new player[1];
        player_list[0] = new player(npc_list, gun_list, scope_list, armar_list);
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list, core_list);
        }
    }
    void display(){
        translate(width/2, height/2);
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);
        scale(1/player_list[0].sc.magnification);

        translate(-player_list[0].get_pos().x, -player_list[0].get_pos().y);
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
        for (int i = 0; i < gun_list.length; i++) {
            gun_list[i].display();
        }
        for (int i = 0; i < npc_list.length; i++) {
            npc_list[i].display();
        }
        for (int i = 0; i < scope_list.length; i++){
            scope_list[i].display();
        }
        for (int i = 0; i < armar_list.length; i++){
            armar_list[i].display();
        }

        if((!core_list[0].display())||(!player_list[0].display())){
            isgameover = true;
            textSize(100*player_list[0].sc.magnification);
            fill(255, 0, 0);
            text("GAME OVER", player_list[0].pos.x, player_list[0].pos.y-300*player_list[0].sc.magnification);

            textSize(60*player_list[0].sc.magnification);
            fill(0);
            text("Score: "+score, player_list[0].pos.x, player_list[0].pos.y-150*player_list[0].sc.magnification);
            // back_to_menu_button();
        }
        if(isgameover == false){
            score++;
        }
    }
    boolean back_to_menu_button(){
        float x = 0;
        float y = height/2+200*player_list[0].sc.magnification;
        float w = 200*player_list[0].sc.magnification;
        float h = 50*player_list[0].sc.magnification;
        rectMode(CORNER);
        textSize(50*player_list[0].sc.magnification);
        textAlign(CENTER, CENTER);
        noStroke();
        if ((mouseX > player_list[0].pos.x + x-w/2)&&(mouseX < player_list[0].pos.x+(x-w/2)+w)&&(mouseY > player_list[0].pos.y+(y-h/2))&&(mouseY < player_list[0].pos.y+(y-h/2)+h)){
            if (mousePressed){
                fill(color(153, 17, 238));
                text("back to menu", player_list[0].pos.x + x, player_list[0].pos.y+y);
                return true;
            }
            fill(color(153, 17, 238));
            text("back to menu", player_list[0].pos.x + x, player_list[0].pos.y+y);
            return false;
        }else{
            fill(color(153, 17, 238));
            text("back to menu", player_list[0].pos.x + x, player_list[0].pos.y+y);
            return false;
        }
    }
}
