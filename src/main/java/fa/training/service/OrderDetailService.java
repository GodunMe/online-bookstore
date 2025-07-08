package fa.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fa.training.model.OrderDetail;
import fa.training.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	List<OrderDetail> getAllOrderItems() {
		return orderDetailRepository.findAll();
	}

	public void save(OrderDetail detail) {
		orderDetailRepository.save(detail);
	}
	
}
