package com.pettory.pettory.jointshopping.command.mapper;

import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroupUser;
import com.pettory.pettory.user.command.domain.aggregate.User;

public class JointShoppingGroupUserMapper {
    public static JointShoppingGroupUser toEntity(JointShoppingGroup jointShoppingGroup, User user) {
        return JointShoppingGroupUser.create(
                jointShoppingGroup,
                user
        );
    }
}
