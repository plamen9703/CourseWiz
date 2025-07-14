CREATE SCHEMA IF NOT EXISTS coursera;

CREATE SEQUENCE IF NOT EXISTS coursera.student_pin_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9999999
    CACHE 1;
CREATE TABLE IF NOT EXISTS coursera.students(
    pin character varying(10) NOT NULL DEFAULT ('STU'::text || lpad((nextval('coursera.student_pin_seq'::regclass))::text, 7, '0'::text)),
    first_name character varying(50)  NOT NULL,
    last_name character varying(50)  NOT NULL,
    time_created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT students_pkey PRIMARY KEY (pin)
);

CREATE TABLE IF NOT EXISTS coursera.instructors(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    first_name character varying(50)  NOT NULL,
    last_name character varying(50) NOT NULL,
    time_created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT instructors_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS coursera.courses(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(150) NOT NULL,
    instructor_id integer NOT NULL,
    total_time smallint NOT NULL,
    credit smallint NOT NULL,
    time_created timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT courses_pkey PRIMARY KEY (id),
    CONSTRAINT courses_instructor_fk FOREIGN KEY (instructor_id)
        REFERENCES coursera.instructors (id)
);

CREATE TABLE IF NOT EXISTS coursera.student_course_xref(
    student_pin character varying(10) NOT NULL,
    course_id integer NOT NULL,
    completion_date date,
    CONSTRAINT student_course_xref_pkey PRIMARY KEY (student_pin, course_id),
    CONSTRAINT course_student_fk FOREIGN KEY (course_id)
        REFERENCES coursera.courses (id),
    CONSTRAINT student_course_fk FOREIGN KEY (student_pin)
        REFERENCES coursera.students (pin)
);

