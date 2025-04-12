package com.tolmic;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CaesarsClient {

    //#region vsu-textes
    public static void text1() {
        Map<String, String> map = new TreeMap<>();
        map.put("ч", "и");
        map.put("у", "з");
        map.put("й", "а");
        map.put("ъ", "н");
        map.put("б", "л");
        map.put("л", "т");
        map.put("и", "о");
        map.put("в", "к");
        map.put("ц", "м");
        map.put("п", "е");
        map.put("ф", "ь");
        map.put("ы", "в");
        map.put("к", "п");
        map.put("а", "р");
        map.put("х", "ч");
        map.put("э", "г");
        map.put("ж", "ф");
        map.put("г", "д");
        map.put("д", "с");
        map.put("ю", "ы");
        map.put("т", "б");
        map.put("р", "щ");
        map.put("о", "ж");
        map.put("м", "ш");
        map.put("е", "у");
        map.put("ь", "х");
        map.put("с", "э");
        map.put("н", "й");
        map.put("ш", "ц");
        map.put("з", "ю");

        CaesarsCipher.descryptPolyAlph(map, "text1");
    }

    public static void text2() {
        // ьрь, эъэ, плт
        Map<String, String> map = new TreeMap<>();
        // "э" - не "а"
        // "жи" не "но" и "э" не "у" - одновременно,
        // "ь" - не "в", "т", "у"
        // "р" - не "н", "е", "у", "ю", "я"
        // "ьрь" - не "или" 
        // "кр" - не "ли"
        // "к" - не "з"

        map.put("г", "б");
        map.put("э", "и");
        map.put("л", "т");
        map.put("ь", "к");
        map.put("т", "о");
        map.put("х", "й");
        map.put("к", "н");
        map.put("п", "ч");
        map.put("р", "а");
        map.put("д", "е");
        map.put("й", "э");
        map.put("ю", "р");
        map.put("и", "ы");
        map.put("ъ", "л");
        map.put("о", "ь");
        map.put("ж", "м");
        map.put("ф", "у");
        map.put("щ", "х");
        map.put("а", "г");
        map.put("в", "с");
        map.put("я", "ш");
        map.put("н", "в");
        map.put("з", "п");
        map.put("ы", "ю");
        map.put("б", "д");
        map.put("ш", "з");
        map.put("у", "ж");
        map.put("е", "я");
        map.put("с", "ц");
        map.put("ц", "щ");
        map.put("ч", "ф");
        map.put("м", "ъ");

        // System.out.println(map.keySet().stream().collect(Collectors.joining()));

        CaesarsCipher.descryptPolyAlph(map, "text2");
    }

    public static void text3() {
        //

        Map<String, String> map = new TreeMap<>();

        map.put("р", "и");

        // охс чхс хс зхс
        map.put("э", "н");
        map.put("х", "т");
        map.put("с", "о");
        map.put("з", "ч");
        map.put("ч", "э");
        map.put("о", "к");
        map.put("у", "х");
        map.put("к", "р");
        map.put("д", "е");
        map.put("ю", "п");
        map.put("ц", "м");
        map.put("и", "г");
        map.put("г", "а");
        map.put("й", "е");
        map.put("ф", "б");
        map.put("а", "л");
        map.put("й", "с");
        map.put("и", "г");
        map.put("е", "ь");
        map.put("т", "д");
        map.put("ж", "ы");
        map.put("п", "щ");
        map.put("л", "у");
        map.put("ъ", "й");
        map.put("м", "з");
        map.put("б", "ю");
        map.put("н", "ж");
        map.put("щ", "ш");
        map.put("ы", "я");
        map.put("ь", "ф");
        map.put("ш", "ц");
        map.put("я", "ъ");

        // System.out.println(map.keySet().stream().collect(Collectors.joining()));
        
        CaesarsCipher.descryptPolyAlph(map, "text3");
    }

    public static void text4() {
        // "кк" - ?
        // "сг" - ?
        // "Му,"
        // "ое" - не "ка"

        Map<String, String> map = new TreeMap<>();
        map.put("ч", "ы");
        map.put("г", "а");
        map.put("л", "к");
        map.put("ц", "в");
        map.put("ж", "ч");
        map.put("м", "т");
        map.put("у", "о");
        map.put("и", "р");
        map.put("ю", "й");
        map.put("ы", "м");
        map.put("о", "б");
        map.put("в", "с");
        map.put("к", "е");
        map.put("ъ", "п");
        map.put("щ", "л");
        map.put("з", "ж");
        map.put("д", "э");
        map.put("е", "г");
        map.put("с", "н");
        map.put("а", "и");
        map.put("я", "у");
        map.put("т", "д");
        map.put("б", "ь");
        map.put("ф", "з");
        map.put("х", "ц");
        map.put("р", "х");
        map.put("ь", "щ");
        map.put("э", "я");
        map.put("ш", "ю");
        map.put("н", "ш");
        map.put("п", "ф");
        map.put("й", "ъ");

        System.out.println(map.keySet().stream().collect(Collectors.joining()));

        CaesarsCipher.descryptPolyAlph(map, "text4");
    }

    public static void text5() {
        // "Жуа хжч" - ?
        // Ачяруцдд - две одинаковые буквы на конец !
        // "ачжчибг" - подходит как "который"

        Map<String, String> map = new TreeMap<>();

        // ачжчибг
        map.put("а", "к");
        map.put("ч", "о");
        map.put("ж", "т");
        map.put("и", "р");
        map.put("б", "ы");
        map.put("х", "ч");
        map.put("у", "а");        
        map.put("ц", "н");
        map.put("ы", "ж");
        map.put("г", "е");
        map.put("я", "м");
        map.put("с", "г");
        map.put("щ", "в");
        map.put("е", "д");
        map.put("т", "л");
        map.put("в", "у");
        map.put("р", "п");
        map.put("д", "и");
        map.put("п", "ю");
        map.put("ф", "х");
        map.put("с", "г");
        map.put("ш", "з");
        map.put("о", "ц");
        map.put("н", "э");
        map.put("к", "я");
        map.put("л", "б");
        map.put("й", "ь");
        map.put("з", "ш");
        map.put("м", "й");
        map.put("ю", "с");
        map.put("э", "ф");
        map.put("ъ", "щ");
        map.put("ь", "ъ");


        System.out.println(map.keySet().stream().collect(Collectors.joining()));

        CaesarsCipher.descryptPolyAlph(map, "text5");
    }

    public static void text6() {
        // "ки-ию" - намёк на "из-за"
        // "ьз" - перед ним нет пробела, возможно, это "но" 
        // "хмз" - возможно "что"

        Map<String, String> map = new TreeMap<>();
        map.put("к", "и");
        map.put("и", "з");
        map.put("ю", "а");
        map.put("ь", "н");
        map.put("з", "о");
        map.put("х", "ч");
        map.put("м", "т");
        map.put("у", "к");
        map.put("ш", "е");
        map.put("в", "э");
        map.put("ъ", "с");
        map.put("л", "ь");
        map.put("а", "л");
        map.put("ы", "м");
        map.put("д", "р");
        map.put("щ", "б");
        map.put("э", "х");
        map.put("р", "д");
        map.put("н", "ж");
        map.put("ж", "й");
        map.put("е", "г");
        map.put("ф", "п");
        map.put("ц", "в");
        map.put("я", "ы");
        map.put("о", "я");
        map.put("ч", "у");
        map.put("п", "ю");
        map.put("т", "щ");
        map.put("с", "ц");
        map.put("б", "ш");
        map.put("й", "ф");


        System.out.println(map.keySet().stream().collect(Collectors.joining()));

        CaesarsCipher.descryptPolyAlph(map, "text6");
    }

    public static void text7() {
        // "Шм-мб" - это скорее всего "из-за" - отсюда "шлш" - это "или"
        // "вйц-ын" - не "как-то" из за "шм-мб". "что-то", "кто-то" - не подходит, остаётся "чем-то"
        // "ъъйеа" - NB!

        Map<String, String> map = new TreeMap<>();
        map.put("ш", "и");
        map.put("м", "з");
        map.put("б", "а");
        map.put("а", "х");
        map.put("и", "к");
        map.put("г", "к");
        map.put("к", "н");
        map.put("ы", "т");
        map.put("й", "е");
        map.put("е", "р");
        map.put("ь", "с");
        map.put("н", "о");
        map.put("в", "ч");
        map.put("ц", "м");
        map.put("у", "д");
        map.put("т", "н");
        map.put("ю", "п");
        map.put("п", "ь");
        map.put("ъ", "в");
        map.put("ж", "ы");
        map.put("р", "ц");
        map.put("с", "й");
        map.put("ф", "г");
        map.put("о", "я");
        map.put("я", "ж");
        map.put("д", "у");
        map.put("э", "Ф");
        map.put("ч", "ю");
        map.put("х", "б");
        map.put("з", "э");


        System.out.println(map.keySet().stream().collect(Collectors.joining()));

        CaesarsCipher.descryptPolyAlph(map, "text7");
    }
    //#endregion

    //#region vishener
    public static void vishenerRun() {
        CaesarsCipher.decryptVishener();
    }
    //#endregion

    public static void main(String[] args) {
        text1();
        text2();
        text3();
        text4();
        text5();
        text6();
        text7();
    }

}
