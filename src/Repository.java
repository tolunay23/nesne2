import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identifiable> {
    List<T> findAll();
    Optional<T> findById(int id);
    T save(T entity);
    boolean deleteById(int id);
}
