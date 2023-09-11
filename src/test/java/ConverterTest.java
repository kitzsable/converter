import org.example.Converter;
import org.example.TreeDTO;
import org.example.TreeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConverterTest {
    private final List<TreeEntity> treesList = Arrays.asList(
            new TreeEntity(1, "name", 4),
            new TreeEntity(2, "name", 3),
            new TreeEntity(3, "name", null),
            new TreeEntity(4, "name", 3),

            new TreeEntity(5, "name", 6),
            new TreeEntity(6, "name", 7),
            new TreeEntity(7, "name", null)
    );

    private final List<TreeEntity> orderedTreesList = Arrays.asList(
            new TreeEntity(3, "name", null),
            new TreeEntity(2, "name", 3),
            new TreeEntity(4, "name", 3),
            new TreeEntity(1, "name", 4),

            new TreeEntity(7, "name", null),
            new TreeEntity(6, "name", 7),
            new TreeEntity(5, "name", 6)
    );

    /**
     * Тестирование конвертации сущности в DTO.
     */
    @Test
    public void convertEntityToDtoTest() {
        TreeEntity entity = new TreeEntity(1, "first", null);
        TreeDTO result = Converter.convertEntityToDTO(entity);

        assertEquals(result.getId(), entity.getId());
        assertEquals(result.getName(), entity.getName());
    }

    /**
     * Тестирование определения корневых элементов.
     */
    @Test
    public void rootsDefinitionTest() {
        Collection<TreeDTO> result = Converter.convert(treesList);

        assertEquals(2, result.size());

        List<Integer> rootIds = result.stream().map(TreeDTO::getId).toList();
        assertTrue(rootIds.containsAll(Arrays.asList(3, 7)));
    }

    /**
     * Тестирование расположения дочерних элементов алгоритмом для любой коллекции.
     * @param nodes Путь до элемента, дочерние элементы которого проверяются.
     *              Для [2, 3, 7] путь будет 2->3->7.
     * @param expectedChildrenIds Идентификаторы ожидаемых элементов.
     */
    @ParameterizedTest
    @DisplayName("Проверка дочерних элементов алгоритмом для любой коллекции")
    @MethodSource("provideDataForSecondLevelTest")
    public void secondLevelDefinitionTest(List<Integer> nodes, Collection<Integer> expectedChildrenIds) {
        Collection<TreeDTO> result = Converter.convert(treesList);

        Collection<Integer> childrenIds = getChildrenIdsByPath(result, nodes);

        assertEquals(expectedChildrenIds.size(), childrenIds.size());
        assertTrue(childrenIds.containsAll(expectedChildrenIds));
    }

    /**
     * Тестирование расположения дочерних элементов алгоритмом для упорядоченной коллекции.
     * @param nodes Путь до элемента, дочерние элементы которого проверяются.
     *              Для [2, 3, 7] путь будет 2->3->7.
     * @param expectedChildrenIds Идентификаторы ожидаемых элементов.
     */
    @ParameterizedTest
    @DisplayName("Проверка дочерних элементов алгоритмом для упорядоченной коллекции")
    @MethodSource("provideDataForSecondLevelTest")
    public void secondLevelDefinitionBySortedConverterTest(List<Integer> nodes, Collection<Integer> expectedChildrenIds) {
        Collection<TreeDTO> result = Converter.convertOrderedNodes(orderedTreesList);

        Collection<Integer> childrenIds = getChildrenIdsByPath(result, nodes);

        assertEquals(expectedChildrenIds.size(), childrenIds.size());
        assertTrue(childrenIds.containsAll(expectedChildrenIds));
    }

    private Collection<Integer> getChildrenIdsByPath(Collection<TreeDTO> dtoList, List<Integer> nodes) {
        Collection<TreeDTO> children = dtoList;
        for (Integer node : nodes) {
            children = children.stream()
                    .filter(dto -> Objects.equals(dto.getId(), node))
                    .findFirst()
                    .get().getChildren();
        }
        return children.stream().map(TreeDTO::getId).toList();
    }

    private static Stream<Arguments> provideDataForSecondLevelTest() {
        return Stream.of(
                Arguments.of(List.of(3), Arrays.asList(2, 4)),
                Arguments.of(Arrays.asList(3, 2), List.of()),
                Arguments.of(Arrays.asList(3, 4), List.of(1)),
                Arguments.of(Arrays.asList(3, 4, 1), List.of()),

                Arguments.of(List.of(7), List.of(6)),
                Arguments.of(Arrays.asList(7, 6), List.of(5)),
                Arguments.of(Arrays.asList(7, 6, 5), List.of())
        );
    }

}
