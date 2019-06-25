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
    scope(float mag){
        is_show = false;
        magnification = mag;
    }
    scope(float xx, float yy, float num){
        x = xx;
        y = yy;
        is_show = true;
        magnification = num;
    }
    void display(){
        if (is_show){
            fill(color(0, 0, 255));
            textSize(30);
            text(int(magnification), x, y+10);
            strokeWeight(3);
            stroke(color(0, 0, 255));
            noFill();
            ellipse(x, y, 40, 40);
        }
    }
}

class armar extends item{
    int armar_level;
    float hitpoints;
    PImage img;
    armar(float xx, float yy, int level){
        x = xx;
        y = yy;
        armar_level = level;
        hitpoints = armar_level*25;
        is_show = true;
        switch(level){
            case 1:
                img = loadImage("ARMOR1.png");
                break;
            case 2:
                img = loadImage("ARMOR2.png");
                break;
            case 3:
                img = loadImage("ARMOR3.png");
                break;
            case 4:
                img = loadImage("ARMOR4.png");
                break;
        }
    }
    float get_hitpoints(){
        return hitpoints;
    }
    void display(){
        if (is_show){
            image(img, x, y, 100, 100);
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

final int HG = 0;
final int SMG = 1;
final int AR = 2;
final int SR = 3;

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
    float mobility;
    PImage img;
    gun(int t){
        type = t;
        is_show = true;
        switch (type){
            case SMG:
                damage = 10;
                amo = 200;
                rate = 4;           //900rpm
                range = 700;
                dispersion = 0.2;
                weight = 0.15;
                mobility = 1.5;
                img = loadImage("SMG.png");
                break;
            case AR:
                damage = 20;
                amo = 100;
                rate = 6;           //600rpm
                range = 1000;
                dispersion = 0.05;
                weight = 0.08;
                mobility = 1;
                img = loadImage("AR.png");
                break;
            case SR:
                damage = 90;
                amo = 20;
                rate = 120;         //30rpm
                range = 2000;
                dispersion = 0.01;
                weight = 0.03;
                mobility = 0.75;
                img = loadImage("SR.png");
                break;
            case HG:
                damage = 15;
                amo = 100;
                rate = 20;          //180rpm
                range = 400;
                dispersion = 0.1;
                weight = 0.4;
                mobility = 2;
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
