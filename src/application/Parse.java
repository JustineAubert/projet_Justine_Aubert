package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Parse {

    public static void getDataFromCSVFile(String csvFilePath) throws Exception {

    	//creation des listes ou seront stockees les donnees au lancement de l application. 
    	//ce peut etre des map ou des listes
        List<Annee> annees = new ArrayList<>();
        List<ZoneGeographique> zones = new ArrayList<>();
        Map<Integer, List<Float>> zoneAnneeMap = new HashMap<>();
        Map<String, List<Float>> zoneGeographiqueMap = new HashMap<>();
        FileReader file = new FileReader(csvFilePath);
        BufferedReader bufRead = new BufferedReader(file);
        
        
        // Lit ligne 1 on s'en sert pour stocker les annees dans une liste
        String line = bufRead.readLine();
        String[] donneesLigne = line.split(",");
        for (int i = 2; i < donneesLigne.length; i++) {
            String cellule = donneesLigne[i];
            cellule = cellule.replaceAll("\"", "");
            //on a une classe annee, donc chaque element de la liste annee et compose de type annee
            Annee annee = new Annee(Integer.parseInt(cellule));
            annees.add(annee);
        }
        
        // Lit les lines � partir de la 2de ( relev�s d'un zone / ann�e )
        line = bufRead.readLine();
        while (line != null) {
            List<Float> temper = new ArrayList<>();

            // Zone geo
            donneesLigne = line.split(",");
            float lat = Float.parseFloat(donneesLigne[0]); // stocker donnee lat
            float lon = Float.parseFloat(donneesLigne[1]); // stocker donnee lon
            ZoneGeographique zone = new ZoneGeographique(lat,lon);//on en cree une zone geo 
            line = bufRead.readLine();
            
            zones.add(zone);
            for (int i = 2; i < donneesLigne.length; i++) {
                Float temp = Float.NaN;
                if (donneesLigne[i].matches("NA")) {
                    temp = Float.NaN;
                }
                else {
                    temp = Float.parseFloat(donneesLigne[i]);
                }

                temper.add(temp);
            }
            // Si tableau pas de la meme taille que temperature, fichier non valide
            if (annees.size() != temper.size()) {
                System.out.println("Fichier non correct");
                System.out.println("Nombre d'annee : " + annees.size());
                System.out.println("Nombre de temperature : " + temper.size());
                throw new Exception();
            }
            AtomicInteger i = new AtomicInteger(0);
            annees.forEach(annee -> {
                // Check si la map contient l'annee
                if (zoneAnneeMap.containsKey(annee.getAnnee())) {
                    // Recuperer la liste de float
                    List<Float> tmp = zoneAnneeMap.get(annee.getAnnee());
                    List<Float> newList = new ArrayList<>(tmp);
                    // Ajout de la valeur
                    newList.add(temper.get(i.get()));
                    // Replace
                    zoneAnneeMap.replace(annee.getAnnee(), newList);
                }
                else {
                    // Sinon on cree une entree avec l'annee
                    zoneAnneeMap.put(annee.getAnnee(), Collections.singletonList((temper.get(i.get()))));
                }
                i.getAndIncrement();
            });
            zoneGeographiqueMap.put(donneesLigne[0] + "," + donneesLigne[1], temper);
       
        }
        bufRead.close();
        file.close();
        Search search = Search.getInstance();
        search.setGeosMap(zoneGeographiqueMap);
        search.setYearsMap(zoneAnneeMap);
        
    }
}