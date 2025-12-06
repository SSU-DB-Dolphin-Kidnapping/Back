SELECT h.test_id,
       COUNT(CASE WHEN h.is_success = 1 THEN 1 END) AS total_success,
       COUNT(CASE WHEN h.is_success = 0 THEN 1 END) AS total_fail
FROM history h
WHERE h.test_id = (SELECT MAX(id) FROM test)
GROUP BY h.test_id;