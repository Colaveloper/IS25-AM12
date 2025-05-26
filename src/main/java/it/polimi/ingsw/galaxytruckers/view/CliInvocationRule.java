package it.polimi.ingsw.galaxytruckers.view;

import java.util.function.Consumer;
import java.util.function.Predicate;

public record CliInvocationRule(
        String regex,
        String description,
        Predicate<String> additionalCondition,
        Consumer<String> invocationAction
        ) {}
