-- ========================================
-- 반응시간 구간별 평균 성공률
-- ========================================
-- 반응시간을 0.05초 단위로 구간화하여
-- 각 구간별 평균 성공률 분석

SELECT
    CONCAT(
        FLOOR(s.avg_reaction_time / 0.05) * 0.05,
        ' ~ ',
        FLOOR(s.avg_reaction_time / 0.05) * 0.05 + 0.05
    ) AS reaction_time_range,
    COUNT(DISTINCT s.id) AS student_count,
    COUNT(h.id) AS total_attempts,
    SUM(CASE WHEN h.is_success = 1 THEN 1 ELSE 0 END) AS total_success,
    SUM(CASE WHEN h.is_success = 0 THEN 1 ELSE 0 END) AS total_fail,
    ROUND(
        SUM(CASE WHEN h.is_success = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(h.id),
        2
    ) AS avg_success_rate_percent,
    ROUND(AVG(s.avg_reaction_time), 3) AS avg_reaction_time
FROM student s
JOIN bucket b ON s.best_bucket = b.id
JOIN bucket_element be ON b.id = be.bucket_id
JOIN history h ON be.id = h.bucket_element_id
WHERE h.test_id = (SELECT MAX(id) FROM test)
GROUP BY FLOOR(s.avg_reaction_time / 0.05)
ORDER BY FLOOR(s.avg_reaction_time / 0.05) ASC;