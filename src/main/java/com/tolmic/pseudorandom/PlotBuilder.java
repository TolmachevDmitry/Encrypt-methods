package com.tolmic.pseudorandom;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public final class PlotBuilder {

    public static void buildGistogram(String[] categories, int[] values, String tittle) {
        // Создаем dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i = 0; i < categories.length; i++) {
            dataset.addValue(values[i], "Частота попадания", categories[i]);
        }

        // Создаем столбчатую диаграмму (гистограмму)
        JFreeChart chart = ChartFactory.createBarChart(
                tittle,       // Заголовок
                "Диапазоны",      // Ось X (категории)
                "Частота попадания", // Ось Y (значения)
                dataset,
                PlotOrientation.VERTICAL, // Ориентация (VERTICAL или HORIZONTAL)
                true,                     // Показывать легенду?
                true,                     // Показывать подсказки?
                false                     // Показывать URL?
        );

        // Отображаем график в окне
        ChartFrame frame = new ChartFrame("Гистограмма продаж", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public static String[] formTittle(double highLimit, int n) {
        double h = highLimit / n;

        String[] tittles = new String[n];

        for (int i = 0; i < n; i++) {
            String a = String.format("%.5f", i * h);
            String b = String.format("%.5f", (i + 10) * h);

            tittles[i] = a + " - " + b;
        }

        return tittles;
    }

}
