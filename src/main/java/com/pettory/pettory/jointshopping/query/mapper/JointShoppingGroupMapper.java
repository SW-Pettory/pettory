package com.pettory.pettory.jointshopping.query.mapper;

import com.pettory.pettory.jointshopping.query.dto.JointShoppingGroupDTO;
import com.pettory.pettory.jointshopping.query.dto.ProvisionRecordDTO;
import com.pettory.pettory.user.query.dto.UserInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JointShoppingGroupMapper {
    List<JointShoppingGroupDTO> selectGroups(@Param("offset") int offset, @Param("limit") int limit, @Param("categoryNum") Long categoryNum, @Param("groupName") String groupName, @Param("products") String products);

    long countGroups(@Param("categoryNum") Long categoryNum, @Param("groupName") String groupName, @Param("products") String products);

    JointShoppingGroupDTO selectGroupByNum(@Param("groupNum") Long groupNum);

    List<JointShoppingGroupDTO> selectBookmarks(@Param("offset") int offset, @Param("limit") int limit, @Param("userId") Long userId);

    long countBookmarks(@Param("userId") Long userId);

    List<UserInfoResponse> selectGroupUsers(@Param("groupNum") Long groupNum);

    long countGroupUsers(@Param("groupNum") Long groupNum);

    List<JointShoppingGroupDTO> selectUserGroups(@Param("offset") int offset, @Param("limit") int limit, @Param("userId") Long userId);

    long countUserGroups(@Param("userId") Long userId);

    List<ProvisionRecordDTO> selectProvisionRecords(@Param("offset") int offset, @Param("limit") int limit, @Param("provisionState") String provisionState);

    long countProvisionRecords( @Param("provisionState") String provisionState);
}
