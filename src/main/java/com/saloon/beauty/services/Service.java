package com.saloon.beauty.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Parent abstract class for all the services of model layer
 */
public abstract class Service {

    final boolean EXECUTING_SUCCESSFUL = true;
    final boolean EXECUTING_FAILED = false;

    /**
     * Casts the given object to {@code boolean} type if it is possible
     * @param executingResult - object need to be casted
     * @return - the type of booleans which is contains by object or
     * {@code false} if object is {@code null}
     */
    boolean checkAndCastExecutingResult(Object executingResult) {
        if (Objects.nonNull(executingResult) && executingResult instanceof Boolean) {
            return (Boolean) executingResult;
        } else {
            return false;
        }
    }

    /**
     * Casts the given object to {@code Optional} type if it is possible
     * @param optionalCandidate - object need to be casted
     * @return - the casted to {@code Optional} given object or empty
     * {@code Optional} if object is {@code null}
     */
    <T> Optional<T> checkAndCastObjectToOptional(Object optionalCandidate) {
        if (optionalCandidate instanceof Optional) {
            return (Optional<T>) optionalCandidate;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Casts the given object to {@code List} type if it is possible
     * @param listCandidate - object need to be casted
     * @return - the casted to {@code List} given object or empty
     * {@code List} if object is {@code null}
     */
    <T> List<T> checkAndCastObjectToList(Object listCandidate) {
        if (listCandidate instanceof List) {
            return (List<T>) listCandidate;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Casts the given object to {@code Long} type if it is possible
     * @param longCandidate - object need to be casted
     * @return - the casted to {@code Long} given object or
     * {@code 0} if object is {@code null}
     */
    Long checkAndCastObjectToLong(Object longCandidate) {
        if (longCandidate instanceof Long) {
            return (Long) longCandidate;
        } else {
            return 0L;
        }
    }

}
