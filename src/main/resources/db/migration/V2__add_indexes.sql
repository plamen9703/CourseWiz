CREATE INDEX IF NOT EXISTS idx_course_instructor_id
    ON coursera.courses USING btree
    (instructor_id ASC NULLS LAST)
    WITH (deduplicate_items=True);

CREATE INDEX IF NOT EXISTS idx_student_course_completion_date
    ON coursera.student_course_xref USING btree
    (completion_date ASC NULLS LAST)
    WITH (deduplicate_items=True);