package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
