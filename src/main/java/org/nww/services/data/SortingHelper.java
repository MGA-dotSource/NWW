package org.nww.services.data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides helper methods for sorting list elements.
 * @param <T>
 */
public class SortingHelper<T> {

    /**
     * Reverse the elements of the passed list into a new ArrayList.
     * @param input the unchanged list of elements to be reverted
     * @return the reverted list
     */
    public List<T> reverse(List<T> input) {

        List<T> reverted = new ArrayList<T>(input.size());

        for (int i = input.size() - 1; i >= 0; i--) {
            reverted.add(input.get(i));
        }
        return reverted;
    }
}
