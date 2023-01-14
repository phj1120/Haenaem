package xyz.parkh.challenge.domain.ui;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import xyz.parkh.challenge.domain.ui.entity.ChallengeSetStyleSector;
import xyz.parkh.challenge.domain.ui.entity.Grid;
import xyz.parkh.challenge.domain.ui.entity.Linear;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
class ChallengeSetStyleSectorRepositoryTest {
    private final ChallengeSetStyleSectorRepository challengeSetStyleSectorRepository;

    private final EntityManager em;

    @DisplayName("상속 확인 테스트")
    @Test
    void extendsTest() {
        Grid grid = new Grid("leftUp", "rightUp", "leftDown", "rightDown");
        Linear linear = new Linear("left", "centerLeft", "centerRight", "right");
        challengeSetStyleSectorRepository.save(grid);
        challengeSetStyleSectorRepository.save(linear);

        em.flush();
        em.close();

        ChallengeSetStyleSector findGrid = challengeSetStyleSectorRepository.findById(grid.getId()).orElseThrow();
        ChallengeSetStyleSector findLinear = challengeSetStyleSectorRepository.findById(linear.getId()).orElseThrow();

        Assertions.assertThat(findGrid).isEqualTo(grid);
        Assertions.assertThat(findLinear).isEqualTo(linear);
    }

    @DisplayName("추상 메서드 확인")
    @Test
    void abstractMethodTest() {
        Grid grid = new Grid("leftUp", "rightUp", "leftDown", "rightDown");
        Linear linear = new Linear("left", "centerLeft", "centerRight", "right");
        challengeSetStyleSectorRepository.save(grid);
        challengeSetStyleSectorRepository.save(linear);

        StyleSector gridStyleSector = grid.getStyleSector();
        StyleSector linearStyleSector = linear.getStyleSector();

        Assertions.assertThat(gridStyleSector).isInstanceOf(GridStyleSector.class);
        Assertions.assertThat(linearStyleSector).isInstanceOf(LinearStyleSector.class);
    }


}