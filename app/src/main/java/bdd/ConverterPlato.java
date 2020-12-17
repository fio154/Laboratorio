package bdd;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import model.Plato;

public class ConverterPlato {
    @TypeConverter
    public static ArrayList<Plato> toList(String value) {
        Type listType = new TypeToken<ArrayList<Plato>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(ArrayList<Plato> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}