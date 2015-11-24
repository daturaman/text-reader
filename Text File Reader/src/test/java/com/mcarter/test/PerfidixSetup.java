package com.mcarter.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.perfidix.AbstractConfig;
import org.perfidix.annotation.BenchmarkConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.MemMeter;
import org.perfidix.meter.Memory;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;

/**
 * Configuration for the Perfidix benchmarking tool.
 * 
 * @author Michael Carter
 *
 */
@BenchmarkConfig
public class PerfidixSetup extends AbstractConfig {

	private static final int RUNS = 50;
	private static final Set<AbstractMeter> PARAM_METERS = new HashSet<>(
			Arrays.asList(new AbstractMeter[] { new TimeMeter(Time.MilliSeconds), new MemMeter(Memory.KibiByte) }));
	private static final Set<AbstractOutput> PARAM_OUTPUT = new HashSet<>(
			Arrays.asList(new AbstractOutput[] { new TabularSummaryOutput() }));
	private static final KindOfArrangement PARAM_ARRANGE = KindOfArrangement.SequentialMethodArrangement;
	private static final double PARAM_GC = 1;

	/**
	 * Default constructor.
	 */
	public PerfidixSetup() {
		super(RUNS, PARAM_METERS, PARAM_OUTPUT, PARAM_ARRANGE, PARAM_GC);
	}
}
