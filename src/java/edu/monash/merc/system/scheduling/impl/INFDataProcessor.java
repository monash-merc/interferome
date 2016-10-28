/*
 * Copyright (c) 2010-2011, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.monash.merc.system.scheduling.impl;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import edu.monash.merc.common.results.DBStats;
import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.dao.impl.GeneDAO;
import edu.monash.merc.dao.impl.SearchDataDAO;
import edu.monash.merc.domain.Data;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.Promoter;
import edu.monash.merc.dto.GeneOntologyBean;

import edu.monash.merc.dto.ProbeGeneBean;
import edu.monash.merc.service.DBStatisticsService;
import edu.monash.merc.service.GeneService;
import edu.monash.merc.system.scheduling.DataProcessor;
import edu.monash.merc.service.DMService;
import edu.monash.merc.wsclient.biomart.BioMartClient;
import edu.monash.merc.wsclient.biomart.CSVGeneCreator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;

import org.hibernate.cache.ehcache.internal.util.HibernateUtil;
import org.hibernate.cfg.Configuration;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.*;

// SearchDAO impl


import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;


/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 11:03 AM
 */
@Service
@Qualifier("infDataProcessor")
@Transactional
public class INFDataProcessor extends HibernateGenericDAO<Data> implements DataProcessor {

    @Autowired
    protected DMService dmService;

    @Autowired
    protected AppPropSettings appSetting;


    private static String MOUSE = "mmusculus_gene_ensembl";

    private static String HUMAN = "hsapiens_gene_ensembl";

    private static String PROBE_HUMAN_TYPE = "Human";

    private static String PROBE_MOUSE_TYPE = "Mouse";

    private static String SPECIES_HUMAN_ID = "Homo sapiens";

    private static String SPECIES_MOUSE_ID = "Mus musculus";

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmService(DMService dmService) {
        this.dmService = dmService;
    }

    public void setAppSetting(AppPropSettings appSetting) {
        this.appSetting = appSetting;
    }

    @Autowired
    private DBStatisticsService dbStatisticsService;
    private DBStats stats;

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        System.out.println("============= start interferome process .........");

        Date importedTime = GregorianCalendar.getInstance().getTime();

        //Gene for HUMAN
        //importEnsemblGenes(HUMAN, importedTime);

        // 9/9/15 Update promoter sequence code goes here (Hibernate read in file)

        //Gene for MOUSE
        //importEnsemblGenes(MOUSE, importedTime);
        long endTime = System.currentTimeMillis();


        importCiiiDERPromoter(SPECIES_HUMAN_ID);


        //importCiiiDERPromoter(SPECIES_MOUSE_ID);


        System.out.println("I am updating the CiiiDER data ...");

        // importTFSite


        //import human and mouse probes
        //importProbes();

        //GeneOntology for HUMAN
        long goStartTime = System.currentTimeMillis();
        //importGeneOntology(HUMAN);

        //import the geneontology for mouse
        importGeneOntology(MOUSE);
        long goEndTime = System.currentTimeMillis();

        logger.info("=====> The total process time for Gene: " + (endTime - startTime) / 1000 + "seconds");

        logger.info("=====> The total process time for GeneOntology: " + (goEndTime - goStartTime) / 1000 + "seconds");

