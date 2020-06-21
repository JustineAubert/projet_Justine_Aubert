package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search {

	//class pour ecrire les fonctions qui permettent de trouver un element par son annee, sa zonegeo... 
    private Map<Integer, List<Float>> yearsMap;
    private Map<String, List<Float>> geosMap;
    private List<Integer> indexOfYears;

    private static Search search = null;

    // Singleton
    public static Search getInstance() {
        if (search == null) {
            search = new Search();
        }
        return search;
    }

    public void setYearsMap(Map<Integer, List<Float>> yearsMap) {
        this.yearsMap = yearsMap;
        this.indexOfYears = new ArrayList<>(yearsMap.keySet());
    }

    public void setGeosMap(Map<String, List<Float>> geosMap) {
        this.geosMap = geosMap;
    }
    
//    public void setAnomalies(List<Anomaly> anomalies) {
//    	this.anomalies = anomalies;
//    }

    public Map<Integer, List<Float>> getYearsMap() {
        return this.yearsMap;
    }

    public Map<String, List<Float>> getGeosMap() {
        return this.geosMap;
    }

    public List<Float> findByYear(Integer year) {
        if (this.yearsMap.containsKey(year)) {
            return this.yearsMap.get(year);
        }
        return new ArrayList<>(1);
    }

    public Float findByYearAndByLongAndLat(Integer year, String lat, String Long) {
        List<Float> floatLatLongs = findByLongAndLat(lat, Long);

        return floatLatLongs.get(this.indexOfYears.indexOf(year));
    }
    
//    public List<Anomaly> findAnomaliesByYear(int year){
//    	return this.anomalies.stream().filter(anomaly -> anomaly.annee.getAnnee()== year).collect(Collectors.toList());
//    }
    
    

    public List<Float> findByLongAndLat(String lat, String Long) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(lat).append(",").append(Long);
//        System.out.println(lat + " " + Long);
        if (this.geosMap.containsKey(stringBuilder.toString())) {
            return this.geosMap.get(stringBuilder.toString());
        }
        return new ArrayList<>(1);
    }

    public float getTempMin() {
        List<Float> floats = new ArrayList<>();
        for (Map.Entry<String, List<Float>> entry : this.geosMap.entrySet()) {
            floats.addAll(entry.getValue());
        }
        final Float[] f = {0.0f};
        floats.forEach(tmpFloat -> {
            if(tmpFloat < f[0]) {
                f[0] =tmpFloat;
            }
        });
        return f[0];
    }

    public float getTempMax() {
        List<Float> floats = new ArrayList<>();
        for (Map.Entry<String, List<Float>> entry : this.geosMap.entrySet()) {
            floats.addAll(entry.getValue());
        }
        final Float[] f = {0.0f};
        floats.forEach(tmpFloat -> {
            if(tmpFloat > f[0]) {
                f[0] =tmpFloat;
            }
        });
        return f[0];
    }
    
    public List<Integer> getIndexOfYear() {
    	return this.indexOfYears;
    }
    
 public List<ZoneGeographique> zones() {
        
        return this.zones();
    }
    public List<Annee> year() {
        
        return this.year();
    }
    
    
}