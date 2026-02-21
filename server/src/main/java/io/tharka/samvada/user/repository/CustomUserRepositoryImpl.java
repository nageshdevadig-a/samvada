package io.tharka.samvada.user.repository;

import com.mongodb.client.result.UpdateResult;
import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.user.dto.UserResponse;
import io.tharka.samvada.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository
{

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean updatePassword(String email, String newPassword) throws UserNotFoundException
    {
        Query query = new Query(Criteria.where("email").is(email));

        Update update = new Update()
                .set("password", newPassword);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        if(updateResult.getMatchedCount() == 0) throw new UserNotFoundException();
        return updateResult.wasAcknowledged();
    }

    @Override
    public Optional<UserResponse> updateAndFetchUser(String email, String userName, String fullName)
    {
        Query query = new Query(Criteria.where("email").is(email));
        query.fields()
                .include("userName")
                .include("email")
                .include("fullName")
                .exclude("_id");

        Update update = new Update()
                .set("userName", userName)
                .set("fullName", fullName);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        UserResponse response = mongoTemplate.findAndModify(
                query,
                update,
                options,
                UserResponse.class,
                mongoTemplate.getCollectionName(User.class)
        );

        return Optional.ofNullable(response);
    }

    @Override
    public boolean deactivateUser(String email)
    {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update()
                .set("isActive", false)
                .set("expiresAt", Instant.now().plus(30, ChronoUnit.DAYS));
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        if (updateResult.getMatchedCount() == 0) throw new UserNotFoundException();
        return updateResult.wasAcknowledged();
    }

    @Override
    public boolean activateUser(String email)
    {
        Query query = new Query();
        query.addCriteria(
                new Criteria().orOperator(
                        Criteria.where("email").is(email),
                        Criteria.where("userName").is(email)
                ));
        Update update = new Update()
                .set("isActive", true)
                .unset("expiresAt");
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        if (updateResult.getMatchedCount() == 0) throw new BadCredentialsException("Invalid username or password.");
        return updateResult.wasAcknowledged();

    }

}
