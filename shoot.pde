system sys;
int moving_vec;

void setup(){
    size(1440, 810);
    sys = new system();
    moving_vec = 0;
    frameRate(60);
}

void draw(){
    translate(width/2,height/2);
    textAlign(CENTER);
    sys.display();
}
