import equations.EquationCalculator;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadingPerformanceTest {

    private ExecutorService s = Executors.newFixedThreadPool(5);

    private long defaultStartTime;
    private long multithreadingStartTime;

    private long defaultTime;
    private long multithreadingTime;

    public void startBenchmark(int loopExponent) {
        System.out.println("Starting benchmark");

        multithreadingStartTime = System.currentTimeMillis();
        for(int i = 0; i < Math.pow(4, loopExponent); i++) {
            if(i == Math.pow(4, loopExponent)) {
                s.execute(() -> {
                    multithreadingTime = System.currentTimeMillis() - multithreadingStartTime;
                    System.out.println("Multiple Thread execution: " + multithreadingTime + " ms");
                });
            } else {
                s.execute(() -> {
                    //System.out.println("Calc");
                    for(int a = 0; a < 4; a++) {
                        String s = "10+10+10";
                        int result = EquationCalculator.calculate(s);
                    }
                });
            }
        }
        s.shutdown();

        /*defaultStartTime = System.currentTimeMillis();
        for(int i = 0; i < Math.pow(4, loopExponent); i++) {
            for(int a = 0; a < 4; a++) {
                String s = "10+10+10";
                int result = EquationCalculator.calculate(s);
            }
        }
        defaultTime = System.currentTimeMillis() - defaultStartTime;*/

        System.out.println("Benchmark complete");
        System.out.println("Single Thread execution: " + defaultTime + " ms");
    }

}