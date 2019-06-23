class world{
    player pl;
    // ArrayList<gun> hoge;
    gun[] hoge;
    npc[] foo;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        hoge = new gun[50];
        // hoge = new ArrayList<gun>();
        gun bar;
        for (int i = 0; i < 50; i++) {
            hoge[i] = new gun(i%4);
            hoge[i].x = random(-world_width/2, world_width/2);
            hoge[i].y = random(-world_width/2, world_height/2);
        }
        foo = new npc[10];
        for (int i = 0; i < foo.length; i++){
            foo[i] = new npc(random(-1000,1000), random(-1000,1000));
        }
        pl = new player(foo, hoge);
    }
    void display(){
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);
        scale(1/pl.sc.magnification);

        translate(-pl.get_pos().x, -pl.get_pos().y);
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
        for (int i = 0; i < hoge.length; i++) {
            hoge[i].display();
        }
        for (int i = 0; i < foo.length; i++) {
            foo[i].display();
        }
        pl.display();
    }
}
