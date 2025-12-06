SELECT c.name AS course_name, p.professor_name, COUNT(h.id) AS fail_count
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
         JOIN professor p ON t.professor_id = p.id
WHERE h.is_success = 0 AND h.failed_reason = '정원 초과'
GROUP BY t.id
ORDER BY fail_count DESC
LIMIT 5;