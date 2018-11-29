
import java.util.*;

/**
 *
 * @author Thomas Hughes
 */
public final class Individual 
{

    public int[] chromosome;
  
    public int fitness = 0;
    Random rand = new Random();

   
    public Individual(int chromoSize) 
    {
        this.chromosome = new int[chromoSize];
        for (int i = 0; i < chromoSize; i++) 
        {
            if (((i + 1) % 8) != 0) {
                this.setGene(i, rand.nextInt(3));
            } else 
            {
                this.setGene(i, rand.nextInt(2));
            }
        }
    }

  
    public Individual(int chromoSize, boolean isEmpty)
    {
        this.chromosome = new int[chromoSize];
        Arrays.fill(chromosome, 0);
    }

    //the normal mutateCommand function.
    public void mutateCommand(int commandGene)
    {
        switch (this.getGene(commandGene))
        {
            case 1:
                this.setGene(commandGene, rand.nextBoolean() ? 0 : 2); // 2 is #
                break;
            case 0:
                this.setGene(commandGene, rand.nextBoolean() ? 1 : 2);
                break;
            default:
                // If the gene is 2.
                this.setGene(commandGene, rand.nextInt(2));
                break;
        }
    }

   
    public void mutateAction(int actionGene) 
    {
        if (this.getGene(actionGene) == 1) 
        {
            this.setGene(actionGene, 0);
        } else {
            this.setGene(actionGene, 1);
        }
    }


    public void CalculateFitnessBaseOnNewRules(int numR, int rSize, Rules[] data, Rules[] rule, int dataSize)
    {
        int indexHolder = 0;
        int counter = 0; //fitness count
        //place the individual's gene into the rule set.
        for (int i = 0; i < numR; i++) 
        {
            // Copy command from gene using indexHolder to work out the next command
            // start and end location.
            rule[i].setCondition(Arrays.copyOfRange(chromosome, indexHolder, indexHolder + rSize));
            // Increment to get to the action index.
            indexHolder += rSize;
            // Set the action and increment to get to the next command start index.
            rule[i].setAction(this.chromosome[indexHolder]);//keeps an index of the genes that its gone through 
            indexHolder++; // increments to take
        }
       
        for (int i = 0; i < dataSize; i++) 
        {
            for (int j = 0; j < numR; j++) 
            {
                if (checkData(rule[j].getCondition(), data[i].getCondition())&&(rule[j].getAction() == data[i].getAction())) 
                {
                   
                        counter++;
                    break;
                }
            }
        }
        this.setFitness(counter);
    }



    public int getGene(int offset) 
    {
        return this.chromosome[offset];
    }

    public void setGene(int offset, int gene) 
    {
        this.chromosome[offset] = gene;
    }

    public int[] getChromosome() 
    {
        return chromosome;
    }

    public int getFitness() 
    {
        return fitness;
    }

    public void setFitness(int fitness) 
    {
        this.fitness = fitness;
    }

    public boolean checkData(int[] rule, int[] data) 
    {
        boolean foundMatch = true;

        if (rule.length == data.length) 
        {
            for (int i = 0; i < rule.length; i++) 
            {
                if (rule[i] != data[i]) 
                {
                    if (rule[i] != 2) 
                    {
                        foundMatch = false;
                        i = rule.length;
                    }
                }
            }
        } else 
        {
            foundMatch = false;
        }
        return foundMatch;
    }

    @Override
    public String toString() 
    {
        String rep = " ";
        for (int i = 0; i < this.chromosome.length; i++) 
        {
            rep += this.getGene(i);
        }
        System.out.println("Fitness: " + this.getFitness());
        return rep;
    }
}
