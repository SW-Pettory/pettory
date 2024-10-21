package com.pettory.pettory.jointshopping.command.mapper;

import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingParticipationRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingParticipationUser;
import com.pettory.pettory.user.command.domain.aggregate.User;

public class JointShoppingParticipationUserMapper {

    public static JointShoppingParticipationUser toEntity(JointShoppingParticipationRequest participationRequest, JointShoppingGroup jointShoppingGroup, User user) {
        return JointShoppingParticipationUser.create(
                participationRequest.getPaymentCost(),
                jointShoppingGroup,
                user
        );
    }
}
