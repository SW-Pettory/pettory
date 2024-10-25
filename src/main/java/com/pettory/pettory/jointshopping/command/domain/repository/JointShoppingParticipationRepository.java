package com.pettory.pettory.jointshopping.command.domain.repository;

import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationState;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;
import com.pettory.pettory.user.command.domain.aggregate.User;

import java.util.List;
import java.util.Optional;

public interface JointShoppingParticipationRepository {
    JointShoppingParticipationUser save(JointShoppingParticipationUser newJointShoppingParticipationUser);

    void deleteById(Long participationNum);

    Optional<JointShoppingParticipationUser> findById(Long participationNum);

    List<JointShoppingParticipationUser> findByJointShoppingGroup(JointShoppingGroup jointShoppingGroup);

    List<JointShoppingParticipationUser> findByJointShoppingGroupAndProductsReceiptYnTrue(JointShoppingGroup jointShoppingGroup);

    JointShoppingParticipationUser findByJointShoppingGroupAndUserAndParticipationState(JointShoppingGroup jointShoppingGroup, User user, JointShoppingParticipationState active);
}
