package com.pettory.pettory.jointshopping.command.domain.service;

import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.repository.BookmarkRepository;
import com.pettory.pettory.jointshopping.command.mapper.BookmarkMapper;
import com.pettory.pettory.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkDomainService {

    private final BookmarkRepository bookmarkRepository;

    /* 도메인 객체를 생성하는 로직 */
    public Bookmark createBookmark(JointShoppingGroup jointShoppingGroup, User user) {

        /* entity 생성 */
        Bookmark newBookmark = BookmarkMapper.toEntity(jointShoppingGroup, user);

        return newBookmark;
    }

    /* 도메인 객체를 저장하는 로직 */
    public Bookmark saveBookmark(Bookmark newBookmark) {
        return bookmarkRepository.save(newBookmark);
    }

    /* 도메인 객체를 삭제하는 로직 */
    public void deleteBookmark(Long bookmarkNum) {
        bookmarkRepository.deleteById(bookmarkNum);
    }

    // 회원정보와 그룹정보로 북마크 찾기
    public Bookmark findBookmark(JointShoppingGroup jointShoppingGroup, User user) {
        return bookmarkRepository.findByJointShoppingGroupAndUser(jointShoppingGroup, user);
    }
}
