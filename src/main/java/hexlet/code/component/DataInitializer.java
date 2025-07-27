package hexlet.code.component;

import hexlet.code.dto.userDto.UserCreateDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
        createTaskStatus();
        createLabel();
    }

    private void createTaskStatus() {

        String draft = "Draft";
        String draftSlug = "draft";
        TaskStatus statusDraft = new TaskStatus();
        statusDraft.setName(draft);
        statusDraft.setSlug(draftSlug);
        taskStatusRepository.save(statusDraft);

        String toReview = "ToReview";
        String toReviewSlug = "to_review";
        TaskStatus statusToReview = new TaskStatus();
        statusToReview.setName(toReview);
        statusToReview.setSlug(toReviewSlug);
        taskStatusRepository.save(statusToReview);

        String toBeFixed = "ToBeFixed";
        String toBeFixedSlug = "to_be_fixed";
        TaskStatus statusToBeFixed = new TaskStatus();
        statusToBeFixed.setName(toBeFixed);
        statusToBeFixed.setSlug(toBeFixedSlug);
        taskStatusRepository.save(statusToBeFixed);

        String toPublish = "ToPublish";
        String toPublishSlug = "to_publish";
        TaskStatus statusToPublish = new TaskStatus();
        statusToPublish.setName(toPublish);
        statusToPublish.setSlug(toPublishSlug);
        taskStatusRepository.save(statusToPublish);

        String published = "Published";
        String publishedSlug = "published";
        TaskStatus statusPublished = new TaskStatus();
        statusPublished.setName(published);
        statusPublished.setSlug(publishedSlug);
        taskStatusRepository.save(statusPublished);
    }

    private void createAdmin() {
        String email = "hexlet@example.com";
        String password = "qwerty";
        String firstName = "admin";
        String lastName = "admin";
        UserCreateDto userData = new UserCreateDto();
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        User user = userMapper.map(userData);
        userRepository.save(user);
    }

    private void createLabel() {
        String feature = "feature";
        Label featureLabel = new Label();
        featureLabel.setName(feature);
        labelRepository.save(featureLabel);

        String bug = "bug";
        Label bugLabel = new Label();
        bugLabel.setName(bug);
        labelRepository.save(bugLabel);
    }
}
