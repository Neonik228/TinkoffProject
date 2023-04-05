package ru.tinkoff.edu.java.parsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.tinkoff.edu.java.App;
import ru.tinkoff.edu.java.responses.StackOverflowResponse;


import static org.junit.jupiter.api.Assertions.*;

public class StackOverFlowTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "https://stackoverflow.com/questions/",
            "https://stackoverflow.com/",
            "https://slackoverflow.com/questions/68538851/lombok-and-autowired",
            "https://stackoverflow.com/question/68538851/lombok-and-autowired",
            "https://stackoverflow.com/questions/lombok-and-autowired/12",
            "https://stackoverflow.com/questions/68538851w/lombok-and-autowired",
            "https://stackoverflow.com/questions//lombok-and-autowired"
    })
    void invalidLink_Null(String link) {
        // given

        // when
        StackOverflowResponse response = (StackOverflowResponse) new App().main(link);

        // then
        assertAll(
                () -> assertNull(response)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://stackoverflow.com/questions/68538851/lombok-and-autowired",
            "https://stackoverflow.com/questions/68538851/",
            "https://stackoverflow.com/questions/68538851",
            "https://stackoverflow.com/questions/68538851/lombok-and-autowired/more-info/mooore-info",
    })
    void validLink_OK(String link) {
        // given

        // when
        StackOverflowResponse response = (StackOverflowResponse) new App().main(link);

        // then
        assertAll(
                () -> assertEquals(response.questionId(), "68538851")
        );
    }

    @Test
    void shortQuestionId_OK() {
        // given
        String link = "https://stackoverflow.com/questions/1";

        // when
        StackOverflowResponse response = (StackOverflowResponse) new App().main(link);

        // then
        assertAll(
                () -> assertEquals(response.questionId(), "1")
        );
    }

    @Test
    void largeQuestionId_OK() {
        // given
        String link = "https://stackoverflow.com/questions/16677777777777777777777777777777777777777777777777777777777";

        // when
        StackOverflowResponse response = (StackOverflowResponse) new App().main(link);

        // then
        assertAll(
                () -> assertEquals(response.questionId(), "16677777777777777777777777777777777777777777777777777777777")
        );
    }
}
