/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.solver.event;

import java.util.Iterator;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.impl.solver.DefaultSolver;

/**
 * Internal API.
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public class SolverEventSupport<Solution_> extends AbstractEventSupport<SolverEventListener<Solution_>> {

    private DefaultSolver<Solution_> solver;

    public SolverEventSupport(DefaultSolver<Solution_> solver) {
        this.solver = solver;
    }

    public void fireBestSolutionChanged(Solution_ newBestSolution, int newUninitializedVariableCount) {
        final Iterator<SolverEventListener<Solution_>> it = eventListenerSet.iterator();
        long timeMillisSpent = solver.getSolverScope().calculateTimeMillisSpent();
        Score newBestScore = solver.getSolverScope().getSolutionDescriptor().getScore(newBestSolution);
        if (it.hasNext()) {
            final BestSolutionChangedEvent<Solution_> event = new BestSolutionChangedEvent<Solution_>(solver,
                    timeMillisSpent, newBestSolution, newBestScore, newUninitializedVariableCount);
            do {
                it.next().bestSolutionChanged(event);
            } while (it.hasNext());
        }
    }

}
