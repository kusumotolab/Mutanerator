package mutanerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * This class represents mutant.
 * A mutant is a variant program that was generated from a given original program.
 * A mutant includes 0 or larger mutations.
 * In the case of 0, the mutant equals to its original program.
 *
 * ミュータント（変異プログラム）を表すクラスである．
 * 0個以上の変異を持つ．
 * 0個の場合は元のプログラムと同じであることを表す，
 */
public class Mutant {

  /**
   * Generating mutants from mutations.
   *
   * ミューテーションからミュータントを生成する．
   *
   * @param mutations ミューテーション
   * @param minimumMutations ミュータントに含まれるべき最小のミューテーション数
   * @param maximumMutations ミュータントに含まれるべき最大のミューテーション数
   * @return 生成したミュータントのリスト
   */
  static public List<Mutant> generateMutants(final Collection<Mutation> mutations,
      final int minimumMutations, final int maximumMutations) {

    final List<Mutant> mutants = new ArrayList<>();

    List<Mutant> currentGenerationMutants = new ArrayList<>();
    currentGenerationMutants.add(new Mutant());
    List<Mutant> nextGenerationMutants = new ArrayList<>();

    int size = 0;
    do {
      size++;
      for (final Mutant currentMutant : currentGenerationMutants) {
        for (final Mutation mutation : mutations) {
          final Mutant newMutant = currentMutant.add(mutation);
          if (null != newMutant) {
            nextGenerationMutants.add(newMutant);
          }
        }
      }

      currentGenerationMutants = nextGenerationMutants.stream()
          .distinct()
          .collect(Collectors.toList());
      nextGenerationMutants = new ArrayList<>();

      if (minimumMutations <= size && size <= maximumMutations) {
        mutants.addAll(currentGenerationMutants);
      }

    } while (size < maximumMutations);

    return mutants;
  }

  private final Set<Mutation> mutations;

  public Mutant() {
    this.mutations = new HashSet<>();
  }

  private Mutant(final Set<Mutation> mutations) {
    this();
    this.mutations.addAll(mutations);
  }

  /**
   * Hash code of a mutant equals to the sum of hash code of included mutations.
   *
   * ミュータントのハッシュコードは内部に含むミューテーションのハッシュコードの和．
   *
   * @return
   */
  @Override
  public int hashCode() {
    return this.mutations.stream()
        .mapToInt(Mutation::hashCode)
        .sum();
  }

  @Override
  public boolean equals(final Object o) {

    if (!(o instanceof Mutant)) {
      return false;
    }

    final Mutant target = (Mutant) o;
    return this.mutations.equals(target.mutations);
  }

  /**
   * Adding a new mutation.
   * Note that this method return a new object where the new mutation has been added.
   * The existing mutant (where this method is invoked) doesn't get changed.
   *
   * 新しいミューテーションを追加する．
   * このメソッドはそのミューテーションが追加された新しいミュータントオブジェクトを返す．
   * 既存のミュータントオブジェクトは変化しない．
   *
   * @param newMutation 新しいミューテーション
   * @return 新しいミューテーションが追加された新しいミュータントオブジェクト
   */
  public Mutant add(final Mutation newMutation) {
    if (this.mutations.stream()
        .map(m -> m.node)
        .anyMatch(n -> n.equals(newMutation.node))) {
      return null;
    }
    final Mutant newMutant = new Mutant(this.mutations);
    newMutant.mutations.add(newMutation);
    return newMutant;
  }

  public Set<Mutation> getMutations() {
    return Collections.unmodifiableSet(this.mutations);
  }

  public int size() {
    return this.mutations.size();
  }

  public String getText(final ASTNode rootNode, final String originalText) {

    final ASTRewrite rewrite = ASTRewrite.create(rootNode.getAST());
    this.mutations.forEach(m -> m.mutator.manipulateAST(m.node, rewrite));

    final Document document = new Document(originalText);
    try {
      final TextEdit edit = rewrite.rewriteAST(document, null);
      edit.apply(document);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new RuntimeException(e);
    }

    return document.get();
  }
}
