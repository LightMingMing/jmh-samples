package com.jmh.samples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.*;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JMHSample_07_FixtureLevelInvocation {

    @State(Scope.Benchmark)
    public static class NormalState {

        ExecutorService executorService;

        @Setup(Level.Trial)
        public void setup() {
            executorService = Executors.newCachedThreadPool();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            executorService.shutdown();
        }
    }

    public static class LaggingState extends NormalState {

        public static final int SLEEP_TIME = Integer.getInteger("sleepTime", 10);

        @Setup(Level.Invocation)
        public void lag() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public double measureHot(NormalState normalState, final Scratch scratch) throws ExecutionException, InterruptedException {
        return normalState.executorService.submit(new Task(scratch)).get();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public double measureCold(LaggingState laggingState, final Scratch scratch) throws ExecutionException, InterruptedException {
        return laggingState.executorService.submit(new Task(scratch)).get();
    }

    @State(Scope.Thread)
    public static class Scratch {
        private double p;

        public double doWork() {
            return p = Math.log(p);
        }
    }

    public static class Task implements Callable<Double> {
        private Scratch s;

        public Task(Scratch s) {
            this.s = s;
        }

        @Override
        public Double call() throws Exception {
            return s.doWork();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(JMHSample_07_FixtureLevelInvocation.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }

}
