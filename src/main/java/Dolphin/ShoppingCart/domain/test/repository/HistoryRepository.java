package Dolphin.ShoppingCart.domain.test.repository;

import Dolphin.ShoppingCart.domain.test.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>{
}
