package com.tolmic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.tolmic.utils.MathUtils.mean;
import static com.tolmic.utils.MathUtils.error;


public class CaesarsCipher {

    //#region Clases for intermediate counters
    private static class Letter {
        public String letter;
        public double frequency;

        public Letter(String letter, double frequency) {
            this.letter     = letter;
            this.frequency  = frequency;
        }
    }

    private static class Separator {
        int index;
        String text;

        public Separator(int index, String text) {
            this.index = index;
            this.text = text;
        }
    }
    //#endregion

    //#region Local variables
    private static String englishLetters = "abcdifghijklmnopqrstuvwzyz";
    private static String russianLetters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    private static final int russianLettersNumber = russianLetters.length();

    private static String wordSeparators = ".!?,:-\n\"\r()«»0123456789[]<>— ";

    private static final double russianLanguageIndex = 0.0553;

    private static Letter letter;
    //#endregion

    //#region Letter-statistics
    private static Map<String, Double> getLetterStatistic() {
        Map<String, Double> map = new TreeMap<>();

        try {
            FileInputStream fis = new FileInputStream((new File("").getAbsolutePath()) + "/src/main/resources/tables/Letters.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                String l = cellIterator.next().getStringCellValue().toLowerCase();
                Double f = cellIterator.next().getNumericCellValue();
                
                map.put(l, f);
            }

            workbook.close();
        } catch (IOException e) {

        }

        return map;
    }

    private static void writeStatistic(Map<String, Integer> map, double n) throws Exception {
        Iterator<String> letters = map.keySet().iterator();

        try (Workbook workbook = new XSSFWorkbook()) {
            
            Sheet sheet = workbook.createSheet("Фактические частоты");
            
            int i = 0;
            while (letters.hasNext()) {
                String letter = letters.next();

                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(letter);
                row.createCell(1).setCellValue(map.get(letter) / n);

                i += 1;
            }
            
            try (FileOutputStream out = new FileOutputStream((new File("").getAbsolutePath()) + "/src/main/resources/tables/Letters.xlsx")) {
                workbook.write(out);
            }
        }

    }

