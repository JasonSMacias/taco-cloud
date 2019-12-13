package tacos.data;

//import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import tacos.Order;
import tacos.User;

public interface OrderRepository extends CrudRepository<Order, Long> {
	// methods formed as verbSubjectByPredicate where Subject is optional
	// so here find is the verb, and DeliveryZip is the property to match the passed parameter with
//	List<Order> findByDeliveryZip(String deliveryZip);
	/*	in this more complicated method name, read is the verb (synonymous with get and find) could also 
	 * 	use count, Orders is the optional subject (spring actually ignores most subjects and uses
	 * 	the parameter), the predicate refers to two order properties separated by And.  DeliveryZip must 
	 *  match the first parameter, and placedAt is specified to match if between second two */ 
//	List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
	List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
