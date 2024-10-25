package com.pettory.pettory.jointshopping.command.domain.service;

import com.pettory.pettory.board.command.domain.aggregate.Category;
import com.pettory.pettory.exception.BadJoinException;
import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.jointshopping.command.domain.aggregate.JointShoppingCategory;
import com.pettory.pettory.jointshopping.command.domain.repository.JointShoppingCategoryRepository;

import com.pettory.pettory.user.command.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JointShoppingCategoryDomainService {

    private final JointShoppingCategoryRepository jointShoppingCategoryRepository;

    /* 도메인 객체를 생성하는 로직 */
    public JointShoppingCategory createCategory(String jointShoppingCategoryTitle, User user) {

        if(!user.getUserRole().equals("ADMIN")){
            throw new BadJoinException("접근 권한이 없습니다.");
        }

        /* entity 생성 */
        JointShoppingCategory newJointShoppingCategory = JointShoppingCategory.create(jointShoppingCategoryTitle);

        return newJointShoppingCategory;
    }

    /* 도메인 객체를 저장하는 로직 */
    public JointShoppingCategory saveCategory(JointShoppingCategory jointShoppingCategory) {
        return jointShoppingCategoryRepository.save(jointShoppingCategory);
    }

    /* 도메인 객체를 수정하는 로직 */
    public JointShoppingCategory updateCategory(Long jointShoppingCategoryNum, String jointShoppingCategoryTitle, User user) {

        if(!user.getUserRole().equals("ADMIN")){
            throw new BadJoinException("접근 권한이 없습니다.");
        }

        JointShoppingCategory jointShoppingCategory  = jointShoppingCategoryRepository.findById(jointShoppingCategoryNum)
                .orElseThrow(() -> new NotFoundException("해당 번호에 맞는 카테고리가 없습니다. code : " + jointShoppingCategoryNum));

        /* 수정을 위해 엔터티 정보 변경 */
        jointShoppingCategory.update(jointShoppingCategoryTitle);

        return jointShoppingCategory;
    }


    /* 도메인 객체를 삭제하는 로직 */
    public void deleteCategory(Long jointShoppingCategoryNum, User user) {
        if(!user.getUserRole().equals("ADMIN")){
            throw new BadJoinException("접근 권한이 없습니다.");
        }

        jointShoppingCategoryRepository.deleteById(jointShoppingCategoryNum);
    }

    /* 이름으로 카테고리 찾기 */
    public JointShoppingCategory findCategory(String categoryTitle) {
        return jointShoppingCategoryRepository.findByJointShoppingCategoryTitle(categoryTitle);
    }

}
