SELECT s.student_name, SUM(c.credit) AS total_earned_credits
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN bucket b ON be.bucket_id = b.id
         JOIN student s ON b.student_id = s.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
WHERE s.id = 1 AND h.is_success = 1
GROUP BY s.id;