        logger.info("=====> The total process time for gene and genontology: " + (goEndTime - startTime) / 1000 + "seconds");
    }


    // String gnQuery = "SELECT count(g) FROM Gene g";
        // Query gnHQL = this.session().createQuery(gnQuery);
        // System.out.println("Results:   " + gnHQL.toString());





    private void importCiiiDERPromoter(String speciesId)  {
        // String path = "/home/mimr/dyin/enrich/";
        // Getting all list of genes

        // List<String> genes = new ArrayList<String>();

//        Session gnSess = session().getSessionFactory().openSession();
//        Transaction gnTx = null;
//
//        try {
//
//            // Session geneSession = session().getSessionFactory().getCurrentSession();
//            gnTx = gnSess.beginTransaction();
//            List<Gene> genes = gnSess.createQuery("SELECT DISTINCT g.ensgAccession FROM Data d INNER JOIN d.probe p INNER JOIN p.genes g").list();
//
//            // "SELECT g FROM Gene g"
//
//            gnTx.commit();
//
//
//            //String gnQuery = "SELECT g FROM Gene g";
//            // Session gnSession = session().getSessionFactory().openSession();
//            // Transaction gnTrans = gnSession.beginTransaction();
//            //gnSession = this.session();
//            //Query gnHQL = gnSession.createQuery(gnQuery);
//            // gnHQL.setParameterList(("genes"), genes);
//            // List<Gene> genes = gnHQL.list();
//
//
//            // JDBC connection to get gene ensgs
//        /*
//        try {
//            Connection geneConnection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/inter", "mimr", "");
//            Statement geneStmt = (Statement) geneConnection.createStatement();
//            ResultSet geneResult = geneStmt.executeQuery("SELECT ensg_accession FROM gene");
//
//            while (geneResult.next()) {
//                String geneEnsg = geneResult.getString("ensg_accession");
//                genes.add(geneEnsg);
//                }
//
//
//
//        }catch (SQLException e) {
//        e.printStackTrace();
//        }
//        */
//
//
//            List<String> geneEnsgList = new ArrayList<String>();
//            for (Gene gene : genes) {
//                String geneEnsg = gene.getEnsgAccession();
//                if (speciesId == SPECIES_HUMAN_ID && geneEnsg.startsWith("ENSG")) {
//                    geneEnsgList.add(geneEnsg);}
//                else if (speciesId == SPECIES_MOUSE_ID && geneEnsg.startsWith("ENSMUSG")) {
//                    geneEnsgList.add(geneEnsg);
//                }
//            }
//
//            String speciesName = speciesId.toString().replaceAll("\\s", "");
//            // Writing gene names into file (.txt) and reading promoter found
//            // BufferedWriter geneWriter = new BufferedWriter(new FileWriter(new File(path + "genes" + speciesName + ".txt")));
//            // for (String geneEnsg : geneEnsgList) {
//            // geneWriter.write(geneEnsg + "\n");
//            // }
//            // geneWriter.close();
//
//            File geneEnsgs = new File(SearchDataDAO.path + "genes" + speciesName + ".txt");
//            FileUtils.writeLines(geneEnsgs, geneEnsgList, "\n");
//
//
//            // convert speciesId to prefix for Config file e.g. Homo sapiens -> Homosapiens
//            String runCiiiDER = "java -jar " + SearchDataDAO.path + "Enrichment.jar" + " -n " + SearchDataDAO.path + "findPromoter/" + speciesName + "Config.ini";
//            Process processCiiiDER = Runtime.getRuntime().exec(runCiiiDER);
//
//            // processCiiiDER.waitFor();
//            //File promoterFile = new File(path + "findPromoter/" + speciesName + "Promoter.fa");
//            // Scanner promoterScanner = new Scanner(promoterFile);
//
//            // List<Promoter> promoters = new ArrayList<Promoter>();
//            List<Promoter> promoters = new ArrayList<Promoter>();
//            Promoter promoter = new Promoter();
//            // while (promoterScanner.hasNextLine()) {
//                // String line = promoterScanner.nextLine();
//            BufferedReader brGeneEnsgs = new BufferedReader(new FileReader(new File(SearchDataDAO.path + "findPromoter/" + speciesName + "Promoter.fa")));
//
//            String line = null;
//            while ((line = brGeneEnsgs.readLine()) != null) {
//                if (line.startsWith(">")) {
//                    String[] identifier = line.split("\\|");
//                    String geneName = identifier[0].replaceFirst(">","");
//                    String ensgAccession = identifier[1];
//                    promoter = new Promoter();
//                    promoter.setGeneName(geneName);
//                    promoter.setEnsgAccession(ensgAccession);
//                } else if (!line.equals("")) {
//                    promoter.setSequence(line);
//                    if(!promoters.contains(promoter)) promoters.add(promoter);
//                }
//            }
//            this.dmService.importPromoter(promoters);
//
//        } catch (IOException e) {
//                e.printStackTrace();
//        } catch (HibernateException ex) {
//            ex.getCause();
//        }

    }

    private void importEnsemblGenes(String species, Date importedTime) {
        BioMartClient client = null;
        try {
            String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

            client = new BioMartClient();
            client.configure(wsURL, species, null);
            List<Gene> geneList = client.importGenes();

            logger.info("============> total genes size : " + geneList.size());
            this.dmService.importGenes(geneList, importedTime);
            logger.info("======== imported the ensembl genes into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }

    }

    private void importProbes() {

        String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

        List<String> wsHumanPlatforms = getProbePlateforms(PROBE_HUMAN_TYPE);

        for (String platform : wsHumanPlatforms) {
            importProbesByType(wsURL, HUMAN, platform, PROBE_HUMAN_TYPE);
        }

        List<String> wsMousePlatforms = getProbePlateforms(PROBE_MOUSE_TYPE);

        for (String platform : wsMousePlatforms) {
            importProbesByType(wsURL, MOUSE, platform, PROBE_MOUSE_TYPE);
        }
    }

    private void importProbesByType(String wsUrl, String species, String platform, String probeType) {
        BioMartClient client = null;
        try {
            client = new BioMartClient();
            client.configure(wsUrl, species, platform);
            List<ProbeGeneBean> probeGeneBeans = client.importProbes(probeType);
            logger.info("============> total probes  size for  " + species + " - " + probeType + " : " + probeGeneBeans.size());
            this.dmService.importProbes(probeGeneBeans);
            //this.dmService.importProbes(null);
            logger.info("======== imported the probes into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }
    }

    private void importGeneOntology(String species) {
        BioMartClient client = null;
        try {
            String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

            client = new BioMartClient();
            client.configure(wsURL, species, null);
            List<GeneOntologyBean> geneOntologyBeans = client.importGeneOntology();

            logger.info("============> total geneontology size : " + geneOntologyBeans.size());
            this.dmService.importGeneOntologies(geneOntologyBeans);
            logger.info("======== imported the geneontology into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (client != null) {
                client.releaseConnection();
            }
        }
    }

    private List<String> getProbePlateforms(String type) {
        List<String> probeSearch = new ArrayList<String>();
        if (StringUtils.equals(type, PROBE_HUMAN_TYPE)) {
            probeSearch.clear();
        probeSearch.add("efg_agilent_sureprint_g3_ge_8x60k");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v1");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v2");
        probeSearch.add("affy_hc_g110");
        probeSearch.add("affy_hg_u133_plus_2");
        probeSearch.add("affy_hg_focus");
        probeSearch.add("affy_hg_u133a_2");
        probeSearch.add("affy_hg_u133a");
        probeSearch.add("affy_hg_u95av2");
        probeSearch.add("affy_hg_u133b");
        probeSearch.add("affy_hg_u95b");
        probeSearch.add("affy_hg_u95c");
        probeSearch.add("affy_hg_u95d");
        probeSearch.add("phalanx_onearray");
        probeSearch.add("illumina_humanht_12");
        probeSearch.add("illumina_humanwg_6_v3");
        probeSearch.add("illumina_humanwg_6_v2");
        probeSearch.add("illumina_humanwg_6_v1");
        probeSearch.add("codelink");
        probeSearch.add("agilent_cgh_44b");
        probeSearch.add("affy_u133_x3p");
        probeSearch.add("affy_hugene_1_0_st_v1");
        probeSearch.add("affy_huex_1_0_st_v2");
        probeSearch.add("affy_hugenefl");
        probeSearch.add("affy_hg_u95a");
        probeSearch.add("affy_hg_u95e");

        }
        if (StringUtils.equals(type, PROBE_MOUSE_TYPE)) {
            probeSearch.clear();
        probeSearch.add("efg_agilent_sureprint_g3_ge_8x60k");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v1");
        probeSearch.add("efg_agilent_wholegenome_4x44k_v2");
        probeSearch.add("affy_mg_u74a");
        probeSearch.add("affy_mg_u74av2");
        probeSearch.add("affy_mg_u74b");
        probeSearch.add("affy_mg_u74bv2");
        probeSearch.add("affy_mg_u74c");
        probeSearch.add("affy_mg_u74cv2");
        probeSearch.add("affy_moe430a");
        probeSearch.add("affy_moe430b");
        probeSearch.add("affy_moex_1_0_st_v1");
        probeSearch.add("affy_mogene_1_0_st_v1");
        probeSearch.add("affy_mouse430_2");
        probeSearch.add("affy_mouse430a_2");
        probeSearch.add("affy_mu11ksuba");
        probeSearch.add("affy_mu11ksubb");
        probeSearch.add("codelink");
        probeSearch.add("illumina_mousewg_6_v1");
        probeSearch.add("illumina_mousewg_6_v2");
        probeSearch.add("phalanx_onearray");
      }
        return probeSearch;
    }
}
