package com.se.demo.repository;

import com.se.demo.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    /*static Optional<MemberEntity> findByNickname(String nickname) {
        return null;
    }*/
    Optional<MemberEntity> findByNickname(String nickname);
<<<<<<< HEAD

=======
    boolean existsByNickname(String nickname);
>>>>>>> 08706729e8c16330e9c6718763f2981c98372624
}

