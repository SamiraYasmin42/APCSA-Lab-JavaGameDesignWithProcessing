/* Grid Class - Used for rectangular-tiled games
 * A 2D array of GridTiles which can be marked
 * Subclass of World that can show all Images & Sprites
 * Author: Joel Bianchi & RJ Morel
 * Last Edit: 5/27/25
 * setAllMarks() method that can take in a 2D array of Strings
 */


import processing.core.PApplet;
import processing.core.PImage;

public class Grid extends World{
  
  //------------------ GRID FIELDS --------------------//
  private int rows;
  private int cols;
  private GridTile[][] board;
  private boolean printingGridMarks = false;
  

  //------------------ GRID CONSTRUCTORS --------------------//

  // Grid Constructor #1: Default constructor that creates a 3x3 Grid  
  public Grid(PApplet p){
     this(p, 3,3);
  }

  //Grid Construtor #2: Only accepts the number of rows & columns (Default for 2023)
  public Grid(PApplet p, int rows, int cols){
    this(p, "grid",null, rows, cols);
  }

  //Grid constructor #3: Sets background image + rows & cols
  public Grid(PApplet p, String screenName, String bgFile, int rows, int cols){
    this(p, screenName, bgFile, null, rows, cols);
  }

  // Grid constructor #4: Takeas in 2D String array parameter to set tile marks
  public Grid(PApplet p, String screenName, String bgFile, String[][] tileMarks, int rows, int cols){
    super(p, screenName, bgFile);

    this.rows = rows;
    this.cols = cols;
    board = new GridTile[rows][cols];
    
    for(int r=0; r<rows; r++){
      for(int c=0; c<cols; c++){
        board[r][c] = new GridTile(p, new GridLocation(r,c));
      }
    }

    if(tileMarks != null){
      setAllMarks(tileMarks);
    }

  }

  //------------------ GRID MARKING METHODS --------------------//
 
  // Method that Assigns a String mark to a location in the Grid.  
  // This mark is not necessarily visible, but can help in tracking
  // what you want recorded at each GridLocation.
  public void setMark(String mark, GridLocation loc){
    board[loc.getRow()][loc.getCol()].setMark(mark);
    if(printingGridMarks){
      printGrid();
    }
  } 
  
  //Method to get the mark value at a location -RJ Morel
  public String getMark(GridLocation loc){
    return board[loc.getRow()][loc.getCol()].getMark();
  }
  
  //Method to get the mark value at a location -RJ Morel
  public boolean removeMark(GridLocation loc){
    boolean isGoodClick = board[loc.getRow()][loc.getCol()].removeMark();
    return isGoodClick;
  }
  
  //Method to check if a location has a mark -RJ Morel
  public boolean hasMark(GridLocation loc){
    GridTile tile = board[loc.getRow()][loc.getCol()];
    boolean isGoodClick = tile.getMark() != tile.getNoMark();
    return isGoodClick;
  } 

  // Method that Assigns a String mark to a location in the Grid.  
  // This mark is not necessarily visible, but can help in tracking
  // what you want recorded at each GridLocation.  
  // Returns true if mark is correctly set (no previous mark) or false if not
  public boolean setNewMark(String mark, GridLocation loc){
    int row = loc.getRow();
    int col = loc.getCol();
    boolean isGoodClick = board[row][col].setNewMark(mark);
    if(printingGridMarks){
      printGrid();
    }
    return isGoodClick;
  } 

  //Method that prints out the marks in the Grid to the console
  public void printGrid(){
    for(int r = 0; r<rows; r++){
      for(int c = 0; c<cols; c++){
         System.out.print(board[r][c]);
      }
      System.out.println();
    } 
  }

  public void startPrintingGridMarks(){
    printingGridMarks = true;
  }
  public void stopPrintingGridMarks(){
    printingGridMarks = false;
  }

