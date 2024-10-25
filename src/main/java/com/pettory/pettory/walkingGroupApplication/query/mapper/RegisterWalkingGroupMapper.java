package com.pettory.pettory.walkingGroupApplication.query.mapper;

import com.pettory.pettory.user.query.dto.UserInfoResponse;
import com.pettory.pettory.walkingGroupApplication.query.dto.RegisterWalkingGroupDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegisterWalkingGroupMapper {
    List<RegisterWalkingGroupDTO> selectRegisterWalkingGroups(
            @Param("offset") int offset, @Param("limit") int limit,
            @Param("userId") Long userId
    );


    long countRegisterWalkingGroups(@Param("userId") Long userId);

    RegisterWalkingGroupDTO selectRegisterWalkingGroupById(@Param("registerWalkingGroupId") int registerWalkingGroupId);

    List<UserInfoResponse> selectGroupUsers(@Param("walkingGroupId") Long walkingGroupId);

    long countGroupUsers(@Param("walkingGroupId") Long walkingGroupId);
}
