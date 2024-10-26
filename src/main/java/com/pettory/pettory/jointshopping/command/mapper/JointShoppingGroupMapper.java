package com.pettory.pettory.jointshopping.command.mapper;

import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.user.command.domain.aggregate.User;

public class JointShoppingGroupMapper {

    public static JointShoppingGroup toEntity(JointShoppingGroupRequest groupRequest, User user, JointShoppingCategory category) {
        return JointShoppingGroup.create(
                groupRequest.getJointShoppingGroupName(),
                groupRequest.getJointShoppingProducts(),
                groupRequest.getJointShoppingInfo(),
                groupRequest.getJointShoppingCost(),
                groupRequest.getJointShoppingGroupMaximumCount(),
                groupRequest.getJointShoppingParticipationMaximumCount(),
                category,
                user
        );
    }
}
