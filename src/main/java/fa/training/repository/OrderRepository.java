package fa.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fa.training.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
