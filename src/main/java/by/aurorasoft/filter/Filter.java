package by.aurorasoft.filter;

import java.util.List;

@FunctionalInterface
public interface Filter<TypeOfObject> {
    List<TypeOfObject> filter(final List<TypeOfObject> filteredObjects);
}
