package by.aurorasoft;

import java.util.List;

@FunctionalInterface
public interface Filter<TypeOfObject>
{
    public abstract List<TypeOfObject> filter(final List<TypeOfObject> filteredObjects);

    @FunctionalInterface
    public interface Filterable<TypeOfResult>
    {
        public abstract TypeOfResult filter();
    }
}
