class item{
    float x, y;
}

class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
    }
    float get_hitpoints(){
        return hitpoints;
    }
}

class gun extends item{
    float damage;
    float penetration;
    float range;
    int amo;
    float rate;   //cool time(frame)
    int shootcount;
    gun(){
        damage = 50;
        penetration = 0.5;
        range = 500;
        amo = 100;
        rate = 30;
        shootcount = 0;
    }
}
