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

package edu.monash.merc.util.csv;

import edu.monash.merc.domain.Tissue;
import edu.monash.merc.domain.TissueExpression;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12/02/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CSVTissueGenerator {
    private List<TissueColumn> columns = new ArrayList<TissueColumn>();

    public List<TissueColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TissueColumn> columns) {
        this.columns = columns;
    }

    public TissueExpression genTissues() {
        TissueExpression tissue = new TissueExpression();
        for (TissueColumn tscolumn : columns) {
            String columnName = tscolumn.getColumnName();
            String columnValue = tscolumn.getColumnValue();
            if (columnName.equalsIgnoreCase(TField.PROBEID)) {
                tissue.setProbeId(columnValue);
            }
            if (columnName.equalsIgnoreCase(TField.EXPRESSION)) {
                tissue.setExpression(Double.parseDouble(columnValue));
            }
            if (columnName.equalsIgnoreCase(TField.TISSUE) ) {
                tissue.setTissueId(columnValue);
            }
        }
        return tissue;
    }
}

