/* Game Class Starter File
 * Authors: Samira Yasmin & Prakriti Chaudhary
 * Last Edit: 5/26/25
 * Added example for using grid method setAllMarks()
 */

import processing.sound.*;

import processing.core.PApplet;
import processing.core.PImage;

public class Game extends PApplet{

  //------------------ GAME VARIABLES --------------------//

  // VARIABLES: Processing variable to do Processing things
  PApplet p;

  // VARIABLES: Title Bar
  String titleText = "Maze";
  String extraText = "CurrentLevel?";
  String name = "Undefined";

  // VARIABLES: Whole Game
  AnimatedSprite runningHorse;
  boolean doAnimation;

  // VARIABLES: splashScreen
  Screen splashScreen;
  PImage splashBg;
  String splashBgFile = "images/splash.png";
  //SoundFile song;

  // VARIABLES: maze1 Screen (pieces on a grid pattern)
  Grid maze1;
  PImage maze1Bg;
  String maze1BgFile = "images/maze1.jpg";
  AnimatedSprite chick;
  String chickFile = "sprites/chick_walk.png";
  String chickJson = "sprites/chick_walk.json";
  int chickRow = 4;
  int chickCol = 1;
  Button b1;
  Button timer;
  CycleTimer mazeTime;


  // VARIABLES: endScreen
  World endScreen;
  World endScreen2;
  PImage endBg;
  PImage endBg2;
  String endBgFile = "images/winScreen.png";
  String endBgFile2 = "images/loseScreen.png";


  // VARIABLES: Tracking the current Screen being displayed
  Screen currentScreen;
  CycleTimer slowCycleTimer;

  boolean start = true;


  //------------------ REQUIRED PROCESSING METHODS --------------------//

  // Processing method that runs once for screen resolution settings
  public void settings() {
    //SETUP: Match the screen size to the background image size
    size(800,600);  //these will automatically be saved as width & height

    // Allows p variable to be used by other classes to access PApplet methods
    p = this;
    
  }

  //Required Processing method that gets run once
  public void setup() {

    p.imageMode(p.CORNER);    //Set Images to read coordinates at corners
    //fullScreen();   //only use if not using a specfic bg image
    
    //SETUP: Set the title on the title bar
    surface.setTitle(titleText);

    //SETUP: Load BG images used in all screens
    splashBg = p.loadImage(splashBgFile);
    splashBg.resize(800,600);
    maze1Bg = p.loadImage(maze1BgFile);
    maze1Bg.resize(p.width, p.height);
    endBg = p.loadImage(endBgFile);

    // //SETUP: If non-moving, Resize all BG images to exactly match the screen size
    // splashBg.resize(p.width, p.height);
    // maze1Bg.resize(p.width, p.height);
    // endBg.resize(p.width, p.height);   

    //SETUP: Construct each Screen, World, Grid
    splashScreen = new Screen(p, "splash", splashBgFile);
    maze1 = new Grid(p, "maze1", maze1BgFile, 15, 21);
    endScreen = new World(p, "end", endBgFile);
    endScreen2 = new World(p, "end2", endBgFile2);
    currentScreen = splashScreen;

    //SETUP: Construct Game objects used in All Screens
    runningHorse = new AnimatedSprite(p, "sprites/horse_run.png", "sprites/horse_run.json", 50.0f, 75.0f, 1.0f);

    //SETUP: Setup more maze1 objects
    chick = new AnimatedSprite(p, chickFile, chickJson, 0.0f, 0.0f, 0.5f);
    chick.resize(40,30);
    maze1.setTileSprite(new GridLocation (chickRow, chickCol), chick);
    String[][] tileMarks = {
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"}, //15 rows 21 cols
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"},
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"},
      {"R","R"," "," "," ","R"," "," "," "," "," ","R"," "," "," "," "," "," "," ","R","R"},
      {" ","S"," ","R"," ","R"," ","R"," ","R"," ","R"," ","R","R","R","R","R"," ","R","R"},//5
      {" "," "," ","R"," "," "," ","R"," ","R"," ","R"," ","R"," "," "," ","R"," ","R","R"},//6
      {"R","R"," ","R","R","R","R","R"," ","R"," ","R"," ","R"," ","R","R","R"," ","R","R"},//7
      {"R","R"," "," "," "," "," ","R"," ","R"," ","R"," ","R"," "," "," "," "," ","R","R"},//8
      {"R","R","R","R","R","R"," ","R"," ","R"," ","R"," ","R","R","R","R","R","R","R","R"},//9
      {"R","R"," "," "," ","R"," ","R"," "," ","R","R"," "," "," ","R"," "," "," ","R","R"},//10
      {"R","R"," ","R"," ","R"," ","R","R"," "," ","R","R","R"," ","R"," ","R"," ","R","R"},//11
      {"R","R"," ","R"," "," "," "," "," ","R"," "," "," "," "," ","R"," ","R"," ","R","R"},//12
      {"R","R"," ","R","R","R","R","R"," ","R","R","R","R","R","R","R"," ","R"," "," ","E"},//13
      {"R","R"," "," "," "," "," ","R"," "," "," "," "," "," "," "," "," ","R"," "," ","E"},
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"},

    };
    maze1.setAllMarks(tileMarks);
    maze1.startPrintingGridMarks();
    System.out.println("Done loading Level 1 (maze1)...");
    timer = new Button(p, "RECT", 300,10, 175, 50, "1:00");
    mazeTime = new CycleTimer(p, 45000);
    timer.setText("Time Now: "+ (double)(mazeTime.getCountdown())/1000 );
    maze1.addSprite(timer);

    //edit the timer
    timer.setButtonColor(PColor.WHITE);

    System.out.println("Done loading Level 1 (maze1)...");


    //SETUP: Sound
    // Load a soundfile from the sounds folder of the sketch and play it back
     SoundFile mazesong = new SoundFile(p, "sounds/chill.mp3");
     mazesong.play();
    
    System.out.println("Game started...");

  } //end setup()


