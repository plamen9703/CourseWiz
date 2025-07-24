CREATE SCHEMA IF NOT EXISTS users;

SET search_path TO users;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_username UNIQUE (username),
    CONSTRAINT unique_email UNIQUE (email)
);

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Permissions Table
CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- User Roles (Many-to-Many)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Role Permissions (Many-to-Many)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- User Permissions (Direct permissions)
CREATE TABLE IF NOT EXISTS user_permissions (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, permission_id)
);

-- User Profile (Link to Coursera)
CREATE TABLE IF NOT EXISTS user_profile (
    user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    student_pin VARCHAR(10) REFERENCES coursera.students(pin) ON DELETE CASCADE,
    instructor_id INT REFERENCES coursera.instructors(id) ON DELETE CASCADE,
    CONSTRAINT user_profile_type CHECK (
        (student_pin IS NULL AND instructor_id IS NULL) OR
        (student_pin IS NOT NULL AND instructor_id IS NULL) OR
        (student_pin IS NULL AND instructor_id IS NOT NULL)
    )
);

-- Indexes for Performance
CREATE  INDEX IF NOT EXISTS idx_user_roles_user ON user_roles(user_id);
CREATE  INDEX IF NOT EXISTS idx_user_roles_role ON user_roles(role_id);
CREATE  INDEX IF NOT EXISTS idx_role_permissions_role ON role_permissions(role_id);
CREATE  INDEX IF NOT EXISTS idx_role_permissions_permission ON role_permissions(permission_id);
CREATE  INDEX IF NOT EXISTS idx_user_permissions_user ON user_permissions(user_id);
CREATE  INDEX IF NOT EXISTS idx_user_permissions_permission ON user_permissions(permission_id);
