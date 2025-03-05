package com.tolmic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CaesarsCipher {

    String englishLetters = "abcdifghijklmnopqrstuvwzyz";
    String russianLetters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private static HashMap<String, Double> getLetterStatistic(int sheetIndex) {
        HashMap<String, Double> letterStatistic = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream((new File("").getAbsolutePath()) + "/src/main/resources/tables/Letters.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                String letter = cellIterator.next().getStringCellValue();
                Double frequence = cellIterator.next().getNumericCellValue();
                
                letterStatistic.put(letter, frequence); 
            }

            workbook.close();
        } catch (IOException e) {
            
        }

        return letterStatistic;
    }

    private static String getText() {
        String content = "";

        try {
            content = Files.readString(Paths.get("file.txt"));
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return content;
    }

    private static HashMap<String, Double> getLetterStatisticFromText() {
        HashMap<String, Double> letterStatistic = new HashMap<>();

        char[] symbols = getText().toCharArray();

        int n = symbols.length;

        for (int i = 0; i < symbols.length; i++) {
            String symb = String.valueOf(symbols[i]);

            Double count = letterStatistic.get(symb);

            letterStatistic.put(symb, (symb != null ? count : 0) + (1 / n));
        }

        return letterStatistic;
    }

    private HashMap<String, Double> getRussianLetterStatistic() {
        return getLetterStatistic(0);
    }

    private HashMap<String, Double> getEnglishLetterStatistic() {
        return getLetterStatistic(1);
    }

    public void decrypt(int sheetIndex) {
        HashMap<String, Double> typicalFrequencyTable = getLetterStatistic(sheetIndex);
        HashMap<String, Double> letterStatistic = getLetterStatisticFromText();
        

    }

    public static void main(String[] args) throws IOException {
        getLetterStatistic(0);
    }

}
