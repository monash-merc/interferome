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

import edu.monash.merc.config.AppPropSettings;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.system.scheduling.DataProcessor;
import edu.monash.merc.service.DMService;
import edu.monash.merc.wsclient.biomart.BioMartClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class INFDataProcessor implements DataProcessor {

    @Autowired
    protected DMService dmService;

    @Autowired
    protected AppPropSettings appSetting;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmService(DMService dmService) {
        this.dmService = dmService;
    }

    public void setAppSetting(AppPropSettings appSetting) {
        this.appSetting = appSetting;
    }

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        System.out.println("============= start interferome process .........");
        importEnsemblGenes("hsapiens_gene_ensembl");
        long endTime = System.currentTimeMillis();
        System.out.println("=====> The total process time for hsapiens_gene_ensembl: " + (endTime - startTime) / 1000 + "seconds");
    }

    private void importEnsemblGenes(String species) {
        try {
            String wsURL = this.appSetting.getPropValue(AppPropSettings.BIOMART_RESTFUL_WS_URL);

            BioMartClient client = new BioMartClient();
            client.configure(wsURL, species);
            List<Gene> geneList = client.importGenes();

            System.out.println("============> total genes size : " + geneList.size());
            this.dmService.importGenes(geneList);
            System.out.println("======== imported the ensembl genes into database successfully");
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
