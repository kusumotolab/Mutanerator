package mutanerator;

import static java.lang.System.exit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.kohsuke.args4j.Option;

public class MutaneratorConfig {

  static public final MutaneratorConfig SINGLETON = new MutaneratorConfig();

  private Path path = null;
  private Path outputDir = Paths.get(".", "mutations");
  private Path logFile = null;
  private long seed = 0l;
  private int mutants = 100;
  private int minimumMutations = 1;
  private int maximumMutations = 1;
  private int startLine = 1;
  private int endLine = Integer.MAX_VALUE;
  private JavaVersion javaVersion = JavaVersion.V1_8;

  @Option(name = "-f", required = true, aliases = "--file-path", metaVar = "<path>",
      usage = "path of target file for mutation")
  public void setPath(final String stringPath) {
    this.path = Paths.get(stringPath);
    if (!Files.isReadable(this.path)) {
      System.err.println("file path specified by option \"-f\" is not readable.");
      System.exit(1);
    }
  }

  @Option(name = "-l", required = true, aliases = "--log-file", metaVar = "<path>",
      usage = "path of log file")
  public void setLogFile(final String stringPath) {
    this.logFile = Paths.get(stringPath);
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

  @Option(name = "--max-mutations", required = false, metaVar = "<number>",
      usage = "maximum mutations in each mutants")
  public void setMaximumMutations(final int mutations) {
    if (mutations < 1) {
      System.err.println("option \"--max-mutations\" must be larger than 0.");
      System.exit(1);
    }
    this.maximumMutations = mutations;
  }

  @Option(name = "--min-mutations", required = false, metaVar = "<number>",
      usage = "minimum mutations in each mutants")
  public void setMinimumMutations(final int mutations) {
    if (mutations < 1) {
      System.err.println("option \"--min-mutations\" must be larger than 0.");
      System.exit(1);
    }
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

  @Option(name = "-j", required = false, aliases = "--java-version", metaVar = "<version>",
      usage = "java version of target source files")
  public void setJavaVersion(final String versionText) {
    this.javaVersion = JavaVersion.get(versionText);
    if (null == this.javaVersion) {
      System.err.println("an invalid value is specified for option \"-j\".");
      System.err.println("specify Java version in \"1.4\" ~ \"1.13\".");
      exit(1);
    }
  }

  public Path getPath() {
    return this.path;
  }

  public Path getLogFile() {
    return this.logFile;
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

  public JavaVersion getJavaVersion() {
    return this.javaVersion;
  }
}
