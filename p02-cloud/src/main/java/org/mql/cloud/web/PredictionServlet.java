package org.mql.cloud.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class PredictionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String temperatureInput = request.getParameter("temperature");

        // Valider l'entrée utilisateur
        if (temperatureInput == null || temperatureInput.isEmpty()) {
            request.setAttribute("error", "La température doit être fournie.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            double temperature = Double.parseDouble(temperatureInput);

            // Charger le fichier ARFF
            String arffFilePath = getServletContext().getRealPath("/cloud.arff");
            DataSource source = new DataSource(arffFilePath);
            Instances data = source.getDataSet();

            if (data == null || data.numInstances() == 0) {
                throw new IllegalStateException("Les données sont nulles ou vides. Vérifiez le fichier ARFF.");
            }

            // Définir la classe cible (la dernière colonne de l'ARFF)
            data.setClassIndex(data.numAttributes() - 1);

            // Créer et entraîner le modèle J48
            J48 tree = new J48();
            tree.buildClassifier(data);

            // Créer une nouvelle instance pour prédiction
            DenseInstance newInstance = new DenseInstance(2);
            newInstance.setValue(data.attribute(0), temperature);
            newInstance.setDataset(data);

            // Prédiction
            double predictionIndex = tree.classifyInstance(newInstance);
            String predictedCategory = data.classAttribute().value((int) predictionIndex);

            // Rediriger vers une page avec les résultats
            request.setAttribute("prediction", predictedCategory);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/resultPage.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "La température doit être un nombre valide.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/error.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur dans le traitement du modèle : " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
