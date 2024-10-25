package com.pettory.pettory.pet.command.domain.repository;

import com.pettory.pettory.pet.command.domain.aggregate.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
    Pet save(Pet newPet);

    Optional<Pet> findById(Long petId);

    // 사용자의 모든 반려동물 조회
    List<Pet> findAllByUser_UserId(Long userId);
}
