package mutanerator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.Test;
import org.mockito.Mockito;

public class MutationTargetsTest {


  @Test
  public void testGetMutationTargets() {

    final MutationTargets mutationTargets = new MutationTargets();
    for (final Mutator mutator : Mutator.values()) {
      final SimpleName node = Mockito.mock(SimpleName.class);
      node.setIdentifier(mutator.name());
      mutationTargets.put(mutator, node);
    }
    final List<Mutation> mutations = mutationTargets.getMutations();
    final List<Boolean> availabilities = mutations.stream()
        .map(m -> m.mutator.isAvailable())
        .collect(Collectors.toList());
    assertThat(availabilities).doesNotContain(Boolean.FALSE);
  }
}
