package com.jmh.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JMHSample_17_SyncIterations {
    private double src;

    /*
     * It turns out if you run the benchmark with multiple threads,
     * the way you start and stop the worker threads seriously affects
     * performance.
     * 事实证明，如果您使用多个线程运行基准测试，那么启动和停止工作线程的方式会严重影响性能。
     *
     * The natural way would be to park all the threads on some sort
     * of barrier, and the let them go "at once". However, that does
     * not work: there are no guarantees the worker threads will start
     * at the same time, meaning other worker threads are working
     * in better conditions, skewing the result.
     * 最自然的方法是把所有的线都放在某种屏障上，然后“立刻”把它们松开。然而，这并不奏效:
     * 不能保证工作线程将同时启动，这意味着其他工作线程的工作条件更好，
     * 从而扭曲了结果。
     *
     * The better solution would be to introduce bogus iterations,
     * ramp up the threads executing the iterations, and then atomically
     * shift the system to measuring stuff. The same thing can be done
     * during the ramp down. This sounds complicated, but JMH already
     * handles that for you.
     * 更好的解决方案是引入伪迭代，增加执行迭代的线程，然后原子性地将系统转移到度量内容上。
     * 同样的事情也可以在下降过程中进行。这听起来很复杂，但是JMH已经为您处理了。
     */
    @Benchmark
    public double test() {
        double s = src;
        for (int i = 0; i < 1000; i++) {
            s = Math.sin(s);
        }
        return s;
    }

    public static void main(String[] args) throws RuntimeException, RunnerException {
        Options opt = new OptionsBuilder().include(JMHSample_17_SyncIterations.class.getSimpleName())
                .warmupTime(TimeValue.seconds(1))
                .measurementTime(TimeValue.seconds(1))
                .threads(Runtime.getRuntime().availableProcessors() * 16)
                .forks(1)
                .syncIterations(true) // switch to false
                .build();
        new Runner(opt).run();
    }

}
