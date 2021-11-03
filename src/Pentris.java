import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;


public class Pentris {
    final public static int height=15;
    final public static int width=5;

    //the startposition for every position
    final public static int StartY=0;
    final public static int StartX=2;

    // This is the minimum amount of seconds the piece waits until it drops 1 down again.
    final public static double minimumWait = 0.25; 
    // Everytime that the timeframe fits in the time, the pieces drop a bit faster
    final public static double accelerationTimeFrame = 10; 
    // Every time frame the pieces will fall 0.05 seconds faster.
    final public static double acceleration = 0.05; 
    // Milliseconds times 1000 creates seconds. //// This will be used for the Thread.sleep(long milliseconds) to convert the milliseconds to seconds
    final public static long millisecondsToSeconds = 1000; 

    

    public static int[][] grid = new int[height][width];
    public static ArrayList<Integer> pentPieces = new ArrayList<Integer>();
    //contains current pieceID
    public static int pieceID;
    //contains the held pieceID
    public static int heldPieceID=-1;
    //contains rotation
    public static int rotation=0;
    //contains the pieceIDs of the next pieces
    public static ArrayList<Integer> pieceIDs = new ArrayList<Integer>();
    //contains all pentominoPieces
    public static int[][][][] pentominoDatabase=PentominoDatabase.data;


    //the current location of a piece
    public static volatile int PieceX=StartX;
    public static volatile int PieceY=StartY;

    
    //variable to end the game
    public static boolean Lost=false;


    // Keys used for playing pentris
    private static int left = KeyEvent.VK_LEFT;
    private static int right = KeyEvent.VK_RIGHT;
    private static int up = KeyEvent.VK_UP;
    private static int down = KeyEvent.VK_DOWN;
    private static int space = KeyEvent.VK_SPACE;
    private static int c = KeyEvent.VK_C;
    private static int z = KeyEvent.VK_Z;


    


    //this method should hold the current piece 
    public static void holdPiece(){
        if(heldPieceID==-1){
            heldPieceID=pieceID;
            PieceX=StartX;
            PieceY=StartY;
            nextPiece();
        }else{
            int temp=pieceID;
            pieceID=heldPieceID;
            heldPieceID=temp;
            PieceX=StartX;
            PieceY=StartY;
        }
    }


    //this method should update the nextpiece and pieceid variables
    //every piece should get its turn in 12 pieces
    public static void nextPiece() { //Lianne
        // choose randomly from 12 pieces, 
        pieceIDs.clear(); // remove all IDs from the pieceIDs array
        pentPieces.add(0);
        pentPieces.add(1);
        pentPieces.add(2);
        pentPieces.add(3);
        pentPieces.add(4);
        pentPieces.add(5);
        pentPieces.add(6);
        pentPieces.add(7);
        pentPieces.add(8);
        pentPieces.add(9);
        pentPieces.add(10);
        pentPieces.add(11); // make an arraylist with the pentomino IDs

        if (pentPieces.size() < 1) { 
            pieceIDs.addAll(pentPieces); // add element to the pieceIDs arraylist
            //If there is only one element in the arraylist, clear the arraylist
            // and add all the IDs to the arraylist again
            pentPieces.clear();
            pentPieces.add(0);
            pentPieces.add(1);
            pentPieces.add(2);
            pentPieces.add(3);
            pentPieces.add(4);
            pentPieces.add(5);
            pentPieces.add(6);
            pentPieces.add(7);
            pentPieces.add(8);
            pentPieces.add(9);
            pentPieces.add(10);
            pentPieces.add(11); 
            PieceX = StartX; // reset starting points
            PieceY = StartY;
            rotation=0; // reset rotation to 0
        }else { // If there's more than one element in the arraylist, you can get a pentomino out of the list
        Collections.shuffle(pentPieces); // randomize the order of the arraylist
            for (int i=0 ; i<pentPieces.size() ; i++) { // loop through shuffled arraylist
                pieceIDs.add(pentPieces.get(i)); // take the first ID and add it to the pieceIDs arraylist
                pentPieces.remove(i); // remove that piece from the pentPieces arraylist
                PieceX = StartX; // reset starting points
                PieceY = StartY;
                rotation=0; // reset rotation to 0
            }
        }
    }

    //this method should rotate a piece if posible has to rotate left and right
    //this should be done in the rotation variable
    public static void rotatePiece(Boolean right){
        // TODO Samanta Dil Mohamed
        System.out.println("Piece is rotated!");
    }


    //this method should make the piece fall by 1 if it can fall
    public static void fallingPiece(){
        // TODO Yuxuan Kong
    }


    
    //Acceleration method, should return an increasingly small int for the amount of second between piece drops
    public static double fallingAcceleration(double time){
        double timeIndicate = 1;
        countingloop:
        for (double i = accelerationTimeFrame; i < time; i+=accelerationTimeFrame){
            timeIndicate -= acceleration;
            if(timeIndicate <= minimumWait){
                timeIndicate = minimumWait;
                break countingloop;
            }    
        }return timeIndicate;
    }


