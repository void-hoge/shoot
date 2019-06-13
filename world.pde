class world{
    player pl;
    float world_width, world_height;
    world(){
        world_width = 10000;
        world_height = 10000;
        pl = new player();
    }
    void display(){
        stroke(0);
        strokeWeight(1);
        background(59, 175, 117);

        translate(pl.get_pos().x, pl.get_pos().y);
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

        pl.display();
    }
}
