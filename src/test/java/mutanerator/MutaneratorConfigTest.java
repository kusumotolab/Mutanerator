package mutanerator;

import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Test;

public class MutaneratorConfigTest {

  @Test
  public void testJavaVersion01() {
    MutaneratorConfig.SINGLETON.setJavaVersion("1.4");
    final JavaVersion javaVersion = MutaneratorConfig.SINGLETON.getJavaVersion();
    assertThat(javaVersion.getJavaCore()).isEqualTo(JavaCore.VERSION_1_4);
  }

  @Test
  public void testJavaVersion02() {
    MutaneratorConfig.SINGLETON.setJavaVersion("1.13");
    final JavaVersion javaVersion = MutaneratorConfig.SINGLETON.getJavaVersion();
    assertThat(javaVersion.getJavaCore()).isEqualTo(JavaCore.VERSION_13);
  }
}