    public static void dropPiece(){
        for (int i = 1; i < (grid.length-pentominoDatabase[pieceID][rotation].length); i++){
            if (!PieceFit(grid, pieceID, rotation, PieceX, PieceY+i)){
                PieceY += i-1; // Piece has to be added on this Y position
            }
        }
    }

    
    //this method removes a line from the grid
    public static void removeLine(int line){
        int[][] updatedGrid = new int[grid.length][grid[0].length];
        int[] clearLine = new int[updatedGrid[0].length]; // creates a 1D array that stores a empty line
        for(int i = 0; i< updatedGrid[0].length; i++){//fills the array with empty blocks
            clearLine[i] = -1;
        }

        int placeInUpdatedGrid = updatedGrid.length - 1;
        for(int j = updatedGrid.length; j >= 0; j--){ //maps through the grid and stores everything int the updated grid that is not in the filled line
            if(j == line){
                j--;
            }
            try{
                updatedGrid[placeInUpdatedGrid] = grid[j];
                placeInUpdatedGrid--;
            }catch(Exception e){}
            
        }
        updatedGrid[0] = clearLine; // fills the top of the grid with empty lines
        grid = updatedGrid; //updates the grid
    }


    //this method should check if a line is full
    public static void lineCheck(){
        int count = 0; 

        for(int line = grid.length - 1; line >= 0; line--){//checks for every line if it is full
            for(int i = 0; i < grid[line].length; i++){//assuming grid is a public variable
                if(grid[line][i] > -1){//checks if every element in a line is > than -1 and thus filled
                    count++;
                }
            }
            if(count >= grid[line].length){//if the count is equal to the grid[line] lenght then the line is full and needs to be removed.
                count = 0;

                removeLine(line);
                
                line++;
            }
            else{
                count = 0;
            }
        }

    }



    
    //this function evaluated if a piece can be placed on a give grid at a certain x and y location
    public static Boolean PieceFit(int[][]grid,int PieceID,int Piecemutation,int x,int y){
        //shorthand for readability
        int[][][][]database=PentominoDatabase.data;


        //if the x is negative then the starting point is of the grid and therefor invalid
        if(x<0){         
            return false;
        //if the piece doesnt extend past the borders
        }else if(grid.length>y+database[PieceID][Piecemutation].length-1){
            if(grid[0].length>x+database[PieceID][Piecemutation][0].length-1){
                //then it wil check for every square whether the matrix has a 1 there(meaning its a square to be placed)
                //and if the grid already has a value there
                for(int i=0;i<database[PieceID][Piecemutation].length;i++){
                    for(int j=0;j<database[PieceID][Piecemutation][0].length;j++){
                        if(grid[i+y][j+x]>=0&database[PieceID][Piecemutation][i][j]==1){
                            ///if so they overlap so you cant place the piece
                            return false;
                        }
                    }
                }
            //int[][]gridclone=clone2Dint(grid);
            //Search.addPiece(gridclone, database[PieceID][Piecemutation], Piecemutation, y, x);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


    //this method should update its location and rotation based on keypad inputs
    public static void keypadMethod(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == left && PieceFit(grid,pieceID,rotation,PieceX-1,PieceY)) {  
            PieceX -= 1; // If the keypad left is pressed the piece should go 1 position to the left. That's why the x coordinate of the piece is subtracted by 1.
            System.out.println("pieceX = "+PieceX);

        }else if (keyCode == right && PieceFit(grid, pieceID, rotation, PieceX+1,PieceY)) {
            PieceX += 1; // If the keypad right is pressed the piece should go 1 position to the right. That's why the x coordinate of the piece is added by 1.
            System.out.println("pieceX = "+PieceX);

        }else if (keyCode == down && PieceFit(grid,pieceID,rotation,PieceX, PieceY+1)) {
            PieceY += 1; // If the keypad down is pressed the piece should go down to the place where it is going to be placed. (To show it smoothly in the UI, drop it down using a much smaller wait then when playing the normal way.)
            fallingPiece();
            System.out.println("pieceY = "+PieceY);

        }else if (keyCode == up){
            rotatePiece(true); // If the keypad up is pressed the piece should be rotated right once.
        
        }else if (keyCode == space) {
            dropPiece(); // Drop the piece if spacebar is pressed.

        }else if (keyCode == z){
            rotatePiece(false); // If the keypad z is pressed the piece should be rotated right once.

        }else if (keyCode == c){
            holdPiece(); // If the keypad c is pessed the piece should be stored and used at a later point in the game.
        }
    }


    public static void main(String[] args){
        long startingTime=System.currentTimeMillis();
        long currentTime;

        UI ui = new UI(width,height,50);

        try{ 
            while(!Lost){
                ui.setState(grid);
                currentTime=System.currentTimeMillis();
                long playingTime = currentTime - startingTime;
                Thread.sleep((long)fallingAcceleration(playingTime));
                lineCheck();
                fallingPiece();
            }
        }
        catch(InterruptedException e){}



    } 
}
