//package src;

import java.util.Random;




public class GA {
    
	static final int TARGET = 165;
	public static int pieceAmount = 500;
	public static int generation = 1000;
	static int mutationRate = 5;
	public static int populationSize = 500;
	public static int tournamentSize=5;
	public static int[][][] answerGrid;
	public static int selectionType=1;
	//selection type can be:
	//1 for tournament
	//2 for roulette
	//3 for elitism
	
    //beginning of the main methat that will have to work with my calculations(see comments at that section)
	public static void main(String[] args) {
	 

		
				
		Random generator = new Random(System.currentTimeMillis());
		Boxes[] boxPopulation = new Boxes[populationSize];
		int[] boxOrientation = new int[pieceAmount];
		int[] boxRotation = new int[pieceAmount];
		int[] BoxPieces = new int[pieceAmount];
		
		//initialize random boxes, rotation and orientation
		
		for (int i = 0; i < populationSize; i++) {
			for (int k = 0; k < pieceAmount; k++) {
				BoxPieces[k] = generator.nextInt(3);
				boxRotation[k] = generator.nextInt(4);
				boxOrientation[k] = generator.nextInt(3);
			} 
			boxPopulation[i] = new Boxes(BoxPieces, boxRotation, boxOrientation);
		}
		
		

		GeneticAlgorithm(boxPopulation, generation);
	}


	//this method takes in variables for the Genetic algorithm and runs it
	public static int[][][] GAmethod(int _pieceamount,
									 int _generations, 
									 int _mutationrate,
									 int _populationsSize,
									 int _TournamentSize,
									 int selection_method){


		//changing variable names for convienience
		pieceAmount = _pieceamount;
		generation = _generations;
		mutationRate = _mutationrate;
		populationSize = _populationsSize;
		tournamentSize=_TournamentSize;
		selectionType=selection_method;

		//setting up variables
		Random generator = new Random(System.currentTimeMillis());
		Boxes[] boxPopulation = new Boxes[populationSize];
		int[] boxOrientation = new int[pieceAmount];
		int[] boxRotation = new int[pieceAmount];
		int[] BoxPieces = new int[pieceAmount];
		
		
		//initialize random boxes, rotation and orientation
		
		for (int i = 0; i < populationSize; i++) {
			for (int k = 0; k < pieceAmount; k++) {
				BoxPieces[k] = generator.nextInt(3);
				boxRotation[k] = generator.nextInt(4);
				boxOrientation[k] = generator.nextInt(3);
				

			} 
			boxPopulation[i] = new Boxes(BoxPieces, boxRotation, boxOrientation);
		}
		

		
		
		// run algorithm with generated population
		GeneticAlgorithm(boxPopulation, generation);
		return answerGrid;
			
	}
	
	

	//sorting based on score
	public static void sortBoxes(Boxes[] population){
        
        int[] scores=new int[population.length];
        for(int i=0;i<population.length;i++){
            scores[i]=population[i].getScore();
        }

        //bubble sort
        int listlength = scores.length;
        for (int i = 0; i < listlength-1; i++){
            for (int j = 0; j < listlength-i-1; j++){
                if (scores[j] > scores[j+1])
                {

                    int temp = scores[j];
                    scores[j] = scores[j+1];
                    scores[1+j] = temp;

                    Boxes temp1= population[j];
                    population[j] =population[j+1];
                    population[j+1] =temp1;
                }
            }
        }
    }

