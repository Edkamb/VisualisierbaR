package com.github.bachelorpraktikum.dbvisualization.database;

import com.github.bachelorpraktikum.dbvisualization.database.model.ABSExportable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class ABSExporter {

    private final List<ABSExportable> elements;
    private boolean exportString;

    ABSExporter() {
        this(new LinkedList<>());
    }

    ABSExporter(List<ABSExportable> elements) {
        this.elements = elements;
    }

    private String getEnclosingFunctionFormattable() {
        String lineSep = System.lineSeparator();
        return String.format("Unit run() {%s%s%s%s}", lineSep, "%s", "%s", lineSep);
    }

    private String getLineSeperatedElements() {
        StringJoiner lineSeperatorJoiner = new StringJoiner(System.lineSeparator());
        elements.forEach(absExportable -> lineSeperatorJoiner.add(absExportable.export()));

        return lineSeperatorJoiner.toString();
    }

    private String leftPad(String string, int number) {
        StringBuilder sb = new StringBuilder(string);
        int charsToGo = 4;
        while (charsToGo > 0) {
            sb.insert(0, ' ');
            charsToGo--;
        }

        return sb.toString();
    }

    private String getExtra() {
        return "";
    }

    /**
     * Writes the exported elements into <code>defaultFilename</code> <tt>~/Run.abs</tt>
     *
     * @return Whether the writting of the file was successfull
     */
    public boolean export() {
        return export(defaultFilename);
    }

    /**
     * Writes the exported elements into <code>filename</code>
     *
     * @return Whether the writting of the file was successfull
     */
    public boolean export(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (!deleteFile(file)) {
                Logger.getLogger(getClass().getName())
                    .info(String.format("Couldn't delete file (%s), abort writing.",
                        file.getAbsolutePath()));
            }
        }
        try (OutputStream outputStream = new FileOutputStream(filename)) {
            file.createNewFile();
            export(outputStream);
        } catch (IOException e) {
            String message = String.format("Couldn't export to %s: \n%s", filename, e);
            Logger.getLogger(getClass().getName()).severe(message);
            return false;
        }

        return true;
    }

    private boolean deleteFile(File file) {
        Logger.getLogger(getClass().getName())
            .info(String.format("Deleting %s", file.getAbsolutePath()));
        return file.delete();
    }

    /**
     * Writes the abs content (enclosed in a `Run` function) to the given stream.
     *
     * @param outputStream Stream to write abs export to
     * @throws IOException Thrown if writting to the <code>outputStream</code> was unsuccessful
     */
    public void export(OutputStream outputStream) throws IOException {
        outputStream.write(getExportString().getBytes());
    }

    public List<ABSExportable> getElements() {
        return elements;
    }

    private String getExportString() {
        return String
            .format(getEnclosingFunctionFormattable(), getLineSeperatedElements(), getExtra());
    }
}
