system sys;

void setup(){
    size(1440, 810);
    sys = new system();
    frameRate(60);
}

void draw(){
    textAlign(CENTER, CENTER);
    sys.display();
}
