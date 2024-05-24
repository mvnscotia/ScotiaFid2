/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : MBReportes.java
 * TIPO        : Bean administrado
 * PAQUETE     : scotiaFid.controller
 * CREADO      : 20210702
 * MODIFICADO  : 20211019 
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package scotiaFid.controller;

import java.io.FileOutputStream;
import java.util.Date;
import java.time.Instant;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CReportes;
import scotiaFid.bean.ReporteCanSaldosBean;
import scotiaFid.util.Constantes;
import scotiaFid.util.Normalizacion;

@ManagedBean(name = "mbReporte")
@ViewScoped
public class MBReporte {

    private static final Logger logger = LogManager.getLogger(MBReporte.class);
    private CClave oClave;
    //private reporte oReporte;
    private CReportes oReportes;
    private SimpleDateFormat formatoFechaHora;
    private PdfPCell celda;
    private PdfPTable tablaEncabezado;
    private PdfPTable tablaSubEncabezado;
    private PdfPTable tablaDetalle;
    private String FILE;
    private String rutaImagen;
    private Image imagen;
    private static SimpleDateFormat formatoFecha;
    private static DecimalFormat formatoDecimal;
    private Document docto;
    private java.util.List<ReporteCanSaldosBean> consulta;
    private FacesMessage mensaje;

    class HeaderFooter extends PdfPageEventHelper {

        Phrase[] header = new Phrase[2];
        int pagenumber;
        String pageFooter;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("");
        }

        @Override
        public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
            header[1] = new Phrase(title.getContent());
            pagenumber = 1;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                docto.add(tablaEncabezado);
                docto.add(tablaSubEncabezado);
                pagenumber++;

            } catch (DocumentException e) {
//                      System.out.println("Error onStartPage: " + e.getMessage()); 
                logger.error("onChapter");
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Rectangle rect = writer.getBoxSize("art");
                rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
                switch (writer.getPageNumber() % 2) {
                    case 0:
                        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[0], rect.getRight(), rect.getTop(), 0);
                        break;
                    case 1:
                        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[1], rect.getLeft(), rect.getTop(), 0);
                        break;
                }
                java.util.Date fechaAct = new java.util.Date();
                pageFooter = "Fecha y hora de generación: " + formatFechaHora(fechaAct) + "        ";
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_LEFT,
                        new Phrase(String.format(pageFooter + "Página %d", pagenumber), new Font(Font.FontFamily.HELVETICA, 7)),
                        5,
                        rect.getBottom() - 5,
                        0);
            } catch (AbstractMethodError e) {
//                        System.out.println("Error onEndPage: " + e.getMessage()); 
                logger.error("onEndPage");
            }
        }
    }

