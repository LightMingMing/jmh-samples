MicroBenchmark  
[jmh-samples](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)
```
@Benchmark

@BenchmarkMode
	Mode
		1. throughput
		2. averageTime
		3. sampleTime  每次操作样本的时间
		4. singleShotTime


@State
	Scope
		1. benchmark
		2. group
		3. thread
	
@OutputTimeUnit(TimeUnit)


@Setup
@TearDown
	Level (默认为trial)
		1. trial  	整个方法的测试   
		2. iteration   一次迭代测试
		3. invocation	一次方法调用 >1 millisecond
			警告：
				1. 实际执行时间短，会有很多的时间戳请求，在人工方面引入一些延迟、吞吐量、伸缩性的影响
				2. 一些遗漏，可能测量中的一些打嗝会隐藏起来，虚假的高吞吐量
				3. 共享状态时，需要同步访问State对象，其它级别能够在度量之外维持，但是该级别需要同步在关键的路径上，导致度量偏移

Blackhole

@OperationsPerInvocation

@Fork
@Group

@CompilerControl
	CompilerControl.Mode
		INLINE
		DNOT_INLIE
		EXCLUDE
```