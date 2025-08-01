package fa.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fa.training.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
