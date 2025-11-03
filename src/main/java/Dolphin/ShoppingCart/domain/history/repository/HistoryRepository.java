package Dolphin.ShoppingCart.domain.history.repository;

import Dolphin.ShoppingCart.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>{
}
