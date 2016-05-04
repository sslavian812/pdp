package ru.ifmo.ctddev.scheduling.genetics;

import ru.ifmo.ctddev.scheduling.ScheduleData;
import ru.ifmo.ctddev.scheduling.Scheduler;
import ru.ifmo.ctddev.scheduling.strategies.ConstantStrategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * todo this class is not thread save
 * Created by viacheslav on 18.02.2016.
 */
public class GeneticStrategyScheduler implements Scheduler {

    public static final int BIG_MUTATION_AMOUNT = 100;
    private String comment;

    // internals:
    private ConstantStrategy strategy;
    private ScheduleData originalScheduleData;
    private List<int[]> currentGeneration;


    //parameters:
    private int G; // generation - size of generation
    private int R; // number of new created individuals at point of reproduction
    private double Pm; // probability of mutation for each inividual.
    private int S; // steps of algorith to be done
    private int E; // (E <= G) number of "elite" individuals to be selected to new generation
    private int T; // number of tournaments to select G-E non-elite inidividuals

    /**
     * if true, new population will be chosen only from children.
     * if false, new population will be chosen from parents and children.
     */
    private boolean onlyChildren;
    private boolean isBigMutationAllowed;

    private double initialCost;
    private BufferedWriter logFile = null;
    private List<String> xs = new ArrayList<>();
    private List<String> ys = new ArrayList<>();


    public GeneticStrategyScheduler(ConstantStrategy strategy) {
        this(strategy, 100, 10, 1.0, 20, 10, 200, false);
    }

