package com.mundo.delivery.repository;

import com.mundo.delivery.documents.locationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface latLngRepository extends MongoRepository<locationDocument, String> {
}
