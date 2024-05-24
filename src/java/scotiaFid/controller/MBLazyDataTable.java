/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import scotiaFid.bean.ContabilidadAsientoBean;
import scotiaFid.bean.ContabilidadBienFideBean;
import scotiaFid.bean.ContabilidadGarantiaGralBean;
import scotiaFid.bean.ContabilidadMovtoBean;
import scotiaFid.bean.ContabilidadSaldoBean;
import scotiaFid.bean.ContabilidadSaldoPromBean;
import scotiaFid.bean.CriterioBusquedaBean;
import scotiaFid.bean.CriterioBusquedaContaAsienBean;
import scotiaFid.bean.CriterioBusquedaContaBean;
import scotiaFid.bean.CriterioBusquedaContaSaldoBean;
import scotiaFid.bean.CriterioBusquedaInstruccionesFideicomisoBean;
import scotiaFid.bean.InstruccionFideicomisoBean;
import scotiaFid.dao.CContabilidad;
import scotiaFid.dao.CInstruccionesFideicomiso;

/**
 *
 * @author Arturo Mellado
 */
//@ViewScoped
public class MBLazyDataTable<T> implements MBLazyDataTableI<T> {

    private static final Logger logger = LogManager.getLogger(MBLazyDataTable.class);

    private FacesMessage mensaje;
    private String valueType;
    private String type = null;
    private CContabilidad ccontabilidad;

    public MBLazyDataTable(String typeValue) {
        this.valueType = typeValue;
    }

    public MBLazyDataTable(String typeValue, String type) {
        this.valueType = typeValue;
        this.type = type;
    }

