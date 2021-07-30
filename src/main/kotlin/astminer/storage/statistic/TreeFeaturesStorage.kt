package astminer.storage.statistic

import astminer.common.model.LabeledResult
import astminer.common.model.Node
import astminer.common.model.Storage
import astminer.featureextraction.BranchingFactor
import astminer.featureextraction.Depth
import astminer.featureextraction.NodeTypes
import astminer.featureextraction.NumberOfNodes
import java.io.File
import java.io.PrintWriter
import kotlin.math.floor
import kotlin.math.log

class TreeFeaturesStorage(override val outputDirectoryPath: String) : Storage {
    private val numberOfNodes = mutableListOf<Int>()
    private val branching = mutableListOf<Double>()
    private val depths = mutableListOf<Int>()
    private val allTypes = mutableSetOf<String>()
    private var numOfUnits = 0

    private val statWriter : PrintWriter
    private val statFile : File

    init {
        File(outputDirectoryPath).mkdirs()
        statFile = File(outputDirectoryPath).resolve("stat.txt")
        statFile.createNewFile()
        statWriter = PrintWriter(statFile)
    }

    override fun store(labeledResult: LabeledResult<out Node>) {
        branching.add(BranchingFactor.compute(labeledResult.root))
        depths.add(Depth.compute(labeledResult.root))
        allTypes.addAll(NodeTypes.compute(labeledResult.root))
        numberOfNodes.add(NumberOfNodes.compute(labeledResult.root))
        numOfUnits++
    }

    override fun close() {
        statWriter.println("Number of parse results: $numOfUnits")
        evaluateIntFeature(numberOfNodes, "Number of nodes")
        evaluateIntFeature(depths, "Tree depth")
        evaluateDoubleFeature(branching, "Branching factor")
        statWriter.println("Number of unique types: ${allTypes.size}")
        statWriter.close()
    }

    private fun evaluateIntFeature(featureList: List<Int>, featureName: String) {
        evaluateDoubleFeature(featureList.map { it.toDouble() }, featureName)
    }

    private fun evaluateDoubleFeature(featureList: List<Double>, featureName: String) {
        statWriter.println("$featureName : [min : ${featureList.minOrNull()}, max : ${featureList.maxOrNull()}, avg: ${featureList.average()}]")
        statWriter.println("Distribution:")
        featureList.groupBy { floor(log(it, 10.0)) }.toSortedMap()
            .map { entry -> "10^${entry.key} - 10^${entry.key + 1} ${entry.value.size}" }
            .map { line -> statWriter.println(line) }
        statWriter.println()
    }
}