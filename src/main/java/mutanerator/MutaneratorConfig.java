package mutanerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.kohsuke.args4j.Option;

public class MutaneratorConfig {

  static public final MutaneratorConfig SINGLETON = new MutaneratorConfig();

  private Path path = null;
  private Path outputDir = Paths.get(".", "mutations");
  private long seed = 0l;
  private int mutants = 100;
  private int minimumMutations = 1;
  private int maximumMutations = 1;
  private int startLine = 1;
  private int endLine = Integer.MAX_VALUE;

  @Option(name = "-f", required = true, aliases = "--file-path", metaVar = "<path>",
      usage = "path of target file for mutation")
  public void setPath(final String stringPath) {
    this.path = Paths.get(stringPath);
    if (!Files.isReadable(this.path)) {
      System.err.println("file path specified by option \"-f\" is not readable.");
      System.exit(1);
    }
  }

  @Option(name = "-r", required = false, aliases = "--random-seed", metaVar = "<value>",
      usage = "random seed for generating mutants")
  public void setRandomSeed(final long seed) {
    this.seed = seed;
  }

  @Option(name = "-m", required = false, aliases = "--mutants", metaVar = "<number>",
      usage = "maximum number of mutants to be generated")
  public void setMutants(final int mutants) {
    if (mutants < 1) {
      System.err.println("option \"-m\" must be larger than 0.");
      System.exit(1);
    }
    this.mutants = mutants;
  }

  @Option(name = "-u", required = false, aliases = "--mutations", metaVar = "<number>",
      usage = "mutations in each mutants")
  public void setMutations(final int mutations) {
    if (mutations < 1) {
      System.err.println("option \"-u\" must be larger than 0.");
      System.exit(1);
    }
    this.maximumMutations = mutations;
    this.minimumMutations = mutations;
  }

  @Option(name = "-s", required = false, aliases = "--start-line", metaVar = "<line>",
      usage = "start line of mutation target range")
  public void setStartLine(final int startLine) {
    if (startLine < 1) {
      System.err.println("option \"-s\" must be larger than 0.");
      System.exit(1);
    }
    this.startLine = startLine;
  }

  @Option(name = "-e", required = false, aliases = "--end-line", metaVar = "<line>",
      usage = "end line of mutation target range")
  public void setEndLine(final int endLine) {
    if (endLine < 1) {
      System.err.println("option \"-e\" must be larger than 0.");
      System.exit(1);
    }
    this.endLine = endLine;
  }

  @Option(name = "-o", required = false, aliases = "--output-dir", metaVar = "<dir>",
      usage = "output directory for generated mutations")
  public void setOutputDir(final String dir) {
    this.outputDir = Paths.get(dir);
  }

  public Path getPath() {
    return this.path;
  }

  public Path getOutputDir() {
    return this.outputDir;
  }

  public long getSeed() {
    return this.seed;
  }

  public int getMutants() {
    return this.mutants;
  }

  public int getMinimumMutations() {
    return this.minimumMutations;
  }

  public int getMaximumMutations() {
    return this.maximumMutations;
  }

  public int getStartLine() {
    return this.startLine;
  }

  public int getEndLine() {
    return this.endLine;
  }
}
