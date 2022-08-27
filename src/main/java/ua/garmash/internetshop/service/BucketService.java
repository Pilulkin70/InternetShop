package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.BucketDto;
import ua.garmash.internetshop.model.Bucket;
import ua.garmash.internetshop.model.User;

public interface BucketService {
    Bucket createBucket(User user, Long productId);

    void addItem(Bucket bucket, Long productIds);

    void delItemsById(String userName, Long productId);

    void updateAmount(String userName, Long productId, final long amountDif);

    BucketDto getBucketByUser(String userName);

    Long commitBucketToOrder(String userName);

    void clearBucket(String userName);
}
