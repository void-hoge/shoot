class npc{
    float x, y;
    float hitpoints;
    float size;
    npc(float xx, float yy){
        x = xx;
        y = yy;
        hitpoints = 100;
        size = 30;
    }

    void display(){
        if (hitpoints > 0){
            noStroke();
            fill(255, 0, 0);
            ellipse(x, y, size, size);
        }
    }
}