    public GeneticStrategyScheduler(ConstantStrategy strategy, int g, int r, double pm, int e, int t, int s) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = false;
        this.isBigMutationAllowed = false;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
    }

    public GeneticStrategyScheduler(ConstantStrategy strategy, int g, int r, double pm, int e, int t, int s, boolean onlyChildren) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = onlyChildren;
        isBigMutationAllowed = false;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
    }


    public GeneticStrategyScheduler(ConstantStrategy strategy, int g, int r, double pm, int e, int t, int s, boolean onlyChildren, boolean isBigMutationAllowed) {
        this.strategy = strategy;
        currentGeneration = new ArrayList<>();
        G = g;
        R = r;
        Pm = pm;
        E = e;
        T = t;
        S = s;
        this.onlyChildren = onlyChildren;
        this.isBigMutationAllowed = isBigMutationAllowed;
        if (E > G)
            throw new RuntimeException("should be E <= G. ");
    }


    /**
     * Creates a schedule for this {@code scheduleData} in genetic way.
     * <p>
     * Note: this method may take a long time.
     *
     * @param scheduleData
     * @return returns optimisation ratio: (initialCost - reachedCost) / initialCost
     */
    @Override
    public double schedule(ScheduleData scheduleData) {
        try {
            logFile = new BufferedWriter(new FileWriter("wolfram.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        originalScheduleData = scheduleData;
        initialCost = scheduleData.getCost();
        currentGeneration.clear();

        // form first generation:
        for (int i = 0; i < G; ++i) {
            currentGeneration.add(scheduleData.getRoute().clone());
        }

        for (int i = 0;; ++i) {
            if (isBigMutationAllowed && (i == S / 4 || i == S / 2 || S == 3.0 / 4.0 * (double) S)) {
                currentGeneration = bigMutations(currentGeneration, (int) Math.sqrt(scheduleData.getSize() / 2));
            }
            List<int[]> children = reproduction(currentGeneration);
            List<int[]> mutated = mutation(children);

            if (!onlyChildren) {
                mutated.addAll(currentGeneration);
            }

            currentGeneration = selection(mutated);


            printState(currentGeneration, originalScheduleData);

            if(originalScheduleData.getFitFunctionCallsCount() >= 200000)
                break;
        }

//        Collections.sort(currentGeneration, originalScheduleData);


        // todo uncomment this if you want graphics
        try {
            logFile.write("\"" + comment + "_" + strategy.getComment() + ":\";");
            logFile.write("xs = {");
            logFile.write(String.join(", ", xs));
            logFile.write("};");
            logFile.newLine();
            logFile.write("ys = {");
            logFile.write(String.join(", ", ys));
            logFile.write("};");
            logFile.newLine();
            logFile.write("list = Transpose[{xs, ys}];");
            logFile.newLine();
            logFile.write("ListPlot[list, AxesLabel -> {\"fit function calls\", \"relative cost\"}, PlotLabel -> \"");
            logFile.write(comment + " - " + strategy.getComment());
                    logFile.write("\", Joined -> True, PlotRange -> {{0, 200500}, {0, 1}}]");
            logFile.newLine();
//            logFile.write("\"===================================================\";");
            logFile.newLine();
            logFile.flush();
            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scheduleData.setRoute(currentGeneration.get(0));
        return (initialCost - scheduleData.getCost()) / initialCost;
    }

    private void printState(List<int[]> currentGeneration, ScheduleData originalScheduleData) {
        //(initialCost - reachedCost) / initialCost;
        xs.add("" + originalScheduleData.getFitFunctionCallsCount());
        ys.add("" + ((initialCost - originalScheduleData.getCost(currentGeneration.get(0))) / initialCost));
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment + " " + strategy.getComment();
    }

    public String getJuliaHist(int bins, int pairs, long ms) {
        return "# " + getComment() + " " + strategy.getComment() + System.lineSeparator() +
                "display(" +
                "plot(x=ratios" + System.lineSeparator() +
                ",Geom.histogram(bincount=" + bins + "), "
                + "Guide.xlabel(\"Optimisation ratio\"), Guide.ylabel(\"Frequency\"),"
                + " Guide.title(\"" + getComment() + " " + strategy.getComment() + " (" + pairs + " pairs)\")))" + System.lineSeparator()
                + "display(\"mean= $(mean(ratios)) , std= $(std(ratios))\")\n"
                + "display(\"average per run: " + ms + " ms\")";
    }

//    public String getJulaiCells(int bins, int pairs) {
//        String head = "{\n" +
//                "   \"cell_type\": \"code\",\n" +
//                "   \"execution_count\": null,\n" +
//                "   \"metadata\": {\n" +
//                "    \"collapsed\": true\n" +
//                "   },\n" +
//                "   \"outputs\": [],\n" +
//                "   \"source\": [";
//        String tail = " ]\n" +
//                "  }";
//
//        List<String> strings = new ArrayList<>();
//        strings.add(head);
//
//        strings.add(enquote("# " + getComment() + " " + strategy.getComment()));
//        strings.add(",\n");
//        strings.add(enquote("plot(x=ratios, Geom.histogram(bincount=" + bins + "), "
//                + "Guide.xlabel(\"Optimisation ratio\"), Guide.ylabel(\"Frequency\"),"
//                + " Guide.title(\"" + getComment() + " (" + pairs + " pairs)\"))"));
////        strings.add(tail +",");
////        strings.add(System.lineSeparator());
////        strings.add(head);
////        strings.add(enquote("# " + getComment() + " " + strategy.getComment()));
//        strings.add(",\n");
//        strings.add(enquote("print(\"mean= $(mean(ratios)), std= $(std(ratios))\")"));
//        strings.add(tail);
//        return String.join("", strings);
//    }
//
//    private String enquote(String s) {
//        return "\"" + s + "\"";
//    }

    private List<int[]> bigMutations(List<int[]> generation, int amount) {
        return generation.stream().map(individual -> {
            for (int i = 0; i < amount; ++i)
                individual = mutate(individual);
            return individual;
        }).collect(Collectors.toList());
    }

    private int[] mutate(int[] individual) {
        originalScheduleData.setRoute(individual);
        int[] route = strategy.getSmallMove().oneStep(originalScheduleData);
        strategy.receiveReward(reward());
        if (originalScheduleData.checkConstraints(route))
            return route;
        else
            return individual;
    }

    private double reward() {
        return 0.0;
    }

    private List<int[]> selection(List<int[]> generation) {
        List<int[]> list = new ArrayList<>(G);
        Collections.sort(generation, originalScheduleData);
        list.addAll(generation.subList(0, E));
        for (int i = 0; i < G - E; ++i) {
            list.add(tournament(generation));
        }
        return list;
    }

    /**
     * Emulates a tournament.
     * T individuals are randomly chosen from generation.
     * The winner is the individual with the best cost.
     *
     * @param generation
     * @return
     */
    private int[] tournament(List<int[]> generation) {
        Random r = new Random();
        List<int[]> participants = new ArrayList<>(T);
        while (participants.size() < T) {
            participants.add(generation.get((int) (r.nextDouble() * generation.size())));
        }
        Collections.sort(participants, originalScheduleData);
        return participants.get(0);
    }

    /**
     * Emulates mutation process.
     * For each individual in generation there will be one try of Optimizer with probability Pm.
     *
     * @param generation
     * @return
     */
    private List<int[]> mutation(List<int[]> generation) {
        final Random r = new Random();
        return generation.stream()
                .map(individual -> {
                    if (r.nextDouble() < Pm)
                        return mutate(individual);
                    else
                        return individual;
                }).collect(Collectors.toList());
    }

    /**
     * Emulates reproduction process.
     * Each individual from generation performs budding reproduction to
     *
     * @param generation
     * @return
     */
    private List<int[]> reproduction(List<int[]> generation) {
        return generation.stream()
                .map(parent -> budding(parent, R))
                .flatMap(List<int[]>::stream)
                .collect(Collectors.toList());
    }


    /**
     * Represents budding reproduction process of one individual.
     *
     * @param oldRoute
     * @param buds     - number of "children"
     * @return
     */
    private List<int[]> budding(int[] oldRoute, int buds) {
        List<int[]> list = new ArrayList<>(buds);

        while (list.size() < buds) {
            list.add(oldRoute.clone());
        }

        return list;
    }

    private List<int[]> buddingWithMutation(int[] oldRoute, int buds) {
        List<int[]> list = new ArrayList<>(buds);

        while (list.size() < buds) {
            originalScheduleData.setRoute(oldRoute);
            int[] route = strategy.getSmallMove().oneStep(originalScheduleData);
            if (originalScheduleData.checkConstraints(route)) {
                list.add(route);
            } else {
                list.add(oldRoute);
            }
        }

        if (!onlyChildren) {
            list.add(oldRoute.clone());
        }
        return list;
    }


    @Override
    public String toString() {
        return "GeneticStrategyScheduler{" + System.lineSeparator() +
                "    comment='" + comment + '\'' + "," + System.lineSeparator() +
                "    strategy=" + strategy.toString("    ") + "," + System.lineSeparator() +
                "    G=" + G + "," + System.lineSeparator() +
                "    R=" + R + "," + System.lineSeparator() +
                "    Pm=" + Pm + "," + System.lineSeparator() +
                "    S=" + S + "," + System.lineSeparator() +
                "    E=" + E + "," + System.lineSeparator() +
                "    T=" + T + "," + System.lineSeparator() +
                "    onlyChildren=" + onlyChildren + "," + System.lineSeparator() +
                "    isBigMutationAllowed=" + isBigMutationAllowed + System.lineSeparator() +
                "}";
    }

    public String getTitle() {
        return comment + " " + strategy.getComment() + (Pm == 0 ? " no mutation" : " ");
    }
}
