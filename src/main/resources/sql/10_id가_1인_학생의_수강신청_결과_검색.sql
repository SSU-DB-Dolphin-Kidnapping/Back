SELECT b.name AS bucket_name, c.name AS course_name, h.is_success, h.failed_reason
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN bucket b ON be.bucket_id = b.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
WHERE b.student_id = 1;