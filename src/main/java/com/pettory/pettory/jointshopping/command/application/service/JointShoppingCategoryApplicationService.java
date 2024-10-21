package com.pettory.pettory.jointshopping.command.application.service;

import com.pettory.pettory.board.command.domain.aggregate.Category;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.jointshopping.command.domain.service.JointShoppingCategoryDomainService;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.aggregate.User;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JointShoppingCategoryApplicationService {

    private final JointShoppingCategoryDomainService jointShoppingCategoryDomainService;
    private final UserRepository userRepository;

    /* 카테고리 등록 */
    @Transactional
    public JointShoppingCategory createCategory(String userEmail, String jointShoppingCategoryTitle) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        /* jointshoppingcategory 도메인 로직 실행, entity 반환 */
        JointShoppingCategory newJointShoppingCategory = jointShoppingCategoryDomainService.createCategory(jointShoppingCategoryTitle, user);

        /* save 로직 실행 */
        JointShoppingCategory jointShoppingCategory = jointShoppingCategoryDomainService.saveCategory(newJointShoppingCategory);

        /* 엔티티 반환 */
        return jointShoppingCategory;
    }

    /* 카테고리 수정 */
    @Transactional
    public JointShoppingCategory updateCategory(String userEmail, Long jointShoppingCategoryNum, String jointShoppingCategoryTitle) {

        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        JointShoppingCategory jointShoppingCategory = jointShoppingCategoryDomainService.updateCategory(jointShoppingCategoryNum, jointShoppingCategoryTitle, user);

        return jointShoppingCategory;
    }

    /* 카테고리 삭제 */
    @Transactional
    public void deleteCategory(String userEmail, Long jointShoppingCategoryNum) {
        UserSecurity.validateCurrentUser(userEmail);

        // 회원 정보 확인
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        jointShoppingCategoryDomainService.deleteCategory(jointShoppingCategoryNum, user);
    }


}
