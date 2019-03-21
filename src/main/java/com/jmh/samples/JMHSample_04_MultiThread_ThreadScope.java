package com.jmh.samples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class JMHSample_04_MultiThread_ThreadScope {
    double x = Math.PI;

    @Benchmark
    public void measure() {
        x++;
    }

    @TearDown(Level.Trial)
    public void tearDownOfTrial() {
        System.out.println("\n" + Thread.currentThread() + " trial:     " + x);
    }

    @TearDown(Level.Iteration)
    public void tearDownForIteration() {
        System.out.println("\n" + Thread.currentThread() + " iteration: " + x);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(JMHSample_04_MultiThread_ThreadScope.class.getSimpleName()).forks(1).threads(4).build();
        new Runner(opt).run();
    }
}