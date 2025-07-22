package org.example.application.dao.users;

import org.example.application.api.users.User;
import org.example.application.repository.users.UserRepository;

public abstract class UserDAO<T extends User> implements UserRepository<T> {
}

