SELECT c.name AS course_name, p.professor_name, t.class_name, ti.day_of_the_week, ti.start_time, ti.end_time
FROM teach t
         JOIN course c ON t.course_id = c.id
         JOIN professor p ON t.professor_id = p.id
         JOIN shoppingcart.teach_info ti On t.id = ti.teach_id
WHERE c.name LIKE '%프로그래밍%';