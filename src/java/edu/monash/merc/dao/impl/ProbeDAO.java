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

package edu.monash.merc.dao.impl;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.Probe;
import edu.monash.merc.repository.IProbeRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * @since 1.0
 *        <p/>
 *        Date: 28/06/12
 *        Time: 2:37 PM
 */
@Scope("prototype")
@Repository
public class ProbeDAO extends HibernateGenericDAO<Probe> implements IProbeRepository {

    @SuppressWarnings("unchecked")
    @Override
     public Pagination<Probe> getProbes(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
         // count total
         Criteria criteria = this.session().createCriteria(this.persistClass);
         criteria.setProjection(Projections.rowCount());
         int total = ((Long) criteria.uniqueResult()).intValue();
         Pagination<Probe> p = new Pagination<Probe>(startPageNo, recordsPerPage, total);

         // query reporter by size-per-page
         Criteria queryCriteria = this.session().createCriteria(this.persistClass);
         // add orders
         if (orderBys != null && orderBys.length > 0)

         {
             for (int i = 0; i < orderBys.length; i++) {
                 Order order = orderBys[i].getOrder();
                 if (order != null) {
                     queryCriteria.addOrder(order);
                 }
             }
         } else

         {
             queryCriteria.addOrder(Order.desc("probeId"));
         }

         // calculate the first result from the pagination and set this value into the start search index
         queryCriteria.setFirstResult(p.getFirstResult());
         // set the max results (size-per-page)
         queryCriteria.setMaxResults(p.getSizePerPage());
         queryCriteria.setComment("getProbes");
         List<Probe> probeList = queryCriteria.list();
         p.setPageResults(probeList);
         return p;
     }

    @Override
    public Probe getProbeByProbeId(String probeId) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("probeId", probeId));
        Probe result = (Probe) criteria.uniqueResult();
        this.session().evict(result);
        return result;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Probe> getProbesByGeneAccession(String geneAccession) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        // create the alias for genes
        Criteria geneCrit = criteria.createAlias("genes", "genes");
        geneCrit.add(Restrictions.eq("genes.ensgAccession", geneAccession));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Probe> getProbesByGeneId(long geneId) {
         Criteria criteria = this.session().createCriteria(this.persistClass);
         // create the alias for genes
         Criteria geneCrit = criteria.createAlias("genes", "genes");
         geneCrit.add(Restrictions.eq("genes.id", geneId));
         return criteria.list();
     }

    @SuppressWarnings("unchecked")
    @Override
    public List<Probe> getProbeBySpecies(String speciesName) {
         Criteria criteria = this.session().createCriteria(this.persistClass);
        // create the alias for species
         Criteria probeCriteria = criteria.createAlias("species","species");
         probeCriteria.add(Restrictions.eq("species.speciesName", speciesName));
        return criteria.list();
    }
}
