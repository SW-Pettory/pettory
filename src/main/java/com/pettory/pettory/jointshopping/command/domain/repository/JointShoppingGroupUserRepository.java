package com.pettory.pettory.jointshopping.command.domain.repository;

import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroupUser;
import com.pettory.pettory.user.command.domain.aggregate.User;

import java.util.List;
import java.util.Optional;

public interface JointShoppingGroupUserRepository {
    JointShoppingGroupUser save(JointShoppingGroupUser newJointShoppingGroupUser);

    void deleteById(Long jointShoppingGroupUserNum);

    Optional<JointShoppingGroupUser> findById(Long jointShoppingGroupUserNum);

    void delete(JointShoppingGroupUser newJointShoppingGroupUser);

    JointShoppingGroupUser saveAndFlush(JointShoppingGroupUser newJointShoppingGroupUser);

    List<JointShoppingGroupUser> findByJointShoppingGroup(JointShoppingGroup jointShoppingGroup);

    List<JointShoppingGroupUser> findByJointShoppingGroupAndUserAndResignYnTrue(JointShoppingGroup jointShoppingGroup, User user);
}
