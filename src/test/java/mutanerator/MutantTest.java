package mutanerator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Test;
import org.mockito.Mockito;

public class MutantTest {

  @Test
  public void testMutantGeneration01() {

    final List<Mutation> mutations = new ArrayList<>();
    final ASTNode node1 = Mockito.mock(InfixExpression.class);
    final Mutation mutation1 = new Mutation(Mutator.ConditionalsBoundary, node1);
    mutations.add(mutation1);
    final Mutation mutation2 = new Mutation(Mutator.NegateConditionals, node1);
    mutations.add(mutation2);

    final List<Mutant> mutants1 = Mutant.generateMutants(mutations, 1, 1);
    assertThat(mutants1).hasSize(2);

    // The two mutation object include the same node, which means the two mutations shouldn't be mixed.
    // 2つのミューテーションは同じASTノードに対する操作だから，これらが1つのミュータントを校正することはないはず．
    final List<Mutant> mutants2 = Mutant.generateMutants(mutations, 2, 2);
    assertThat(mutants2).isEmpty();
  }

  @Test
  public void testMutantGeneration02() {

    final List<Mutation> mutations = new ArrayList<>();
    final ASTNode node1 = Mockito.mock(InfixExpression.class);
    final Mutation mutation1 = new Mutation(Mutator.ConditionalsBoundary, node1);
    mutations.add(mutation1);
    final ASTNode node2 = Mockito.mock(InfixExpression.class);
    final Mutation mutation2 = new Mutation(Mutator.NegateConditionals, node2);
    mutations.add(mutation2);

    final List<Mutant> mutants1 = Mutant.generateMutants(mutations, 1, 1);
    assertThat(mutants1).hasSize(2);

    // The two mutation objects include different nodes, which means a new mutant should be generated from them.
    // 2つのミューテーションは違うASTノードに対する操作だから，これらの組み合わせから新しいミュータントが生成されるはず．
    final List<Mutant> mutants2 = Mutant.generateMutants(mutations, 2, 2);
    assertThat(mutants2).hasSize(1);
  }
}
