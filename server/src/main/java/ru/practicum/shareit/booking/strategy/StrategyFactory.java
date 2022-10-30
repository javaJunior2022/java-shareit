package ru.practicum.shareit.booking.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.strategy.strategyimpl.Strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyFactory {

    private Map<BookingState, Strategy> strategies;

    @Autowired
    public StrategyFactory(Set<Strategy> strategySet) {
        createStrategy(strategySet);
    }

    public Strategy findStrategy(BookingState state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<Strategy> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy -> strategies.put(strategy.getState(), strategy));
    }

}