//    class reporte {
    public void main(Long numFiso, String nomFiso) {
        if (numFiso == null || numFiso.equals(0)) {
            mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "Debe ingresar un numero de Fideicomiso");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
        } else {
            ejecutaReporte(numFiso, nomFiso);
        }
    }

    public void ejecutaReporte(Long numFiso, String nomFiso) {
        PdfWriter escritorPDF = null;
        //INICIO CAVC
        FileOutputStream fos = null;
        //FIN CAVC
        try {
            FILE = "CAN_SALD" + numFiso.toString() + ".pdf";
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoDecimal = new DecimalFormat("$###,###,###,###,###,##0.00");
            docto = new Document();
            String archivoUbicacion = Constantes.RUTA_TEMP; //CAVC
            if (archivoUbicacion != null) {
                archivoUbicacion = archivoUbicacion.concat("/".concat(FILE));
                //INICIO CAVC
                String archivoUbicacionNormalizado = Normalizacion.parse(archivoUbicacion);
                fos = new FileOutputStream(archivoUbicacionNormalizado);
                escritorPDF = PdfWriter.getInstance(docto, fos);
                // FIN CAVC
                HeaderFooter evento = new HeaderFooter();
                escritorPDF.setBoxSize("art", new Rectangle(1500, 10, 10, 10));
                escritorPDF.setPageEvent(evento);

                oReportes = new CReportes();
                consulta = oReportes.onReporte_ConsultaReporteCancelaSaldo(numFiso);
                oReportes = null;
                if (!consulta.isEmpty()) {
                    String fechaAplica = formatFecha(consulta.get(0).getSaldFecAplica());
                    onReporte_Encabezado(numFiso, nomFiso);
                    onReporte_SubEncabezado(consulta.get(0).getTipMovto(), consulta.get(0).getMonNomMoneda(), fechaAplica);

                    docto.setPageSize(PageSize.LETTER);
                    docto.setMargins(36, 36, 36, 36);
                    docto.setMarginMirroring(Boolean.TRUE);
                    docto.open();
                    onReporte_Detalle(numFiso);
                    docto.close();
                    escritorPDF.close();
                    String destinoURL = "/scotiaFid/SArchivoDescarga?".concat(FILE);
                    FacesContext.getCurrentInstance().getExternalContext().redirect(destinoURL);
                } else {
                    mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fiduciario", "No existe Información para generar reporte");
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                }
            }
        } catch (DocumentException | IOException | SQLException e) {
            logger.error("MBReporte ejecutaReporte");
        } finally {
            
            if(fos != null) {
                try{
                    fos.close();
                }catch (IOException ioe){
                    logger.error("MBReporte ejecutaReporte error al cerrar FileOutputStream");
                }
            }
            
            if (escritorPDF != null) {
                try{
                    escritorPDF.close();
                } catch (UnknownError e) {
                    logger.error("MBReporte Document Close");
                }
            }
            
            if (docto != null && docto.isOpen()) {
                try{
                    docto.close();
                } catch (UnknownError e) {
                    logger.error("MBReporte Document Close");
                }
            }
        }
    }

    private void onReporte_Encabezado(Long numFiso, String nomFiso) {
        try {
            tablaEncabezado = null;
            tablaEncabezado = new PdfPTable(3);
            tablaEncabezado.setWidthPercentage(108);
            tablaEncabezado.setWidths(new int[]{33, 58, 17});

            oClave = new CClave();
            rutaImagen = oClave.onClave_ObtenDesc(690, 150);
            oClave = null;

            celda = null;
            imagen = Image.getInstance(rutaImagen.concat("LogoRep.png"));
            celda = new PdfPCell(imagen);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("CANCELACIÓN DE SALDOS POR EXTINCIÓN DE CONTRATO", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);
            celda = null;

//            Date fechaSis = new java.sql.Date(formatoFecha.parse((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fechaSistema")).getTime());
            Date fechaSis = Date.from(Instant.now());

            celda = new PdfPCell(new Paragraph("Fecha: " + formatFecha(fechaSis), new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);

            celda = null;

            celda = new PdfPCell(new Paragraph("    ", new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(0);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(3);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);

            celda = null;
            celda = new PdfPCell(new Paragraph("    ", new Font(Font.FontFamily.HELVETICA, 1, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(1);
            celda.setBorderWidthTop(1);
            celda.setBorderWidthLeft(1);
            celda.setBorderWidthRight(1);
            celda.setColspan(3);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);

            celda = null;

            celda = new PdfPCell(new Paragraph("Fideicomiso: " + numFiso + " " + nomFiso, new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(0);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(3);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaEncabezado.addCell(celda);

            celda = null;

        } catch (DocumentException e) {
            logger.error("onReporte_Encabezado");
        } catch (IOException e) {
            logger.error("onReporte_Encabezado");
        } catch (SQLException e) {
            logger.error("onReporte_Encabezado");
        } catch (NumberFormatException e) {
            logger.error("onReporte_Encabezado");
        }
    }

    private void onReporte_Detalle(Long numFiso) {
        try {

            Double tAbonos = 0.00;
            Double tCargos = 0.00;

            String monNomMonedaAnt = consulta.get(0).getMonNomMoneda();

            tablaDetalle = null;
            tablaDetalle = new PdfPTable(14);
            tablaDetalle.setWidthPercentage(108);
            tablaDetalle.setWidths(new int[]{5, 5, 5, 5, 5, 5, 5, 10, 15, 10, 4, 4, 15, 15});

            Integer itemR;
            for (itemR = 0; itemR <= consulta.size() - 1; itemR++) {
                if (!consulta.get(itemR).getMonNomMoneda().equals(monNomMonedaAnt)) {

                    onReporteTotales(tCargos, tAbonos);
                    docto.add(tablaDetalle);

                    String fechaAplica = formatFecha(consulta.get(itemR).getSaldFecAplica());
                    onReporte_SubEncabezado(consulta.get(itemR).getTipMovto(), consulta.get(itemR).getMonNomMoneda(), fechaAplica);
                    docto.newPage();

                    tAbonos = 0.00;
                    tCargos = 0.00;

                    tablaDetalle = null;
                    tablaDetalle = new PdfPTable(14);
                    tablaDetalle.setWidthPercentage(108);
                    tablaDetalle.setWidths(new int[]{5, 5, 5, 5, 5, 5, 5, 10, 15, 10, 4, 4, 15, 15});

                    monNomMonedaAnt = consulta.get(itemR).getMonNomMoneda();
                }
                if (consulta.get(itemR).getAsieCarAbo().equals("C")) {
                    tCargos = tCargos + consulta.get(itemR).getAsiImporte();
                }
                if (consulta.get(itemR).getAsieCarAbo().equals("A")) {
                    tAbonos = tAbonos + consulta.get(itemR).getAsiImporte();
                }

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getSecMovto().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getCconCta().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getCconsCta().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getCcons2Cta().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getCcons3Cta().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getCcons4Cta().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getSaldAx2().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getSaldAx3().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(formatDecimal(consulta.get(itemR).getSaldSaldoIniSim()), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getDetmFolioOp().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getAsieSecAsiento().toString(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;
// SE OCULTA RESULTADO DE COLUMNA C/A
                celda = new PdfPCell(new Paragraph(consulta.get(itemR).getAsieCarAbo(), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
//                celda = new PdfPCell(new Paragraph("  ", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(formatDecimal(consulta.get(itemR).getAsiImporte()), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

                celda = new PdfPCell(new Paragraph(formatDecimal(consulta.get(itemR).getSaldSaldoActual()), new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);
                tablaDetalle.addCell(celda);
                celda = null;

            }

            onReporteTotales(tCargos, tAbonos);
            docto.add(tablaDetalle);

        } catch (DocumentException e) {
            logger.error("onReporte_Detalle");
        } catch (NumberFormatException e) {
            logger.error("onReporte_Detalle");
        }
    }

    private void onReporteTotales(Double tCargos, Double tAbonos) {
        try {

            celda = new PdfPCell(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(0);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(14);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaDetalle.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(0);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(7);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaDetalle.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("TOTAL CARGOS:  " + formatDecimal(tCargos) + "     TOTAL ABONOS:  " + formatDecimal(tAbonos), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(1);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(7);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaDetalle.addCell(celda);
            celda = null;
        } catch (NumberFormatException e) {
            logger.error("onReporteTotales");
        }
    }

    private void onReporte_SubEncabezado(String tipoCuenta, String moneda, String fechaAplicacion) {
        try {
            tablaSubEncabezado = null;
            tablaSubEncabezado = new PdfPTable(14);
            tablaSubEncabezado.setWidthPercentage(108);
            tablaSubEncabezado.setWidths(new int[]{5, 5, 5, 5, 5, 5, 5, 10, 15, 10, 4, 4, 15, 15});

            celda = new PdfPCell(new Paragraph(tipoCuenta, new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(1);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(12);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("Fecha Aplicación: " + fechaAplicacion, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            celda.setBorderWidthBottom(0);
            celda.setBorderWidthTop(1);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(12);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph(moneda, new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK)));
            celda.setBorderWidthBottom(1);
            celda.setBorderWidthTop(0);
            celda.setBorderWidthLeft(0);
            celda.setBorderWidthRight(0);
            celda.setColspan(14);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("MVTO", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("CTA", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("SCTA", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("2SCTA", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("3SCTA", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("4SCTA", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("AUX2", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("AUX3", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("SALDO INICIAL", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("FOLIO OP", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("SEC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("C/A", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("IMPORTE", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            celda = new PdfPCell(new Paragraph("SALDO ACTUAL", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);
            tablaSubEncabezado.addCell(celda);
            celda = null;

            //               docto.add(tablaSubEncabezado);
        } catch (DocumentException e) {
            logger.error("onReporte_SubEncabezado");
        }
    }

    public synchronized String formatFecha(Date fecha) {
        return formatoFecha.format(fecha);
    }

    public synchronized String formatDecimal(Double importe) {
        return formatoDecimal.format(importe);
    }

    public synchronized String formatFechaHora(Date fecha) {
        return formatoFechaHora.format(fecha);
    }

}

//    public void main(Integer numFiso, String nomFiso) {
//        oReporte = new reporte();
//        oReporte.main(numFiso, nomFiso);
//        oReporte = null;
//    }

//}