  // Sets the marks for an entire grid from a 2D String array
  // tileMarks MUST match the same number of rows & columns as the grid
  public void setAllMarks(String[][] tileMarks){

    if(tileMarks != null){

      int markRows = rows;
      int markCols = cols;
      
      if(tileMarks.length > rows){
        System.out.println("TileMarks has TOO MANY rows!");
      } else if (tileMarks.length < rows){
        System.out.println("TileMarks does not have enough rows!");
        markRows = tileMarks.length;
      }

      if(tileMarks[0].length > cols){
        System.out.println("TileMarks has TOO MANY columns!");
      } else if (tileMarks[0].length < cols){
        System.out.println("TileMarks does not have enough columns!");
        markCols = tileMarks[0].length;
      }

      // System.out.println(markR)

      for(int r=0; r<markRows; r++){
        for(int c=0; c<markCols; c++){
          board[r][c].setMark(tileMarks[r][c]);
        }
      }
      
    } else {
      System.out.println("Cannot setup tileMarks because object is NULL!");
    }
  }


  //------------------ GRID ACCESSOR METHODS --------------------//

  //Method that returns the GridLocation of where the mouse is currently hovering over
  public GridLocation getGridLocation(){
      
    int row = p.mouseY/(p.pixelHeight/this.rows);
    int col = p.mouseX/(p.pixelWidth/this.cols);

    return new GridLocation(row, col);
  } 

  //Accessor method that provide the x-pixel value given a GridLocation loc
  public int getX(GridLocation loc){
    int widthOfOneTile = p.pixelWidth/this.cols;
    //calculate the left of the grid GridLocation
    int pixelX = (widthOfOneTile * loc.getCol()); 
    return pixelX;
  }
  public int getX(int row, int col){
    return getX(new GridLocation(row, col));
  }
  public int getCenterX(GridLocation loc){
    return getX(loc) + getTileWidth()/2;
  }
  
  //Accessor method that provide the y-pixel value given a GridLocation loc
  public int getY(GridLocation loc){
    int heightOfOneTile = p.pixelHeight/this.rows;
    //calculate the top of the grid GridLocation
    int pixelY = (heightOfOneTile * loc.getRow()); 
    return pixelY;
  }
  public int getY(int row, int col){
    return getY(new GridLocation(row,col));
  }
  public int getCenterY(GridLocation loc){
    return getY(loc) + getTileHeight()/2;
  }
  
  //Accessor method that returns the number of rows in the Grid
  public int getNumRows(){
    return rows;
  }
  
  //Accessor method that returns the number of cols in the Grid
  public int getNumCols(){
    return cols;
  }

  //Accessor method that returns the width of 1 Tile in the Grid
  public int getTileWidth(){
    return p.pixelWidth/this.cols;
  }
  //Accessor method that returns the height of 1 Tile in the Grid
  public int getTileHeight(){
    return p.pixelHeight/this.rows;
  }

  //Returns the GridTile object stored at a specified GridLocation
  public GridTile getTile(GridLocation loc){
    return board[loc.getRow()][loc.getCol()];
  }

  //Returns the GridTile object stored at a specified row and column
  public GridTile getTile(int r, int c){
    return board[r][c];
  }


  //------------------ GRID TILE IMAGE METHODS --------------------//

  //Method that sets the image at a particular tile in the grid & displays it
  public void setTileImage(GridLocation loc, PImage pi){
    GridTile tile = getTile(loc);
    tile.setImage(pi);
    //showTileImage(loc);
  }

  //Method that returns the PImage associated with a particular Tile
  public PImage getTileImage(GridLocation loc){
    GridTile tile = getTile(loc);
    return tile.getImage();
  }


  //Method that returns if a Tile has a PImage
  public boolean hasTileImage(GridLocation loc){
    GridTile tile = getTile(loc);
    return tile.hasImage();
  }

  //Method that clears the tile image
  public void clearTileImage(GridLocation loc){
    setTileImage(loc,null);
  }

