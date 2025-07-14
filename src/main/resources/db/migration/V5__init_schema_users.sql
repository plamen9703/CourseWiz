CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_username UNIQUE (username),
    CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE users.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);
CREATE TABLE users.permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);
CREATE TABLE users.user_roles (
    user_id INTEGER REFERENCES users.users(id) ON DELETE CASCADE,
    role_id INTEGER REFERENCES users.roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
CREATE TABLE users.role_permissions (
    role_id INTEGER REFERENCES users.roles(id) ON DELETE CASCADE,
    permission_id INTEGER REFERENCES users.permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);
CREATE TABLE users.user_permissions (
    user_id INT REFERENCES users.users(id) ON DELETE CASCADE,
    permission_id INT REFERENCES users.permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, permission_id)
);
CREATE TABLE users.user_profile (
    user_id INT PRIMARY KEY REFERENCES users.users(id) ON DELETE CASCADE,
    student_pin VARCHAR(10) REFERENCES coursera.students(pin),
    instructor_id INT REFERENCES coursera.instructors(id),
    CONSTRAINT user_profile_type CHECK(
        (student_pin IS NULL AND instructor_id IS NULL) OR
        (student_pin IS NOT NULL AND instructor_id IS NULL) OR
        (student_pin IS NULL AND instructor_id IS NOT NULL)
    )
);
