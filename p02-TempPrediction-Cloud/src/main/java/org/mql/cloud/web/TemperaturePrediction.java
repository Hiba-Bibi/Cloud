package org.mql.cloud.web;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.classifiers.Evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

public class TemperaturePrediction {
    public static void main(String[] args) throws Exception {
        // Chemin absolu du fichier ARFF
        String arffFilePath = "chemin/vers/votre/cloud.arff"; // Remplacez avec le chemin absolu
        File file = new File(arffFilePath);

        if (!file.exists()) {
            throw new FileNotFoundException("Fichier ARFF introuvable : " + arffFilePath);
        }

        // Charger le fichier ARFF
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();

        if (data == null || data.numInstances() == 0) {
            throw new IllegalStateException("Les données sont nulles ou vides. Vérifiez le fichier ARFF.");
        }

        // Définir la classe cible (le dernier attribut)
        data.setClassIndex(data.numAttributes() - 1);

        // Créer et entraîner le modèle J48
        J48 tree = new J48();
        tree.buildClassifier(data);

        // Evaluation avec validation croisée
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(tree, data, 10, new Random(1));

        // Affichage des résultats de l'évaluation
        System.out.println("Résumé de la validation croisée :");
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());
        System.out.println(eval.toMatrixString());
    }
}
