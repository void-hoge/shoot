class npc{
    float x, y;
    float hitpoints;
    float entity_size;
    npc(float xx, float yy){
        x = xx;
        y = yy;
        hitpoints = 100;
        entity_size = 30;
    }

    npc(){
        x = 0;
        y = 0;
        hitpoints = 100;
        entity_size = 30;
    }

    void display(){
        if (hitpoints > 0){
            noStroke();
            fill(255, 0, 0);
            ellipse(x, y, entity_size, entity_size);
            textSize(20);
            text(hitpoints, x, y);
        }
    }
}
