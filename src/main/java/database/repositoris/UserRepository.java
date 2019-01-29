package database.repositoris;

import database.enteties.User;
import database.util.RepositoryActionInvoker;
import database.util.RepositoryActionResult;
import org.hibernate.TransactionException;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRepository extends BaseRepository {


    public UserRepository() {
    }

    public List<User> finedAll() {
        List<User> result = (List<User>) executeAction(repositoryActionResult -> {
            repositoryActionResult
                    .setResult(this.entityManager
                            .createNativeQuery("SELECT * FROM users", User.class)
                            .getResultList());
        }).getResult();
        return result;
    }


    public void save(User user) {
        executeAction(repositoryActionResult -> {
            this.entityManager.persist(user);
        });
    }

    public void addFriend(User user, User friend) {

        executeAction(repositoryActionResult -> {
            user.addFriend(friend);
            friend.addFriend(user);
            this.entityManager.merge(user);
            this.entityManager.merge(friend);
        });

    }

    public User finedById(String userId) {
        User result = (User) executeAction(repositoryActionResult -> {
            User userFromDataBase;
            try {
                userFromDataBase = (User) this.entityManager
                        .createNativeQuery("SELECT * FROM users WHERE id = '"
                                        + userId + "'"
                                , User.class)
                        .getSingleResult();
            } catch (NoResultException ignore) {
                userFromDataBase = null;
            }
            repositoryActionResult
                    .setResult(userFromDataBase);
        })
                .getResult();
        return result;
    }

    public User finedByUsername(String username) {
        User result = (User) executeAction(repositoryActionResult -> {
            User userFromDatabase ;
            try{
                userFromDatabase = (User) this.entityManager
                                .createNativeQuery("SELECT * FROM users WHERE name = '"
                                                + username + "'"
                                        , User.class)
                                .getSingleResult();
            }catch (NoResultException ignore){
                userFromDatabase = null;
            }
            repositoryActionResult.setResult(userFromDatabase);
        }).getResult();
        return result;
    }
}
