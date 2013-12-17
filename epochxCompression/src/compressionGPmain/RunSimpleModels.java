package compressionGPmain;

import org.epochx.gp.model.*;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import static org.epochx.stats.StatField.*;
import org.epochx.stats.Stats;
import org.epochx.tools.random.MersenneTwisterFast;

public class RunSimpleModels {
    public static void main(String[] args) {

        // Construct the model.
        final GPModel[] models = new GPModel[2];
        models[0] = new EvenParity(4);
        models[1] = new EvenParityCompression(4);

        for (int i=0;i<2;i++)
        {
            models[i].setPopulationSize(500);
            models[i].setNoGenerations(200);
            models[i].setMaxDepth(8);
            models[i].setCrossover(new KozaCrossover(models[i]));
            models[i].setProgramSelector(new TournamentSelector(models[i], 7));
            models[i].setRNG(new MersenneTwisterFast());
        }

        Life.get().addGenerationListener(new GenerationAdapter(){
            @Override
            public void onGenerationEnd() {
                Stats.get().print(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITTEST_PROGRAM);
            }
        });
        
        //System.out.println("\n\nVersion 1: GP with Hamming-based fitness\n");
        //models[0].run();
        System.out.println("\n\nVersion 2: GP with compression-based fitness\n");
        models[1].run();
       
    }
}
