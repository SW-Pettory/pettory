package com.pettory.pettory.jointshopping.command.domain.aggregate;

import com.pettory.pettory.pet.command.domain.aggregate.Pet;
import com.pettory.pettory.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkNum;

    @ManyToOne
    @JoinColumn(name = "joint_shopping_group_num", nullable = false)
    JointShoppingGroup jointShoppingGroup;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public Bookmark(JointShoppingGroup jointShoppingGroup, User user) {
        this.jointShoppingGroup = jointShoppingGroup;
        this.user = user;
    }

    public static Bookmark create(JointShoppingGroup jointShoppingGroup, User user) {
        return new Bookmark(jointShoppingGroup,user);
    }

}
