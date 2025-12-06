DELETE FROM bucket_element
WHERE teach_id = 21
  AND bucket_id = (SELECT id FROM bucket WHERE student_id = 1 AND name = '플랜 B' LIMIT 1);