package recipes.persistent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import recipes.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
        User findUserByEmail(String email);
}
