/*
 * 
 */
package scotiaFid.servlet;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.bean.ReporteEstBean;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CContrato;
import scotiaFid.dao.CReportes;
import scotiaFid.util.CValidacionesUtil;

/**
 *
 * @author Bardock
 */
@WebServlet(name = "PDFReportServlet", urlPatterns = {"/PDFReportServlet"})
public class PDFReportServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PDFReportServlet.class);
    private static final String ERROR_GEN = "Error al generar Reporte";
    private static volatile Integer colsNumero;
    private static volatile Integer fuenteEstilo;
    private static volatile String mensajeError = new String();

    private static volatile String fechaE;
    private static volatile String contratoNombre;
    private static volatile String contratoTpoNeg;
    private static volatile String textoRepDir;
    private static volatile String textoRepSit;
    private static volatile String textoRepTit;
    private static volatile String rutaImagen;
    private static volatile String[] arrColumnas;
    private static volatile String[] arrColumnaEstruct;
    private static volatile String[] arrParametros;

    private static volatile Document docto;
    private static volatile Font fuente;
    private static volatile Paragraph parrafo;
    private static volatile Phrase phrase;
    private static volatile Paragraph par1;
    private static volatile Paragraph par2;
    private static volatile Paragraph par3;
    private static volatile Image imagen;
    private static volatile PdfPCell celdaEncabezado;
    private static volatile PdfPCell celda;
    private static volatile PdfPTable tablaEncabezado;
    private static volatile PdfPTable tablaEncabezado1;
    private static volatile PdfPTable tablaEncabezado2;
//    private static volatile PdfPTable tablaFirma;
    private static volatile PdfPTable tablaReporte;
    private static volatile PdfPTable dollarReportTable;
    private static volatile PdfPTable mnEncabezadoTable;
    private static volatile PdfPTable dollarEncabezadoTable;

    private static volatile DecimalFormat formatoDecimal;
    private static volatile DecimalFormat formatoEntero;
    private static volatile DecimalFormat formatoEntero2;
    private static volatile SimpleDateFormat formatoFechaHora;
    private static volatile SimpleDateFormat formatoFecha;

    private static volatile Calendar calendario;

    private static volatile CContrato oContrato;
    private static volatile CClave oClave;
    private static volatile CReportes oReporte;
    private static volatile List<ReporteEstBean> consultaReporte;
    private static volatile List<ReporteEstBean> consultaReporteEdoRes;
    private static volatile ReporteEstBean consultaReporteEdoResPaso;

    private static volatile Integer itemR = 0;
    private static volatile Integer itemC = 0;
    private static volatile boolean isDollarPage = false;
    private static volatile boolean isMN = false;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("ISO-8859-1");
        response.setContentType("application/pdf");

        try {
            class HeaderFooter extends PdfPageEventHelper {

                Phrase[] header = new Phrase[2];
                int pagenumber;
                String pageFooter;

                public void onOpenDocument(PdfWriter writer, Document document) {
                    header[0] = new Phrase("");
                }

                public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
                    header[1] = new Phrase(title.getContent());
                    pagenumber = 1;
                }

                public void onStartPage(PdfWriter writer, Document document) {
                    try {
                        docto.add(tablaEncabezado);
                        if (arrParametros[3].contains("ESTADO DE RESULTADOS")) {

                            docto.add(tablaEncabezado1);
                        }
                        if ((!isDollarPage) && isMN) {
                            docto.add(mnEncabezadoTable);
                        } else {
                            docto.add(dollarEncabezadoTable);
                        }
                        pagenumber++;

                    } catch (DocumentException e) {
//                      System.out.println("Error onStartPage: " + e.getMessage()); 
                        logger.error(e.getMessage());
                    }
                }

                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        Rectangle rect = writer.getBoxSize("art");
                        rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        switch (writer.getPageNumber() % 2) {
                            case 0:
                                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, header[0], rect.getRight(), rect.getTop(), 0);
                                break;
                            case 1:
                                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, header[1], rect.getLeft(), rect.getTop(), 0);
                                break;
                        }
                        java.util.Date fechaAct = new java.util.Date();
                        pageFooter = "Fecha y hora de generación: " + formatFechaHora(fechaAct) + "        ";
                        ColumnText.showTextAligned(writer.getDirectContent(),
                                Element.ALIGN_LEFT,
                                new Phrase(String.format("Página %d", pagenumber), new Font(Font.FontFamily.HELVETICA, 7)),
                                740,
                                rect.getBottom() - 5,
                                0);
                    } catch (AbstractMethodError e) {
//                        System.out.println("Error onEndPage: " + e.getMessage()); 
                        logger.error(e.getMessage());
                    }
                }
            }

            arrParametros = request.getQueryString().split("&");
            arrParametros[3] = arrParametros[3].replace("%20", " ");
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            if (arrParametros[3].contains("BALANZA")) {
                formatoDecimal = new DecimalFormat("$###,###,###,###,###,##0.00");
            } else {
                formatoDecimal = new DecimalFormat("###,###,###,###,###,##0.00");
            }
            formatoEntero = new DecimalFormat("###,###,###,###,###,###");
            formatoEntero2 = new DecimalFormat("################00");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            mensajeError = new String();

            //Obtenemos la estructura de las columnas del reporte
            oReporte = new CReportes();
            if (arrParametros[3].contains("RESULTADOS")) {
                arrColumnas = "C5!C2!C1!C2".split("!");
            } else {
                arrColumnas = oReporte.onReporte_ObtenEstructuraColumnas(Integer.parseInt(arrParametros[0])).split("!");
            }

            consultaReporte = oReporte.onReporte_ConsultaReporte(Integer.parseInt(arrParametros[1]), Integer.parseInt(arrParametros[0]), Integer.parseInt(arrParametros[2]));
            oReporte = null;

            oClave = new CClave();
            rutaImagen = oClave.onClave_ObtenDesc(690, 150);
            oClave = null;
