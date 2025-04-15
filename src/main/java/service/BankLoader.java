package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Bank;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class BankLoader {
    public static List<Bank> loadBanksFromJson(String filePath) {
        List<Bank> bankList = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            Type listType = new TypeToken<List<Bank>>() {}.getType();
            bankList = new Gson().fromJson(dataArray, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bankList;
    }
}
