import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository<T extends Identifiable> implements Repository<T> {

    private final List<T> items = new ArrayList<>();

    public InMemoryRepository() {}

    public InMemoryRepository(Collection<T> initialData) {
        items.addAll(initialData);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Optional<T> findById(int id) {
        return items.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public T save(T entity) {
        deleteById(entity.getId());
        items.add(entity);
        return entity;
    }

    @Override
    public boolean deleteById(int id) {
        return items.removeIf(x -> x.getId() == id);
    }
}
