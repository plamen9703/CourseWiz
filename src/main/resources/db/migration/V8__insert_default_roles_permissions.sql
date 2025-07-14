INSERT INTO users.roles(name)
VALUES
('admin'),
('instructor'),
('student'),
('instructor-admin'),
('student-admin'),
('course-admin');

INSERT INTO users.permissions(name)
VALUES
('create_own'),
('view_own'),
('update_own'),
('delete_own'),
('view_students_own');
