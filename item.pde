abstract class item{
    float x, y;
    boolean is_show;
    abstract void display();
}

// final int NONE = 0;
// final int SCOPE = 1;
// final int ARMAR = 2;
// final int GUN = 3;

class scope extends item{
    float magnification;
    scope(){
        is_show = true;
        magnification = 1;
    }
    scope(float num){
        is_show = true;
        magnification = num;
    }
    void display(){
        if (is_show){

        }
    }
}

class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
        is_show = true;
    }
    float get_hitpoints(){
        return hitpoints;
    }
    void display(){
        if (is_show){
            text(armar_level, x, y);
        }
    }
    void showHP(){
        if (armar_level != 0){
            stroke(0);
            strokeWeight(1);
            noFill();
            rect(-width/2+250, height/2-150, -armar_level*25*2, 30);
            switch (armar_level){
                case 1:
                    //white
                    fill(255);
                    break;
                case 2:
                    //blue
                    fill(#87CEFA);
                    break;
                case 3:
                    //purple
                    fill(#9370DB);
                    break;
                case 4:
                    //yellow (gold)
                    fill(#FFFF00);
                    break;
            }
            noStroke();

            rect(-width/2+250, height/2-150, -hitpoints*2, 30);
        }
    }
}

final int SMG = 0;
final int AR = 1;
final int SR = 2;
final int HG = 3;

class gun extends item{
    int type;
    float damage;
    float range;
    int amo;
    int mag_size;       //未実装
    int rate;
    int shoot_ct;//cool time
    float dispersion;
    float gap;
    float weight;
    PImage img;
    gun(int t){
        type = t;
        is_show = true;
        switch (type){
            case SMG:
                damage = 10;
                amo = 200;
                rate = 4;           //900rpm
                range = 400;
                dispersion = 0.2;
                weight = 0.15;
                img = loadImage("SMG.png");
                break;
            case AR:
                damage = 20;
                amo = 100;
                rate = 6;           //600rpm
                range = 700;
                dispersion = 0.05;
                weight = 0.08;
                img = loadImage("AR.png");
                break;
            case SR:
                damage = 90;
                amo = 20;
                rate = 120;         //30rpm
                range = 2000;
                dispersion = 0.01;
                weight = 0.03;
                img = loadImage("SR.png");
                break;
            case HG:
                damage = 15;
                amo = 100;
                rate = 20;          //180rpm
                range = 400;
                dispersion = 0.1;
                weight = 0.4;
                img = loadImage("HG.png");
                break;
        }
        shoot_ct = 0;
        set_gap();
        x = 0;
        y = 0;
    }
    void set_gap(){
        gap = random(-dispersion, dispersion);
    }
    void display(){
        if (is_show){
            imageMode(CENTER);
            switch (type){
                case SMG:
                    image(img, x, y, 200, 100);
                    break;
                case AR:
                    image(img, x, y, 200, 100);
                    break;
                case SR:
                    image(img, x, y, 200, 100);
                    break;
                case HG:
                    image(img, x, y, 200, 100);
                    break;
            }
        }
    }
}
