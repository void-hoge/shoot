# SHOOT

A TPS game inspired by [surviv.io](http://surviv.io).  
This game is a [processing](https://processing.org) project.

## Installation

Open [shoot.pde](shoot/shoot.pde) in processing IDE, then click "RUN" button.

## Usage

- ### Menu screen
    1. Select difficulty from five options.
    2. Press the logo, "SHOOT" to start the game.

- ### Move
    - Press 'W' to move forward.
    - Press 'S' to move backward.
    - Press 'A' to move left.
    - Press 'D' to move right.
- ### Pickup, aim and shoot guns.
    - Press 'E' to pickup scope, weapon and armor.
    - Aim enemy by using your mouse pointer.
    - Press left button of mouse to shoot the gun.

## Game rules

- ### Defend mode (only this mode now (5/July/2019))
    - The central circle of the map is the "core" to protect.
    - It is game over when core or your HP becomes zero.
    - The score is displayed on the game over screen.
- ### Items
    - #### Guns
        - ##### Assault Rifle ([AR.png](shoot/AR.png))
            - Medium range, rapid fire and good DPM(damage per minute).
        - ##### Sub machine-gun ([SMG.png](shoot/SMG.png))
            - Super rapid fire.
            - Short range and bad accuracy.
        - ##### Sniper Rifle ([SR.png](shoot/SR.png))
            - Super long range, Extremely high damage and high accuracy.
            - Slow reload, bad operability and mobility.
        - ##### Hand gun (HG.png)
            - Good operability and mobility.
            - Bad rapidity and damage.

    - #### Scope
        - ①②③④ mark on the map are scope.
        - The display range of the map will be expanded by picking it up.
        - A high magnification scope can aim at enemies far away, but it is difficult to aim accurately.

    - #### Armor
        - When you are hit by an enemy, if the armor's HP left, it will be deducted from the armor's HP first. (This system is based on [APEX legends](https://www.ea.com/ja-jp/games/apex-legends).)
        - Armor has four levels.
        - <font color="Silver">White</font> is lowest, <font color="CornflowerBlue">light blue</font> and <font color="BlueViolet">purple</font> follow it, <font color="Yellow">yellow</font> is highest.
        - Armor's HP increases by a quarter of his HP for each level. (You can check your armor by looking at the HP bar at the bottom left of the screen.)

## Code review

The game was created based on object orientation.
The class structure is as follows.

- system class
    - world class
        - player class
            - gun class
            - scope class
            - armor class
        - npc class
            - gun class
        - item classes (including gun, scope, and armor class)
        - core class

- #### system class
    - The system class is a class for switching menu screen and game screen.
    - The task of this class is to receive the difficulty information and pass it to the constructor of the world class.

- #### world class
    - It's a class that brings together all the game screen, including the map, the player, NPCs, and items.
    - Player, NPCs and items are all members of this class.

- #### player class
    - It is a class that performs processing related to the player.
    - It has instance of the class gun, scope and armor.

- #### npc class
    - This class controls the movement and shooting of the NPC.
    - It has instance of gun class.

- #### item classes
    - This class holds the specification of each items.

- #### core class
    - This is a class for the "core" that appears in defend mode.

All codes are referenced [official reference](https://processing.org/reference/) and [プロセッシングを始めよう 第二版](https://www.oreilly.co.jp/books/9784873117737/).

## Image of items (guns, armors)
- All pictures are drawn using Microsoft Paint.
- [This site](https://www.peko-step.com/tool/alphachannel.html) is used for transparent background.
    ##### Referenced image of guns
    - [AR.png](shoot/AR.png)
        - [FN SCAR® 17S](https://fnamerica.com/products/rifles/fn-scar-17s/)
    - [SMG.png](shoot/SMG.png)
        - [H&K MP5A3](https://hk-usa.com/hk-models/mp5a3-2/)
    - [SR.png](shoot/SR.png)
        - [Barrett Firearms MODEL 82A1®](https://barrett.net/firearms/model82a1)
    - [HG.png](HG.png)
        - [Glock G17 Gen 5](https://us.glock.com/en/pistols/g17-gen5)

## License

The processing application is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).
