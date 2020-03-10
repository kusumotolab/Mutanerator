package mutanerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.dom.ASTNode;

public class MutationTargets {

  final Map<Mutator, List<ASTNode>> mutators;

  public MutationTargets() {
    this.mutators = new HashMap<>();
    for (final Mutator mutator : Mutator.values()) {
      this.mutators.put(mutator, new ArrayList<ASTNode>());
    }
  }

  public List<ASTNode> getTargetNodes(final Mutator mutator) {
    return this.mutators.get(mutator);
  }

  public void put(final Mutator mutator, final ASTNode candidate) {
    this.mutators.get(mutator)
        .add(candidate);
  }

  public Mutation select(final int value) {
    final Mutator selectedMutator = this.selectMutator(value);
    final ASTNode selectedNode = this.selectNode(selectedMutator, value);
    return new Mutation(selectedMutator, selectedNode);
  }

  private Mutator selectMutator(final int value) {
    final List<Mutator> availableMutators = this.mutators.entrySet()
        .stream()
        .filter(e -> !e.getValue()
            .isEmpty())
        .map(e -> e.getKey())
        .collect(Collectors.toList());

    // 可能な変異がない場合はプログラムを終了する
    if (availableMutators.isEmpty()) {
      System.err.println("No mutation is available for the specified program.");
      System.exit(1);
    }

    final int index = value % availableMutators.size();
    return availableMutators.get(index);
  }

  private ASTNode selectNode(final Mutator mutator, final int value) {
    final List<ASTNode> nodes = this.mutators.get(mutator);
    final int index = value % nodes.size();
    return nodes.get(index);
  }
}
