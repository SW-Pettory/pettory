package com.pettory.pettory.jointshopping.service;

import com.pettory.pettory.jointshopping.command.application.service.BookmarkApplicationService;
import com.pettory.pettory.jointshopping.query.dto.JointShoppingGroupListResponse;
import com.pettory.pettory.jointshopping.query.service.JointShoppingGroupQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
public class BookmarkServiceTest {

    @Autowired
    private BookmarkApplicationService bookmarkApplicationService;
    @Autowired
    private JointShoppingGroupQueryService jointShoppingGroupQueryService;

    private static Stream<Arguments> getBookmark() {
        return Stream.of(
                Arguments.of(
                        1,
                        5,
                        5L
                )
        );
    }

    private static Stream<Arguments> getInsertBookmark() {
        return Stream.of(
                Arguments.of(
                        4L,
                        6L
                )
        );
    }

    /* 즐겨찾기 등록 테스트 */
    @ParameterizedTest
    @MethodSource("getInsertBookmark")
    void testCreateBookmark(String userEmail, Long jointShoppingGroupNum) {
        Assertions.assertDoesNotThrow(
                () -> bookmarkApplicationService.createBookmark(userEmail, jointShoppingGroupNum)
        );
    }

    /* 즐겨찾기 삭제 테스트 */
    @Test
    void testDeleteBookmark() {
        Assertions.assertDoesNotThrow(
                () -> bookmarkApplicationService.deleteBookmark("userEmail", 7L)
        );
    }

    /* 즐겨찾기된 모임 목록 조회 테스트 */
    @ParameterizedTest
    @MethodSource("getBookmark")
    void testGetBookmarks(Integer page, Integer size, Long userId) {
        Assertions.assertDoesNotThrow(
                () -> {
                    JointShoppingGroupListResponse response = jointShoppingGroupQueryService.getBookmarks("userEmail",page, size);
                    response.getGroupList().forEach(group -> System.out.println(group));
                }
        );
    }

}
