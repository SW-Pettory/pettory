package com.pettory.pettory.jointshopping.command.application.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.domain.aggregate.Bookmark;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingGroup;
import com.pettory.pettory.jointshopping.command.domain.service.BookmarkDomainService;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingGroupDomainService;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkApplicationService {

    private final UserRepository userRepository;
    private final JointShoppingGroupDomainService jointShoppingGroupDomainService;
    private final BookmarkDomainService bookmarkDomainService;

    /* 즐겨찾기 등록 */
    @Transactional
    public Bookmark createBookmark(String userEmail, Long jointShoppingGroupNum) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.findGroup(jointShoppingGroupNum);

        /* Bookmark 도메인 로직 실행, entity 반환 */
        Bookmark newBookmark = bookmarkDomainService.createBookmark(jointShoppingGroup, user);

        /* save 로직 실행 */
        Bookmark bookmark = bookmarkDomainService.saveBookmark(newBookmark);

        /* 엔티티 반환 */
        return bookmark;

    }

    /* 즐겨찾기 삭제 */
    @Transactional
    public Long deleteBookmark(String userEmail, Long jointShoppingGroupNum) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingGroup jointShoppingGroup = jointShoppingGroupDomainService.findGroup(jointShoppingGroupNum);

        // 회원정보와 그룹정보로 북마크 찾기
        Bookmark bookmark = bookmarkDomainService.findBookmark(jointShoppingGroup, user);

        // 북마크 삭제
        bookmarkDomainService.deleteBookmark(bookmark.getBookmarkNum());

        return bookmark.getBookmarkNum();
    }
}
