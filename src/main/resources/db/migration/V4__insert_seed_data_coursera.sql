-- Instructors
INSERT INTO coursera.instructors ( first_name, last_name, time_created) VALUES
( 'Alice', 'Newton', NOW() - INTERVAL '500 days'),
( 'Bob', 'Einstein', NOW() - INTERVAL '400 days'),
( 'Clara', 'Curie', NOW() - INTERVAL '300 days'),
( 'Daniel', 'Turing', NOW() - INTERVAL '200 days'),
( 'Eva', 'Lovelace', NOW() - INTERVAL '100 days');

-- Students
INSERT INTO coursera.students ( first_name, last_name, time_created) VALUES
( 'Tom', 'Baker', NOW() - INTERVAL '200 days'),
( 'Jane', 'Doe', NOW() - INTERVAL '180 days'),
( 'Alice', 'Smith', NOW() - INTERVAL '160 days'),
( 'Bob', 'Johnson', NOW() - INTERVAL '140 days'),
( 'Eva', 'Williams', NOW() - INTERVAL '120 days'),
( 'Liam', 'Brown', NOW() - INTERVAL '100 days'),
( 'Mia', 'Davis', NOW() - INTERVAL '80 days'),
( 'Noah', 'Wilson', NOW() - INTERVAL '60 days'),
( 'Ava', 'Moore', NOW() - INTERVAL '40 days'),
( 'Elijah', 'Taylor', NOW() - INTERVAL '20 days');

-- Courses
INSERT INTO coursera.courses (name, instructor_id, total_time, credit, time_created) VALUES
('Intro to Math', 1, 30, 3, NOW() - INTERVAL '300 days'),
('Physics Basics', 2, 45, 4, NOW() - INTERVAL '290 days'),
( 'Chemistry 101', 3, 50, 4, NOW() - INTERVAL '280 days'),
( 'Computer Science', 4, 60, 5, NOW() - INTERVAL '270 days'),
( 'Advanced Math', 1, 40, 3, NOW() - INTERVAL '260 days'),
( 'Biology', 3, 35, 3, NOW() - INTERVAL '250 days'),
( 'Algorithms', 4, 50, 4, NOW() - INTERVAL '240 days'),
( 'Data Structures', 4, 50, 4, NOW() - INTERVAL '230 days'),
( 'English Literature', 5, 25, 2, NOW() - INTERVAL '220 days'),
( 'Creative Writing', 5, 30, 2, NOW() - INTERVAL '210 days'),
( 'Philosophy', 2, 40, 3, NOW() - INTERVAL '200 days'),
( 'Art History', 5, 30, 2, NOW() - INTERVAL '190 days'),
( 'Web Development', 4, 50, 4, NOW() - INTERVAL '180 days'),
( 'Statistics', 1, 45, 4, NOW() - INTERVAL '170 days'),
( 'Machine Learning', 4, 60, 5, NOW() - INTERVAL '160 days');

-- Student–Course Enrollments (xref)
-- We'll give some students high total credits, others low, with completion dates for filtering

INSERT INTO coursera.student_course_xref (student_pin, course_id, completion_date) VALUES
-- S1001 (Tom) — high credits
('STU0000001', 1, '2025-01-15'),
('STU0000001', 2, '2025-02-01'),
('STU0000001', 4, '2025-03-10'),
('STU0000001', 14, '2025-04-05'),

-- S1002 (Jane) — medium credits
('STU0000002', 3, '2025-01-20'),
('STU0000002', 6, '2025-02-18'),
('STU0000002', 11, '2025-03-22'),

-- S1003 (Alice) — low credits
('STU0000003', 9, '2025-04-11'),
('STU0000003', 10, '2025-05-09'),

-- S1004 (Bob) — medium credits
('STU0000004', 5, '2025-03-17'),
('STU0000004', 8, '2025-05-12'),

-- S1005 (Eva) — high credits
('STU0000005', 1, '2025-01-01'),
('STU0000005', 7, '2025-02-14'),
('STU0000005', 15, '2025-06-30'),

-- S1006 (Liam) — one course
('STU0000006', 12, '2025-04-25'),

-- S1007 (Mia) — none completed (should be filtered out)
('STU0000007', 13, NULL),

-- S1008 (Noah) — random
('STU0000008', 4, '2025-03-01'),

-- S1009 (Ava) — random
('STU0000009', 6, '2025-03-19'),
('STU0000009', 13, '2025-05-20'),

-- S1010 (Elijah) — only 1 course
('STU0000010', 11, '2025-02-10');
