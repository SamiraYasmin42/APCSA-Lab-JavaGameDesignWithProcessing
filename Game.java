/* Game Class Starter File
 * Authors: Samira Yasmin & Prakriti Chaudhary
 * Last Edit: 5/26/25
 * Added example for using grid method setAllMarks()
 */

//import processing.sound.*;

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
  String splashBgFile = "images/start.png";
  //SoundFile song;

  // VARIABLES: maze1 Screen (pieces on a grid pattern)
  Grid maze1;
  PImage maze1Bg;
  String maze1BgFile = "images/maze1.jpg";
  PImage piece1;   // Use PImage to display the image in a GridLocation
  String piece1File = "images/x_wood.png";
  int piece1Row = 3;
  int piece1Col = 0;
  AnimatedSprite chick;
  String chickFile = "sprites/chick_walk.png";
  String chickJson = "sprites/chick_walk.json";
  int chickRow = 3;
  int chickCol = 0;
  int health = 3;
  Button b1;

  // VARIABLES: maze2 Screen (pieces on a grid pattern)
  Grid maze2;


  // VARIABLES: endScreen
  World endScreen;
  PImage endBg;
  String endBgFile = "images/youwin.png";


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

    //SETUP: If non-moving, Resize all BG images to exactly match the screen size
    splashBg.resize(p.width, p.height);
    maze1Bg.resize(p.width, p.height);
    endBg.resize(p.width, p.height);   

    //SETUP: Construct each Screen, World, Grid
    splashScreen = new Screen(p, "splash", splashBg);
    maze1 = new Grid(p, "maze1", maze1Bg, 14, 21);
    endScreen = new World(p, "end", endBg);
    currentScreen = splashScreen;

    //SETUP: Construct Game objects used in All Screens
    runningHorse = new AnimatedSprite(p, "sprites/horse_run.png", "sprites/horse_run.json", 50.0f, 75.0f, 1.0f);

    //SETUP: Setup more maze1 objects
    piece1 = p.loadImage(piece1File);
    piece1.resize(maze1.getTileWidth(),maze1.getTileHeight());
    chick = new AnimatedSprite(p, chickFile, chickJson, 0.0f, 0.0f, 0.5f);
    chick.resize(35,60);
    maze1.setTileSprite(new GridLocation (chickRow, chickCol), chick);
    b1 = new Button(p, "rect", 625, 525, 150, 50, "GoTo Level 2");
    maze1.addSprite(b1);
    // b1.setFontStyle("fonts/spidermanFont.ttf");
    b1.setFontStyle("Calibri");
    b1.setTextColor(PColor.WHITE);
    b1.setButtonColor(PColor.BLACK);
    b1.setHoverColor(PColor.get(100,50,200));
    b1.setOutlineColor(PColor.WHITE);
    String[][] tileMarks = { //14 rows and 21 columns
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"},
      {"R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R","R"},
      {"R","R"," "," "," ","R"," "," "," "," "," ","R"," "," "," "," "," "," "," ","R","R"},
      {" "," "," ","R"," ","R"," ","R "," "," "," "," "," "," "," "," "," "," "," "," "," "},
    };
    maze1.setAllMarks(tileMarks);
    maze1.startPrintingGridMarks();
    System.out.println("Done loading Level 1 (maze1)...");
    
    //SETUP: Setup more maze2 objects

    System.out.println("Done loading Level 2 (maze2)...");



    //SETUP: Sound
    // Load a soundfile from the sounds folder of the sketch and play it back
     //song = new SoundFile(p, "sounds/Lenny_Kravitz_Fly_Away.mp3");
     //song.play();
    
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
      populateSprites();
      moveSprites();
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
      if(p.keyCode == 83 && chickRow != maze1.getNumRows()-1){
         GridLocation nextLoc = new GridLocation(chickRow+1, chickCol);
          if (!maze1.getMark(nextLoc).equals("R")){    
          chickRow++;
          }
      }
      if (p.key == 'w' && chickRow != 0 ){
        GridLocation nextLoc = new GridLocation(chickRow-1, chickCol);
        if (!maze1.getMark(nextLoc).equals("R")){
          chickRow--;
        }  
      }
      if (p.key == 'a'&& chickCol != 0){
         GridLocation nextLoc = new GridLocation(chickRow, chickCol-1);
         if (!maze1.getMark(nextLoc).equals("R")){
        chickCol--;
         }
      }
      if (p.key == 'd' && chickCol != maze1.getNumCols()-1){
         GridLocation nextLoc = new GridLocation(chickRow, chickCol+1);
         if (!maze1.getMark(nextLoc).equals("R")){
        chickCol++;
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



    }


    //CHANGING SCREENS BASED ON KEYS
    //change to level1 if 1 key pressed, level2 if 2 key is pressed
    if(p.key == '1'){
      currentScreen = maze1;
    } else if(p.key == '2'){
      currentScreen = maze2;
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
      surface.setTitle(titleText + "\t// CurrentScreen: " + extraText + " \t // Name: " + name + "\t // Health: " + health );

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

      // Print a '1' in console when level1
      System.out.print("1");

      // Displays the piece1 image
      // GridLocation piece1Loc = new GridLocation(piece1Row,piece1Col);
      // maze1.setTileImage(piece1Loc, piece1);

      // Displays the chick image
      GridLocation chickLoc = new GridLocation(chickRow, chickCol);
      maze1.setTileSprite(chickLoc, chick);

      // Moves to next level based on a button click
      // b1.show();
      if(b1.isClicked()){
        System.out.println("\nButton Clicked");
        currentScreen = maze2;
      }
    
    }
    
    // UPDATE: maze2 Screen
    if(currentScreen == maze2){

      // Print a '2' in console when skyWorld
      System.out.print("2");



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

  // Populates enemies or other sprites on the Screen
  public void populateSprites(){

    //What is the index for the last column?
    

    //Loop through all the rows in the last column

      //Generate a random number


      //10% of the time, decide to add an enemy image to a Tile
      

  }

  // Moves around the enemies/sprites on the Screen
  public void moveSprites(){

    //Loop through all of the rows & cols in the grid

        //Store the current GridLocation

        //Store the next GridLocation

        //Check if the current tile has an image that is not piece1      


          //Get image/sprite from current location
            

          //CASE 1: Collision with piece1


          //CASE 2: Move enemy over to new location


          //Erase image/sprite from old location

          //System.out.println(loc + " " + grid.hasTileImage(loc));

            
        //CASE 3: Enemy leaves screen at first column

  }

  // Checks if there is a collision between Sprites on the Screen
  public boolean checkCollision(GridLocation loc, GridLocation nextLoc){

    //Check what image/sprite is stored in the CURRENT location
    // PImage image = grid.getTileImage(loc);
    // AnimatedSprite sprite = grid.getTileSprite(loc);

    //if empty --> no collision

    //Check what image/sprite is stored in the NEXT location

    //if empty --> no collision

    //check if enemy runs into player

      //clear out the enemy if it hits the player (using cleartTileImage() or clearTileSprite() from Grid class)

      //Update status variable

    //check if a player collides into enemy

    return false; //<--default return
  }

  // Indicates when the main game is over
  public boolean isGameOver(){
    
    return false; //by default, the game is never over
  }

  // Describes what happens after the game is over
  public void endGame(){
      System.out.println("Game Over!");

      // Update the title bar

      // Show any end imagery
      currentScreen = endScreen;

  }

} //close class
