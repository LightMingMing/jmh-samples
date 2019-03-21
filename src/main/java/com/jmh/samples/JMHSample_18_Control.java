package com.jmh.samples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Control;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

@State(Scope.Group)
public class JMHSample_18_Control {
    public final AtomicBoolean flag = new AtomicBoolean();

    @Benchmark
    @Group("ping_pong")
    public void ping(Control cnt) {
        while (!cnt.stopMeasurement && !flag.compareAndSet(false, true)) {

        }
    }

    @Benchmark
    @Group("ping_pong")
    public void pong(Control cnt) {
        while (!cnt.stopMeasurement && !flag.compareAndSet(true, false)) {

        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_18_Control.class.getSimpleName())
                .threads(2)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
