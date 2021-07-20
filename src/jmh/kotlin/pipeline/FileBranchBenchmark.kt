package pipeline

import astminer.config.*
import astminer.labelextractor.FileNameExtractor
import astminer.pipeline.Pipeline
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State

@State(Scope.Benchmark)
open class FileBranchBenchmark {
    @Setup
    fun pathsSetup() {
        BenchmarksSetup().setup()
    }

    @Benchmark
    fun Antlr_pathPresentation_longFile() {
        val setup = BenchmarksSetup()
        val config = PipelineConfig(
            inputDir = setup.longFilePath,
            outputDir = setup.longFileResultsPath,
            parser = ParserConfig(ParserType.Antlr, listOf(FileExtension.Java)),
            labelExtractor = FileNameExtractorConfig(),
            storage = CsvAstStorageConfig()
        )
        Pipeline(config).run()
    }

    @Benchmark
    fun Antlr_pathPresentation_Idea() {
        val setup = BenchmarksSetup()
        val config = PipelineConfig(
            inputDir = setup.bigProjectPath,
            outputDir = setup.bigProjectResultsPath,
            parser = ParserConfig(ParserType.Antlr, listOf(FileExtension.Java)),
            labelExtractor = FileNameExtractorConfig(),
            storage = CsvAstStorageConfig()
        )
        Pipeline(config).run()
    }

    @Benchmark
    fun GumTreeJDT_pathPresentation_longFile() {
        val setup = BenchmarksSetup()
        val config = PipelineConfig(
            inputDir = setup.longFilePath,
            outputDir = setup.longFileResultsPath,
            parser = ParserConfig(ParserType.GumTree, listOf(FileExtension.Java)),
            labelExtractor = FileNameExtractorConfig(),
            storage = CsvAstStorageConfig()
        )
        Pipeline(config).run()
    }
    
    @Benchmark
    fun GumTreeJDT_pathPresentation_idea() {
        val setup = BenchmarksSetup()
        val config = PipelineConfig(
            inputDir = setup.bigProjectPath,
            outputDir = setup.bigProjectResultsPath,
            parser = ParserConfig(ParserType.GumTree, listOf(FileExtension.Java)),
            labelExtractor = FileNameExtractorConfig(),
            storage = CsvAstStorageConfig()
        )
        Pipeline(config).run()
    }
}