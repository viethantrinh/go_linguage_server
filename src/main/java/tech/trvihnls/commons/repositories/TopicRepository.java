package tech.trvihnls.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.trvihnls.commons.domains.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("select t from Topic t where t.level.id = ?1 order by t.displayOrder")
    List<Topic> findByLevelIdOrderByDisplayOrderAsc(Long id);

}
