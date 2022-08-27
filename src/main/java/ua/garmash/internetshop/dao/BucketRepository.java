package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Bucket;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
}

