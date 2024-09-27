package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private final Map<String, List<String>> countryLanguages;
    private final Map<String, Map<String, String>> translations;
    private final Map<String, String> codeToCountry;
    private final Map<String, String> countryToCode;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            final JSONArray jsonArray = new JSONArray(jsonString);

            this.countryLanguages = new HashMap<>();
            this.translations = new HashMap<>();
            this.codeToCountry = new HashMap<>();
            this.countryToCode = new HashMap<>();

            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String country = jsonObject.getString("en");
                String countryCode = jsonObject.getString("alpha3");
                List<String> languages = new ArrayList<>();
                Map<String, String> translates = new HashMap<>();

                for (String i: jsonObject.keySet()) {
                    if (!("id".equals(i)) && !("alpha2".equals(i)) && !("alpha3".equals(i))) {
                        languages.add(i);
                        translates.put(i, jsonObject.getString(i));
                    }
                }
                countryLanguages.put(country, languages);
                translations.put(countryCode, translates);
                codeToCountry.put(countryCode, country);
                countryToCode.put(country, countryCode);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        String countryName = codeToCountry.get(country);
        return countryLanguages.get(countryName);
    }

    @Override
    public List<String> getCountries() {
        List<String> countryCodeList = new ArrayList<>();
        for (String i: countryLanguages.keySet()) {
            countryCodeList.add(countryToCode.get(i));
        }
        return countryCodeList;
    }

    @Override
    public String translate(String country, String language) {
        if (getCountries().contains(country) && translations.get(country).containsKey(language)) {
            return translations.get(country).get(language);
        }
        return null;
    }
}
