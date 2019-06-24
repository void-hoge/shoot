class world{
    player[] player_list;
    gun[] gun_list;
    npc[] npc_list;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        gun_list = new gun[10];
        gun bar;
        for (int i = 0; i < gun_list.length; i++) {
            gun_list[i] = new gun(i%4);
            gun_list[i].x = random(-world_width/2, world_width/2);
            gun_list[i].y = random(-world_width/2, world_height/2);
        }
        npc_list = new npc[5];
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i] = new npc(random(-1000,1000), random(-1000,1000));
        }
        player_list = new player[1];
        player pl = new player(npc_list, gun_list);
        player_list[0] = pl;
        for (int i = 0; i < npc_list.length; i++){
            npc_list[i].set_terget(player_list);
        }
    }
    void display(){
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
        player_list[0].display();
    }
}
