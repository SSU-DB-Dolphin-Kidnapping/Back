package Dolphin.ShoppingCart.domain.course.repository;

import Dolphin.ShoppingCart.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>{
}
