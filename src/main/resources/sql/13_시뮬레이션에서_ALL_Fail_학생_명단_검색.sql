SELECT s.student_name, s.student_number
FROM student s
WHERE s.id IN (
    SELECT b.student_id
    FROM history h
             JOIN bucket_element be ON h.bucket_element_id = be.id
             JOIN bucket b ON be.bucket_id = b.id
    WHERE h.test_id = (SELECT MAX(id) FROM test)
    GROUP BY b.student_id
    HAVING SUM(CASE WHEN h.is_success = 1 THEN 1 ELSE 0 END) = 0
);