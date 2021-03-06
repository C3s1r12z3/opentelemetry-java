/*
 * Copyright 2019, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.metrics;

import io.opentelemetry.metrics.LongMeasure.BoundLongMeasure;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Measure to report instantaneous measurement of a long value.
 *
 * <p>Example:
 *
 * <pre>{@code
 * class YourClass {
 *
 *   private static final Meter meter = OpenTelemetry.getMeterRegistry().get("my_library_name");
 *   private static final LongMeasure measure =
 *       meter.
 *           .longMeasureBuilder("doWork_latency")
 *           .setDescription("gRPC Latency")
 *           .setUnit("ns")
 *           .build();
 *   private static final LongMeasure.BoundLongMeasure boundMeasure = measure.bind(labelset);
 *
 *   void doWork() {
 *      long startTime = System.nanoTime();
 *      // Your code here.
 *      boundMeasure.record(System.nanoTime() - startTime);
 *   }
 * }
 * }</pre>
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface LongMeasure extends Measure<BoundLongMeasure> {

  /**
   * Records the given measurement, associated with the current {@code Context} and provided set of
   * labels.
   *
   * @param value the measurement to record.
   * @param labelKeyValuePairs the set of labels to be associated to this recording
   * @throws IllegalArgumentException if value is negative.
   * @since 0.3.0
   */
  void record(long value, String... labelKeyValuePairs);

  @Override
  BoundLongMeasure bind(String... labelKeyValuePairs);

  /**
   * A {@code Bound Instrument} for a {@code LongMeasure}.
   *
   * @since 0.1.0
   */
  @ThreadSafe
  interface BoundLongMeasure extends InstrumentWithBinding.BoundInstrument {
    /**
     * Records the given measurement, associated with the current {@code Context}.
     *
     * @param value the measurement to record.
     * @throws IllegalArgumentException if value is negative.
     * @since 0.1.0
     */
    void record(long value);

    @Override
    void unbind();
  }

  /** Builder class for {@link LongMeasure}. */
  interface Builder extends Measure.Builder {
    @Override
    Builder setDescription(String description);

    @Override
    Builder setUnit(String unit);

    @Override
    Builder setConstantLabels(Map<String, String> constantLabels);

    @Override
    Builder setAbsolute(boolean absolute);

    @Override
    LongMeasure build();
  }
}
