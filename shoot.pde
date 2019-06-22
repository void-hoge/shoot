system sys;

void setup(){
    size(1440, 810);
    sys = new system();
    frameRate(60);
}

void draw(){
    translate(width/2,height/2);
    textAlign(CENTER);
    sys.display();
}
