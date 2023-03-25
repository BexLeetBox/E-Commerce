package ntnu.idatt2105.webshop.repositories;

import ntnu.idatt2105.webshop.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Iterable<Message> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(Long senderId, Long receiverId);

}
