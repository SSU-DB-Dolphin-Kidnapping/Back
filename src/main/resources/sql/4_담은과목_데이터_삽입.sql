INSERT INTO bucket_element (bucket_id, teach_id, priority, created_at, updated_at)
VALUES (
           (SELECT id FROM bucket WHERE student_id = 1 AND name = '플랜 B' LIMIT 1),
           21, -- 머신러닝
           1,
           NOW(),
           NOW()
       );