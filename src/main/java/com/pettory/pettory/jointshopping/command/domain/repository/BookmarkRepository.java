package com.pettory.pettory.jointshopping.command.domain.repository;

import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.user.command.domain.aggregate.User;

public interface BookmarkRepository {
    Bookmark save(Bookmark newBookmark);

    void deleteById(Long bookmarkNum);

    Bookmark findByJointShoppingGroupAndUser(JointShoppingGroup jointShoppingGroup, User user);
}
