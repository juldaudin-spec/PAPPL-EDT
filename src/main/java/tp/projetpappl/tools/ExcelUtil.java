/* -----------------------------------------
 * Projet Kepler
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
package tp.projetpappl.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;

/**
 *
 * @author kwyhr
 */
public class ExcelUtil {

    private Workbook document;
    private Sheet sheet;
    private String fileName;
    private int maxRow;
    private Row currentRow;
    private int lastCol;
    private XSSFFont font;
    private File tempExcel;

    /**
     *
     * @param fileName
     * @param sheetName
     */
    public ExcelUtil(String fileName, String sheetName) {
        this.fileName = fileName;
        this.document = new XSSFWorkbook();
        this.sheet = document.createSheet(sheetName);

        this.maxRow = -1;
        this.lastCol = -1;
        this.currentRow = null;
        this.font = ((XSSFWorkbook) this.document).createFont();
        this.font.setFontName("Arial");
        this.font.setFontHeightInPoints((short) 12);
        this.font.setBold(false);

        this.tempExcel = create(fileName + "_", ".xlsx");
    }

    private File create(String fileName, String ext) {
        if ((fileName != null) && (!fileName.isEmpty())) {
            if ((ext != null) && (!ext.isEmpty())) {
                if (!ext.startsWith(".")) {
                    ext = "." + ext;
                }
            } else {
                ext = "";
            }
            try {
                File tempFileLocal = File.createTempFile(fileName, ext);
                return tempFileLocal;
            } catch (IOException ex) {
                Logger.getLogger(ExcelUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     *
     * @param theFont
     * @param theSize
     */
    public void setDefault(String theFont, int theSize) {
        this.font = ((XSSFWorkbook) this.document).createFont();
        this.font.setFontName(theFont);
        this.font.setFontHeightInPoints((short) theSize);
    }

    /**
     *
     * @return
     */
    public Workbook getDocument() {
        return this.document;
    }

    /**
     *
     * @return
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     *
     * @return
     */
    public File getExcelFile() {
        return tempExcel;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return fileName + ".xlsx";
    }

    /**
     *
     * @param theFont
     * @param theSize
     * @param bold
     * @param backgroundColor
     * @param alignmentH
     * @param alignmentV
     * @param borderstyle
     * @param wrap
     * @return
     */
    public CellStyle createStyle(String theFont, Integer theSize, boolean bold,
            IndexedColors backgroundColor, HorizontalAlignment alignmentH, VerticalAlignment alignmentV,
            BorderStyle borderstyle, boolean wrap) {
        CellStyle theStyle = this.document.createCellStyle();

        Font aFont = ((XSSFWorkbook) this.document).createFont();

        if (theFont != null) {
            aFont.setFontName(theFont);
        } else {
            aFont.setFontName(this.font.getFontName());
        }

        if (theSize != null) {
            aFont.setFontHeightInPoints(theSize.shortValue());
        } else {
            aFont.setFontHeightInPoints(this.font.getFontHeightInPoints());
        }

        aFont.setBold(bold);

        theStyle.setFont(aFont);

        if (backgroundColor != null) {
            theStyle.setFillForegroundColor(backgroundColor.getIndex());
            theStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        /*        if (foregroundColor != null) {
            theStyle.setFillForegroundColor(backgroundColor.getIndex());
            theStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }*/
        if (alignmentH != null) {
            theStyle.setAlignment(alignmentH);
        } else {
            theStyle.setAlignment(HorizontalAlignment.LEFT);
        }
        if (alignmentV != null) {
            theStyle.setVerticalAlignment(alignmentV);
        } else {
            theStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        if (borderstyle != null) {
            theStyle.setBorderLeft(borderstyle);
            theStyle.setBorderRight(borderstyle);
            theStyle.setBorderTop(borderstyle);
            theStyle.setBorderBottom(borderstyle);
        } else {
            theStyle.setBorderLeft(BorderStyle.NONE);
            theStyle.setBorderRight(BorderStyle.NONE);
            theStyle.setBorderTop(BorderStyle.NONE);
            theStyle.setBorderBottom(BorderStyle.NONE);
        }
        theStyle.setWrapText(wrap);

        return theStyle;
    }

    /**
     * Set header
     *
     * @param title
     */
    public void setHeader(String title) {
        Header header = this.sheet.getHeader();
        //header.setLeft(Global.getInstitution());
        header.setCenter(title);
        header.setRight(HeaderFooter.page() + "/" + HeaderFooter.numPages());
    }

    /**
     * Set header
     *
     * @param title
     */
    public void setFooter(String title) {
        Footer footer = this.sheet.getFooter();
        //footer.setLeft(Global.getInstitution());
        footer.setCenter(title);
        footer.setRight(HeaderFooter.page() + "/" + HeaderFooter.numPages());
    }

    /**
     *
     * @param lineNumber
     * @return
     */
    public Row createRow(int lineNumber) {
        if (lineNumber > this.maxRow) {
            if (this.maxRow < 0) {
                this.maxRow = 0;
            }
            for (int i = this.maxRow; i <= lineNumber; i++) {
                sheet.createRow(i);
            }
            this.maxRow = lineNumber;
        }
        this.currentRow = sheet.getRow(lineNumber);
        if (this.currentRow.getRowNum() > 0) {
            this.lastCol = this.currentRow.getLastCellNum() - 1;
        } else {
            this.lastCol = -1;
        }
        return this.currentRow;
    }

    /**
     *
     * @return
     */
    public Row newRow() {
        if (this.maxRow < 0) {
            this.maxRow = -1;
        }
        this.maxRow++;
        this.currentRow = sheet.createRow(this.maxRow);
        return this.currentRow;
    }

    /**
     *
     * @param row
     * @param colNumber
     * @return
     */
    public Cell createCell(Row row, int colNumber) {
        Cell theCell = row.getCell(colNumber);
        if (theCell == null) {
            theCell = row.createCell(colNumber);
        }
        return theCell;
    }

    /**
     *
     * @param colNumber
     * @return
     */
    public Cell createCell(int colNumber) {
        return createCell(this.currentRow, colNumber);
    }

    /**
     *
     * @param row
     * @param colNumber
     * @param text
     * @param style
     */
    public void setCell(Row row, int colNumber, Object text, CellStyle style) {
        Cell theCell = createCell(row, colNumber);
        if (theCell != null) {
            if (text != null) {
                switch (text.getClass().getName()) {
                    case "Integer":
                        theCell.setCellValue((Integer) text);
                        break;
                    case "Date":
                        theCell.setCellValue((Date) text);
                        break;
                    case "Double":
                        theCell.setCellValue((Double) text);
                        break;
                    case "LocalDateTime":
                        theCell.setCellValue((LocalDateTime) text);
                        break;
                    case "LocalDate":
                        theCell.setCellValue((LocalDate) text);
                        break;
                    case "Calendar":
                        theCell.setCellValue((Calendar) text);
                        break;
                    case "RichTextString":
                        theCell.setCellValue((RichTextString) text);
                        break;
                    default:
                        theCell.setCellValue((String) text.toString());
                        break;
                }
            }

            if (style != null) {
                theCell.setCellStyle(style);
            }
        }
    }

    /**
     *
     * @param colNumber
     * @param text
     * @param style
     */
    public void setCell(int colNumber, Object text, CellStyle style) {
        setCell(this.currentRow, colNumber, text, style);
    }

    /**
     *
     * @param row
     * @param colNumber
     * @param text
     */
    public void setCell(Row row, int colNumber, Object text) {
        setCell(row, colNumber, text, null);
    }

    /**
     *
     * @param colNumber
     * @param text
     */
    public void setCell(int colNumber, Object text) {
        setCell(this.currentRow, colNumber, text, null);
    }

    /**
     *
     * @param colNumber
     */
    public void setCell(int colNumber) {
        setCell(this.currentRow, colNumber, null, null);
    }

    /**
     *
     * @return
     */
    public File close() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(tempExcel);
            document.write(outputStream);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelUtil.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ExcelUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException ex) {
                Logger.getLogger(ExcelUtil.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return tempExcel;
    }

    /**
     *
     * @return
     */
    public FileOutputStream closeStream() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(tempExcel);
            document.write(outputStream);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelUtil.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(ExcelUtil.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException ex) {
                Logger.getLogger(ExcelUtil.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        return outputStream;
    }

    public static File createExcelFile(Groupe groupe) {

        String fileName = "EDT";
        String title = "Emploi du temps";
        String sheetName = "Feuille1";

        // Create document
        ExcelUtil document = new ExcelUtil(fileName, sheetName);
        document.setDefault("Calibri", 12);

        // Styles
        CellStyle cellStdLeft = document.createStyle(null, null, false,
                IndexedColors.WHITE, HorizontalAlignment.LEFT, VerticalAlignment.TOP, BorderStyle.THIN, true);
        CellStyle cellStdRight = document.createStyle(null, null, false,
                IndexedColors.WHITE, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, BorderStyle.THIN, true);
        CellStyle cellStdCenter = document.createStyle(null, null, false,
                IndexedColors.WHITE, HorizontalAlignment.CENTER, VerticalAlignment.TOP, BorderStyle.THIN, true);
        CellStyle cellValidCenter = document.createStyle(null, null, false,
                IndexedColors.LIGHT_GREEN, HorizontalAlignment.CENTER, VerticalAlignment.TOP, BorderStyle.THIN, true);

        // Header and footer
        document.setHeader(title);

        // Set width
        document.getSheet().setColumnWidth(0, 50 * 256);    // Semaine
        document.getSheet().setColumnWidth(1, 6 * 256);    //
        
        // Columns headers
        document.newRow();
        document.setCell(0, "Semaine", cellStdCenter);
        document.setCell(1, "Lundi", cellStdCenter);
        document.setCell(41, "Mardi", cellStdCenter);
        document.setCell(81, "Mercredi", cellStdCenter);
        document.setCell(121, "Jeudi", cellStdCenter);
        document.setCell(161, "Vendredi", cellStdCenter);
        
        document.newRow();
        for(Integer i=1; i<201 ; i++){
            int caseDuJour = (i-1)%40;
            int heure = 8 + caseDuJour/4;
            Integer minutes = caseDuJour%4 *15;
            String minstr = minutes.toString();
            document.setCell(i, heure+"h"+minstr);
        }
        
        List<Seance> listSeance = groupe.getSeanceList();
        
        Collections.sort(listSeance, Seance.getComparator());

        // Document
        int rank = 0;
        Integer weekprecedent = null;
        for (Seance seance : listSeance) {
            Date date = seance.getHDebut();
            Integer year = date.getYear() + 1900;
            Integer month = date.getMonth() + 1;
            Integer day = date.getDate();
            Integer jour = date.getDay();
            Integer heure = date.getHours();
            Integer min = date.getMinutes();
            Integer fin = min + seance.getDuree();
            String monthstr = month.toString();
            if (month < 10) {
                monthstr = "0" + month.toString();
            }
            String daystr = day.toString();
            if (day < 10) {
                daystr = "0" + day.toString();
            }
            Integer week = day - jour + 1;//Premier jour de la semaine
            String datestr = year.toString() + "-" + monthstr + "-" + daystr;
            String weekstr = year.toString() + "-" + monthstr + "-" + week;
            String weekfinstr = year.toString() + "-" + monthstr + "-" + (week + 7);
            if (week != weekprecedent) {
                Integer increment;
                if(weekprecedent == null){
                    increment = week;
                    weekstr = year.toString() + "-" + monthstr + "-" + increment.toString();
                    Integer finalinc = increment +  6;
                    weekfinstr = year.toString() + "-" + monthstr + "-" + finalinc.toString();

                    rank++;
                    document.newRow();
                    document.setCell(0, "Semaine du " + weekstr + " au " + weekfinstr, cellStdLeft);
                } else {
                    increment = weekprecedent;
                }
                while (increment < week) {
                    weekstr = year.toString() + "-" + monthstr + "-" + increment.toString();
                    Integer finalinc = increment +  6;
                    weekfinstr = year.toString() + "-" + monthstr + "-" + finalinc.toString();

                    rank++;
                    document.newRow();
                    document.setCell(0, "Semaine du " + weekstr + " au " + weekfinstr, cellStdLeft);
                    increment = increment + 7;
                }
            }
            String contenuCell = "";
            for(Enseignant enseignant : seance.getEnseignantList()){
                if(contenuCell.isBlank()){
                    contenuCell= enseignant.getInitiales();
                } else{
                    contenuCell= contenuCell + ", "+ enseignant.getInitiales();
                }
            }
            for(Enseignant enseignant : seance.getEnseignantList()){
                if(contenuCell.isBlank()){
                    contenuCell= enseignant.getInitiales();
                } else{
                    contenuCell= contenuCell + ", "+ enseignant.getInitiales();
                }
            }
            float  h = heure;
            float hfin = heure + (float) fin/60;
            while(h<hfin && h < 18){
                if (h>=heure+(float) min/60){
                    int cellule = 40*(jour-1)+1+Math.round((h-8)*4);
                    document.setCell(cellule, seance.getAcronyme().getAcronyme()+"\n"+seance.getIntitule().getIntitule()+"\n"+contenuCell, cellStdLeft);
                }
                h= h + (float) 1/4;
            }
            weekprecedent = week;
        }

        // Close document
        File theFile = document.close();
        return theFile;
        //return ResponseEntity.unprocessableEntity().body((InputStreamResource) null);
    }
}
