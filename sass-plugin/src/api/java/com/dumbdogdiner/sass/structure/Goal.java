package com.dumbdogdiner.sass.structure;

import com.dumbdogdiner.sass.api.stats.store.statistic.Statistic;
import com.google.gson.JsonPrimitive;

import java.util.Comparator;
import java.util.Map;

/**
 * A <b>&#2B50; GOAL &#2B50;</b>, which represents an end state for a statistic
 * When the statistic updates, uhh... goals are asked to check and then fire rewards i guess
 */

public class Goal<T> {
    Statistic monitoredStatistic;


    Map<Comparator<T>, Reward> rewards;


    // The idea: when statistic is updated, the statistic's data type T is compared with all
    // comparators, and rewards are thusly issued

}
