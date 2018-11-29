
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Thomas Hughes
 */
public class GeneticAlgorithm 
{
    //adjustable
    public static final int POPSIZE = 100; //size of pop
    public static final double MUTATION = 0.01; //chance of muation
    public static final int NUMOFRULES = 10; //number of rules used
//////////////generation in SimGinAl
    
    
    //fixed
    public static final int CHROMELENG = 60; // actPos x rules
    public static final int COMMANDSIZE = 5; // (condtionlength) 5 or 7 length of conditional in rule
    public static final int ACTPOS = 6; // (output)6 or 8 for position of the 'out' gene
    public static final int DATASIZE = 32; //from the 32 to 64 index size of data set array
    public static final String INPUTDATA ="data1.txt";
    Random random = new Random();

    // Constructor
    public GeneticAlgorithm() 
    {
    }


        public Rules[] readDataFromInputFile(Rules[] data) 
    {
        String space = " ";
        List<String> numbers1 = new ArrayList(), numbers2 = new ArrayList();
        Scanner scan = new Scanner(GeneticAlgorithm.class.getResourceAsStream(INPUTDATA));
        
        while (scan.hasNextLine()) 
        {
            numbers1.add(scan.next());
            numbers2.add(scan.next());

        }

        for (int i = 0; i < numbers1.size(); i++) 
        {
            String temp = numbers1.get(i);
            int[] tempInts = new int[temp.length()];
            for (int j = 0; j < temp.length(); j++) 
            {
//                data[i].getCondition()[j] = temp.charAt(j) - '0';
              tempInts[j] = Integer.parseInt(String.valueOf(temp.charAt(j)));
            }
            data[i].setCondition(Arrays.copyOf(tempInts, tempInts.length));
        }
        for (int k = 0; k < numbers2.size(); k++) 
        {
            data[k].setAction(Integer.parseInt(numbers2.get(k)));
        }
        return data;
    }
//    
 //initiate the population of the individuals.
    public Individual[] initiatePop() 
    {
        Individual[] population = new Individual[POPSIZE];
        for (int i = 0; i < POPSIZE; i++) 
        {
            population[i] = new Individual(CHROMELENG);
        }
        return population;
    }
    //initiates the data set.
    public Rules[] initiateDataPop() 
    {
        Rules[] d = new Rules[DATASIZE];
        for (int i = 0; i < DATASIZE; i++) 
        {
            d[i] = new Rules(COMMANDSIZE);
        }
        return d;
    }

   

//initiates the rule set.
    public Rules[] initiateRulePop() 
    {
        Rules[] r = new Rules[NUMOFRULES];
        for (int i = 0; i < NUMOFRULES; i++) 
        {
            r[i] = new Rules(COMMANDSIZE);
        }
        return r;
    }

    //loops through population and evaluates each in turn.
    public void evaluatePopulation(Individual[] population, Rules[] data, Rules[] rule) 
    {
        for (Individual population1 : population) 
        {
            population1.CalculateFitnessBaseOnNewRules(NUMOFRULES, COMMANDSIZE, data, rule, DATASIZE);
        }
    }

    //tournament selection, faces two random individuals against each other, takes the fittest.
    public Individual[] tournementSelection(Individual[] population) 
    {
        Individual[] bestParents = new Individual[POPSIZE];
        for (int i = 0; i < population.length; i++) 
        {
            
            Individual pop1, pop2;
            pop1 = population[random.nextInt(population.length)];
            pop2 = population[random.nextInt(population.length)];

            if (pop1.getFitness() >= pop2.getFitness()) 
            {
                bestParents[i] = pop1;
            } else {
                bestParents[i] = pop2;
            }
        }

        return bestParents;
    }

    // random bit-wise crossover
    public Individual[] crossover(Individual[] population) 
    {
        Individual[] offSpringWCrossover = new Individual[population.length];

        for (int i = 0; i < population.length - 1; i += 2) 
        {
            Individual offspring1 = new Individual(population[0].getChromosome().length);
            Individual offspring2 = new Individual(population[0].getChromosome().length);
            int crossPoint = random.nextInt(CHROMELENG);

            for (int j = 0; j < crossPoint; j++) 
            {
                offspring1.setGene(j, population[i].getGene(j));
                offspring2.setGene(j, population[i + 1].getGene(j));
            }
            for (int j = crossPoint; j < population[0].getChromosome().length; j++) 
            {
                offspring1.setGene(j, population[i + 1].getGene(j));
                offspring2.setGene(j, population[i].getGene(j));
            }

            offSpringWCrossover[i] = offspring1;
            offSpringWCrossover[i + 1] = offspring2;
        }
        return offSpringWCrossover;
    }

