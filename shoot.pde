system sys;

void setup(){
    size(1440, 810);
    sys = new system();
}

void draw(){
    translate(width/2,height/2);
    sys.display();
}