// Fecha anterior
            oContrato = new CContrato();
            contratoNombre = oContrato.onContrato_ObtenNombre(Long.parseLong(arrParametros[1]));
            contratoTpoNeg = oContrato.onContrato_ObtenNegocio(Long.parseLong(arrParametros[1]));
            oContrato = null;

// Fecha anterior
            Date fechaAnt;
            if (arrParametros[7].equals("13")) {
                fechaAnt = formatFechaParse("01/12/".concat(arrParametros[6]));

            } else {
                fechaAnt = formatFechaParse("01/".concat(arrParametros[7].concat("/".concat(arrParametros[6]))));
            }
            calendario = Calendar.getInstance();
            calendario.setTime(fechaAnt);

            textoRepSit = formatoEntero2F(calendario.getActualMaximum(Calendar.DAY_OF_MONTH)) + "/" + formatoEntero2F(calendario.get(Calendar.MONTH) + 1) + "/" + formatoEntero2F(calendario.get(Calendar.YEAR));

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * C R E A C I Ó N   D E L   R E P O R T E
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            if (arrParametros[3].contains("RESULTADOS")) {
                tablaReporte = new PdfPTable(10);
                tablaReporte.setWidthPercentage(100);
                tablaReporte.setWidths(new int[]{14, 15, 10, 10, 10, 10, 10, 1, 10, 10});

                dollarReportTable = new PdfPTable(10);
                dollarReportTable.setWidthPercentage(100);
                dollarReportTable.setWidths(new int[]{14, 15, 10, 10, 10, 10, 10, 1, 10, 10});

                dollarEncabezadoTable = new PdfPTable(10);
                dollarEncabezadoTable.setWidthPercentage(100);
                dollarEncabezadoTable.setWidths(new int[]{14, 15, 10, 10, 10, 10, 10, 1, 10, 10});

                mnEncabezadoTable = new PdfPTable(10);
                mnEncabezadoTable.setWidthPercentage(100);
                mnEncabezadoTable.setWidths(new int[]{14, 15, 10, 10, 10, 10, 10, 1, 10, 10});
            } else {
                tablaReporte = new PdfPTable(10);
                tablaReporte.setWidthPercentage(100);
                tablaReporte.setWidths(new int[]{14, 6, 10, 10, 10, 10, 10, 10, 10, 10});

                dollarReportTable = new PdfPTable(10);
                dollarReportTable.setWidthPercentage(100);
                dollarReportTable.setWidths(new int[]{14, 6, 10, 10, 10, 10, 10, 10, 10, 10});

                dollarEncabezadoTable = new PdfPTable(10);
                dollarEncabezadoTable.setWidthPercentage(100);
                dollarEncabezadoTable.setWidths(new int[]{14, 6, 10, 10, 10, 10, 10, 10, 10, 10});

                mnEncabezadoTable = new PdfPTable(10);
                mnEncabezadoTable.setWidthPercentage(100);
                mnEncabezadoTable.setWidths(new int[]{14, 6, 10, 10, 10, 10, 10, 10, 10, 10});

            }
            colsNumero = arrColumnas.length;
            fuenteEstilo = 0;
            Integer tableFlag = 0;
            Integer indiceTit = 0;
            isDollarPage = false;
            isMN = false;
            if (arrParametros[3].contains("RESULTADOS")) {
                consultaReporteEdoRes = new ArrayList<ReporteEstBean>();
                for (itemR = 0; itemR <= consultaReporte.size() - 1; itemR++) {
                    consultaReporteEdoResPaso = consultaReporte.get(itemR);
                    consultaReporteEdoResPaso.setReporteEstCol04(consultaReporteEdoResPaso.getReporteEstCol03());
                    consultaReporteEdoResPaso.setReporteEstTipoCol04(consultaReporteEdoResPaso.getReporteEstTipoCol03());
                    if (!consultaReporte.get(itemR).getReporteEstCol02().contains("TOTAL DEL MES")) {
                        consultaReporteEdoResPaso.setReporteEstTipoCol03("");
                    }
                    consultaReporteEdoResPaso.setReporteEstCol03("");
                    consultaReporteEdoRes.add(consultaReporteEdoResPaso);
                }
                consultaReporte = consultaReporteEdoRes;

                celda = new PdfPCell();
                celda.setBorder(0);
                celda.setColspan(10);
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                
               
                if (!consultaReporte.isEmpty()){
                phrase = new Phrase("MONEDA NACIONAL", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                celda.addElement(phrase);
                mnEncabezadoTable.addCell(celda);
                celda = new PdfPCell();
                celda.setBorder(0);
                celda.setColspan(10);
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                phrase = new Phrase("DOLARES", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                celda.addElement(phrase);
//                phrase = new Phrase(" ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
//                celda.addElement(phrase);
                dollarEncabezadoTable.addCell(celda);
                }
            }
            for (itemR = 0; itemR <= consultaReporte.size() - 1; itemR++) {
                if (consultaReporte.get(itemR).getReporteEstCol01().contains("SUBFISO")) {
                    if (arrParametros[3].contains("ESTADO DE RESULTADOS")) {
                        if (consultaReporte.get(itemR + 1).getReporteEstCol01().contains("DOLAR")) {
                            tableFlag = 1;
                        } else {
                            isMN = Boolean.TRUE;
                        }
                    }
                }

                if (consultaReporte.get(itemR).getReporteEstFormato().equals("0")) {
                    for (itemC = 1; itemC <= colsNumero; itemC++) {
                        parrafo = null;
                        celda = null;
                        fuente = null;
                        parrafo = new Paragraph(new String());
                        celda = new PdfPCell();
                        celda.setBorder(0);
                        if (itemC == 1) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol01().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol01().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                        if (consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("INGRESOS")
                                                || consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("EGRESOS")
                                                || consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("INGRESOS")
                                                || consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("Gastos de administración".toUpperCase(Locale.US))
                                                || consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("Gastos de operación".toUpperCase(Locale.US))
                                                || consultaReporte.get(itemR).getReporteEstCol01().toUpperCase(Locale.US).equals("Otros Gastos".toUpperCase(Locale.US))) {
                                            fuenteEstilo = Font.BOLDITALIC | Font.UNDERLINE;
                                        }

                                    }

                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthLeft(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                                celda.setBorderWidthLeft(1);
                                            }
                                        }
                                        if (arrParametros[3].contains("RESULTADOS")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol02().contains("TOTAL DEL MES")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthBottom(1);
                                                celda.setBorderWidthLeft(1);
                                            }
                                        }
                                    }

                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol01())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol01())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol01()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            if (arrParametros[3].contains("RESULTADOS") && (consultaReporte.get(itemR).getReporteEstCol01().equals("MONEDA NACIONAL") || consultaReporte.get(itemR).getReporteEstCol01().equals("DOLAR AMERICANO"))) {
                                                parrafo = new Paragraph(" ", fuente);

                                            } else {
                                                parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol01(), fuente);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    // if (itemR == 0) dollarEncafuenteEstilobezadoTable.addCell(celda);

                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);
                                }
                            }
                        }
                        if (itemC == 2) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol02().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol02().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                        if (arrParametros[3].contains("RESULTADOS")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol02().contains("TOTAL DEL MES")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }

                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol02())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol02())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol02()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol02(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 3) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol03().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol03().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                        if (arrParametros[3].contains("RESULTADOS")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol02().contains("TOTAL DEL MES")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }

                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol03())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol03())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol03()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol03(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 4) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol04().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol04().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                        if (arrParametros[3].contains("RESULTADOS")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol02().contains("TOTAL DEL MES")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthBottom(1);
                                                celda.setBorderWidthRight(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol04())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol04())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol04()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol04(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 5) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol05().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol05().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol05())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol05())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol05()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol05(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 6) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol06().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol06().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol06())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol06())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol06()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol06(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    if (consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            || consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        celda.setColspan(2);
                                    }
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    if (consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            || consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        celda.setColspan(2);
                                    }
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 7) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol07().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol07().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol07())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol07())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol07()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol07(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    if (!consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            && !consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        mnEncabezadoTable.addCell(celda);
                                        //  if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                    }
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    if (!consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            && !consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        dollarEncabezadoTable.addCell(celda);
                                    }
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 8) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol08().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol08().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol06().contains("MOVIMIENTOS")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthTop(1);
                                                celda.setBorderWidthRight(1);
                                            }
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol08())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol08())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol08()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol08(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    if (consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            || consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        celda.setColspan(2);
                                    }
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    if (consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            || consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        celda.setColspan(2);
                                    }
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 9) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol09().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol09().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                        if (arrParametros[3].contains("BALANZA")) {
                                            if (consultaReporte.get(itemR).getReporteEstCol01().contains("CTAM")) {
                                                celda.setBorder(0);
                                                celda.setBorderWidthBottom(1);
                                                celda.setBorderWidthRight(1);
                                            }
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol09())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol09())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol09()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol09(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    if (!consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            && !consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {

                                        if (itemR != 0) {
                                            mnEncabezadoTable.addCell(celda);
//                                        dollarEncabezadoTable.addCell(celda);
                                        } else {
                                            fechaE = formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol09()).getTime()));
                                            celda = new PdfPCell();
//                                        parrafo = new Paragraph(new String(),fuente);
//                                        celda.addElement(parrafo);
                                            celda.setBorderWidth(0); //MARCO
                                            mnEncabezadoTable.addCell(celda);
//                                        dollarEncabezadoTable.addCell(celda);
                                        }

                                    }
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    if (!consultaReporte.get(itemR).getReporteEstCol06().equals("                  MOVIMIENTOS")
                                            && !consultaReporte.get(itemR).getReporteEstCol08().equals("                 SALDO   ACTUAL")) {
                                        dollarEncabezadoTable.addCell(celda);
                                    }
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                        if (itemC == 10) {
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol10().contains(";")) {
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol10().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX = 0; itemX <= arrColumnaEstruct.length - 1; itemX++) {
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("E")) {//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            fuenteEstilo = Font.NORMAL;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            fuenteEstilo = Font.BOLD;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            fuenteEstilo = Font.ITALIC;
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            fuenteEstilo = Font.BOLDITALIC;
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("T")) {
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("B")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBorderWidthLeft(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBorderWidthRight(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBorderWidthTop(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBorderWidthBottom(1);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBorder(1);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("F")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol10())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol10())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol10()).getTime())), fuente);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol10(), fuente);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("A")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            parrafo.setAlignment(Element.ALIGN_LEFT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            parrafo.setAlignment(Element.ALIGN_CENTER);
                                        }
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0, 1).equals("R")) {
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 0) {
                                            celda.setBackgroundColor(BaseColor.WHITE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 1) {
                                            celda.setBackgroundColor(new BaseColor(240, 240, 240));
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 2) {
                                            celda.setBackgroundColor(BaseColor.GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 3) {
                                            celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 4) {
                                            celda.setBackgroundColor(BaseColor.BLACK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 5) {
                                            celda.setBackgroundColor(BaseColor.RED);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 6) {
                                            celda.setBackgroundColor(BaseColor.PINK);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 7) {
                                            celda.setBackgroundColor(BaseColor.ORANGE);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 8) {
                                            celda.setBackgroundColor(BaseColor.YELLOW);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 9) {
                                            celda.setBackgroundColor(BaseColor.GREEN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 10) {
                                            celda.setBackgroundColor(BaseColor.MAGENTA);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 11) {
                                            celda.setBackgroundColor(BaseColor.CYAN);
                                        }
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1)) == 12) {
                                            celda.setBackgroundColor(BaseColor.BLUE);
                                        }
                                    }
                                }
                                celda.addElement(parrafo);
                            } else {
                                celda.addElement(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK)));
                            }

                            if (tableFlag.equals(0)) {
                                if (itemR <= indiceTit) {
                                    mnEncabezadoTable.addCell(celda);
                                    //if (itemR == 0) dollarEncabezadoTable.addCell(celda);
                                } else {
                                    tablaReporte.addCell(celda);
                                }
                            } else {
                                if (itemR <= indiceTit) {
                                    dollarEncabezadoTable.addCell(celda);
                                } else {
                                    dollarReportTable.addCell(celda);

                                }
                            }
                        }
                    }
                }
                if (consultaReporte.get(itemR).getReporteEstFormato().equals("1")) {
//                docto.add(parrafo);
                    phrase = new Phrase(contratoTpoNeg.concat(":     "), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                    parrafo = new Paragraph();
                    parrafo.add(phrase);

                    celda = new PdfPCell();


                    phrase = new Phrase(arrParametros[1], new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
                    parrafo.add(phrase);

                    celda.addElement(parrafo);
                    celda.setBorder(0);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_TOP);
                    celda.setColspan(2);
                    if (consultaReporte.get(itemR).getReporteEstCol01().contains("NACIONAL")) {
                        mnEncabezadoTable.addCell(celda);
                    } else {
                        dollarEncabezadoTable.addCell(celda);
                    } 

                    parrafo = new Paragraph();                    
                    phrase = new Phrase(contratoNombre, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
                    parrafo.add(phrase);

                    celda = new PdfPCell();
                    celda.addElement(parrafo);
                    celda.setBorder(0);
                    celda.setColspan(6);
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setVerticalAlignment(Element.ALIGN_TOP);
                    if (consultaReporte.get(itemR).getReporteEstCol01().contains("NACIONAL")) {
                        mnEncabezadoTable.addCell(celda);
                    } else {
                        dollarEncabezadoTable.addCell(celda);
                    }

                    parrafo = new Paragraph();                    
                    phrase = new Phrase(consultaReporte.get(itemR).getReporteEstCol01().trim(), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_RIGHT);
                    parrafo.add(phrase);
                    

                    celda = new PdfPCell();
                    celda.addElement(parrafo);
                    celda.setBorder(0);
                    celda.setColspan(2);
                    celda.setHorizontalAlignment(Element.ALIGN_RIGHT); 
                    celda.setVerticalAlignment(Element.ALIGN_TOP);
                    if (consultaReporte.get(itemR).getReporteEstCol01().contains("NACIONAL")) {
//                        tablaReporte.addCell(celda);
                        mnEncabezadoTable.addCell(celda); 
                        isMN = Boolean.TRUE;
                    } else {
//                        dollarReportTable.addCell(celda);
                        dollarEncabezadoTable.addCell(celda);
                        tableFlag = 1;
                    }
                    parrafo = null;
                    celda = null;
                    fuente = null;
                    parrafo = new Paragraph(new String());
                    celda = new PdfPCell();
                    celda.setBorder(0);
                    indiceTit = itemR + 2;
                }
            }


            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * E N C A B E Z A D O
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            textoRepTit = arrParametros[3].substring(arrParametros[3].indexOf(" ")).concat("\n");
            celdaEncabezado = new PdfPCell();

            if (arrParametros[3].contains("BALANZA")) {
                Date fechaSistema = formatFechaParse(arrParametros[5]);
                calendario = Calendar.getInstance();
                calendario.setTime(fechaSistema);
                if ((calendario.get(Calendar.YEAR)) == Integer.parseInt(arrParametros[6]) && (calendario.get(Calendar.MONTH) + 1) == Integer.parseInt(arrParametros[7])) {

                    par1 = new Paragraph("Fiduciario", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD | Font.UNDERLINE));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    par2 = new Paragraph("Balanza de Comprobación al Día Anterior ".concat(arrParametros[14]), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                    par2 = new Paragraph("  ", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                } else {
//                    celdaEncabezado.addElement(new Chunk("Fiduciario\n\n",  new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));
//                    celdaEncabezado.addElement(new Chunk("Balanza de Comprobación al Día Anterior ".concat(textoRepSit),  new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL)));
                    par1 = new Paragraph("Fiduciario", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD | Font.UNDERLINE));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    par2 = new Paragraph("Balanza de Comprobación al Día Anterior ".concat(textoRepSit), new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                    par2 = new Paragraph("  ", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                }
            }

            if (arrParametros[3].contains("ESTADO DE RESULTADOS")) {
                Date fechaSistema = formatFechaParse(arrParametros[5]);
                synchronized (fechaSistema) {
                    calendario = Calendar.getInstance();
                    calendario.setTime(fechaSistema);
                }
                if ((calendario.get(Calendar.YEAR)) == Integer.parseInt(arrParametros[6]) && (calendario.get(Calendar.MONTH) + 1) == Integer.parseInt(arrParametros[7])) {
                    par1 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    if (consultaReporte.size() > 0) {
                        textoRepTit = "Situación al: ".concat(arrParametros[14]);
                        par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par2.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par2);
                        textoRepTit = "PERIODICIDAD 01/01/".concat(arrParametros[6]).concat(" al ").concat(arrParametros[14]);
                        par3 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par3.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par3);
                    } else {
                        textoRepTit = " ";
                        par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par2.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par2);
                        celdaEncabezado.addElement(par2);
                    }

                } else {
                    par1 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    if (consultaReporte.size() > 0) {
                        textoRepTit = "Situación al: ".concat(textoRepSit);
                        par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par2.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par2);
                        textoRepTit = "\n\n PERIODICIDAD 01/01/".concat(arrParametros[6]).concat(" al ").concat(textoRepSit);
                        par3 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par3.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par3);
                    } else {
                        textoRepTit = " ";
                        par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                        par2.setAlignment(Element.ALIGN_CENTER);
                        celdaEncabezado.addElement(par2);
                        celdaEncabezado.addElement(par2);
                    }
                }
            }

            celdaEncabezado.setBorder(0);
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaEncabezado.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado = new PdfPTable(3);
            tablaEncabezado.setWidthPercentage(100);
            tablaEncabezado.setWidths(new int[]{15, 70, 15});

            //Logotipo
            celda = null;
            imagen = Image.getInstance(rutaImagen.concat("LogoRep.png"));
            celda = new PdfPCell(imagen);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_TOP);
            celda.setVerticalAlignment(Element.ALIGN_LEFT);
            celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celda);

            celda = null;
