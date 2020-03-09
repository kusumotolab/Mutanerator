package mutanerator;

import org.eclipse.jdt.core.dom.ASTNode;

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

  public void apply() {
    this.mutator.mutate(this);
  }

  public void unapply() {
    this.mutator.recover(this);
  }
}
