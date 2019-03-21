package com.jmh.samples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_12_Forking {
    /*
     * JVMs are notoriously good at profile-guided optimizations. This is bad
     * for benchmarks, because different tests can mix their profiles together,
     * and then render the "uniformly bad" code for every test. Forking (running
     * in a separate process) each test can help to evade this issue.
     *
     * JMH will fork the tests by default.
     */
    public interface Counter {
        int inc();
    }

    public class Counter1 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    public class Counter2 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    public int measure(Counter c) {
        int s = 0;
        for (int i = 0; i < 10; i++) {
            s += c.inc();
        }
        return s;
    }

    Counter1 c1 = new Counter1();
    Counter2 c2 = new Counter2();

    /*
     * We first measure the Counter1 alone...
     * Fork(0) helps to run in the same JVM.
     */
    @Benchmark
    @Fork(0)
    public int measure_1_c1() {
        return measure(c1);
    }

    @Benchmark
    @Fork(0)
    public int measure_2_c2() {
        return measure(c2);
    }

    @Benchmark
    @Fork(0)
    public int measure_3_c1_again() {
        return measure(c1);
    }

    /*
     * These two tests have explicit @Fork annotation.
     * JMH takes this annotation as the request to run the test in the forked JVM.
     * It's even simpler to force this behavior for all the tests via the command
     * line option "-f". The forking is default, but we still use the annotation
     * for the consistency.
     *
     * This is the test for Counter1.
     */
    @Benchmark
    @Fork(1)
    public int measure_4_forked_c1() {
        return measure(c1);
    }

    /*
     * ... and this is the test for Counter2
     */
    @Benchmark
    @Fork(1)
    public int measure_5_forked_c2() {
        return measure(c2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_12_Forking.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
/**
 * # Run progress: 60.00% complete, ETA 00:03:21
 * # Fork: N/A, test runs in the host VM
 * # *** WARNING: Non-forked runs may silently omit JVM options, mess up profilers, disable compiler hints, etc. ***
 * # *** WARNING: Use non-forked runs only for debugging purposes, not for actual performance runs. ***
 * # Warmup Iteration   1: 2.238 ns/op
 * # Warmup Iteration   2: 2.268 ns/op
 * # Warmup Iteration   3: 2.284 ns/op
 * # Warmup Iteration   4: 2.314 ns/op
 * # Warmup Iteration   5: 2.276 ns/op
 */