//            celdaPdfPCell(new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
//
//           celda.addElement(paragraphEncabezado);
//           c  elda.setBorder(0);
//           celda.setHorizontalAlignment(Element.ALIGN_CENTER); 
//           celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
//           celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celdaEncabezado);

            celda = new PdfPCell();
            if (arrParametros[3].contains("ESTADO DE RESULTADOS")) {
                textoRepDir = "SCOTIABANK INVERLAT, S.A.\nDIVISION FIDUCIARIA\nBLVD AVILA CAMACHO #1\nPISO 9\nCOL. POLANCO\nCIUDAD DE MEXICO C.P. 11009\nR.F.C. SIN9412025I4";
                par1 = new Paragraph(textoRepDir, new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK));
                par1.setAlignment(Element.ALIGN_LEFT);
                celda.addElement(par1);
            } else {
                par1 = new Paragraph(fechaE, new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK));
                par1.setAlignment(Element.ALIGN_CENTER);
                celda.addElement(par1);
            }

            //celda = new PdfPCell(new Paragraph(new String()));
            celda.setBorderWidth(0);
            celda.setBorderColor(BaseColor.BLACK);
//            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celda);

//            if (!arrParametros[1].equals("0")) {
                tablaEncabezado1 = new PdfPTable(1);
                tablaEncabezado1.setWidthPercentage(100);
                tablaEncabezado1.setWidths(new int[]{100});
            if (arrParametros[3].contains("ESTADO DE RESULTADOS") && !consultaReporte.isEmpty()) {

                celda = null;

                phrase = new Phrase(contratoTpoNeg.concat(":     "), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                parrafo = new Paragraph();
                parrafo.add(phrase);

                phrase = new Phrase(arrParametros[1].concat("     ").concat(contratoNombre), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
                parrafo.add(phrase);

                celda = new PdfPCell(parrafo);
                celda.setBorder(0);
                tablaEncabezado1.addCell(celda);

            }
            tablaEncabezado2 = new PdfPTable(1);
            tablaEncabezado2.setWidthPercentage(100);
            tablaEncabezado2.setWidths(new int[]{100});
            celda = null;
            celda = new PdfPCell(parrafo);
            celda.setBorder(0);
            tablaEncabezado2.addCell(celda);

            /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * C R E A C I Ó N   D E L   D O C U M E N T O
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            docto = new Document();
            PdfWriter escritorPDF = PdfWriter.getInstance(docto, response.getOutputStream());
            HeaderFooter evento = new HeaderFooter();
            escritorPDF.setBoxSize("art", new Rectangle(1500, 10, 10, 10));
            escritorPDF.setPageEvent(evento);

            docto.setPageSize(PageSize.LETTER);
            if (arrParametros[4].equals("HORIZONTAL")) {
                docto.setPageSize(PageSize.LETTER.rotate());    
            }
            docto.setMargins(20, 20, 20, 20);
            docto.setMarginMirroring(Boolean.TRUE);
            docto.open();
//            docto.add(new Paragraph("\n"));
            if (isMN) {
                docto.add(tablaReporte);
            }

            if (tableFlag.equals(1)) {
                isDollarPage = true;
                if (isMN) {
                    docto.newPage();
                }
//                docto.add(new Paragraph("\n"));
                docto.add(dollarReportTable);
            }
            if (consultaReporte.isEmpty() && arrParametros[3].contains("ESTADO DE RESULTADOS")) {
//                        Rectangle rect = escritorPDF.getBoxSize("art");
//                        rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
//
//                        ColumnText.showTextAligned(escritorPDF.getDirectContent(),
//                                Element.ALIGN_LEFT,
//                                new Phrase(String.format("NO SE ENCONTRO INFORMACION"), new Font(Font.FontFamily.HELVETICA, 7)),
//                                300,
//                                rect.getBottom() - 20,
//                                0);
                Paragraph paragraphNI = new Paragraph("\n \n \n \n \n \n \n \n \n NO SE ENCONTRO INFORMACION", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED));
                paragraphNI.setAlignment(Element.ALIGN_CENTER);
                docto.add(paragraphNI);
            }
            docto.close();
        } catch (NullPointerException DOCErr) {
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Motivo     : Error al momento de genera el documento PDF.\n" +
            //               "Descripción:" + DOCErr.getMessage() + ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute(DOCErr.getMessage(), ERROR_GEN);
            logger.error(DOCErr.getMessage());
        } catch (DocumentException DOCErr) {
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Motivo     : Error al momento de genera el documento PDF.\n" +
            //               "Descripción:" + DOCErr.getMessage() + ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute(DOCErr.getMessage(), ERROR_GEN);
            logger.error(DOCErr.getMessage());
        } catch (IOException e) {
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute(e.getMessage(), ERROR_GEN);
            logger.error(e.getMessage());
        } catch (NumberFormatException e) {
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute(e.getMessage(), ERROR_GEN);
            logger.error(e.getMessage());
        } catch (SQLException e) {
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute(e.getMessage(), ERROR_GEN);
            logger.error(e.getMessage());
        } finally {
            if (mensajeError.length() > 1) {
                getServletContext().getRequestDispatcher("/vista/message.jsp").forward(request, response);
            }
        }
    }

    public synchronized String formatFecha(java.util.Date fecha) {
        return formatoFecha.format(fecha);
    }

    public synchronized java.util.Date formatFechaParse(String fecha) {
        java.util.Date fechaSal;
        try {
            fechaSal = formatoFecha.parse(fecha);
        } catch (ParseException e) {
            fechaSal = new java.util.Date();
            logger.error("onContabilidadGrales_ObtenContratoNombre()");
        }
        return fechaSal;
    }

    public synchronized String formatFechaHora(java.util.Date fecha) {
        return formatoFechaHora.format(fecha);
    }

    public synchronized String formatDecimal(Double importe) {
        return formatoDecimal.format(importe);
    }

    public synchronized String formatoEnteroF(Long importe) {
        return formatoEntero.format(importe);
    }

    public synchronized String formatoEntero2F(int importe) {
        return formatoEntero2.format(importe);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (Throwable e) {
            logger.error("Error en la servlet:", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Ocurrió un error en la servlet.");
            } catch (IOException io) {
                logger.error("Error en la servlet:", io);
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (Throwable e) {
            logger.error("Error en la servlet:", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Ocurrió un error en la servlet.");
            } catch (IOException io) {
                logger.error("Error en la servlet:", io);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