	//print 3d grid
	public static void print3dint(int[][][] answerGrid){
        for(int i=0;i<answerGrid.length;i++){
            for(int j=0;j<answerGrid[0].length;j++){
                for(int k=0;k<answerGrid[0][0].length;k++){
                    System.out.print(answerGrid[i][j][k]+" ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }

	//method which tries to find a highest scoring idividual
	public static void GeneticAlgorithm(Boxes [] Population, int generations){
		//put the method here in setboxes

		
		
		Boxes[] newPopulation = new Boxes[Population.length];
		
		//method 
		//System.out.println(AIJudgeParcels.judgeVolumes(tempBoxes[k].getAllBoxes(), tempBoxes[k].getRotation(), tempBoxes[k].getOrientation()));
		
		Selection.setTournamentSize(tournamentSize);
		
		//for every generation do crossover and mutate and score the indiduals
		for (int j = 0; j < generations; j++) {
			AIJudgeParcels.scoring(Population);
			//file for ensuring each individual get into a tournament
			GenerationSelector.setPopulation(Population);
			
			newPopulation=Selection.selectionMethod(Population,selectionType);


			mutation(newPopulation);
			Population = newPopulation;
		}
		AIJudgeParcels.scoring(Population);
		
		//sort last generation on score
		sortBoxes(Population);
		//choose the highest scoring individual to print the score of
		System.out.println("Generation:" + generation + "\nScore:" + Population[populationSize-1].getScore()+ "\n\n\n");
		//make a grid with the highest scoring individual's pieces
		answerGrid=AIJudgeParcels.getMatrix(Population[populationSize-1]);

	}



	
	//something with the parcels and rotations
	public static int getMaxRotation(int parcelID){
		if(parcelID==0){
			return 4;
		}
		if(parcelID==1){
			return 6;
		}
		else{
			return 1;
		}
	}

    //this is my crossover method. If you see any optimalisations possible, lmk. I'd be interested in learning other ways.
	public static Boxes[] crossoverBoxes(Boxes box1, Boxes box2, int crossoverLocations){
		Boxes [] newBoxes = new Boxes [4];
		//clone box 1 values for second parent
		Boxes temporaryBox = box1.clone();
		Boxes CrossoverBox1 = box1.clone();
		Boxes CrossoverBox2 = box2.clone();

		int crossoverLocation = crossoverLocations;

		//cross over individual one
		for (int i = 0; i < crossoverLocation; i++) {
			CrossoverBox1.getAllBoxes()[i] = CrossoverBox2.getAllBoxes()[i];
			CrossoverBox1.getOrientation()[i] = CrossoverBox2.getOrientation()[i];
			CrossoverBox1.getRotation()[i] = CrossoverBox2.getRotation()[i];
		}

		
		//crossover individual 2 using clones
		for (int i = 0; i < crossoverLocation; i++) {
			CrossoverBox2.getAllBoxes()[i] = temporaryBox.getAllBoxes()[i];
			CrossoverBox2.getOrientation()[i] = temporaryBox.getOrientation()[i];
			CrossoverBox2.getRotation()[i] = temporaryBox.getRotation()[i];
		}

		newBoxes[0]= box1;
		newBoxes[1]= box2;
		//return the boxes in a list
		return newBoxes;

        /*
        How this method works: I take 2 chromosomes. I pick a crossover location.
        the location is the point where the chromosomes will be broken. 
        Then I move one part of parent 1 to the new part of parent 2. Then I move 
        the part of parent 2 back to parent 1. I have made a copy(temporary box) of parent 1(box 1) so 
        the code will remember what is was before the switch.
        */

	}

	

	//this is a mutation method which gives a chance of having a box rotation and or orientation to change
	public static void mutation(Boxes[] boxpopulation){
		int roll;
		Random rand=new Random();
		//for every individual in the population
		for(int i=0;i<boxpopulation.length;i++){
			//get its chromosomes
			int[] chromosomes=boxpopulation[i].getAllBoxes();
			int[] rotations=boxpopulation[i].getRotation();
			int[] orientation=boxpopulation[i].getOrientation();

			
			
			//for every chromosome 
			for(int j=0;j<chromosomes.length;j++){
				//roll a 100 sided die and if its lower then five mutate that piece into another one
				roll=rand.nextInt(100);
				if(roll<mutationRate){
					chromosomes[j]=rand.nextInt(3);
					rotations[j]=rand.nextInt(getMaxRotation(chromosomes[j]));
				}
			
				//roll a 100 sided die and if its lower then five mutate that rotation into another one
				roll=rand.nextInt(100);
				if(roll<mutationRate){
					rotations[j]=rand.nextInt(getMaxRotation(boxpopulation[i].getAllBoxes()[j]));
				}
			//!!!!!!! be careful !!!!!! wss een out of index error

				//roll a 100 sided die and if its lower then five mutate that rotation into another one
				roll=rand.nextInt(100);
				if(roll<mutationRate){
					orientation[j]=rand.nextInt(3);
				}
			}

		}

	 }

	
}
