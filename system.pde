class system{
    world hoge;
    boolean ismenu;
    int difficulty;
    system(){
        ismenu = true;
        difficulty = 0;
        hoge = new world(EASY);
    }
    void display(){
        if (!ismenu){
            hoge.display();
        }else{
            noStroke();
            color begin = color(0, 255, 255);
            color end = color(255, 0, 255);
            for (int i = 0; i < width; i+=5){
                color c = lerpColor(begin, end, float(i)/width);
                fill(c);
                rect(i, 0, 5, height);
            }

            if(start_button()){
                ismenu = false;
            }
            // difficulty = difficulty_button(difficulty);
        }
    }
}

final String[] difficulty_name = {"BORING", "EASY", "NORMAL", "HARD", "INSANE"};

boolean start_button(){
    float x = (width/2)-300;
    float y = 25;
    float w = 600;
    float h = 100;
    rectMode(CORNER);
    textSize(150);
    textAlign(CENTER, CENTER);
    noStroke();
    if ((mouseX > x)&&(mouseX < x+w)&&(mouseY > y)&&(mouseY < y+h)){
        if (mousePressed){
            fill(color(255, 0, 255));
            text("SHOOT", width/2, 50);
            return true;
        }
        fill(color(255, 0, 255));
        text("SHOOT", width/2, 50);
        return false;
    }else{
        fill(color(0, 255, 255));
        text("SHOOT", width/2, 50);
        return false;
    }
}

int difficulty_button(){
    return 1;
}
