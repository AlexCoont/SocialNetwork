package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Friendship;
import project.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {

    @Query("select f from Friendship f " +
            "join FriendshipStatus fs on fs.id = f.status " +
            "where (f.srcPerson = :person OR f.dstPerson = :person) " +
            "AND fs.code = 'FRIEND'")
    List<Friendship> findAllBySrcPersonOrDstPersonAndStatus
            (Person person, Pageable pageable);

    // Поиск френдшипа, содержащего определенную пару пользователей
    @Query(value = "select f from Friendship f " +
            "join  FriendshipStatus fs on fs = f.status " +
            "where ((f.srcPerson = :firstFriend and f.dstPerson = :secondFriend) " +
            "or (f.srcPerson = :secondFriend and f.dstPerson = :firstFriend)) ")
    Optional<Friendship> findByFriendsCouple(Person firstFriend, Person secondFriend);
}
