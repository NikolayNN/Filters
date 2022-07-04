package by.aurorasoft;

import java.util.List;

public interface Filter<TypeOfResult>
{
    public abstract List<TypeOfResult> filter(final List<TypeOfResult> filteredObjects);
}
