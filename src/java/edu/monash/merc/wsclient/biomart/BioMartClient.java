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

package edu.monash.merc.wsclient.biomart;

import au.com.bytecode.opencsv.CSVReader;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.exception.WSException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 26/06/12
 *        Time: 10:21 AM
 */
public class BioMartClient {

    private String wsUrl = "http://www.biomart.org/biomart/martservice/result?query=";

    private String chromosome = "7";

    private boolean configured;

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public boolean configure(String wsUrl, String chromosome) {
        this.wsUrl = wsUrl;
        this.chromosome = chromosome;
        configured = true;
        return configured;
    }

    private byte[] getWsResponse() {
        GetMethod httpget = null;
        try {
            HttpClient httpclient = new HttpClient();
            String query = genQueryString(chromosome);
            String url = wsUrl + URLEncoder.encode(query, "UTF-8");
            httpget = new GetMethod(url);
            int statusCode = httpclient.executeMethod(httpget);
            if (statusCode != HttpStatus.SC_OK) {
                throw new WSException("failed to get the gene information");
            } else {
                return httpget.getResponseBody();
            }
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {

            if (httpget != null) {
                httpget.releaseConnection();
            }
        }

    }


    public List<Gene> importGenes() {
        if (!configured) {
            throw new WSException("The configure method must be called first.");
        }

        CSVReader csvReader = null;
        List<Gene> tpbGenes = new ArrayList<Gene>();
        try {
            BioMartClient bioMartClient = new BioMartClient();
            byte[] response = bioMartClient.getWsResponse();
            csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(response)));
            String[] columnsLines = csvReader.readNext();

            String[] columnValuesLines;
            while ((columnValuesLines = csvReader.readNext()) != null) {
                CSVGeneCreator geneCreator = new CSVGeneCreator();
                for (int i = 0; i < columnsLines.length; i++) {

                    geneCreator.getColumns().add(new CSVColumn(columnsLines[i], columnValuesLines[i]));
                }
                Gene tpbGene = geneCreator.createGene();

                if (StringUtils.isNotBlank(tpbGene.getEnsgAccession())) {
                    tpbGenes.add(tpbGene);
                }
            }
        } catch (Exception ex) {
            throw new WSException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
            } catch (Exception x) {
                //ignore whatever caught
            }
        }
        return tpbGenes;
    }

    public String genQueryString(String chromosome) {

        StringBuilder query = new StringBuilder();
        query.append("<?xml version='1.0' encoding='UTF-8'?>");
        query.append("<!DOCTYPE Query>");
        query.append("<Query  virtualSchemaName = 'default' formatter = 'CSV' header = '1' uniqueRows = '0' count = '' >");
        query.append("<Dataset name = 'hsapiens_gene_ensembl' interface = 'default' >");
        query.append("<Filter name = 'source' value = 'ensembl'/>");
        if (StringUtils.isNotBlank(chromosome)) {
            query.append("<Filter name = 'chromosome_name' value = '").append(chromosome).append("'/>");
        }
        query.append("<Attribute name = 'ensembl_gene_id' />");
        query.append("<Attribute name = 'description' />");
        query.append("<Attribute name = 'chromosome_name' />");
        query.append("<Attribute name = 'start_position' />");
        query.append("<Attribute name = 'end_position' />");
        query.append("<Attribute name = 'strand' />");
        query.append("<Attribute name = 'band' />");
        query.append("<Attribute name = 'external_gene_id' />");
        query.append("</Dataset>").append("</Query>");
        return query.toString();
    }

    public static void main(String[] args) throws Exception {
        String wsUrl = "http://www.biomart.org/biomart/martservice/result?query=";
        String chromosome = "";

        BioMartClient bioMartClient = new BioMartClient();
        bioMartClient.configure(wsUrl, chromosome);
        List<Gene> tpbGeneList = bioMartClient.importGenes();
        System.out.println(" size : " + tpbGeneList.size());
        for (Gene gene : tpbGeneList) {
            System.out.println(gene.getEnsgAccession() + " - " + gene.getDescription() + " - " + gene.getChromosome() + " - " + gene.getStartPosition() +
                    " - " + gene.getEndPosition() + " - " + gene.getStrand() + " - " + gene.getBand() + " - " + gene.getGeneName());
        }
    }
}
