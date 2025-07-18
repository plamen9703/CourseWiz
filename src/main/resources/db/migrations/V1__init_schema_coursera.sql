CREATE SCHEMA IF NOT EXISTS coursera;

SET search_path TO coursera;

CREATE SEQUENCE IF NOT EXISTS student_pin_seq
    INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9999999 CACHE 1;

CREATE TABLE IF NOT EXISTS instructors
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS courses
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    instructor_id INT NOT NULL,
    total_time SMALLINT NOT NULL,
    credit SMALLINT NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT courses_instructor_fk FOREIGN KEY (instructor_id)
        REFERENCES instructors (id)
);

CREATE INDEX IF NOT EXISTS idx_course_instructor_id
    ON courses (instructor_id);

CREATE TABLE IF NOT EXISTS students
(
    pin VARCHAR(10) NOT NULL DEFAULT ('STU' || lpad(nextval('student_pin_seq')::text, 7, '0')),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT students_pkey PRIMARY KEY (pin)
);

CREATE TABLE IF NOT EXISTS student_course_xref
(
    student_pin VARCHAR(10) NOT NULL,
    course_id INT NOT NULL,
    completion_date DATE,
    PRIMARY KEY (student_pin, course_id),
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (student_pin) REFERENCES students (pin)
);

CREATE INDEX IF NOT EXISTS idx_student_course_completion_date ON student_course_xref (completion_date);
CREATE INDEX IF NOT EXISTS idx_student_course_id ON student_course_xref (course_id);
CREATE INDEX IF NOT EXISTS idx_student_course_pin ON student_course_xref (student_pin);
