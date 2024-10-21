package com.pettory.pettory.jointshopping.command.domain.repository;

import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;

import java.util.List;
import java.util.Optional;

public interface JointShoppingParticipationRepository {
    JointShoppingParticipationUser save(JointShoppingParticipationUser newJointShoppingParticipationUser);

    void deleteById(Long participationNum);

    Optional<JointShoppingParticipationUser> findById(Long participationNum);

    List<JointShoppingParticipationUser> findByJointShoppingGroup(JointShoppingGroup jointShoppingGroup);

    List<JointShoppingParticipationUser> findByJointShoppingGroupAndProductsReceiptYnTrue(JointShoppingGroup jointShoppingGroup);
}
