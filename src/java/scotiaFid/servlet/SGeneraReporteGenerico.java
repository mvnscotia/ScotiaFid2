/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *                     EFICIENCIA EN SOFTWARE S.A. DE C.V.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * INSTALACION : Scotiabank
 * ARCHIVO     : SGeneraReporteGenerico.java
 * TIPO        : Servlet
 * PAQUETE     : mx.com.bmxt.sofi.servlet
 * CREADO      : 20210705
 * MODIFICADO  : 20210705
 * AUTOR       : j.ranfery.delatorre 
 * NOTAS       : 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scotiaFid.bean.ReporteEstBean;
import scotiaFid.dao.CContrato;
import scotiaFid.dao.CClave;
import scotiaFid.dao.CReportes;
import scotiaFid.util.CValidacionesUtil;

@WebServlet(name = "SGeneraReporteGenerico", urlPatterns = {"/SGeneraReporteGenerico"})
public class SGeneraReporteGenerico extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SGeneraReporteGenerico.class);
    private static final String ERROR_GEN = "Erorr al generar Reporte"; 
  //Atributos privados
  //private Boolean                              agregoCelda;
    private static volatile Integer                              colsNumero;
    private static volatile Integer                              fuenteEstilo;
    private static volatile String                               mensajeError = new String();
    
    private static volatile String                               contratoNombre;
    private static volatile String                               contratoTpoNeg;
    private static volatile String                               textoRepDir;
    private static volatile String                               textoRepSit;
    private static volatile String                               textoRepTit;
    private static volatile String                               rutaImagen;
    private static volatile String[]                             arrColumnas;
    private static volatile String[]                             arrColumnaEstruct;
    private static volatile String[]                             arrParametros;
    
    private static volatile Document                             docto;
    private static volatile Font                                 fuente;
    private static volatile Paragraph                            parrafo;
    private static volatile Paragraph                            par1;
    private static volatile Paragraph                            par2;
    private static volatile Image                                imagen;
    private static volatile PdfPCell                             celda;
    private static volatile PdfPCell                             celdaEncabezado;
    private static volatile PdfPTable                            tablaEncabezado;
    private static volatile PdfPTable                            tablaFirma;
    private static volatile PdfPTable                            tablaReporte;
    
    private static volatile DecimalFormat                        formatoDecimal;
    private static volatile DecimalFormat                        formatoEntero;
    private static volatile DecimalFormat                        formatoEntero2;
    private static volatile SimpleDateFormat                     formatoFechaHora;
    private static volatile SimpleDateFormat                     formatoFecha;
    
    private static volatile Calendar                             calendario;
    
    private static volatile CContrato                            oContrato;
    private static volatile CClave                               oClave;
    private static volatile CReportes                            oReporte;
    private static volatile List<ReporteEstBean>                 consultaReporte;
  //private List<ReporteFirmaEdoFinBean>         consultaFirma;
    
    private static volatile Integer                              itemR = 0;
    private static volatile Integer                              itemC = 0;
    private static volatile Integer                              contadorLineas = 0;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
      //request.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("ISO-8859-1");
        response.setContentType("application/pdf");
        try{
            class HeaderFooter extends PdfPageEventHelper {
                Phrase[] header = new Phrase[2];
                int      pagenumber;
                String   pageFooter;
                
                public void onOpenDocument(PdfWriter writer, Document document) {
                    header[0] = new Phrase("");
                }

                public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
                    header[1] = new Phrase(title.getContent());
                    pagenumber = 1;
                }

                public void onStartPage(PdfWriter writer, Document document) {
                    try{
                        pagenumber++;
                        docto.add(tablaEncabezado);
                      //System.out.println("Reporte " + arrParametros[3]);
                        if ((pagenumber>1)&&(arrParametros[3].contains("BALANZA"))){
                            PdfPTable tbl = new PdfPTable(10);
                            tbl.setTotalWidth(720);
                            tbl.setWidthPercentage(100);
                            tbl.setWidths(new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10});
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph(new String(), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setColspan(10);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.WHITE);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph(new String(), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setColspan(10);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.WHITE);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("CUENTA", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("AUX2", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("AUX3", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("DESCRIPCION", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            
                            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            celda.setColspan(2);
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("SALDO ANTERIOR", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("CARGOS", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("ABONOS", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("SALDO DEUDOR", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            celda = null;
                            celda = new PdfPCell(new Paragraph("SALDO ACREEDOR", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                            celda.setBorder(0);
                            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            
                            tbl.addCell(celda);
                            
                            float x = document.leftMargin();
                            float hei = document.topMargin();
                            //align bottom between page edge and page margin
                            float y = document.top() + hei;

                            //write the table
                            tbl.writeSelectedRows(0, -1, x, y, writer.getDirectContent());
                        }
                    }catch(DocumentException e){
//                        System.out.println("Error onStartPage: " + e.getMessage());
                          logger.error(e.getMessage());
                    }
                } 

                public void onEndPage(PdfWriter writer, Document document) {
                    try{
/*                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(tablaFirma);*/

                        Rectangle rect = writer.getBoxSize("art");
                        rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        switch(writer.getPageNumber() % 2) {
                        case 0:
                            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[0],rect.getRight(), rect.getTop(), 0);
                            break;
                        case 1:
                            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, header[1],rect.getLeft(), rect.getTop(), 0);
                            break;
                        }
                        //Float abajo = rect.getBottom(); CAVC
                        java.util.Date fechaAct = new java.util.Date();
                        pageFooter = "Fecha y hora de generación: " + formatFechaHora(fechaAct) + "        ";
                        String espacios = "                               ";
                        ColumnText.showTextAligned(writer.getDirectContent(), 
                                                   Element.ALIGN_LEFT, 
                                                   new Paragraph(espacios +"________________________________                                                                                                                                                          ________________________________", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)),
                                                   5,            
                                                   30, 
                                                   0);
                        ColumnText.showTextAligned(writer.getDirectContent(), 
                                                   Element.ALIGN_LEFT, 
                                                    new Paragraph(espacios +"        Gerente de Administración                                                                                                                                                                              Gerente de Contabilidad", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)),
                                                   5, 
                                                   15, 
                                                   0);
//                        ColumnText.showTextAligned(writer.getDirectContent(), 
//                                                   Element.ALIGN_LEFT, 
//                                                   new Phrase(String.format(pageFooter + "Página %d", pagenumber), new Font(Font.FontFamily.HELVETICA, 7)),
//                                                   5, 
//                                                   5, 
//                                                   0);
                    }catch(AbstractMethodError e){
//                        System.out.println("Error onEndPage: " + e.getMessage()); 
                          logger.error(e.getMessage());
                        }
                }    
            }
          //System.out.println("Query String: ".concat(request.getQueryString()));
          //Inicializamos
          //arrParametros[ 0].- Reporte  Número
          //arrParametros[ 1].- Contrato Número
          //arrParametros[ 2].- Reporte  Nombre
          //arrParametros[ 3].- Usuario  Número
          //arrParametros[ 4].- Reporte  Formato
          //arrParametros[ 5].- Fecha    Sistema
          //arrParametros[ 6].- Año
          //arrParametros[ 7].- Mes
          //arrParametros[ 8].- CTAM 
          //arrParametros[ 9].- SC1
          //arrParametros[10].- SC2
          //arrParametros[11].- SC3
          //arrParametros[12].- SC4
          //arrParametros[13].- CtoInv (Auxiliar)
          
            arrParametros    = request.getQueryString().split("&");
            arrParametros[3] = arrParametros[3].replace("%20", " ");
            formatoFecha     = new SimpleDateFormat("dd/MM/yyyy");
            formatoDecimal   = new DecimalFormat("###,###,###,###,###,##0.00");
            formatoEntero    = new DecimalFormat("###,###,###,###,###,###");
            formatoEntero2    = new DecimalFormat("################00");
            formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            mensajeError     = new String();
            
            
          //Obtenemos la estructura de las columnas del reporte
            oReporte         =  new CReportes();
            arrColumnas      = oReporte.onReporte_ObtenEstructuraColumnas(Integer.parseInt(arrParametros[0])).split("!");
            consultaReporte  = oReporte.onReporte_ConsultaReporte(Integer.parseInt(arrParametros[1]), Integer.parseInt(arrParametros[0]), Integer.parseInt(arrParametros[2]));
            oReporte         = null;
            
            oClave           = new CClave();
            rutaImagen       = oClave.onClave_ObtenDesc(690, 150);
            oClave           = null;
            
            oContrato        = new CContrato();
            contratoNombre   = oContrato.onContrato_ObtenNombre(Long.parseLong(arrParametros[1]));
            contratoTpoNeg   = oContrato.onContrato_ObtenNegocio(Long.parseLong(arrParametros[1]));
            oContrato        = null;

            Date fechaAnt = formatFechaParse("01/".concat(arrParametros[7].concat("/".concat(arrParametros[6]))));
            synchronized (fechaAnt) {
                calendario = Calendar.getInstance();
                calendario.setTime(fechaAnt);
            }
            textoRepSit = formatoEntero2F(calendario.getActualMaximum(Calendar.DAY_OF_MONTH)) + "/" + formatoEntero2F(calendario.get(Calendar.MONTH) + 1) + "/" + formatoEntero2F(calendario.get(Calendar.YEAR));

        
           /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * C R E A C I Ó N   D E L   R E P O R T E
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            tablaReporte = new PdfPTable(10);
            tablaReporte.setWidthPercentage(100);
            tablaReporte.setWidths(new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10});

            colsNumero   = arrColumnas.length;
            fuenteEstilo = 0;

            for (itemR=0; itemR<= consultaReporte.size()-1; itemR++){
                if (consultaReporte.get(itemR).getReporteEstFormato().equals("0")){
                contadorLineas++;    
                    for (itemC=1; itemC<= colsNumero; itemC++){
                        parrafo = null;
                        celda   = null;
                        fuente  = null;
                        parrafo = new Paragraph(new String());
                        celda   = new PdfPCell();
                        celda.setBorder(0);
                        if (itemR.equals(0)) {
                            if (itemC.equals(1)) {
                                celda.setBorderWidthTop(1);
                                celda.setBorderWidthLeft(1);
                                celda.setBorderWidthBottom(1);
                            } else {
                                if (itemC.equals(5)) {
                                    celda.setBorderWidthTop(1);
                                    celda.setBorderWidthRight(1);
                                    celda.setBorderWidthBottom(1);
                                } else {
                                    celda.setBorderWidthTop(1);
                                    celda.setBorderWidthBottom(1);
                                }
                            }
                        }
                        if (itemC== 1){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol01().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol01().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                            
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol01())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol01())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol01()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol01(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 2){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol02().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol02().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol02())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol02())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol02()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol02(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 3){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol03().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol03().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol03())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol03())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol03()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol03(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 4){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol04().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol04().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol04())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol04())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol04()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol04(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 5){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol05().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol05().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol05())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol05())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol05()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol05(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 6){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol06().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol06().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol06())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol06())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol06()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol06(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 7){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol07().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol07().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol07())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol07())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol07()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol07(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 8){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol08().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol08().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol08())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol08())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol08()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol08(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC== 9){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol09().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol09().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol09())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol09())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol09()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol09(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                        if (itemC==10){
                            celda.setColspan(Integer.parseInt(arrColumnas[itemC - 1].substring(1)));
                            if (consultaReporte.get(itemR).getReporteEstTipoCol10().contains(";")){
                                String stringWithoutLineBreak = consultaReporte.get(itemR).getReporteEstTipoCol10().replaceAll("\r\n", "");
                                arrColumnaEstruct = stringWithoutLineBreak.split(";");
                                for (int itemX=0; itemX<= arrColumnaEstruct.length - 1; itemX++){
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("E")){//Estilo
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) fuenteEstilo = Font.NORMAL;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) fuenteEstilo = Font.BOLD;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) fuenteEstilo = Font.ITALIC;
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) fuenteEstilo = Font.BOLDITALIC;   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("T")){
                                        fuente = new Font(Font.FontFamily.HELVETICA, Integer.parseInt(arrColumnaEstruct[itemX].substring(1)), fuenteEstilo, BaseColor.BLACK);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("B")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBorderWidthLeft(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBorderWidthRight(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBorderWidthTop(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBorderWidthBottom(1);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBorder(1);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("F")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo = new Paragraph(formatDecimal(CValidacionesUtil.validarDouble(consultaReporte.get(itemR).getReporteEstCol10())),fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo = new Paragraph(formatoEnteroF(Long.parseLong(consultaReporte.get(itemR).getReporteEstCol10())),fuente);   
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo = new Paragraph(formatFecha(new java.util.Date(formatFechaParse(consultaReporte.get(itemR).getReporteEstCol10()).getTime())), fuente);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) parrafo = new Paragraph(consultaReporte.get(itemR).getReporteEstCol10(), fuente);   
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("A")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) parrafo.setAlignment(Element.ALIGN_LEFT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) parrafo.setAlignment(Element.ALIGN_RIGHT);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) parrafo.setAlignment(Element.ALIGN_CENTER);
                                    }
                                    if (arrColumnaEstruct[itemX].substring(0,1).equals("R")){
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 0) celda.setBackgroundColor(BaseColor.WHITE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 1) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 2) celda.setBackgroundColor(BaseColor.GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 3) celda.setBackgroundColor(BaseColor.DARK_GRAY);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 4) celda.setBackgroundColor(BaseColor.BLACK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 5) celda.setBackgroundColor(BaseColor.RED);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 6) celda.setBackgroundColor(BaseColor.PINK);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 7) celda.setBackgroundColor(BaseColor.ORANGE);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 8) celda.setBackgroundColor(BaseColor.YELLOW);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))== 9) celda.setBackgroundColor(BaseColor.GREEN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==10) celda.setBackgroundColor(BaseColor.MAGENTA);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==11) celda.setBackgroundColor(BaseColor.CYAN);
                                        if (Integer.parseInt(arrColumnaEstruct[itemX].substring(1))==12) celda.setBackgroundColor(BaseColor.BLUE);
                                    }
                                }
                                celda.addElement(parrafo);
                            }else{
                                celda.addElement(parrafo);
                            }
                            tablaReporte.addCell(celda);
                        }
                    }
                }
                if (consultaReporte.get(itemR).getReporteEstFormato().equals("1")){
                        celda = new PdfPCell(new Paragraph(consultaReporte.get(itemR).getReporteEstCol01().concat(" ") +
                                                           consultaReporte.get(itemR).getReporteEstCol02().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol03().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol04().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol05().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol06().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol07().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol08().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol09().concat(" ") +
                                                       consultaReporte.get(itemR).getReporteEstCol10().trim(), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
                    celda.setBorder(0);
                    celda.setColspan(10);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                  //celda.setBackgroundColor(BaseColor.DARK_GRAY);
                    tablaReporte.addCell(celda);
                }
                if (consultaReporte.get(itemR).getReporteEstCol01().contains("TOTAL")) {
                    celda = new PdfPCell(new Paragraph("\n"));
                    celda.setBorder(0);
                    tablaReporte.addCell(celda);
                }
                
            }
           /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * E N C A B E Z A D O
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            textoRepTit     = arrParametros[3].substring(arrParametros[3].indexOf(" ")).concat("\n");
            if (arrParametros[3].contains("BALANZA")){
                Date fechaSistema = formatFechaParse(arrParametros[5]);
                synchronized (fechaSistema) {
                    calendario        = Calendar.getInstance();
                    calendario.setTime(fechaSistema);    
                }
                if ((calendario.get(Calendar.MONTH) + 1)== Integer.parseInt(arrParametros[7])){
                    textoRepTit = "Fiduciario\n\n" + "Balanza de Comprobación al Día Anterior ".concat(arrParametros[14]);
                }else{
                    textoRepTit = "Fiduciario\n\n" + "Balanza de Comprobación al ".concat(textoRepSit);   
                }
            }
            celdaEncabezado = new PdfPCell();
            if (arrParametros[3].contains("BALANCE")){
                Date fechaSistema = formatFechaParse(arrParametros[5]);
                synchronized (fechaSistema) {
                    calendario = Calendar.getInstance();
                    calendario.setTime(fechaSistema);    
                }
                
                if ((calendario.get(Calendar.YEAR)) == Integer.parseInt(arrParametros[6])&&(calendario.get(Calendar.MONTH) + 1)== Integer.parseInt(arrParametros[7])){
//                    textoRepTit = textoRepTit.concat("Situación al: ".concat(arrParametros[14]));
                    par1 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    textoRepTit = "Situación al: ".concat(arrParametros[14]);
                    par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                }else{
//                    textoRepTit = textoRepTit.concat("Situación al: ".concat(textoRepSit));
                    par1 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
                    par1.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par1);
                    textoRepTit = "Situación al: ".concat(textoRepSit);
                    par2 = new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK));
                    par2.setAlignment(Element.ALIGN_CENTER);
                    celdaEncabezado.addElement(par2);
                }
                celdaEncabezado.setBorder(0);
                celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaEncabezado.setBackgroundColor(BaseColor.WHITE);

                tablaFirma  = new PdfPTable(5);
                tablaFirma.setWidthPercentage(100);
                tablaFirma.setWidths(new int[]{20, 20, 20, 20, 20});
                
                celda = null;
                celda = new PdfPCell(new Paragraph(new String(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);

                tablaFirma.addCell(celda);
                
                
                celda = null;
                celda = new PdfPCell(new Paragraph("GERENTE DE ADMINISTRACION", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setBorderColorTop(BaseColor.BLACK);
                celda.setBorderWidthTop(1);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);

                tablaFirma.addCell(celda);
                
                celda = null;
                celda = new PdfPCell(new Paragraph(new String(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);

                tablaFirma.addCell(celda);
                
                celda = null;
                celda = new PdfPCell(new Paragraph("GERENTE DE CONTABILIDAD", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setBorderColorTop(BaseColor.BLACK);
                celda.setBorderWidthTop(1);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);

                tablaFirma.addCell(celda);
                
                celda = null;
                celda = new PdfPCell(new Paragraph(new String(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
                celda.setBorder(0);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setBackgroundColor(BaseColor.WHITE);

                tablaFirma.addCell(celda);
            } 
            tablaEncabezado = new PdfPTable(3); 
            tablaEncabezado.setWidthPercentage(100);
            tablaEncabezado.setWidths(new int[]{15, 70, 15});  

          //Logotipo
            celda  = null;
            imagen = Image.getInstance(rutaImagen.concat("LogoRep.png"));
            celda  = new PdfPCell(imagen);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_TOP);
            celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celda);

            celda = null;
            celda = new PdfPCell(new Paragraph(textoRepTit, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celdaEncabezado);

            celda = null;
            textoRepDir = "SCOTIABANK INVERLAT, S.A.\nDIVISION FIDUCIARIA\nBLVD AVILA CAMACHO #1\nPISO 9\nCOL. POLANCO\nCIUDAD DE MEXICO C.P. 11009\nR.F.C. SIN9412025I4";
            parrafo = new Paragraph(textoRepDir, new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK));
            celda = new PdfPCell(parrafo);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setBackgroundColor(BaseColor.WHITE);

            tablaEncabezado.addCell(celda);
           /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            * C R E A C I Ó N   D E L   D O C U M E N T O
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
            docto            = new  Document();
            PdfWriter escritorPDF = PdfWriter.getInstance(docto, response.getOutputStream());
            HeaderFooter evento   = new HeaderFooter();
            escritorPDF.setBoxSize("art", new Rectangle(1500, 10, 10, 10));
            escritorPDF.setPageEvent(evento);

            docto.setPageSize(PageSize.LETTER);
            if (arrParametros[4].equals("HORIZONTAL"))docto.setPageSize(PageSize.LETTER.rotate());
            docto.setMargins(20, 20, 20, 20);
            docto.setMarginMirroring(Boolean.TRUE);
            docto.open();
//            docto.add(tablaEncabezado);
            
            if (!arrParametros[1].equals("0")){
                docto.add(new Paragraph("\n"));
                parrafo = new Paragraph(contratoTpoNeg.concat(":    ").concat(arrParametros[1].concat("     ").concat(contratoNombre)), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
                parrafo.setAlignment(Element.ALIGN_LEFT);
                docto.add(parrafo);

            }
            docto.add(new Paragraph("\n"));
            docto.add(tablaReporte);
            docto.add(new Paragraph("\n"));
/*            if (arrParametros[3].contains("BALANCE")){
                contadorLineas = +24 - contadorLineas ;
                if (contadorLineas < 6){
                docto.newPage();
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                        }
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(new Paragraph("\n"));
                docto.add(tablaFirma);
            }*/
            docto.close();
        }catch(DocumentException DOCErr){
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Motivo     : Error al momento de genera el documento PDF.\n" +
            //               "Descripción:" + Err.getMessage() + ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute("message",  ERROR_GEN);
            logger.error(DOCErr.getMessage());
        }catch(IOException e){
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute("message",  ERROR_GEN);
            logger.error(e.getMessage());
        }catch(NumberFormatException e){
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute("message",  ERROR_GEN);
            logger.error(e.getMessage());
        }
        //mensajeError = "Error En Tiempo de Ejecución.\n" +
        //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
        catch(SQLException e){
            //mensajeError = "Error En Tiempo de Ejecución.\n" +
            //               "Descripción:" + e.getMessage()+ ". Renglón:" + itemR.toString() + ". Columna: " + itemC.toString();
            request.setAttribute("message",  ERROR_GEN);
            logger.error(e.getMessage());
        }finally{
            if (mensajeError.length()>1) getServletContext().getRequestDispatcher("/vista/message.jsp").forward(request, response);
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
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
}