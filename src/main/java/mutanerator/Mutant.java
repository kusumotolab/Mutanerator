package mutanerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
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

  private final List<Mutation> mutations;

  static public List<Mutant> generateMutants(final List<Mutation> mutations,
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

      if(minimumMutations <= size && size <= maximumMutations){
        mutants.addAll(nextGenerationMutants);
      }

      currentGenerationMutants = nextGenerationMutants;
      nextGenerationMutants = new ArrayList<>();
    }while(size < maximumMutations);

    return mutants;
  }

  public Mutant() {
    this.mutations = new ArrayList<>();
  }

  private Mutant(final List<Mutation> mutations) {
    this();
    this.mutations.addAll(mutations);
  }

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

  public List<Mutation> getMutations() {
    return Collections.unmodifiableList(this.mutations);
  }

  public int size() {
    return this.mutations.size();
  }

  public String getText(final ASTNode rootNode, final String originalText){

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
