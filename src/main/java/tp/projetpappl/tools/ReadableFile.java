package tp.projetpappl.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author kwyhr
 */
public class ReadableFile {

    public static final int NOFILE = 0;
    public static final int TEXTFILE = 1;
    public static final int EXCELFILE = 2;

    private BufferedReader reader;
    private String delimiter;

    private Workbook workbook;
    private Sheet sheet;
    private int lineNumber;
    private Iterator<Row> rowIterator;

    /**
     * Get file as a readable file
     *
     * @param aFile
     */
    public ReadableFile(File aFile) {
        this.reader = null;
        this.sheet = null;
        this.lineNumber = 0;
        this.rowIterator = null;

        if (aFile != null) {
            boolean isExcelFile = false;

            // Try to check excel file
            try {
                FileInputStream fileIS = new FileInputStream(aFile);
                // Do not remove, it is used to detect Excel files
                workbook = WorkbookFactory.create(fileIS);
                this.sheet = workbook.getSheetAt(0);
                if (this.sheet != null) {
                    isExcelFile = true;
                    this.rowIterator = this.sheet.rowIterator();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
                this.sheet = null;
            } catch (IOException ex) {
                // Not recognzed
                this.sheet = null;
            }

            if (!isExcelFile) {
                // Try csv file
                try {
                    this.reader = new BufferedReader(new FileReader(aFile));
                } catch (FileNotFoundException ex) {
                    this.reader = null;
                    Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Get readablefile file type
     *
     * @return
     */
    public int getFileType() {
        if (this.sheet != null) {
            return EXCELFILE;
        } else if (this.reader != null) {
            return TEXTFILE;
        }
        return NOFILE;
    }

    /**
     * Check if we can use this file
     *
     * @return
     */
    public boolean isReadable() {
        if (this.sheet != null) {
            return true;
        }
        if (this.reader != null) {
            return true;
        }
        return false;
    }

    /**
     * Get next line as a String. Separator is tab
     *
     * @return
     */
    public String readLine() {
        String returnedValue = null;
        this.lineNumber++;
        if (this.sheet != null) {
            Row row = null;
            if (this.rowIterator != null) {
                if (this.rowIterator.hasNext()) {
                    row = this.rowIterator.next();
                }
            } else {
                row = this.sheet.getRow(lineNumber - 1);
            }
            StringBuilder theString = new StringBuilder();
            if (row != null) {
                int j = 0;
                for (Cell cell : row) {
                    Object s = "";
                    CellType ct = cell.getCellType();
                    if (ct == CellType.NUMERIC) {
                        s = cell.getNumericCellValue();
                    } else if (ct == CellType.STRING) {
                        s = cell.getStringCellValue();
                    }
                    int index = cell.getColumnIndex();
                    while (j <= index) {
                        j++;
                        if (j > 1) {
                            theString.append("\t");
                        }
                    }
                    theString.append(s);
                }
                returnedValue = theString.toString();
            }
        } else if (this.reader != null) {
            try {
                returnedValue = this.reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnedValue;
    }

    /**
     * Get next line as a String array
     *
     * @return
     */
    public List<String> readList() {
        List<String> returnedValue = null;
        this.lineNumber++;

        if (this.sheet != null) {
            Row row = sheet.getRow(lineNumber);

            if (row != null) {
                returnedValue = new ArrayList<String>();
                for (Cell cell : row) {
                    String s = cell.getStringCellValue();
                    returnedValue.add(s);
                }
            }
        } else if (this.reader != null) {
            try {
                String line = this.reader.readLine();
                if (line != null) {
                    if (delimiter == null) {
                        if (line.contains("\t")) {
                            delimiter = "\t";
                        } else if (line.contains(";")) {
                            delimiter = "\t;";
                        } else if (line.contains(",")) {
                            delimiter = "\t,";
                        }
                    }

                    returnedValue = new ArrayList<String>();
                    StringTokenizer st = new StringTokenizer(line, delimiter);
                    while (st.hasMoreElements()) {
                        String s = st.nextToken().trim();
                        returnedValue.add(s);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnedValue;
    }

    /**
     * Close Readable file
     */
    public void close() {
        if (this.sheet != null) {
            try {
                this.workbook.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.workbook = null;
            this.sheet = null;
        } else if (this.reader != null) {
            try {
                this.reader.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadableFile.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.reader = null;
        }
    }
}