    //mutate function, checks if gene position is actPos, mutates accordingly.
    public Individual[] mutation(Individual[] population) 
    {
        for (Individual population1 : population) {
            for (int j = 0; j < population1.getChromosome().length; j++) {
                if (random.nextDouble() < MUTATION) {
                    //mutates if random double is less than set mutation rate
                    if (((j + 1) % ACTPOS) == 0) 
                    {
                        population1.mutateAction(j); //gene is in action Position.
                    } else 
                    {
                        population1.mutateCommand(j);
                    }
                }
            }
        }
        return population;
    }

    //end of loop selection, returns array of best individuals.
    public Individual[] OverwriteCurrentPopulationToAvoidLocalMaxima(Individual[] previousPop, Individual[] currentGen) 
    {
        SortArrayUsingFitness(previousPop);
        SortArrayUsingFitness(currentGen);

        // Put the best Previous Generation individual into the weakest current generation index.
        // This avoids getting stuck within the local maxima.
        currentGen[currentGen.length - 1] = previousPop[0];
        SortArrayUsingFitness(currentGen);
        return currentGen;
    }

    private void SortArrayUsingFitness(Individual[] pop) 
    {
        // Order the array to the fittest at the top, and the weakest at the bottom.
        for (int i = 0; i < pop.length; i++) 
        {
            for (int j = i + 1; j < pop.length; j++) 
            {
                if (pop[i].fitness < pop[j].fitness) 
                {
                    Individual temp = pop[i];
                    pop[i] = pop[j];
                    pop[j] = temp;
                }
            }
        }
    }

    public int averageFitness(Individual[] population) 
    {
        // Works out the population's average fitness.
        return (int) totalFitness(population) / population.length;
    }

    public int totalFitness(Individual[] population) 
    {
        int population_fitness = 0;
        for (Individual population1 : population) 
        {
            population_fitness += population1.getFitness();
        }
        return population_fitness;
    }

    public Individual bestIndividual(Individual[] pop) 
    {
        Individual bestFittistIndividual = pop[0];
        for (Individual population1 : pop) {
            if (population1.getFitness() >= bestFittistIndividual.getFitness()) 
            {
                bestFittistIndividual = population1;
            }
        }
        return bestFittistIndividual;
    }

    //converts gene of fittest individual into rule set, then prints the rules.
    public void printFitness(Individual[] pop, Rules[] rule) 
    {
        System.out.println("Best Gene: " + bestIndividual(pop));
        int k = 0;
        for (int i = 0; i < NUMOFRULES; i++) 
        {
            for (int j = 0; j < COMMANDSIZE; j++) 
            {
                rule[i].getCondition()[j] = bestIndividual(pop).chromosome[k++];
            }
            rule[i].setAction(bestIndividual(pop).chromosome[k++]);
        }
        for (int l = 0; l < NUMOFRULES; l++) 
        {
            for (int i = 0; i < rule[l].getCondition().length; i++) 
            {
                System.out.print(rule[l].getCondition()[i] == 2 ? "#" : rule[l].getCondition()[i]);
            }
            System.out.print(" " + rule[l].getAction() + "\n");
        }
        System.out.println("Fittest Individual: " + bestIndividual(pop).fitness 
                +"\n"+ "Total: " + totalFitness(pop)+"\n" + "Average: " + averageFitness(pop));
       
    }

    public void writeDataToOutputFile(List<String> theData) 
    {

        try 
        {
            try (FileWriter fw = new FileWriter(new File("data.csv")); 
                    BufferedWriter bufferedWriter = new BufferedWriter(fw)) 
            
            {
                bufferedWriter.append("Fittest Gene");
                bufferedWriter.append("              ");
                
                bufferedWriter.append("Average");
                bufferedWriter.append(",");
                bufferedWriter.append("\r\n");
                
                bufferedWriter.flush();
                
                for (int i = 0; i < theData.size(); i++) 
                {
                    bufferedWriter.append(theData.get(i));
                    bufferedWriter.append("             ");
                    if ((i + 1) % 2 == 0) 
                    {
                        bufferedWriter.append("\r\n");
                    }
                }
                bufferedWriter.flush();
            }
        } catch (IOException ex) 
        {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    //reads the data in and places it into a data set object.


//        // open a file
//        
//        for (int i = 0; i < theData.size(); i += 2) {
//            String fitness = theData.get(i);
//            String average = theData.get(i+1);
//            
//            String finalString = fitness + "," + average;
//            
//            //write to file
//        }
//        
//        //close file
