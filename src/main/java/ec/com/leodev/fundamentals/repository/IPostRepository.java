package ec.com.leodev.fundamentals.repository;

import ec.com.leodev.fundamentals.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository extends JpaRepository<Posts, Long> {
}
