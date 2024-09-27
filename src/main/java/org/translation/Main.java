package org.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
         Translator translator = new JSONTranslator();
//        Translator translator = new InLabByHandTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String country = promptForCountry(translator);
            String quit = "quit";
            if (quit.equals(country)) {
                break;
            }
            // TODO Task: Once you switch promptForCountry so that it returns the country
            //            name rather than the 3-letter country code, you will need to
            //            convert it back to its 3-letter country code when calling promptForLanguage
            String language = promptForLanguage(translator, country);
            if (quit.equals(country)) {
                break;
            }
            // TODO Task: Once you switch promptForLanguage so that it returns the language
            //            name rather than the 2-letter language code, you will need to
            //            convert it back to its 2-letter language code when calling translate.
            //            Note: you should use the actual names in the message printed below though,
            //            since the user will see the displayed message.
            CountryCodeConverter counConverter = new CountryCodeConverter();
            LanguageCodeConverter lanConverter = new LanguageCodeConverter();
            System.out.println(country + " in " + language + " is "
                    + translator.translate(counConverter.fromCountry(country), lanConverter.fromLanguage(language)));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        List<String> coutryNames = new ArrayList<>();
        CountryCodeConverter converter = new CountryCodeConverter();
        for (String country : countries) {
            coutryNames.add(converter.fromCountryCode(country));
        }
        coutryNames.sort(String::compareTo);
//        System.out.println(countries);

        System.out.println(coutryNames);

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        CountryCodeConverter converter = new CountryCodeConverter();
        String coutryCode = converter.fromCountry(country);
        List<String> languages = translator.getCountryLanguages(coutryCode);

        List<String> langName = new ArrayList<>();
        LanguageCodeConverter langConverter = new LanguageCodeConverter();
        for (String lanCode : languages) {
            langName.add(langConverter.fromLanguageCode(lanCode));
        }
        langName.sort(String::compareTo);

//        System.out.println(languages);

        System.out.println(langName);

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
