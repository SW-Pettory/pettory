package com.pettory.pettory.walkingRecord.query.service;

import com.pettory.pettory.exception.NotFoundException;
import com.pettory.pettory.exception.UnauthorizedException;
import com.pettory.pettory.pet.command.domain.aggregate.Pet;
import com.pettory.pettory.pet.command.domain.repository.PetRepository;
import com.pettory.pettory.pet.query.dto.PetListDTO;
import com.pettory.pettory.pet.query.mapper.PetMapper;
import com.pettory.pettory.security.util.UserSecurity;
import com.pettory.pettory.user.command.domain.repository.UserRepository;
import com.pettory.pettory.user.query.mapper.UserMapper;
import com.pettory.pettory.walkingRecord.command.domain.aggregate.WalkingRecord;
import com.pettory.pettory.walkingRecord.query.dto.WalkingRecordDailyResponse;
import com.pettory.pettory.walkingRecord.query.dto.WalkingRecordDetailResponse;
import com.pettory.pettory.walkingRecord.query.dto.WalkingRecordSummaryResponse;
import com.pettory.pettory.walkingRecord.query.mapper.WalkingRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkingRecordQueryService {

    private final WalkingRecordMapper walkingRecordMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    // 산책 기록 요약과 반려동물 ID 반환
    @Transactional(readOnly = true)
    public Map<String, Object> getWalkingRecordSummary(
            String userEmail, Integer year, Integer month, String filterType
    ) {
        // 로그인 사용자 정보 검증
        UserSecurity.validateCurrentUser(userEmail);

        // 사용자 ID 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        // 사용자의 반려동물 목록 조회
        List<PetListDTO> petList = petMapper.findPetIdsAndNamesByUserId(userId);

        if (petList.isEmpty()) {
            throw new NotFoundException("등록된 반려동물이 없습니다.");
        }

        // familyId 조회
        Long familyId = userRepository.findFamilyIdByUserId(userId).orElse(null);

        // 산책 기록 조회
        List<WalkingRecordSummaryResponse> walkingRecords = getWalkingRecordsByFilterType(userId, familyId, year, month, filterType);

        // 결과를 Map으로 통합
        Map<String, Object> result = new HashMap<>();
        result.put("pets", petList);  // 반려동물 목록
        result.put("summaries", walkingRecords);  // 산책 기록 요약

        return result;
    }

    // filterType에 따라 산책 기록을 조회하는 메서드
    private List<WalkingRecordSummaryResponse> getWalkingRecordsByFilterType(Long userId, Long familyId, Integer year, Integer month, String filterType) {

        List<WalkingRecordSummaryResponse> walkingRecords;

        switch (filterType) {
            case "self":
                // 현재 사용자의 반려동물 산책 기록만 조회
                walkingRecords = walkingRecordMapper.findWalkingRecordsByUserIdForPetAndMonth(userId, year, month);
                break;

            case "family":
                // 가족 구성원이 있을 경우, 가족의 산책 기록만 조회
                if (familyId != null) {
                    walkingRecords = walkingRecordMapper.findWalkingRecordsByFamilyExcludeUserAndMonth(familyId, userId, year, month);
                } else {
                    walkingRecords = new ArrayList<>();  // 가족이 없을 경우 빈 리스트 반환
                }
                break;

            case "all":
                // 가족이 있으면 가족 구성원과 본인의 산책 기록 모두 조회, 없으면 본인 기록만 조회
                if (familyId != null) {
                    walkingRecords = walkingRecordMapper.findAllWalkingRecordsByFamilyAndPetAndMonth(familyId, year, month);
                } else {
                    walkingRecords = walkingRecordMapper.findWalkingRecordsByUserIdForPetAndMonth(userId, year, month);
                }
                break;

            default:
                throw new IllegalArgumentException("잘못된 filterType 값입니다: " + filterType);
        }

        return walkingRecords;
    }


    // 1. 산책 기록 월별 요약 조회
//    @Transactional(readOnly = true)
//    public List<WalkingRecordSummaryResponse> getWalkingRecordSummary(
//            String userEmail, int year, int month, Long petId, String filterType
//    ) {
//
//        UserSecurity.validateCurrentUser(userEmail);
//
//        // 로그인 회원 id 조회
//        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();
//
//        // familyId 조회
//        // 회원이 속한 가족이 없으면 null 처리
//        Long familyId = userRepository.findFamilyIdByUserId(userId).orElse(null);
//
//        // 반려동물 정보 조회
//        Pet pet = petRepository.findById(petId)
//                .orElseThrow(() -> new NotFoundException("반려동물이 존재하지 않습니다."));
//
//        // 해당 반려동물을 현재 회원이 등록한 것이 아니라면
//        if (!pet.getUser().getUserId().equals(userId)) {
//            // 해당 반려동물을 가족 구성원이 등록한 것인지 확인
//            if (familyId == null
//                    || pet.getFamily() == null
//                    || !familyId.equals(pet.getFamily().getFamilyId())) {
//                throw new UnauthorizedException("해당 반려동물에 접근할 권한이 없습니다.");
//            }
//        }
//
//        List<WalkingRecordSummaryResponse> walkingRecords;
//
//        switch (filterType) {
//            case "self":
//                // 현재 회원의 반려동물 산책 기록 요약 조회
//                walkingRecords = walkingRecordMapper.findWalkingRecordsByUserIdForPetAndMonth(userId, petId, year, month);
//                break;
//            case "family":
//                if (familyId == null) {
//                    // 가족이 없으면 빈 리스트 반환
//                    walkingRecords = new ArrayList<>();
//                } else {
//                    // 가족 구성원의 반려동물 산책 기록 요약 조회
//                    walkingRecords = walkingRecordMapper.findWalkingRecordsByFamilyExcludeUserAndMonth(familyId, userId, petId, year, month);
//                }
//                break;
//            case "all":
//                if (familyId == null) {
//                    // 가족이 없으면 자신의 반려동물 산책 기록만 조회
//                    walkingRecords = walkingRecordMapper.findWalkingRecordsByUserIdForPetAndMonth(userId, petId, year, month);
//                } else {
//                    // 회원 포함 가족 전체의 반려동물 산책 기록 요약 조회
//                    walkingRecords = walkingRecordMapper.findAllWalkingRecordsByFamilyAndPetAndMonth(familyId, petId, year, month);
//                }
//                break;
//            default:
//                throw new NotFoundException("잘못된 filterType 값입니다: " + filterType);
//        }
//
//        return walkingRecords;
//    }


//    @Transactional(readOnly = true)
//    public List<WalkingRecordSummaryResponse> getWalkingRecordsForMonth(String userEmail, Long petId, int year, int month) {
//
//        UserSecurity.validateCurrentUser(userEmail);
//
//        // 로그인 회원 id 조회
//        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();
//
//        // 가족 ID 조회
//        Long familyId = userRepository.findFamilyIdByUserId(userId).orElse(null);
//
//        // 한 달 동안의 산책 기록 조회
//        List<WalkingRecordSummaryResponse> walkingRecords;
//
//        if (familyId != null) {
//            // 가족 구성원의 기록까지 모두 조회
//            walkingRecords = walkingRecordMapper.findAllWalkingRecordsByFamilyAndPetAndMonth(familyId, petId, year, month);
//        } else {
//            // 본인의 기록만 조회
//            walkingRecords = walkingRecordMapper.findWalkingRecordsByUserIdForPetAndMonth(userId, petId, year, month);
//        }
//
//        return walkingRecords;  // 이 결과를 v-calendar에서 사용할 수 있습니다.
//    }

    // 2. 날짜별 산책 기록 조회
    @Transactional(readOnly = true)
    public List<WalkingRecordDailyResponse> getWalkingRecordsByDate(String userEmail, Long petId, LocalDate date) {

        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        // 반려동물 조회
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("반려동물이 존재하지 않습니다."));

        // 가족 ID 조회
        Long familyId = userRepository.findFamilyIdByUserId(userId).orElse(null);

        // 해당 날짜의 모든 산책 기록 조회
        List<WalkingRecordDailyResponse> walkingRecords;

        if (familyId != null) {
            // 가족 구성원의 산책 기록까지 모두 조회
            walkingRecords = walkingRecordMapper.findWalkingRecordsByDateAndFamily(date, petId, familyId);
        } else {
            // 회원 본인의 산책 기록만 조회
            walkingRecords = walkingRecordMapper.findWalkingRecordsByDateAndUserId(date, petId, userId);
        }

        return walkingRecords;
    }

    // 3. 산책 기록 상세 조회(특정 산책 기록의 상세 정보 조회)
    @Transactional(readOnly = true)
    public WalkingRecordDetailResponse getWalkingRecordDetail(String userEmail, Long walkingRecordId) {

        UserSecurity.validateCurrentUser(userEmail);

        // 로그인 회원 id 조회
        Long userId = userMapper.findUserIdByEmail(userEmail).getUserId();

        // 가족 ID 조회
        Long familyId = userRepository.findFamilyIdByUserId(userId).orElse(null);

        WalkingRecordDetailResponse walkingRecordDetail;

        // familyId가 있으면 가족 내에서 조회, 없으면 현재 회원으로 조회
        if (familyId != null) {
            // 가족 구성원의 산책 기록까지 모두 조회
            walkingRecordDetail = walkingRecordMapper.findWalkingRecordDetailByIdAndFamily(walkingRecordId, familyId);
        } else {
            // 회원 본인의 산책 기록만 조회
            walkingRecordDetail = walkingRecordMapper.findWalkingRecordDetailByIdAndUserId(walkingRecordId, userId);
        }

        if (walkingRecordDetail == null) {
            throw new NotFoundException("해당 산책 기록을 찾을 수 없습니다.");
        }

        return walkingRecordDetail;
    }
}
