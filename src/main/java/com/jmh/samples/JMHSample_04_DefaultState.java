package com.jmh.samples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class JMHSample_04_DefaultState {
    double x = Math.PI;

    @Benchmark
    public void measure() {
        x ++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(JMHSample_04_DefaultState.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }
}