  //Required Processing method that automatically loops
  //(Anything drawn on the screen should be called from here)
  public void draw() {

    // DRAW LOOP: Update Screen Visuals
    updateTitleBar();
    updateScreen();

    // DRAW LOOP: Set Timers
    int cycleTime = 1;  //milliseconds
    int slowCycleTime = 300;  //milliseconds
    if(slowCycleTimer == null){
      slowCycleTimer = new CycleTimer(p, slowCycleTime);
    }

    // DRAW LOOP: Populate & Move Sprites
    if(slowCycleTimer.isDone()){

    }

    // DRAW LOOP: Pause Game Cycle
    currentScreen.pause(cycleTime);   // slows down the game cycles

    // DRAW LOOP: Check for end of game
    if(isGameOver()){
      endGame();
    }

  } //end draw()

  //------------------ USER INPUT METHODS --------------------//


  //Known Processing method that automatically will run whenever a key is pressed
  public void keyPressed(){

    //check what key was pressed
    System.out.println("\nKey pressed: " + p.key); //key gives you a character for the key pressed

    //What to do when a key is pressed?
    
    //KEYS FOR LEVEL1
    if(currentScreen == maze1){

      GridLocation nextloc;
      //set [S] key to move the chick down & avoid Out-of-Bounds errors
      if (p.key == 'w' ){
        GridLocation nextLoc = new GridLocation(chickRow-1, chickCol);
        if (!maze1.getMark(nextLoc).equals("R") && chickRow != 0){
          chickRow--;
        }
          
      }
      if (p.key == 'a'){
        GridLocation nextLoc = new GridLocation(chickRow, chickCol-1);
        if (!maze1.getMark(nextLoc).equals("R") && chickCol != 0){
          chickCol--;
        }
      }
      if (p.key == 'd'){
        GridLocation nextLoc = new GridLocation(chickRow, chickCol + 1);
        if (!maze1.getMark(nextLoc).equals("R") && chickCol!= maze1.getNumCols()-1){
          chickCol++;
        }
      }
      if (p.key == 's'){
        GridLocation nextLoc = new GridLocation(chickRow+1, chickCol);
        if (!maze1.getMark(nextLoc).equals("R") && chickRow != maze1.getNumRows()-1){
          chickRow++;
        }
      }

      // if the 'n' key is pressed, ask for their name
      if(p.key == 'n'){
        name = Input.getString("What is your name?");
      }

      // if the 't' key is pressed, then toggle the animation on/off
      if(p.key == 't'){
        //Toggle the animation on & off
        doAnimation = !doAnimation;
        System.out.println("doAnimation: " + doAnimation);
      }

      if ("E".equals(maze1.getMark(new GridLocation(chickRow, chickCol)))){
        currentScreen = endScreen;
      }



    }

    //CHANGING SCREENS BASED ON KEYS
    //change to level1 if 1 key pressed, level2 if 2 key is pressed
    if(p.key == '1'){
      currentScreen = maze1;
    } else if(p.key == 'e'){
      currentScreen = endScreen;
    } else if(p.key == '2'){
      currentScreen = endScreen2;
    }

  }

