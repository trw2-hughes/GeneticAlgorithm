
import java.util.*;

/**
 *
 * @author Thomas Hughes
 */
public class simGinAl 
{

    public static final int GENS = 100;
// other variables in simGinAl
    public static void main(String[] args) 
    {
        //variables to control the algorithm.

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        Individual[] population = geneticAlgorithm.initiatePop();
        Rules[] data = geneticAlgorithm.initiateDataPop();
        Rules[] rule = geneticAlgorithm.initiateRulePop();
        data = geneticAlgorithm.readDataFromInputFile(data);
        List<String> theData = new ArrayList();

        int generation = 1;
        for (int i = 0; i < GENS; i++) 
        {
            int bestFitness;
            int averageFitness;
            Individual[] bestParents;
            Individual[] offSpringCrossover;
            Individual[] offSpringMutation;

            System.out.println("\nGeneration: " + generation);

            bestParents = geneticAlgorithm.tournementSelection(population);
            offSpringCrossover = geneticAlgorithm.crossover(bestParents);
            offSpringMutation = geneticAlgorithm.mutation(offSpringCrossover);
            // After performing crossover and mutation the gene would have changed drastically.
            // Therefore, the fitness needs to be recalculated.
            geneticAlgorithm.evaluatePopulation(offSpringMutation, data, rule);
             // Overwrite the previous generation with the new generation to avoid
            // getting stuck in a local maxima.
            population = geneticAlgorithm.OverwriteCurrentPopulationToAvoidLocalMaxima(population, offSpringMutation);
            geneticAlgorithm.evaluatePopulation(population, data, rule);
            geneticAlgorithm.printFitness(population, rule);
            theData.add(String.valueOf(bestFitness = population[0].fitness));
            theData.add(String.valueOf(averageFitness = geneticAlgorithm.averageFitness(population)));
            generation++;
            }
        geneticAlgorithm.writeDataToOutputFile(theData);
    }
}
            

            
