package com.tolmic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CaesarsCipher {

    private static class Letter {
        public String letter;
        public double frequency;

        public Letter(String letter, double frequency) {
            this.letter     = letter;
            this.frequency  = frequency;
        }
    }

    private static String englishLetters = "abcdifghijklmnopqrstuvwzyz";
    private static String russianLetters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private static String punctuationMarks = ".!?";

    private static Letter letter;

    private static Letter getLetterStatistic(int sheetIndex) {
        double maxFrequency = 0;

        try {
            FileInputStream fis = new FileInputStream((new File("").getAbsolutePath()) + "/src/main/resources/tables/Letters.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                String l = cellIterator.next().getStringCellValue();
                Double f = cellIterator.next().getNumericCellValue();
                
                if (f > maxFrequency) {
                    letter = new Letter(l, f);
                    maxFrequency = f;
                }
            }

            workbook.close();
        } catch (IOException e) {
            
        }

        return letter;
    }

    private static String getText() {
        String content = "";

        try {
            content = Files.readString(Paths.get((new File("").getAbsolutePath()) + "/src/main/resources/text/text1.txt"));
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return content;
    }

    private static Letter getLetterStatisticFromText(String text) {
        HashMap<String, Integer> letterStatistic = new HashMap<>();

        char[] symbols = text.toLowerCase().toCharArray();
        int n = symbols.length;

        double maxCount = 0;
        Letter letter = null;

        for (int i = 0; i < symbols.length; i++) {
            String symb = String.valueOf(symbols[i]);

            if (russianLetters.indexOf(symb) < 0) {
                continue;
            }

            Integer count = letterStatistic.get(symb);

            if (count == null) {
                letterStatistic.put(symb, 1);
            } else {
                letterStatistic.put(symb, count + 1);
            }

            count = letterStatistic.get(symb);
            if (count > maxCount) {
                letter = new Letter(symb, count);
                maxCount = count;
            }
        }

        return letter;
    }

    private static int calculateKey(String typical, String actual) {
        int typicalIndex = russianLetters.indexOf(typical.toLowerCase());
        int actualIndex = russianLetters.indexOf(actual.toLowerCase());

        return actualIndex - typicalIndex;
    }

    private static String getShiftedLetter(String letter, int key) {
        int n = russianLetters.length();

        int ind0 = russianLetters.indexOf(letter);

        int realIndex = ind0 - key;

        if (realIndex < 0) {
            realIndex = n - Math.abs(realIndex);
        } else if (realIndex >= n) {
            realIndex = realIndex - n;
        }

        return String.valueOf(russianLetters.charAt(realIndex));
    }

    private static String decryptMain(String text, int key) {
        List<String> decryptedText = new ArrayList<>();

        char[] chars = text.toLowerCase().toCharArray();

        boolean isNewSentence = true;
        for (char c : chars) {
            String symb = String.valueOf(c).toLowerCase();

            if (russianLetters.indexOf(symb) >= 0) {
                String descriptedLetter = getShiftedLetter(symb, key);
                
                if (isNewSentence) {
                    descriptedLetter = descriptedLetter.toUpperCase();
                    isNewSentence = false;
                }

                decryptedText.add(descriptedLetter);
            } else {
                if (punctuationMarks.indexOf(symb) >= 0) {
                    isNewSentence = true;
                }

                decryptedText.add(symb);
            }
        }

        return decryptedText.stream().collect(Collectors.joining());
    }

    public static void decrypt(int sheetIndex) {
        String text = getText();

        // Letter typicalMostCommonLetter = getLetterStatistic(sheetIndex);
        // Letter actualMostCommonLetter = getLetterStatisticFromText(text);
        
        // int key = calculateKey(typicalMostCommonLetter.letter, actualMostCommonLetter.letter);

        for (int i = 1; i <= 33; i++) {
            String descriptedText = decryptMain(text, i);

            System.out.println(descriptedText.substring(0, 50));
        }
    }

    public static void main(String[] args) throws IOException {
        decrypt(0);
        // System.out.println(getShiftedLetter("ю", -15));
    }

}
