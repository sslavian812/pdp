package ru.ifmo.ctddev.old;

import ru.ifmo.ctddev.generate.GausianDatasetGeneratorImpl;
import ru.ifmo.ctddev.generate.UniformDatasetGenerator;
import ru.ifmo.ctddev.scheduling.smallmoves.*;

/**
 * Created by viacheslav on 16.12.2015.
 */
public class GlobalTester {

    public static void main(String[] args) {
        StepTester g1Lin2OptTester        = new StepTester(new GausianDatasetGeneratorImpl(), new Lin2opt());
        StepTester u_Lin2OptTester        = new StepTester(new UniformDatasetGenerator(),     new Lin2opt());

        StepTester g1CoupleExchangeTester = new StepTester(new GausianDatasetGeneratorImpl(), new CoupleExchange());
        StepTester u_CoupleExchangeTester = new StepTester(new UniformDatasetGenerator(),     new CoupleExchange());

        StepTester g1DoubleBridgeTester   = new StepTester(new GausianDatasetGeneratorImpl(), new DoubleBridge());
        StepTester u_DoubleBridgeTester   = new StepTester(new UniformDatasetGenerator(),     new DoubleBridge());


        StepTester g1PointExchangeTester  = new StepTester(new GausianDatasetGeneratorImpl(), new PointExchange());
        StepTester u_PointExchangeTester  = new StepTester(new UniformDatasetGenerator(),     new PointExchange());

        StepTester g1RelocateBlockTester  = new StepTester(new GausianDatasetGeneratorImpl(), new RelocateBlock());
        StepTester u_RelocateBlockTester  = new StepTester(new UniformDatasetGenerator(),     new RelocateBlock());


        g1Lin2OptTester       .run();
        u_Lin2OptTester       .run();
        g1CoupleExchangeTester.run();
        u_CoupleExchangeTester.run();
        g1DoubleBridgeTester  .run();
        u_DoubleBridgeTester  .run();
        g1PointExchangeTester .run();
        u_PointExchangeTester .run();
        g1RelocateBlockTester .run();
        u_RelocateBlockTester .run();
    }
}
