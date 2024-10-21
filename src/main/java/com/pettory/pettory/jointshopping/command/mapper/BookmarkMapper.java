package com.pettory.pettory.jointshopping.command.mapper;

import com.pettory.pettory.jointshopping.command.application.dto.JointShoppingGroupRequest;
import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.user.command.domain.aggregate.User;

public class BookmarkMapper {

    public static Bookmark toEntity(JointShoppingGroup jointShoppingGroup, User user) {
        return Bookmark.create(
                jointShoppingGroup,
                user
        );
    }
}