    public LazyDataModel<T> getLazyDataModel(T cbc) {
        LazyDataModel<T> response = null;

        response = new LazyDataModel<T>() {

            @Override
            public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<T> lazyData = null;
                try {
//                    logger.info("********++++++++++ INICIO PAGINACION  ********++++++++");
//                    logger.info("LazyDataModel--Criterios" + cbc);
//                    logger.info("LazyDataModel--Offset" + first);
//                    logger.info("LazyDataModel--Size" + pageSize);
                    if (valueType.contains("ContabilidadMovtoBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadMovtoBean>();
                    } else if (valueType.contains("ContabilidadBienFideBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadBienFideBean>();
                    } else if (valueType.contains("ContabilidadAsientoBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadAsientoBean>();
                    } else if (valueType.contains("ContabilidadSaldoBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadSaldoBean>();
                    } else if (valueType.contains("ContabilidadSaldoPromBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadSaldoPromBean>();
                    } else if (valueType.contains("InstruccionFideicomisoBean")) {
                        lazyData = (List<T>) new ArrayList<InstruccionFideicomisoBean>();
                    } else if (valueType.contains("ContabilidadGarantiaGralBean")) {
                        lazyData = (List<T>) new ArrayList<ContabilidadGarantiaGralBean>();
                    }
                    lazyData = fetchLazyData(first, pageSize, cbc);
//                    logger.info( "********++++++++++ FIN PAGINACION  ********++++++++");

                } catch (SQLException Err) {
                    logger.error(Err.getMessage());
                }
                return lazyData;
            }

            @Override
            public void setRowIndex(int rowIndex) {
                setPageSize(10);
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }
        };
        return response;
    }

    public List<T> fetchLazyData(int first, int pageSize, T cbc) throws SQLException {
        List<T> lazyData = null;

//        logger.info("fetchLazyData Indicador de Offset" + first);
//        logger.info("fetchLazyData Indicador de page Size" + pageSize); 
//        logger.info("fetchLazyData Indicador de Criterios" + cbc);
        if (valueType.contains("ContabilidadMovtoBean")) {
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "ContabilidadMovtoBean", "00", "20", "00");            
            if (valueType.contains("ContabilidadMovtoBeanAdm")) {
                lazyData = (List<T>) new ArrayList<ContabilidadMovtoBean>();

                ccontabilidad = new CContabilidad();
                lazyData = (List<T>) ccontabilidad.accountingPolicyPaginatedAdm((CriterioBusquedaContaBean) cbc, Integer.toString(first), type);
                ccontabilidad = null;

            } else {
                lazyData = (List<T>) new ArrayList<ContabilidadMovtoBean>();

                ccontabilidad = new CContabilidad();
                CriterioBusquedaContaBean cbs = (CriterioBusquedaContaBean) cbc;
                if (cbs.getCriterioTipoFecha() != null && cbs.getCriterioTipoFecha().equals("FCB")) {
                    lazyData = (List<T>) ccontabilidad.accountingPolicyPaginatedFCB((CriterioBusquedaContaBean) cbc, Integer.toString(first), type);
                } else {
                    lazyData = (List<T>) ccontabilidad.accountingPolicyPaginated((CriterioBusquedaContaBean) cbc, Integer.toString(first), type);
                }
                ccontabilidad = null;
            }
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados accountingPolicyPaginated " + lazyData.size(), "00", "20", "00");
        } else if (valueType.contains("ContabilidadBienFideBean")) {
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "ContabilidadBienFideBean", "00", "20", "00");
            lazyData = (List<T>) new ArrayList<ContabilidadBienFideBean>();

            ccontabilidad = new CContabilidad();
            lazyData = (List<T>) ccontabilidad.getTrustPropertyPaginated((CriterioBusquedaContaBean) cbc, Integer.toString(first));
            //oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados getTrustPropertyPaginated " + lazyData.size(), "00", "20", "00");
            ccontabilidad = null;
        } else if (valueType.contains("ContabilidadAsientoBean")) {
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "ContabilidadAsientoBean", "00", "20", "00");
            lazyData = (List<T>) new ArrayList<ContabilidadAsientoBean>();

            ccontabilidad = new CContabilidad();
            lazyData = (List<T>) ccontabilidad.getBookEntriesPaginated((CriterioBusquedaContaAsienBean) cbc, Integer.toString(first));
            //oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados getBookEntriesPaginated" + lazyData.size(), "00", "20", "00");
            ccontabilidad = null;
        } else if (valueType.contains("ContabilidadSaldoBean")) {
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "ContabilidadSaldoBean", "00", "20", "00");
            CriterioBusquedaContaSaldoBean cbs = (CriterioBusquedaContaSaldoBean) cbc;
            lazyData = (List<T>) new ArrayList<ContabilidadSaldoBean>();

            ccontabilidad = new CContabilidad();
            if ((cbs.getCriterioTipo().equals("ACT")) || (cbs.getCriterioTipo().equals("MSA"))) {
                lazyData = (List<T>) ccontabilidad.getBalanceInquiries(cbs, Integer.toString(first));
                //    //oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados getBalanceInquiries" + lazyData.size(), "00", "20", "00");
            }
            if (cbs.getCriterioTipo().equals("HST")) {
                lazyData = (List<T>) ccontabilidad.getBalanceInquiriesHistorical(cbs, Integer.toString(first));
                //    //oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados getBalanceInquiriesHistorical" + lazyData.size(), "00", "20", "00");   
            }
            ccontabilidad = null;
        } else if (valueType.contains("ContabilidadSaldoPromBean")) {
            lazyData = (List<T>) new ArrayList<ContabilidadSaldoPromBean>();

            ccontabilidad = new CContabilidad();
            lazyData = (List<T>) ccontabilidad.getAverageBalanceInquiriesPaginated((CriterioBusquedaBean) cbc, Integer.toString(first));
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "RecuperadosgetAverageBalanceInquiriesPaginated" + lazyData.size(), "00", "20", "00");
            ccontabilidad = null;
        } else if (valueType.contains("InstruccionFideicomisoBean")) {
            lazyData = (List<T>) new ArrayList<InstruccionFideicomisoBean>();

            CInstruccionesFideicomiso oInstruccionesFideicomiso = new CInstruccionesFideicomiso();
            lazyData = (List<T>) oInstruccionesFideicomiso.getInstructionsPaginated((CriterioBusquedaInstruccionesFideicomisoBean) cbc, Integer.toString(first));
            oInstruccionesFideicomiso = null;
        } else if (valueType.contains("ContabilidadGarantiaGralBean")) {
            lazyData = (List<T>) new ArrayList<ContabilidadGarantiaGralBean>();
            ccontabilidad = new CContabilidad();
            lazyData = (List<T>) ccontabilidad.getGuaranteesPaginated((CriterioBusquedaContaBean) cbc, Integer.toString(first));
            ////oGeneraLog.onGeneraLog(MBLazyDataTable.class, "0E", "INFO", "00", "00", "Recuperados getGuaranteesPaginated" + lazyData.size(), "00", "20", "00");
            ccontabilidad = null;
            boolean aplicaGarantia;
            aplicaGarantia = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("aplicaGarantia");
            if (aplicaGarantia) {
                mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fiduciario", "Operación realizada correctamente");
                FacesContext.getCurrentInstance().addMessage(null, mensaje);
                aplicaGarantia = false;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AplicaGarantia", aplicaGarantia);
            }
        }

        return lazyData;

    }
}
