package com.mundo.delivery.repository;

import com.mundo.delivery.documents.delivery;
import com.mundo.delivery.documents.locationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface deliveryRepository extends MongoRepository<delivery, String> {
}
