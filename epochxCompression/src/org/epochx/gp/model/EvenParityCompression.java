/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.gp.model;

import java.util.*;
import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;
import lzwCompression.*;

/**
 * GP model for the even parity problems.
 * 
 * <h4>Even parity problem</h4>
 * 
 * Given n binary inputValues, a program that solves the even-n-parity problem
 * will
 * return true in all circumstances where an even number of the inputValues are
 * true
 * (or 1), and return false whenever there is an odd number of true inputValues.
 */
public class EvenParityCompression extends GPModel {

	// The names of the inputValues used in the grammar.
	private final Variable[] variables;

	// The boolean input sequences.
	private final boolean[][] inputValues;

	/**
	 * Constructs an EvenParity model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the even parity problem should be
	 *        for
	 */
	public EvenParityCompression(final int noInputBits) {
		// Generate the input sequences.
		inputValues = BoolUtils.generateBoolSequences(noInputBits);

		// Define functions.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new NandFunction());
		syntax.add(new AndFunction());
		syntax.add(new OrFunction());
		syntax.add(new NotFunction());

		// Define terminal variables.
		variables = new Variable[noInputBits];
		for (int i = 0; i < noInputBits; i++) {
			variables[i] = new Variable("d" + i, Boolean.class);
			syntax.add(variables[i]);
		}

		setSyntax(syntax);
	}

	/**
	 * Calculates the fitness score for the given program. The fitness of a
	 * program for the even-parity problem is calculated by evaluating it
	 * using each of the possible sets of input values. There are
	 * <code>2^noInputBits</code> possible sets of inputs. The fitness of the
	 * program is the quantity of those input sequences that the program
	 * returned an incorrect response for. That is, a fitness value of
	 * <code>0.0</code> indicates the program responded correctly for every
	 * possible set of input values.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;

		double score = 0;
                //CWP
                boolean[] outputs = new boolean[inputValues.length];
                boolean[] targets = new boolean[inputValues.length];
                
		// Execute on all possible inputs.
                int j=0;
		for (final boolean[] in: inputValues) 
                {
                    for (int i = 0; i < in.length; i++) 
                    {
                        variables[i].setValue(in[i]);
                    }
                    
                    outputs[j] = (Boolean)program.evaluate();
                    targets[j] = isEvenNoTrue(in);
                    
                    //if ((Boolean) program.evaluate() == isEvenNoTrue(in)) 
                    //{
                    //    score++;
                    //}
                    j++;
		}
                
                //just print it!
                System.out.println("\n");
                System.out.print("Output:      ");
                for (int k=0;k<inputValues.length;k++)
                { System.out.print(outputs[k]?0:1); }
                System.out.println();
                System.out.print("Target:      ");
                for (int k=0;k<inputValues.length;k++)
                { System.out.print(targets[k]?0:1); }
                System.out.println();
                //end of print it!
                
                boolean[] difference = new boolean[inputValues.length];

                for (int i=0;i<inputValues.length;i++)
                {
                    difference[i] = outputs[i]==targets[i];
                }
                System.out.print("Difference:  ");
                for (int k=0;k<inputValues.length;k++)
                { System.out.print(difference[k]?0:1); }
                System.out.println();
                
                /** calculate the compressed size of the difference **/
                int compressedLength = lzwCompression.compress(difference);
                System.out.println(compressedLength);
                int output = compressedLength;
                
                /* check for complete solution */
                boolean solved = true;
                for (int i=0;i<inputValues.length;i++)
                {
                    if (outputs[i]!=targets[i]) { solved = false; }
                }
                if (solved) { output = 0; }
                    
		return output;
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private boolean isEvenNoTrue(final boolean[] input) {
		int noTrues = 0;

		for (final boolean b: input) {
			if (b) {
				noTrues++;
			}
		}

		return ((noTrues % 2) == 0);
	}

	@Override
	public Class<?> getReturnType() {
		return Boolean.class;
	}

}
