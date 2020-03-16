package mutanerator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * This class represents mutation.
 * A mutation consists of where a given program is changed and how it is changed.
 *
 * ミューテーション（変異）を表すクラスである．
 * 変異は，変更の箇所と変候の方法（変異子）で表される．
 */
public class Mutation {

  final public Mutator mutator;
  final public ASTNode node;

  public Mutation(final Mutator mutator, final ASTNode target) {
    this.mutator = mutator;
    this.node = target;
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Mutation)) {
      return false;
    }
    final Mutation target = (Mutation) o;
    return this.mutator.equals(target.mutator) && this.node.equals(target.node);
  }

  @Override
  public int hashCode() {
    return this.mutator.hashCode() + this.node.hashCode();
  }

  public String apply(final ASTNode rootNode, final String text) {

    final ASTRewrite rewrite = ASTRewrite.create(rootNode.getAST());
    this.mutator.manipulateAST(this.node, rewrite);

    final Document document = new Document(text);
    try {
      final TextEdit edit = rewrite.rewriteAST(document, null);
      edit.apply(document);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new RuntimeException(e);
    }

    return document.get();
  }
}