  // Known Processing method that automatically will run when a mouse click triggers it
  public void mouseClicked(){
    
    // Print coordinates of mouse click
    System.out.println("\nMouse was clicked at (" + p.mouseX + "," + p.mouseY + ")");

    // Display color of pixel clicked
    int color = p.get(p.mouseX, p.mouseY);
    PColor.printPColor(p, color);

    // if the Screen is a Grid, print grid coordinate clicked
    if(currentScreen instanceof Grid){
      System.out.println("Grid location --> " + ((Grid) currentScreen).getGridLocation());
    }

    // if the Screen is a Grid, "mark" the grid coordinate to track the state of the Grid
    if(currentScreen instanceof Grid){
      ((Grid) currentScreen).setMark("X",((Grid) currentScreen).getGridLocation());
    }

    // what to do if clicked? (ex. assign a new location to piece1)
    if(currentScreen == maze1){



    }
    

  }



  //------------------ CUSTOM  GAME METHODS --------------------//

  // Updates the title bar of the Game
  public void updateTitleBar(){

    if(!isGameOver()) {

      extraText = currentScreen.getName();

      //set the title each loop
      surface.setTitle(titleText + "\t// CurrentScreen: " + extraText + " \t // Name: " + name );

      //adjust the extra text as desired
    
    }
  }

  // Updates what is drawn on the screen each frame
  public void updateScreen(){

    // UPDATE: first lay down the Background
    currentScreen.showBg();

    // UPDATE: splashScreen
    if(currentScreen == splashScreen){

      // Print an s in console when splashscreen is up
      System.out.print("s");

      // Change the screen to level 1 between 3 and 5 seconds
      if(splashScreen.getScreenTime() > 3000 && splashScreen.getScreenTime() < 5000){
        currentScreen = maze1;
      }
    }

    // UPDATE: level1Grid Screen
    if(currentScreen == maze1){

      // Print a '1' in console when maze1
      System.out.print("1");

      timer.setText("Time Now: "+(double)(mazeTime.getCountdown()/1000) );

      // Displays the chick image
      GridLocation chickLoc = new GridLocation(chickRow, chickCol);
      maze1.setTileSprite(chickLoc, chick);

    
    }
    

    // UPDATE: End Screen
    if(currentScreen == endScreen){

    }

    // UPDATE: Any Screen
    if(doAnimation){
      runningHorse.animateHorizontal(0.5f, 1.0f, true);
    }

    // UPDATE: Other built-in to current World/Grid/HexGrid
    currentScreen.show();

  }


  // Indicates when the main game is over
  public boolean isGameOver(){

    if(mazeTime.getTime() > 45000 )
    {
      currentScreen = endScreen2;
    }
    
    return false; //by default, the game is never over
  }

  // Describes what happens after the game is over
  public void endGame(){
      System.out.println("Game Over!");

      // Update the title bar

      // Show any end imagery
      currentScreen = endScreen2;

  }

 

} //close class
