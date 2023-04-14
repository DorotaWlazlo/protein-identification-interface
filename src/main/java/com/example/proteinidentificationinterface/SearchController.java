package com.example.proteinidentificationinterface;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import apps.mscandb.*;
import mscanlib.common.*;
import mscanlib.ms.db.DB;
import mscanlib.ms.db.DBTools;
import mscanlib.ms.mass.*;
import mscanlib.ms.msms.*;
import mscanlib.ms.msms.dbengines.*;
import mscanlib.ms.msms.dbengines.mscandb.io.*;
import mscanlib.ms.mass.EnzymeMap;
import mscanlib.ms.msms.io.*;
import mscanlib.system.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SearchController implements DbEngineListener {
    private String					mFilenames[] = null;	//nazwy plikow wejsciowych
    private DbEngineSearchConfig    mConfig = null;		//obiekt konfiguracji przeszukania
    private PrintWriter             out = null;

    @PostMapping("/")
    protected void doPost(@ModelAttribute("configFormObject") ConfigFormObject configFormObject) throws IOException
    {
        /*
         * Pobieranie nazwy pliku z danymi
         */
        MultipartFile file = configFormObject.getFile();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(!fileName.isEmpty())
        {
            /*
             * Zapis pliku z danymi na serwerze (docelowo trzeba zadbać o )
             */
            String dir = "upload/dir";

            Path path = Paths.get(dir + "/" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            this.mFilenames = new String[]{path.toString()};

            /*
             * Pobranie konfiguracji z HttpServletRequest. Mysle, ze takie rozwiazanie bedzie wygodniejsze niz tworzenie pliku.
             */
            this.mConfig = this.getConfig(configFormObject);

            //this.out = response.getWriter();

            /*
             * Uruchomienia przeszukania: informacje o jego rozpoczeciu, przebiegu i zakonczeniu sa dostepne w metodach interfejsu DbEngineListener (notifyInitalized, notifyUpdated i notifyFinished)
             */
            MScanDb dbEngine = new MScanDb(this.mFilenames,this.mConfig);
            dbEngine.addDbEngineListener(this);
            dbEngine.start();
        }
        else
        {
            System.out.println("Missing input file");
        }
    }
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @GetMapping("/enzymeNames")
    public String[] getEnzymeNames() {
        return EnzymeMap.getNames(true);
    }

    @GetMapping("/ptmNames")
    public String[] getPtmFixNames() {
        return PTMMap.getPTMNames(false,true);
    }

    @GetMapping("/tolUnitNames")
    public String[] getTolUnitNames() {
        return MassTools.getMMDUnitNames();
    }


    //
    //DbEngineListener - metody interfejsu pozwalaja servletowi uzyskiwac informacje o stanie przeszukania

    //T.R. 27.10.2017 notyfikacja o bledach
    /**
     * Informacje o bledach
     */
    @Override
    public void notifyError(DbEngine engine, Object evt)
    {
        System.out.println("SEARCH ERROR: " + evt.toString());
    }

    /**
     * Informacja o rozpoczeciu przeszukania
     */
    @Override
    public void notifyInitalized(DbEngine engine)
    {
        System.out.println("SEARCH STARTED");
    }
    /**
     * Informacje uaktualnieniu stanu przeszukania
     */
    @Override
    public void notifyUpdated(DbEngine engine, Object evt)
    {
        System.out.println("SEARCH UPDATED: "  + evt.toString());
    }
    /**
     * Informacja o zakonczeniu przeszukania
     */
    @Override
    public void notifyFinished(DbEngine engine)
    {
        System.out.println("SEARCH FINISHED");

        /*
         * Odczyt plikow z wynikami: zostana zapisane w tym samym katalogu co pliki wejsciowe i beda mialy takie saea nazwy i rozszerzenia .out
         */
        for (int i=0;i<this.mFilenames.length;i++)
            this.readResultFile(MScanSystemTools.replaceExtension(this.mFilenames[i],"out"),this.out);
    }

    //
    //Metody pomocnicze
    /**
     * Metoda tworzaca konfiguracje przeszukania na podstawie obiektu formularza konfiguracji
     *
     * @param configFormObject
     * @return
     */
    private DbEngineSearchConfig getConfig(ConfigFormObject configFormObject)
    {
        DbEngineSearchConfig	config = new DbEngineSearchConfig();


        //T.R. 27.10.2017 MScanDB bedzie dzialal w trybie embedded (czyli nie uzyje System.exit przy bledzie)
        config.setEmbedded(true);

        config.setSearchTitle("TITLE");																//nazwa przeszukania
        config.setUser("USER");																		//uzytkownik
        config.setUserMail("USER@MIAL");															//mail

        config.setSmp(false);																		//wylaczenie wielowatkowosci

        //T.R. 27.10.2017 Konwersja nazwy typu bazy danych na identyfikator
        DB db = new DB(DBTools.getDbIndex("Uniprot"));												//identyfikator numeryczny (pobrany z bazy danych)

        db.setDbFilename("e:\\Projects\\tmp\\fasta\\uniprot_sprot.Homo_sapiens.fasta");				//plik FASTA (pobrany z bazy danych)
        db.setDbName("NAZWA_BAZY");																	//nazwa (pobrana z formularza)
        db.setDbVersion("WERSJA_BAZY");																//wersja (pobrana z bazy danych)
        db.setIdRegExp("ID_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        db.setIdRegExp("NAME_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        config.addDB(db);

        config.setTaxonomy("Homo sapiens");															//taksonomia

        config.getDigestConfig().setEnzyme(EnzymeMap.getEnzyme(configFormObject.getEnzyme()));	//enzym
        config.getDigestConfig().setMissedCleavages(configFormObject.getMissedCleavages());	//liczba niedotrawek

        //config.getScoringConfig().getFragmentationConfig().setInstrument(new MsMsInstrument(request.getParameter("instrument")));	//rodzaj spektrometru

        config.setParentMMD(configFormObject.getPepTol());					//tolerancja masy jonow peptydowych (macierzystych)
        config.setParentMMDUnit(MassTools.getMMDUnit(configFormObject.getPepTolUnit()));			//jednostka tolerancji masy jonow peptydowych (macierzystych)

        config.setFragmentMMD(configFormObject.getFragTol());					//tolerancja masy jonow fragmentacyjnych (potomnych)
        config.setFragmentMMDUnit(MassTools.getMMDUnit(configFormObject.getFragTolUnit()));		//jednostka tolerancji masy jonow fragmentacyjnych (potomnych)

        String[] names = configFormObject.getPtmFix();										//modyfikacje stale
        config.getDigestConfig().setFixedPTMs(PTMTools.getPTMs(names));

        names = configFormObject.getPtmVar();													//modyfikacje zmienne
        config.getDigestConfig().setVariablePTMs(PTMTools.getPTMs(names));

        return config;
    }

    /**
     *
     * @param filename
     */
    public void readResultFile(String filename,PrintWriter out)
    {
        try
        {
            /*
             * Odczyt pliku
             */
            System.out.println("Reading file: " + filename);
            MScanDbOutFileReader reader=new MScanDbOutFileReader(filename,new MsMsScanConfig());
            reader.readFile();
            reader.closeFile();

            /*
             * Pobranie i wyswietlenie naglowka
             */
            MsMsFileHeader header=reader.getHeader();
            out.println("\nTitle: " + header.getSearchTitle());
            out.println("User: " + header.getUser());
            out.println("User mail: " + header.getUserMail());
            out.println("Data file: " + header.getMsDataFile());

            //T.R. 27.10.2017 Zmiana sposobu pobierania informacji o bazie danych
            DB db=header.getDB(0);
            out.println("Database name: " + db.getDbName());
            //out.println("Database type: " + DBTools.getDbName(db.getDbType()));
            out.println("Database version: " + db.getDbVersion());
            out.println("Database FASTA file: " + db.getDbFilename());
            out.println("Database Taxonomy: " + db.getTaxonomy());

            out.println("Enzyme: " + header.getEnzyme());
            out.println("Missed cleavages: " + header.getMissedCleavages());
            out.println("Instrument: " + header.getInstrumentName());
            out.println("Parent MMD: " + header.getParentMMDString());
            out.println("Fragment MMD: " + header.getFragmentMMDString());
            out.println("Fixed PTMs: " + header.getFixedPTMsString());
            out.println("Variable PTMs: " + header.getVariablePTMsString());

            /*
             * Pobranie map bialek i peptydow
             */
            LinkedHashMap<String,MsMsProteinHit> proteinsMap=new LinkedHashMap<String,MsMsProteinHit>();
            LinkedHashMap<String,MsMsPeptideHit> peptidesMap=new LinkedHashMap<String,MsMsPeptideHit>();
            reader.createHashes(proteinsMap,peptidesMap);

            //T.R. 27.10.2017 potrzebne do pobrania sekwencji w postaci HTML
            PTMSymbolsMap map=header.getVariablePTMsMap();

            /*
             * Wyswietlenie wynikow
             */
            for (MsMsProteinHit protein : proteinsMap.values())
            {
                //informacje o bialku: ID, nazwa, score, liczba peptydow
                out.println("Protein: " + protein.getId() + "\t" + protein.getName() + "\t" + protein.getScore() + "\t" + protein.getPeptidesCount());

                //Wyswietlenie peptydow bialka
                for (MsMsPeptideHit peptide : protein.getPeptides().values())
                {
                    //T.R. 27.10.2017 Demonstracja sposobu pobrania sekwencji w postaci HTML. Parametry:
                    //terms - null
                    //withHeader - jezeli true, to ciag ma tez znaczniki <html> </html>
                    //bold - pogrubienie
                    //italic - kursywa
                    //fontFace - nazwa czcionki
                    //fontColor - kolor
                    //symbolsMap - mapa modyfikacji pobrana z naglowka

                    //informacje o peptydzie: sekwencja i masa teoretyczna (wynikajaca z sekwencji)
                    out.println("\tPeptide: " + peptide.getSequence() + "\t" + peptide.getCalcMass() + "\t" + peptide.getQueriesCount() + "\t" + peptide.getSequenceHTML(null,false, false, false, null, null,map));

                    //wyswietlenie widm przypisanych do peptydu
                    for (MsMsQuery query: peptide.getQueriesList())
                    {
                        //informacje o widmie: numer, m/z zmierzone, stopien naladowania, masa zmierzona, roznica mas w PPM, score
                        out.println("\t\tQuery:" + query.getNr() + "\t" + query.getMz() + "\t+" + query.getCharge() + "\t" + query.getMass() + "\t" + MassTools.getDeltaPPM(query.getMass(), peptide.getCalcMass()) + "\t" + query.getScore());
                    }
                }
            }
        }
        catch (MScanException mse)
        {
            System.out.println(mse);
        }

    }

}
