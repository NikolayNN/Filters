package by.aurorasoft;

import java.util.Collection;
import java.util.List;

public interface Filter {
    List<Filterable> filter(Collection<Filterable> f);
}
