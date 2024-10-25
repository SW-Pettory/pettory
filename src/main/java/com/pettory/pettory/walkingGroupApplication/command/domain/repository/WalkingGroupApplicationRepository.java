package com.pettory.pettory.walkingGroupApplication.command.domain.repository;

import com.pettory.pettory.walkingGroupApplication.command.domain.aggregate.RegisterWalkingGroup;
import com.pettory.pettory.walkingGroupApplication.command.domain.aggregate.WalkingGroupApplication;

import java.util.Optional;

public interface WalkingGroupApplicationRepository {

    WalkingGroupApplication save(WalkingGroupApplication newWalkingGroupApplication);

    RegisterWalkingGroup save(RegisterWalkingGroup newRegisterWalkingGroupApplication);

    Optional<WalkingGroupApplication> findById(int walkingGroupApplicationId);

    void deleteById(int walkingGroupApplicationId);
}
