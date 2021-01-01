package me.weekbelt.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import me.weekbelt.guestbook.entity.Guestbook;
import me.weekbelt.guestbook.entity.QGuestbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);
        if (result.isPresent()) {
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Change Title....");
            guestbook.changeContent("Changed Content...");
            guestbookRepository.save(guestbook);
        }
    }

    @Test
    public void testQuery1() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";

        // 검색 조건 설정
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.title.contains(keyword);
        booleanBuilder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);
        result.stream().forEach(System.out::println);
    }

    @Test
    public void testQuery2() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";

        // 검색 조건 설정
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        booleanBuilder.and(exAll);
        booleanBuilder.and(qGuestbook.gno.gt(0L));  // title 혹은 content가 문자열 1을 포함하고 gno가 0이상인 조건검색

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);
        result.stream().forEach(System.out::println);
    }
}