    private static Letter getLetterStatisticFromText(String[] text) {
        HashMap<String, Integer> letterStatistic = new HashMap<>();
        
        int n = text.length;

        double maxCount = 0;
        Letter letter = null;

        for (int i = 0; i < n; i++) {
            String symb = text[i];

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
    
    private static Map<String, Integer> calculateInvestmentNumber(String[] sequence) {
        Map<String, Integer> investmentNumbers = new TreeMap<>();

        for (String letter : sequence) {
            Integer number = investmentNumbers.get(letter);
            
            investmentNumbers.put(letter, (number != null ? number : 0) + 1);
        }

        return investmentNumbers;
    }
    //#endregion

    //#region Working with I/O data
    private static String[] getText(String file) {
        String text = "";

        try {
            text = Files.readString(Paths.get((new File("").getAbsolutePath()) + "/src/main/resources/text/" + file + ".txt"));
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return Arrays.stream(text.split("")).toArray(String[]::new);
    }

    public static List<Separator> getSeparators(String[] text) {
        int n = text.length;

        boolean isSeparator = false;
        String sep = "";
        int index = -1;
        int sum = 0;

        List<Separator> separators = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            String symb = text[i];

            if (wordSeparators.indexOf(symb) != -1) {
                if (!isSeparator) {
                    isSeparator = true;
                    index = i;
                }

                sep += symb;
            } else if (isSeparator) {
                isSeparator = false;
                separators.add(new Separator(index - sum, sep));

                sum += sep.length();
                sep = "";
            }
        }

        return separators;
    }

    public static void writeToFile(String text, String file) {
        String path = (new File("").getAbsolutePath()) + "/src/main/resources/text/" + file + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {  // Автоматическое закрытие
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getLetterSequence(String[] text) {
        int n = text.length;

        List<String> letterSequence = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String symb = text[i];

            if (wordSeparators.indexOf(symb) == -1) {
                letterSequence.add(symb.toLowerCase());
            }
        }

        return letterSequence.stream().toArray(String[]::new);
    }

    private static String buildDescryptedText(String[] text, List<Separator> separators) {
        int n = text.length;

        StringBuilder sb = new StringBuilder("");

        int separatorNumber = -1;
        Separator currSeparator = new Separator(0, "");

        for (int i = 0; i < n; i++) {
            String symb = text[i];

            if (currSeparator.index == i) {
                String separatorText = currSeparator.text;
                sb.append(separatorText);

                symb = (separatorText.contains(".") || i == 0) ? symb.toUpperCase() : symb;
                
                separatorNumber += 1;
                currSeparator = (separatorNumber < separators.size()) ? separators.get(separatorNumber) : currSeparator;
            }

            sb.append(symb);
        }

        return sb.toString();
    }
    
    public static void countTextStatisticsAndWriteToTable(String file) {
        String[] text = getLetterSequence(getText(file));

        Map<String, Integer> map = calculateInvestmentNumber(text);

        try {
            writeStatistic(map, text.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //#endregion

    //#region Decryption methods

    private static String fromArrayToString(String[] textArray) {
        return Stream.of(textArray).collect(Collectors.joining());
    }

    //#region Vishener cipher
    private static String[][] buildMatrix(String[] text, int t) {
        int m = text.length / t;
        String[][] matrix = new String[t][m];

        for (int i = 0; i < t; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = text[i + j * t];
            }
        }

        return matrix;
    }

    private static void printAnalysisResult(int t, double mean, String tittle) {
        System.out.println(tittle);
        System.out.println("i: " + t);
        System.out.println("Mean: " + mean);
        System.out.println();
    }

    private static double countIMSum(Map<String, Integer> map) {
        return map.values()
                        .stream()
                        .map(ni -> ni * (ni - 1))
                        .mapToInt(Integer::intValue)
                        .sum();
    }

    private static double[] countIM(String[][] groups) {
        int n = groups.length;
        int m = groups[0].length;

        double[] ic = new double[n];
        for (int i = 0; i < n; i++) {
            Map<String, Integer> map = calculateInvestmentNumber(groups[i]);

            ic[i] = countIMSum(map) / (m * (m - 1));                                              
        }

        return ic;
    }

    private static void findKeyLength(String[] text) {
        for (int t = 1; t < 20; t++) {
            String[][] matrix = buildMatrix(text, t);

            double[] ic = countIM(matrix);

            double mean = mean(ic);

            printAnalysisResult(t, mean, "IC");
        }
    }

    private static String shiftLetter(String letter, int h) {
        int x1 = russianLetters.indexOf(letter);
        int x2 = (x1 + h) % russianLettersNumber;

        if (x2 < 0) {
            x2 = russianLettersNumber + x2;
        }

        return russianLetters.charAt(x2) + "";
    }

    private static int askUserAboutKeySize() {
        int t = 0;
        
        try {
            BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            System.out.print("Based counted ic, choose key size: ");
            t = Integer.valueOf(scanner.readLine());
            System.out.println();
        } catch (IOException e) {
            
        }

        return t;
    }

    private static double findMI(String[] row1, String[] row2, int s) {
        Map<String, Integer> frequency1 = calculateInvestmentNumber(row1);
        Map<String, Integer> frequency2 = calculateInvestmentNumber(row2);

        Iterator<String> rowLetters1 = frequency1.keySet().iterator();

        int n = row1.length;
        double mi = 0;
        while (rowLetters1.hasNext()) {
            String l = rowLetters1.next();

            Integer f1 = frequency1.get(l);
            Integer f2 = frequency2.get(shiftLetter(l, s));
            f2 = f2 != null ? f2 : 0;

            mi += f1 * f2 ;
        }

        return mi / (n * n);
    }

    private static int[] mutualCoincidenceIndex(String[] text, int t) {
        String[][] m = buildMatrix(text, t);

        int[] shifts = new int[t];

        for (int i = 1; i < t; i++) {
            double minError = 1000;

            for (int s = 1; s <= russianLettersNumber; s++) {
                double mi = findMI(m[i], m[0], s);

                double error = error(mi, russianLanguageIndex);
                if (error < minError) {
                    minError = error;
                    shifts[i] = s;
                }
            }
        }

        return shifts;
    }

    private static String vigenere(String c, String k) {
        int h = russianLetters.indexOf(k);
        return shiftLetter(c, -h);
    }

    private static String d(String[] openText, String[] key) {
        int n = openText.length;
        int k = key.length;

        String[] mText = new String[n];
        for (int i = 0; i < n; i++) {
            mText[i] = vigenere(openText[i], key[i % k]);
        }

        return Stream.of(mText).collect(Collectors.joining());
    }

    private static void goThroughLetters(String[] text, int[] shifts) {
        System.out.println("Possible descriptions: ");

        int k = shifts.length;

        for (int i = 0; i < russianLettersNumber; i++) {
            String firstLetter = String.valueOf(russianLetters.charAt(i));

            String[] key = new String[k];
            key[0] = firstLetter;
            for (int j = 1; j < k; j++) {
                key[j] = shiftLetter(firstLetter, -shifts[j]);
            }
            
            System.out.print(russianLetters.indexOf(firstLetter) + ": ");
            System.out.println(d(text, key).substring(0, 20));
        }
    }

    private static String askUserAboutFirstKeyLetter() {
        int letterNumber = 0;
        
        try {
            BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            System.out.print("How letter you decided to have in first place of key: ");
            letterNumber = Integer.valueOf(scanner.readLine());
            System.out.println();
        } catch (IOException e) {

        }

        return String.valueOf(russianLetters.charAt(letterNumber));
    }

    private static String[] formKey(int[] shifts) {
        String firstLetter = askUserAboutFirstKeyLetter();

        int n = shifts.length;
        String[] key = new String[n];

        key[0] = firstLetter;
        for (int i = 1; i < n; i++) {
            key[i] = shiftLetter(firstLetter, -shifts[i]);
        }
        
        return key;
    }

    public static void decryptVishener() {
        String[] openText = getText("Vishener");
        List<Separator> separators = getSeparators(openText);

        String[] letterSequence = getLetterSequence(openText);

        findKeyLength(letterSequence);

        int keySize = askUserAboutKeySize();

        int[] shifts = mutualCoincidenceIndex(letterSequence, keySize);

        goThroughLetters(letterSequence, shifts);

        String[] key = formKey(shifts);

        System.out.println("Descripted text: " + d(letterSequence, key));
    }
    //#endregion

    //#region Description groping method
    private static void changeLetter(String[] letterSequence, Map<String, String> map) {
        int n = letterSequence.length;

        for (int i = 0; i < n; i++) {
            String t = map.get(letterSequence[i]);

            if (t != null) {
                letterSequence[i] = t;
            }
        }
    }

    public static void descryptPolyAlph(Map<String, String> map, String file) {
        String[] openText = getText(file);

        List<Separator> separators = getSeparators(openText);
        String[] letterSequence = getLetterSequence(openText);

        changeLetter(letterSequence, map);

        String desryptedText = buildDescryptedText(letterSequence, separators);

        System.out.println("Descrypted " + file + ":");
        System.out.println(desryptedText);
    }
    //#endregion

    //#region Simple Caesars cipher
    private static void shiftAllLetters(String[] letterSequence, int key) {
        int n = letterSequence.length;
        for (int i = 0; i < n; i++) {
            String letter = letterSequence[i].toLowerCase();

            if (russianLetters.indexOf(letter) != -1) {
                letterSequence[i] = shiftLetter(letter, key);
            }
        }
    }

    public static void encryptSimpleCaesar(int key) {
        String[] mText = getText("CaesarM");

        shiftAllLetters(mText, key);

        writeToFile(fromArrayToString(mText), "Caesar");
    }

    public static void decriptSimpleCaesar(int key) {
        String[] openText = getText("Caesar");

        shiftAllLetters(openText, -key);

        System.out.println(fromArrayToString(openText));
    }
    //#endregion
    //#endregion

    public static void main(String[] args) throws Exception {
        int key = 5;

        decriptSimpleCaesar(key);
    }

}