  public void showTileImage(GridLocation loc){
    GridTile tile = getTile(loc);
    if(tile.hasImage()){
      p.image(tile.getImage(),getX(loc),getY(loc));
    }
  }

  //Method to show all the PImages stored in each GridTile
  public void showGridImages(){

    //Loop through all the Tiles and display its images/sprites
      for(int r=0; r<getNumRows(); r++){
        for(int c=0; c<getNumCols(); c++){

          //Store temporary GridLocation
          GridLocation tempLoc = new GridLocation(r,c);
          
          //Check if the tile has an image
          if(hasTileImage(tempLoc)){
            showTileImage(tempLoc);
          }
        }
      }
  }
  //to be deprecated
  public void showImages(){
    showGridImages();
  }

  // Displays all World + Screen + Grid visuals
  public void show(){
    super.show();
    this.showGridImages();
    this.showGridSprites();
  }



  //------------------  GRID SPRITES METHODS --------------------//

  //Method that sets the Sprite at a particular tile in the grid & displays it
  public void setTileSprite(GridLocation loc, Sprite sprite){
    GridTile tile = getTile(loc);
    if(sprite == null){
      tile.setSprite(null);
      //System.out.println("Cleared tile @ " + loc);
      return;
    }
    // sprite.setLeft(getX(loc));
    // sprite.setTop(getY(loc));
    sprite.setCenterX(getCenterX(loc));
    sprite.setCenterY(getCenterY(loc));
    tile.setSprite(sprite);
    showTileSprite(loc);
    //System.out.println("Succcessfully set tile @ " + loc);
  }
  
  //Method that returns the PImage associated with a particular Tile
  public Sprite getTileSprite(GridLocation loc){
    GridTile tile = getTile(loc);
    //System.out.println("Grid.getTileSprite() " + tile.getSprite());
    return tile.getSprite();
  }
  
  //Method that returns if a Tile has a PImage
  public boolean hasTileSprite(GridLocation loc){
    GridTile tile = getTile(loc);
    return tile.hasSprite();
  }

  //Method that clears the tile image
  public void clearTileSprite(GridLocation loc){
    setTileSprite(loc,null);
  }

  //Method that checks for an AnimatedSprite and animates it
  public void animateTileSprite(GridLocation loc){
    try{
      AnimatedSprite aSprite = (AnimatedSprite)getTileSprite(loc);
      aSprite.animate();
      //System.out.println("animating");
    } catch (Exception e) {
      System.out.println("Is your Sprite an AnimatedSprite?\n"+e);
    }
  }

  public void showTileSprite(GridLocation loc){
    GridTile tile = getTile(loc);
    if(tile.hasSprite()){
      tile.getSprite().show();
    }
  }

  
  //Method to show all the PImages stored in each GridTile
  public void showGridSprites(){

    //Loop through all the Tiles and display its images/sprites
      for(int r=0; r<getNumRows(); r++){
        for(int c=0; c<getNumCols(); c++){

          //Store temporary GridLocation
          GridLocation tempLoc = new GridLocation(r,c);
          
          //Check if the tile has an image
          if(hasTileSprite(tempLoc)){

            //check if Sprite is Animated
            if(getTileSprite(tempLoc).getIsAnimated()){
              animateTileSprite(tempLoc);
            } else {
              //setTileSprite(tempLoc, getTileSprite(tempLoc));
              showTileSprite(tempLoc);
            }
          }
        }
      }
  }

  //Method to clear the screen from all Images & Sprites
  public void clearGrid(){

    //Loop through all the Tiles and display its images/sprites
      for(int r=0; r<getNumRows(); r++){
        for(int c=0; c<getNumCols(); c++){

            //Store temporary GridLocation
            GridLocation tempLoc = new GridLocation(r,c);
            
            //Check if the tile has an image
            if(hasTileSprite(tempLoc)){
              setTileSprite(tempLoc, getTileSprite(tempLoc));
              //showTileSprite(tempLoc);
    
            }
          }
        }
    }

}
