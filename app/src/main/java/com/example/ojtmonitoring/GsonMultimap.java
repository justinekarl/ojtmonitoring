package com.example.ojtmonitoring;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ties together Guava's multi-map and Gson for proper serialization.
 */
public class GsonMultimap {

    private static final Multimap<Integer, HashMap<Integer,Integer>> mmap = ArrayListMultimap.create();
    private static final Type mmapType = new TypeToken<Multimap<Integer, HashMap<Integer,Integer>>>(){}.getType();
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Multimap.class, new MultiMapAdapter<Integer, HashMap<Integer,Integer>>())
            .create();

//    public static void main(String[] args) {
//        String json = gson.toJson(mmap, mmapType);
//        System.out.println(json);
//        Multimap<KeyClass, ValueClass> mmap2 = gson.fromJson(json, mmapType);
//        System.out.println(Maps.difference(mmap.asMap(), mmap2.asMap()).entriesDiffering());
//        System.out.println(Maps.difference(mmap.asMap(), mmap2.asMap()).entriesOnlyOnLeft());
//        System.out.println(Maps.difference(mmap.asMap(), mmap2.asMap()).entriesOnlyOnRight());
//    }

    public static final class MultiMapAdapter<K,V> implements JsonSerializer<Multimap<K,V>>, JsonDeserializer<Multimap<K,V>> {
        private static final Type asMapReturnType;
        static {
            try {
                asMapReturnType = Multimap.class.getDeclaredMethod("asMap").getGenericReturnType();
            } catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public JsonElement serialize(Multimap<K, V> src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.asMap(), asMapType(typeOfSrc));
        }
        @Override
        public Multimap<K, V> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Map<K, Collection<V>> asMap = context.deserialize(json, asMapType(typeOfT));
            Multimap<K, V> multimap = ArrayListMultimap.create();
            for (Map.Entry<K, Collection<V>> entry : asMap.entrySet()) {
                multimap.putAll(entry.getKey(), entry.getValue());
            }
            return multimap;
        }

        private static Type asMapType(Type multimapType) {
            return TypeToken.of(multimapType).resolveType(asMapReturnType).getType();
        }
    }

    private static final class KeyClass {
        private long id;
        private String value;
        private boolean isActive;

        private KeyClass(long id, String value, boolean active) {
            this.id = id;
            this.value = value;
            isActive = active;
        }
    }

    private static final class ValueClass {
        private long id;
        private double[] rates;
        private List<Date> dates;

        private ValueClass(long id, double[] rates, List<Date> dates) {
            this.id = id;
            this.rates = rates;
            this.dates = dates;
        }
    }
}