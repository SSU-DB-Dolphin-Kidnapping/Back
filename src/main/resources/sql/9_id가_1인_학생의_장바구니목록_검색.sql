SELECT b.name AS bucket_name, c.name AS course_name, be.priority
FROM bucket b
         JOIN bucket_element be ON b.id = be.bucket_id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
         JOIN student s ON b.student_id = s.id
WHERE s.id = 1
ORDER BY b.id, be.priority;