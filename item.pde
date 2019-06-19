abstract public class item{
    float x, y;
    abstract void display();
}

public class scope extends item{
    float magnification;
    scope(){
        magnification = 1;
    }
    scope(float num){
        magnification = num;
    }
    void display(){

    }
}

public class armar extends item{
    int armar_level;
    float hitpoints;
    armar(int level){
        armar_level = level;
        hitpoints = armar_level*25;
    }
    float get_hitpoints(){
        return hitpoints;
    }
    void display(){
        text(armar_level, x, y);
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
        textSize(10);
        switch (type){
            case SMG:
                // text("SMG", x, y);
                image(img, x, y, 200, 100);
                break;
            case AR:
                // text("AR", x, y);
                image(img, x, y, 200, 100);
                break;
            case SR:
                // text("SR", x, y);
                image(img, x, y, 200, 100);
                break;
            case HG:
                // text("HG", x, y);
                image(img, x, y, 200, 100);
                break;
        }
    }
}
