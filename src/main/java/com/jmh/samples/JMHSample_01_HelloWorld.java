package com.jmh.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JMHSample_01_HelloWorld {

    @Benchmark
    public void hello() {

    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(JMHSample_01_HelloWorld.class.getSimpleName())
                .forks(1).build();
        new Runner(options).run();
    }
}
