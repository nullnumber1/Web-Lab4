package nullnumber1.lab4.service;

import nullnumber1.lab4.model.User;

import java.util.List;

public interface UserService {
    User register(User user);

    List<User> getAll();

    User findByUsername(String userName);

    User findById(Long id);

    void delete(Long id);
}
