class system{
    world hoge;
    boolean[] ismenu_pointer;
    int difficulty;
    difficulty_button button;
    system(){
        ismenu_pointer = new boolean[1];
        ismenu_pointer[0] = true;
        difficulty = 0;
        button = new difficulty_button();
    }
    void display(){
        if (!ismenu_pointer[0]){
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
                ismenu_pointer[0] = false;
                hoge = new world(difficulty, ismenu_pointer);
            }
            button.boring_button(width/2-550, 200);
            button.easy_button(width/2-280, 200);
            button.normal_button(width/2, 200);
            button.hard_button(width/2+280, 200);
            button.insane_button(width/2+550, 200);
            difficulty = button.difficulty;
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

class difficulty_button{
    int difficulty;
    difficulty_button(){
        difficulty = NORMAL;
    }
    void boring_button(float x, float y){
        if (difficulty == BORING){
            fill(color(153, 17, 238));
        }else{
            fill(255);
        }
        textAlign(CENTER, CENTER);
        textSize(70);
        text(difficulty_name[BORING], x, y);
        if(mousePressed&&(mouseX > x-120)&&(mouseX < (x-120)+240)&&(mouseY > y-35)&&(mouseY < (y-35)+70)){
            difficulty = BORING;
        }
    }
    void easy_button(float x, float y){
        if (difficulty == EASY){
            fill(color(153, 17, 238));
        }else{
            fill(255);
        }
        textAlign(CENTER, CENTER);
        textSize(70);
        text(difficulty_name[EASY], x, y);
        if(mousePressed&&(mouseX > x-80)&&(mouseX < (x-80)+160)&&(mouseY > y-35)&&(mouseY < (y-35)+70)){
            difficulty = EASY;
        }
    }
    void normal_button(float x, float y){
        if (difficulty == NORMAL){
            // fill(0x26c6da);
            fill(color(153, 17, 238));
        }else{
            fill(255);
        }
        textAlign(CENTER, CENTER);
        textSize(70);
        text(difficulty_name[NORMAL], x, y);
        if(mousePressed&&(mouseX > x-120)&&(mouseX < (x-120)+240)&&(mouseY > y-35)&&(mouseY < (y-35)+70)){
            difficulty = NORMAL;
        }
    }
    void hard_button(float x, float y){
        if (difficulty == HARD){
            fill(color(153, 17, 238));
        }else{
            fill(255);
        }
        textAlign(CENTER, CENTER);
        textSize(70);
        text(difficulty_name[HARD], x, y);
        if(mousePressed&&(mouseX > x-80)&&(mouseX < (x-80)+160)&&(mouseY > y-35)&&(mouseY < (y-35)+70)){
            difficulty = HARD;
        }
    }
    void insane_button(float x, float y){
        if (difficulty == INSANE){
            fill(color(153, 17, 238));
        }else{
            fill(255);
        }
        textAlign(CENTER, CENTER);
        textSize(70);
        text(difficulty_name[INSANE], x, y);
        if(mousePressed&&(mouseX > x-120)&&(mouseX < (x-120)+240)&&(mouseY > y-35)&&(mouseY < (y-35)+70)){
            difficulty = INSANE;
        }
    }
}
